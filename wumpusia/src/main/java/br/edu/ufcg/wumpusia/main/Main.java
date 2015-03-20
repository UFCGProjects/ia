
package br.edu.ufcg.wumpusia.main;

import br.edu.ufcg.wumpusgame.core.Wumpus;
import br.edu.ufcg.wumpusgame.models.Cell;
import br.edu.ufcg.wumpusgame.models.Hazard;
import br.edu.ufcg.wumpusia.utils.Utils;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.Viewer;

import java.util.Iterator;
import java.util.Scanner;

public class Main {

    private static Node mCurrentNode;

    public static void main(final String args[]) {
        final Graph graph = new MultiGraph("Tutorial 1");

        graph.setAutoCreate(true);
        graph.setStrict(false);
        graph.addAttribute("ui.stylesheet", Utils.styleSheet);

        final Wumpus wumpus = new Wumpus();
        int movesCount = 0;

        final Scanner sc = new Scanner(System.in);

        final Viewer display = graph.display();

        while (!wumpus.isGameOver()) {

            if (!refreshNodes(graph, wumpus)) {
                refreshDanger(graph, wumpus);
            }

            refreshNodesLabel(graph);

            String nextMove = findNextMoveToSafeNode(graph);

            if (nextMove == null) {
                nextMove = findNextMoveToWumpusNode(graph);
            }

            if (nextMove == null) {
                nextMove = findNextMoveToLessDangerousNode(graph);
            }

            System.out.println(nextMove);

            sc.nextLine();

            execMove(wumpus, nextMove);
            movesCount++;

            System.out.println(wumpus.toString());
        }
        System.out.println(movesCount + "");
        System.out.println(wumpus.getWin() + "");
        sc.nextLine();
        display.close();
    }

    private static void execMove(final Wumpus wumpus, final String nextMove) {
        if (nextMove.equalsIgnoreCase("up")) {
            wumpus.moveUp();
        } else if (nextMove.equalsIgnoreCase("down")) {
            wumpus.moveDown();
        } else if (nextMove.equalsIgnoreCase("left")) {
            wumpus.moveLeft();
        } else if (nextMove.equalsIgnoreCase("right")) {
            wumpus.moveRight();
        } else if (nextMove.equalsIgnoreCase("aw")) {
            wumpus.attackUp();
        } else if (nextMove.equalsIgnoreCase("as")) {
            wumpus.attackDown();
        } else if (nextMove.equalsIgnoreCase("aa")) {
            wumpus.attackLeft();
        } else if (nextMove.equalsIgnoreCase("ad")) {
            wumpus.attackRight();
        }
    }

    private static boolean refreshNodes(final Graph graph, final Wumpus wumpus) {
        mCurrentNode = addNode(graph, wumpus.getCurrentCell());

        addVizinhos(graph, wumpus);

        final boolean isVisited = mCurrentNode.getAttribute("visited");

        mCurrentNode.setAttribute("visited", true);

        for (final Node n : graph) {
            if (n.getAttribute("visited")) {
                n.setAttribute("ui.class", "visited");
            } else {
                n.setAttribute("ui.class", "unvisited");
            }
        }

        mCurrentNode.setAttribute("ui.class", "current");

        return isVisited;
    }

    private static void refreshDanger(final Graph graph, final Wumpus wumpus) {
        if (wumpus.getCurrentCell().getHazard().equals(Hazard.NONE)) {
            graph.getNode(wumpus.getCurrentCell().getId()).setAttribute("safe", true);
            graph.getNode(wumpus.getCurrentCell().getId()).setAttribute("danger-flap", 0);
            graph.getNode(wumpus.getCurrentCell().getId()).setAttribute("danger-breeze", 0);
            graph.getNode(wumpus.getCurrentCell().getId()).setAttribute("danger-wumpus", 0);
        }

        if (wumpus.getCurrentCell().isFeelBreeze()) {
            addVizinhosDanger(graph, wumpus, "breeze", 25);
        } else {
            setVizinhosSafe(graph, wumpus, "danger-breeze");
        }

        if (wumpus.getCurrentCell().isHearFlapping()) {
            addVizinhosDanger(graph, wumpus, "flap", 25);
        } else {
            setVizinhosSafe(graph, wumpus, "danger-flap");
        }

        if (wumpus.getCurrentCell().isSmellWumpus()) {
            addVizinhosDanger(graph, wumpus, "wumpus", 25);
        } else {
            setVizinhosSafe(graph, wumpus, "danger-wumpus");
        }
    }

