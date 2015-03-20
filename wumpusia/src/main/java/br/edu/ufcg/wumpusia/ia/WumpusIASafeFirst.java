package br.edu.ufcg.wumpusia.ia;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;

import java.util.Iterator;

public class WumpusIASafeFirst extends WumpusIA {

    public WumpusIASafeFirst(Long seed) {
        super(seed);
    }

    public WumpusIASafeFirst() {
        super();
    }


    @Override
    String heuristicaToFindMove() {
        String nextMove = findNextMoveToSafeNode();

        if (nextMove == null) {
            nextMove = findNextMoveToWumpusNode();
        }

        if (nextMove == null) {
            nextMove = findNextMoveToLessDangerousNode();
        }

        return nextMove;
    }

    protected String findNextMoveToSafeNode() {
        final Iterator<Node> breadthFirstIterator = getCurrentNode().getBreadthFirstIterator();

        Node nodeResult;

        while (breadthFirstIterator.hasNext()) {
            final Node node = breadthFirstIterator.next();
            final boolean isVisited = node.getAttribute("visited");

            final Integer dangerBreeze = node.getAttribute("danger-breeze");
            final Integer dangerFlap = node.getAttribute("danger-flap");
            final Integer dangerWumpus = node.getAttribute("danger-wumpus");

            // Se houver algum nó não visitado com perigo igual a zero: visita ele.
            if (!isVisited) {
                if (dangerBreeze + dangerFlap + dangerWumpus == 0) {
                    nodeResult = node;
                    return findMove(nodeResult);
                }
            }
        }

        return null;
    }

    protected String findNextMoveToLessDangerousNode() {
        final Iterator<Node> breadthFirstIterator = getCurrentNode().getBreadthFirstIterator();

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

        return findMove(nodeResult);
    }

    protected String findNextMoveToWumpusNode() {
        final Iterator<Node> breadthFirstIterator = getCurrentNode().getBreadthFirstIterator();

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

        dijkstra.init(getGraph());
        dijkstra.setSource(getCurrentNode().getId());
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

}
