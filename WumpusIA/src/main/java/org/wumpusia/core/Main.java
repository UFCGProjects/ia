
package org.wumpusia.core;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.Viewer;
import org.wumpusgame.core.Wumpus;
import org.wumpusgame.models.Cell;
import org.wumpusgame.models.Hazard;
import org.wumpusia.utils.Utils;

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

        final Scanner sc = new Scanner(System.in);

        final Viewer display = graph.display();

        while (!wumpus.isGameOver()) {

            if (!refreshNodes(graph, wumpus)) {
                refreshDanger(graph, wumpus);
            }

            refreshNodesLabel(graph);

            final String nextMove = findNextMove(graph);

            //            System.out.println(String.format("up: %s - down: %s - left: %s - right: %s",
            //                    mCurrentNode.getAttribute("up"), mCurrentNode.getAttribute("down"),
            //                    mCurrentNode.getAttribute("left"), mCurrentNode.getAttribute("right")));

            //            System.out.println(wumpus.toString());

            //            final String in = sc.nextLine();

            //            if (in.equalsIgnoreCase("w")) {
            //                wumpus.moveUp();
            //            } else if (in.equalsIgnoreCase("s")) {
            //                wumpus.moveDown();
            //            } else if (in.equalsIgnoreCase("a")) {
            //                wumpus.moveLeft();
            //            } else if (in.equalsIgnoreCase("d")) {
            //                wumpus.moveRight();
            //            }

            System.out.println("Moving to: " + nextMove);

            if (nextMove.equalsIgnoreCase("up")) {
                wumpus.moveUp();
            } else if (nextMove.equalsIgnoreCase("down")) {
                wumpus.moveDown();
            } else if (nextMove.equalsIgnoreCase("left")) {
                wumpus.moveLeft();
            } else if (nextMove.equalsIgnoreCase("right")) {
                wumpus.moveRight();
            }

            try {
                Thread.sleep(2000);
            } catch (final InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        sc.nextLine();
        display.close();

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
            graph.getNode(wumpus.getCurrentCell().getId()).setAttribute("danger", 0);
        }

        if (wumpus.getCurrentCell().isFeelBreeze()) {
            addVizinhosDanger(graph, wumpus, 25);
        }

        if (wumpus.getCurrentCell().isHearFlapping()) {
            addVizinhosDanger(graph, wumpus, 25);
        }

        if (wumpus.getCurrentCell().isSmellWumpus()) {
            addVizinhosDanger(graph, wumpus, 25);
        }

        if (!wumpus.getCurrentCell().isSmellWumpus() && !wumpus.getCurrentCell().isFeelBreeze()
                && !wumpus.getCurrentCell().isHearFlapping()) {
            setVizinhosSafe(graph, wumpus);
        }
    }

    private static void refreshNodesLabel(final Graph graph) {
        for (final Node n : graph) {
            n.setAttribute("ui.label", n.getAttribute("id") + " : " + n.getAttribute("danger"));
        }
    }

    private static void addVizinhos(final Graph graph, final Wumpus wumpus) {
        final Cell currentCell = wumpus.getCurrentCell();
        Node node = null;

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

        final Edge edge = graph.addEdge(currentCell.getId() + "-" + otherCell.getId(),
                currentCell.getId(),
                otherCell.getId());

        return node;

    }

    public static Node addNode(final Graph graph, final Cell cell) {
        Node node = graph.getNode(cell.getId());

        if (node == null) {
            node = graph.addNode(cell.getId());

            node.setAttribute("id", cell.getId());
            node.setAttribute("danger", 0);
            node.setAttribute("visited", false);
        }

        return node;
    }

    private static String findNextMove(final Graph graph) {
        final Iterator<Node> breadthFirstIterator = mCurrentNode.getBreadthFirstIterator();

        Node nodeResult = null;
        Integer minDanger = Integer.MAX_VALUE;

        while (breadthFirstIterator.hasNext()) {
            final Node node = breadthFirstIterator.next();
            final String nodeClass = node.getAttribute("ui.class");
            final Integer nodeDanger = node.getAttribute("danger");

            if (nodeClass.equals("unvisited") && nodeDanger < minDanger) {
                nodeResult = node;
                minDanger = nodeDanger;
            }
        }

        final Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.edge, null, "length");

        dijkstra.init(graph);
        dijkstra.setSource(mCurrentNode.getId());
        dijkstra.compute();

        final Path shortestPath = dijkstra.getShortestPath(nodeResult);

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

    private static void addVizinhosDanger(final Graph graph, final Wumpus wumpus, final int danger) {
        final Cell currentCell = wumpus.getCurrentCell();

        if (wumpus.hasUpCell(currentCell)) {
            addDanger(graph, currentCell, wumpus.getUpCell(currentCell), danger);
        }

        if (wumpus.hasDownCell(currentCell)) {
            addDanger(graph, currentCell, wumpus.getDownCell(currentCell), danger);
        }

        if (wumpus.hasLeftCell(currentCell)) {
            addDanger(graph, currentCell, wumpus.getLeftCell(currentCell), danger);
        }

        if (wumpus.hasRightCell(currentCell)) {
            addDanger(graph, currentCell, wumpus.getRightCell(currentCell), danger);
        }

    }

    private static void addDanger(final Graph graph, final Cell currentCell, final Cell otherCell,
            final Integer danger) {
        final Node n = graph.getNode(otherCell.getId());

        if (!n.hasAttribute("safe")) {
            final Integer oldDanger = n.getAttribute("danger");
            n.setAttribute("danger", oldDanger + danger);
        }
    }

    private static void setVizinhosSafe(final Graph graph, final Wumpus wumpus) {
        final Cell currentCell = wumpus.getCurrentCell();

        if (wumpus.hasUpCell(currentCell)) {
            graph.getNode(wumpus.getUpCell(currentCell).getId()).setAttribute("safe", true);
            graph.getNode(wumpus.getUpCell(currentCell).getId()).setAttribute("danger", 0);
        }

        if (wumpus.hasDownCell(currentCell)) {
            graph.getNode(wumpus.getDownCell(currentCell).getId())
                    .setAttribute("safe", true);
            graph.getNode(wumpus.getDownCell(currentCell).getId()).setAttribute("danger", 0);
        }

        if (wumpus.hasLeftCell(currentCell)) {
            graph.getNode(wumpus.getLeftCell(currentCell).getId())
                    .setAttribute("safe", true);
            graph.getNode(wumpus.getLeftCell(currentCell).getId()).setAttribute("danger", 0);
        }

        if (wumpus.hasRightCell(currentCell)) {
            graph.getNode(wumpus.getRightCell(currentCell).getId()).setAttribute("safe", true);
            graph.getNode(wumpus.getRightCell(currentCell).getId()).setAttribute("danger", 0);
        }

    }

}