    private static void refreshNodesLabel(final Graph graph) {
        for (final Node n : graph) {
            final String msg = String.format("%s : (%s,%s,%s)", n.getAttribute("id"),
                    n.getAttribute("danger-wumpus"), n.getAttribute("danger-breeze"),
                    n.getAttribute("danger-flap"));
            n.setAttribute("ui.label", msg);
        }
    }

    private static void addVizinhos(final Graph graph, final Wumpus wumpus) {
        final Cell currentCell = wumpus.getCurrentCell();
        Node node;

        if (wumpus.hasUpCell(currentCell)) {
            node = addVizinho(graph, currentCell, wumpus.getUpCell(currentCell));
            mCurrentNode.setAttribute(node.getId(), "up");
            node.setAttribute(mCurrentNode.getId(), "down");
        }

        if (wumpus.hasDownCell(currentCell)) {
            node = addVizinho(graph, currentCell, wumpus.getDownCell(currentCell));
            mCurrentNode.setAttribute(node.getId(), "down");
            node.setAttribute(mCurrentNode.getId(), "up");
        }

        if (wumpus.hasLeftCell(currentCell)) {
            node = addVizinho(graph, currentCell, wumpus.getLeftCell(currentCell));
            mCurrentNode.setAttribute(node.getId(), "left");
            node.setAttribute(mCurrentNode.getId(), "right");
        }

        if (wumpus.hasRightCell(currentCell)) {
            node = addVizinho(graph, currentCell, wumpus.getRightCell(currentCell));
            mCurrentNode.setAttribute(node.getId(), "right");
            node.setAttribute(mCurrentNode.getId(), "left");
        }

    }

    private static Node addVizinho(final Graph graph, final Cell currentCell, final Cell otherCell) {
        final Node node = addNode(graph, otherCell);

        graph.addEdge(currentCell.getId() + "-" + otherCell.getId(), currentCell.getId(), otherCell.getId());

        return node;

    }

    public static Node addNode(final Graph graph, final Cell cell) {
        Node node = graph.getNode(cell.getId());

        if (node == null) {
            node = graph.addNode(cell.getId());

            node.setAttribute("id", cell.getId());
            node.setAttribute("danger-wumpus", 0);
            node.setAttribute("danger-breeze", 0);
            node.setAttribute("danger-flap", 0);
            node.setAttribute("visited", false);
        }

        return node;
    }

    private static String findNextMoveToSafeNode(final Graph graph) {
        final Iterator<Node> breadthFirstIterator = mCurrentNode.getBreadthFirstIterator();

        Node nodeResult;

        while (breadthFirstIterator.hasNext()) {
            final Node node = breadthFirstIterator.next();
            final String nodeClass = node.getAttribute("ui.class");

            final Integer dangerBreeze = node.getAttribute("danger-breeze");
            final Integer dangerFlap = node.getAttribute("danger-flap");
            final Integer dangerWumpus = node.getAttribute("danger-wumpus");

            // Se houver algum nó não visitado com perigo igual a zero: visita ele.
            if (nodeClass.equals("unvisited")
                    && dangerBreeze + dangerFlap + dangerWumpus == 0) {
                nodeResult = node;
                return findMove(graph, nodeResult);
            }
        }

        return null;
    }

    private static String findNextMoveToLessDangerousNode(final Graph graph) {
        final Iterator<Node> breadthFirstIterator = mCurrentNode.getBreadthFirstIterator();

        Node nodeResult = null;
        Integer minDanger = Integer.MAX_VALUE;

        while (breadthFirstIterator.hasNext()) {
            final Node node = breadthFirstIterator.next();
            final String nodeClass = node.getAttribute("ui.class");

            final Integer dangerBreeze = node.getAttribute("danger-breeze");
            final Integer dangerFlap = node.getAttribute("danger-flap");
            final Integer dangerWumpus = node.getAttribute("danger-wumpus");

            // Se houver algum nó não visitado com perigo igual a zero: visita ele.
            if (nodeClass.equals("unvisited")
                    && dangerBreeze + dangerFlap + dangerWumpus < minDanger) {
                nodeResult = node;
                minDanger = dangerBreeze + dangerFlap + dangerWumpus;
            }
        }

        return findMove(graph, nodeResult);
    }

    private static String findMove(final Graph graph, final Node toNode) {
        if (toNode == null) {
            return null;
        }

        final Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.edge, null, "length");

