
package br.edu.ufcg.wumpusia.ia;

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

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

public abstract class WumpusIA {

    private Node mCurrentNode;
    private Graph mGraph;
    private Wumpus mWumpus;

    public WumpusIA() {
        mGraph = new MultiGraph("Wumpus IA");

        getGraph().setAutoCreate(true);
        getGraph().setStrict(false);
        getGraph().addAttribute("ui.stylesheet", Utils.styleSheet);

        mWumpus = new Wumpus();
    }

    public void run() throws IOException {
        final Scanner sc = new Scanner(System.in);

        //final Viewer display = getGraph().display();

        int movesCount = 0;
        while (!getWumpus().isGameOver()) {

            if (!refreshNodes()) {
                refreshDanger();
            }

            refreshNodesLabel();

            String nextMove = heuristicaToFindMove();

            // DEBUG:
            //     Imprimi o proximo movimento da IA
            //System.out.println(nextMove);

            // DEBUG:
            //     Segura os movimentos da IA
            //sc.nextLine();

            execMove(nextMove);
            movesCount++;

            // DEBUG:
            //     Mostra o tabuleiro do WUMPUS no console.
            //System.out.println(getWumpus().toString());
        }

        System.out.println(movesCount + "");
        System.out.println(getWumpus().getWin() + "");
        Utils.addResultado("simples", movesCount, getWumpus().getWin());
        //sc.nextLine();
        //display.close();

    }

    abstract String heuristicaToFindMove();

    protected void execMove(final String nextMove) {
        if (nextMove.equalsIgnoreCase("up")) {
            getWumpus().moveUp();
        } else if (nextMove.equalsIgnoreCase("down")) {
            getWumpus().moveDown();
        } else if (nextMove.equalsIgnoreCase("left")) {
            getWumpus().moveLeft();
        } else if (nextMove.equalsIgnoreCase("right")) {
            getWumpus().moveRight();
        } else if (nextMove.equalsIgnoreCase("aw")) {
            getWumpus().attackUp();
        } else if (nextMove.equalsIgnoreCase("as")) {
            getWumpus().attackDown();
        } else if (nextMove.equalsIgnoreCase("aa")) {
            getWumpus().attackLeft();
        } else if (nextMove.equalsIgnoreCase("ad")) {
            getWumpus().attackRight();
        }
    }

    protected boolean refreshNodes() {
        mCurrentNode = addNode(getWumpus().getCurrentCell());

        addVizinhos();

        final boolean isVisited = mCurrentNode.getAttribute("visited");

        mCurrentNode.setAttribute("visited", true);

        for (final Node n : getGraph()) {
            if (n.getAttribute("visited")) {
                n.setAttribute("ui.class", "visited");
            } else {
                n.setAttribute("ui.class", "unvisited");

                if (n.getAttribute("safe") != null) {
                    n.setAttribute("ui.class", "unvisitedsafe");
                }
            }

        }

        mCurrentNode.setAttribute("ui.class", "current");

        return isVisited;
    }

    protected void refreshDanger() {
        if (getWumpus().getCurrentCell().getHazard().equals(Hazard.NONE)) {
            setNodeSafe(getGraph().getNode(getWumpus().getCurrentCell().getId()));
        }

        if (getWumpus().getCurrentCell().isFeelBreeze()) {
            addVizinhosDanger("breeze", 25);
        }

        if (getWumpus().getCurrentCell().isHearFlapping()) {
            addVizinhosDanger("flap", 25);
        }

        if (getWumpus().getCurrentCell().isSmellWumpus()) {
            addVizinhosDanger("wumpus", 25);
        }

        if (!getWumpus().getCurrentCell().isFeelBreeze() && !getWumpus().getCurrentCell().isHearFlapping() && !getWumpus().getCurrentCell().isSmellWumpus()) {
            setVizinhosSafe();
        }

    }

    protected void refreshNodesLabel() {
        for (final Node n : getGraph()) {
            final String msg = String.format("%s : (%s,%s,%s)", n.getAttribute("id"),
                    n.getAttribute("danger-wumpus"), n.getAttribute("danger-breeze"),
                    n.getAttribute("danger-flap"));
            n.setAttribute("ui.label", msg);
        }
    }

