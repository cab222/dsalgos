package cabkata.graphs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import cabkata.graphs.Graph.Node;
import cabkata.graphs.Graph.WeightedEdge;
import cabkata.sets.DisjointSet;

public final class GraphAlgorithms<V> {
    private final Map<V, NodeSate> nodeState = new HashMap<V, NodeSate>();
    private final Map<V, V> predecessors = new HashMap<V, V>();
    private final Map<V, Integer> finishTime = new HashMap<V, Integer>();
    private final Map<V, Integer> startTime = new HashMap<V, Integer>();
    private final Map<V, Integer> distances = new HashMap<V, Integer>();
    private int time = 0;
    private final static Comparator<WeightedEdge<? extends Object>> EDGE_COMPARATOR = new Comparator<WeightedEdge<? extends Object>>()
    {
        @Override
        public int compare(WeightedEdge<?> o1, WeightedEdge<?> o2)
        {
            if (o1 == o2)
                return 0;

            if (o1.getWeight() < o2.getWeight())
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    };

    private static enum NodeSate
    {
        UNVISITED, VISITED, FINISHED
    }

    /**
     * Performs a topological sort
     * 
     * @param graph
     * @return toplogical sort of graph elements V
     */
    public List<V> toplogicalSort(Graph<V> graph)
    {
        List<V> topologicalSort = new ArrayList<V>();
        initializeGraph(graph);

        for (Node<V> node : graph.getNodes())
        {
            if (isNodeUnvisted(node))
            {
                visit(node, topologicalSort);
            }
        }
        return topologicalSort;
    }

    private boolean isNodeUnvisted(Node<V> node)
    {
        return nodeState.get(node.getValue()) == NodeSate.UNVISITED;
    }

    private void markNodeVisited(Node<V> node, int start)
    {
        nodeState.put(node.getValue(), NodeSate.VISITED);
        startTime.put(node.getValue(), Integer.valueOf(start));
    }

    private void markNodeFinished(Node<V> node, int finish)
    {
        finishTime.put(node.getValue(), Integer.valueOf(finish));
        nodeState.put(node.getValue(), NodeSate.FINISHED);
    }

    private void markPredecessor(Node<V> node, Node<V> parent)
    {
        predecessors.put(node.getValue(), parent.getValue());
    }

    private void visit(Node<V> node, List<V> topologicalSort)
    {
        time++;
        markNodeVisited(node, time);
        for (WeightedEdge<V> edge : node.getAdjacentyList())
        {
            Node<V> adjacentNode = edge.getTo();
            if (isNodeUnvisted(adjacentNode))
            {
                markPredecessor(adjacentNode, node);
                visit(adjacentNode, topologicalSort);
            }
        }
        time++;
        markNodeFinished(node, time);
        topologicalSort.add(0, node.getValue());
    }

    private void initializeGraph(Graph<V> graph)
    {
        time = 0;
        nodeState.clear();
        predecessors.clear();
        finishTime.clear();
        startTime.clear();
        distances.clear();

        for (Node<V> node : graph.getNodes())
        {
            nodeState.put(node.getValue(), NodeSate.UNVISITED);
            predecessors.put(node.getValue(), null);
            finishTime.put(node.getValue(), Integer.valueOf(0));
            startTime.put(node.getValue(), Integer.valueOf(0));
            distances.put(node.getValue(), Integer.valueOf(0));
        }
    }

    public int getStart(V v)
    {
        return startTime.containsKey(v) ? startTime.get(v).intValue() : 0;
    }

    public int getFinish(V v)
    {
        return finishTime.containsKey(v) ? finishTime.get(v).intValue() : 0;
    }

    public void breadthFirstSearch(Graph<V> graph, V source)
    {
        initializeGraph(graph);
        Queue<Node<V>> q = new LinkedList<Node<V>>();
        Node<V> sourceNode = graph.getNode(source);
        nodeState.put(sourceNode.getValue(), NodeSate.VISITED);
        distances.put(sourceNode.getValue(), Integer.valueOf(0));
        q.offer(sourceNode);

        while (!q.isEmpty())
        {
            Node<V> currentNode = q.remove();
            for (WeightedEdge<V> edge : currentNode.getAdjacentyList())
            {
                Node<V> adjacentNode = edge.getTo();
                if (isNodeUnvisted(adjacentNode))
                {
                    nodeState.put(adjacentNode.getValue(), NodeSate.VISITED);
                    markPredecessor(adjacentNode, currentNode);
                    distances.put(
                            adjacentNode.getValue(),
                            Integer.valueOf(distances.get(
                                    currentNode.getValue()).intValue() + 1));
                    q.offer(adjacentNode);
                }
            }
            nodeState.put(currentNode.getValue(), NodeSate.FINISHED);
        }
    }

    public int getDistance(V v)
    {
        return distances.get(v).intValue();
    }

    public Set<WeightedEdge<V>> minimumSpanningTreeKruskal(Graph<V> graph)
    {
        Set<WeightedEdge<V>> mst = new HashSet<WeightedEdge<V>>();
        List<WeightedEdge<V>> edges = new ArrayList<WeightedEdge<V>>(
                graph.getEdges());
        Collection<Node<V>> nodes = graph.getNodes();
        DisjointSet<Node<V>> disjointSet = new DisjointSet<Node<V>>();

        // Now we have a forest of singleton node sets
        for (Node<V> node : nodes)
        {
            disjointSet.add(node);
        }

        // edges are now sorted by weight
        Collections.sort(edges, EDGE_COMPARATOR);

        for (WeightedEdge<V> edge : edges)
        {
            Node<V> from = edge.getFrom();
            Node<V> to = edge.getTo();
            Set<Node<V>> fromSet = disjointSet.get(from);
            Set<Node<V>> toSet = disjointSet.get(to);

            if (!fromSet.equals(toSet))
            {
                disjointSet.union(from, to);
                mst.add(edge);
            }
        }

        return mst;
    }

    public Set<WeightedEdge<V>> minimumSpanningTreePrim(Graph<V> graph,
            V rootValue)
    {
        Node<V> rootNode = graph.getNode(rootValue);
        if (rootNode == null)
            throw new IllegalArgumentException("root value not found in graph");

        initializeGraph(graph);
        HashMap<V, WeightedEdge<V>> mst = new HashMap<V, WeightedEdge<V>>();
        final Map<Node<V>, Double> nodeKeyValues = new HashMap<Node<V>, Double>();
        for (Node<V> node : graph.getNodes())
        {
            nodeKeyValues.put(node, Double.MAX_VALUE);
        }

        PriorityQueue<Node<V>> priorityQ = new PriorityQueue<Node<V>>(graph
                .getNodes().size(), new Comparator<Node<V>>()
        {
            @Override
            public int compare(Node<V> o1, Node<V> o2)
            {
                return nodeKeyValues.get(o1).compareTo(nodeKeyValues.get(o2));
            }
        });
        priorityQ.addAll(graph.getNodes());
        updateNodeKey(rootNode, nodeKeyValues, priorityQ, 0);

        while (!priorityQ.isEmpty())
        {
            Node<V> currentNode = priorityQ.poll();
            for (WeightedEdge<V> edge : currentNode.getAdjacentyList())
            {
                Node<V> adjacentNode = edge.getTo();

                if (priorityQ.contains(adjacentNode)
                        && edge.getWeight() < nodeKeyValues.get(adjacentNode)
                                .doubleValue())
                {
                    predecessors.put(adjacentNode.getValue(),
                            currentNode.getValue());
                    mst.put(adjacentNode.getValue(), edge);
                    updateNodeKey(adjacentNode, nodeKeyValues, priorityQ,
                            edge.getWeight());
                }
            }
        }

        return new HashSet<WeightedEdge<V>>(mst.values());
    }

    private void updateNodeKey(Node<V> node,
            final Map<Node<V>, Double> nodeKeyValues,
            PriorityQueue<Node<V>> priorityQ, double value)
    {
        nodeKeyValues.put(node, Double.valueOf(value));
        priorityQ.remove(node);
        priorityQ.add(node);
    }
}