        dijkstra.init(graph);
        dijkstra.setSource(mCurrentNode.getId());
        dijkstra.compute();

        final Path shortestPath = dijkstra.getShortestPath(toNode);

        final Iterator<Node> nodeIterator = shortestPath.getNodeIterator();

        Node prev = nodeIterator.next();
        String move = "";

        while (nodeIterator.hasNext()) {
            final Node next = nodeIterator.next();
            move = next.getAttribute(prev.getId());
            prev = next;
        }

        return move;
    }

    private static String findNextMoveToWumpusNode(final Graph graph) {
        final Iterator<Node> breadthFirstIterator = mCurrentNode.getBreadthFirstIterator();

        Node resultNode = null;
        Integer maxDanger = 0;

        while (breadthFirstIterator.hasNext()) {
            final Node node = breadthFirstIterator.next();
            final String nodeClass = node.getAttribute("ui.class");

            final Integer dangerWumpus = node.getAttribute("danger-wumpus");

            // Se houver algum nó não visitado com perigo igual a zero: visita ele.
            if (nodeClass.equals("unvisited") && dangerWumpus > maxDanger) {
                resultNode = node;
                maxDanger = dangerWumpus;
            }
        }

        if (resultNode == null) {
            return null;
        }

        final Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.edge, null, "length");

        dijkstra.init(graph);
        dijkstra.setSource(mCurrentNode.getId());
        dijkstra.compute();

        final Path shortestPath = dijkstra.getShortestPath(resultNode);

        final Iterator<Node> nodeIterator = shortestPath.getNodeIterator();

        Node prev = nodeIterator.next();
        String move = "";

        Node next = null;

        while (nodeIterator.hasNext()) {
            next = nodeIterator.next();

            move = next.getAttribute(prev.getId());
            prev = next;
        }

        // System.out.println(next.getAttribute(node.getId()));

        final String attackMove = next.getAttribute(resultNode.getId());

        if (attackMove != null) {
            if (attackMove.equalsIgnoreCase("up")) {
                move = "aw";
            } else if (attackMove.equalsIgnoreCase("down")) {
                move = "as";
            } else if (attackMove.equalsIgnoreCase("left")) {
                move = "aa";
            } else if (attackMove.equalsIgnoreCase("right")) {
                move = "ad";
            }
        }

        return move;
    }

    private static void addVizinhosDanger(final Graph graph, final Wumpus wumpus,
            final String dangerType, final int danger) {
        final Cell currentCell = wumpus.getCurrentCell();

        if (wumpus.hasUpCell(currentCell)) {
            addDanger(graph, currentCell, wumpus.getUpCell(currentCell), dangerType, danger);
        }

        if (wumpus.hasDownCell(currentCell)) {
            addDanger(graph, currentCell, wumpus.getDownCell(currentCell), dangerType, danger);
        }

        if (wumpus.hasLeftCell(currentCell)) {
            addDanger(graph, currentCell, wumpus.getLeftCell(currentCell), dangerType, danger);
        }

        if (wumpus.hasRightCell(currentCell)) {
            addDanger(graph, currentCell, wumpus.getRightCell(currentCell), dangerType, danger);
        }

    }

    private static void addDanger(final Graph graph, final Cell currentCell, final Cell otherCell,
            final String dangerType, final Integer danger) {
        final Node n = graph.getNode(otherCell.getId());

        if (!n.hasAttribute("safe")) {
            final Integer oldDanger = n.getAttribute("danger-" + dangerType);
            n.setAttribute("danger-" + dangerType, oldDanger + danger);
        }
    }

    private static void setVizinhosSafe(final Graph graph, final Wumpus wumpus,
            final String dangerType) {
        final Cell currentCell = wumpus.getCurrentCell();

        if (wumpus.hasUpCell(currentCell)) {
            graph.getNode(wumpus.getUpCell(currentCell).getId()).setAttribute(dangerType, 0);
        }

        if (wumpus.hasDownCell(currentCell)) {
            graph.getNode(wumpus.getDownCell(currentCell).getId()).setAttribute(dangerType, 0);
        }

        if (wumpus.hasLeftCell(currentCell)) {
            graph.getNode(wumpus.getLeftCell(currentCell).getId()).setAttribute(dangerType, 0);
        }

        if (wumpus.hasRightCell(currentCell)) {
            graph.getNode(wumpus.getRightCell(currentCell).getId()).setAttribute(dangerType, 0);
        }

    }

}