    protected void addVizinhos() {
        final Cell currentCell = getWumpus().getCurrentCell();
        Node node;

        if (getWumpus().hasUpCell(currentCell)) {
            node = addVizinho(currentCell, getWumpus().getUpCell(currentCell));
            mCurrentNode.setAttribute(node.getId(), "up");
            node.setAttribute(mCurrentNode.getId(), "down");
        }

        if (getWumpus().hasDownCell(currentCell)) {
            node = addVizinho(currentCell, getWumpus().getDownCell(currentCell));
            mCurrentNode.setAttribute(node.getId(), "down");
            node.setAttribute(mCurrentNode.getId(), "up");
        }

        if (getWumpus().hasLeftCell(currentCell)) {
            node = addVizinho(currentCell, getWumpus().getLeftCell(currentCell));
            mCurrentNode.setAttribute(node.getId(), "left");
            node.setAttribute(mCurrentNode.getId(), "right");
        }

        if (getWumpus().hasRightCell(currentCell)) {
            node = addVizinho(currentCell, getWumpus().getRightCell(currentCell));
            mCurrentNode.setAttribute(node.getId(), "right");
            node.setAttribute(mCurrentNode.getId(), "left");
        }

    }

    protected Node addVizinho( final Cell currentCell, final Cell otherCell) {
        final Node node = addNode(otherCell);

        getGraph().addEdge(currentCell.getId() + "-" + otherCell.getId(), currentCell.getId(), otherCell.getId());

        return node;

    }

    protected Node addNode(final Cell cell) {
        Node node = getGraph().getNode(cell.getId());

        if (node == null) {
            node = getGraph().addNode(cell.getId());

            node.setAttribute("id", cell.getId());
            node.setAttribute("danger-wumpus", 0);
            node.setAttribute("danger-breeze", 0);
            node.setAttribute("danger-flap", 0);
            node.setAttribute("visited", false);
        }

        return node;
    }


    protected String findMove(final Node toNode) {
        if (toNode == null) {
            return null;
        }

        final Dijkstra dijkstra = new Dijkstra(Dijkstra.Element.edge, null, "length");

        dijkstra.init(getGraph());
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



    protected void addVizinhosDanger(final String dangerType, final int danger) {
        final Cell currentCell = getWumpus().getCurrentCell();

        if (getWumpus().hasUpCell(currentCell)) {
            addDanger(getWumpus().getUpCell(currentCell), dangerType, danger);
        }

        if (getWumpus().hasDownCell(currentCell)) {
            addDanger(getWumpus().getDownCell(currentCell), dangerType, danger);
        }

        if (getWumpus().hasLeftCell(currentCell)) {
            addDanger(getWumpus().getLeftCell(currentCell), dangerType, danger);
        }

        if (getWumpus().hasRightCell(currentCell)) {
            addDanger(getWumpus().getRightCell(currentCell), dangerType, danger);
        }

    }

    protected void addDanger(final Cell cell, final String dangerType, final Integer danger) {
        final Node n = getGraph().getNode(cell.getId());

        if (!n.hasAttribute("safe")) {
            final Integer oldDanger = n.getAttribute("danger-" + dangerType);
            n.setAttribute("danger-" + dangerType, oldDanger + danger);
        }
    }

    protected void setVizinhosSafe() {
        final Cell currentCell = getWumpus().getCurrentCell();

        if (getWumpus().hasUpCell(currentCell)) {
            setNodeSafe(getGraph().getNode(getWumpus().getUpCell(currentCell).getId()));
        }

        if (getWumpus().hasDownCell(currentCell)) {
            setNodeSafe(getGraph().getNode(getWumpus().getDownCell(currentCell).getId()));
        }

        if (getWumpus().hasLeftCell(currentCell)) {
            setNodeSafe(getGraph().getNode(getWumpus().getLeftCell(currentCell).getId()));
        }

        if (getWumpus().hasRightCell(currentCell)) {
            setNodeSafe(getGraph().getNode(getWumpus().getRightCell(currentCell).getId()));
        }
    }

    protected void setNodeSafe(final Node node) {
        node.setAttribute("safe", true);
        node.setAttribute("danger-flap", 0);
        node.setAttribute("danger-breeze", 0);
        node.setAttribute("danger-wumpus", 0);

        if (node.getAttribute("ui.class").equals("unvisited")) {
            node.setAttribute("ui.class", "unvisitedsafe");
        }
    }

    protected Node getCurrentNode() {
        return mCurrentNode;
    }

    protected Graph getGraph() {
        return mGraph;
    }

    protected Wumpus getWumpus() {
        return mWumpus;
    }

}
