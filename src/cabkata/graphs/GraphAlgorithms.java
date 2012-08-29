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
    private final Map<V, NodeSate> nodeState;
    private final Map<V, V> predecessors;
    private final Map<V, Integer> finishTime;
    private final Map<V, Integer> startTime;
    private final Map<V, Integer> distances;
    private final Map<Node<V>, Double> nodeKeys;
    private int time;
    
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


    public GraphAlgorithms()
    {
        nodeState = new HashMap<V, NodeSate>();
        predecessors = new HashMap<V, V>(); //BFS,DFS
        finishTime = new HashMap<V, Integer>(); //DFS
        startTime = new HashMap<V, Integer>(); //DFS
        distances = new HashMap<V, Integer>(); //BFS, PRIM
        nodeKeys = new HashMap<Node<V>, Double>(); //PRIM
        time = 0; //DFS
    }
    
    /**
     * Performs a topological sort
     * 
     * @param graph
     * @return toplogical sort of graph elements V
     */
    public List<V> toplogicalSort(Graph<V> graph)
    {
        validateGraphNotNull(graph);
        List<V> topologicalSort = new ArrayList<V>();
        initializeAlgoState(graph);

        for (Node<V> node : graph.getNodes())
        {
            if (isNodeUnvisted(node))
            {
                visit(node, topologicalSort);
            }
        }
        return topologicalSort;
    }

    private void validateGraphNotNull(Graph<V> graph)
    {
        if(graph == null)
        {
            throw new IllegalArgumentException("graph is null");   
        }
    }
    
    private void validateGraphAndSourceNotNull(Graph<V> graph, V source)
    {
        validateGraphNotNull(graph);
        if(source == null)
        {
            throw new IllegalArgumentException("source is null");
        }            
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

    private void initializeAlgoState(Graph<V> graph)
    {
        validateGraphNotNull(graph);
        
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
            nodeKeys.put(node, Double.MAX_VALUE);
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
        validateGraphAndSourceNotNull(graph, source);
        initializeAlgoState(graph);
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
        validateGraphAndSourceNotNull(graph, rootValue);
        
        Node<V> rootNode = graph.getNode(rootValue);
        if (rootNode == null)
            throw new IllegalArgumentException("root value not found in graph");

        initializeAlgoState(graph);
        HashMap<V, WeightedEdge<V>> mst = new HashMap<V, WeightedEdge<V>>();

        PriorityQueue<Node<V>> priorityQ = new PriorityQueue<Node<V>>(graph
                .getNodes().size(), new Comparator<Node<V>>()
        {
            @Override
            public int compare(Node<V> o1, Node<V> o2)
            {
                return getNodeKey(o1).compareTo(getNodeKey(o2));
            }
        });
        priorityQ.addAll(graph.getNodes());
        updateNodeKeyAndQ(rootNode, priorityQ, 0);

        while (!priorityQ.isEmpty())
        {
            Node<V> currentNode = priorityQ.poll();
            for (WeightedEdge<V> edge : currentNode.getAdjacentyList())
            {
                Node<V> adjacentNode = edge.getTo();

                if (priorityQ.contains(adjacentNode)
                        && edge.getWeight() < getNodeKey(adjacentNode))
                {
                    predecessors.put(adjacentNode.getValue(), currentNode.getValue());
                    mst.put(adjacentNode.getValue(), edge);
                    updateNodeKeyAndQ(adjacentNode, priorityQ, edge.getWeight());
                }
            }
        }

        return new HashSet<WeightedEdge<V>>(mst.values());
    }

    private Double getNodeKey(Node<V> adjacentNode)
    {
        return nodeKeys.get(adjacentNode);
    }

    private void updateNodeKeyAndQ(Node<V> node,
            PriorityQueue<Node<V>> priorityQ, double value)
    {
        setNodeKey(node, value);
        priorityQ.remove(node);
        priorityQ.add(node);
    }

    private void setNodeKey(Node<V> node, double value)
    {
        nodeKeys.put(node, Double.valueOf(value));
    }

    /**
     * 
     * @param graph
     * @param source
     * @return boolean indicating whether the graph contains negative cycles
     */
    public boolean singleSourceShortestPathBellmanFord(Graph<V> graph, V source)
    {
        validateGraphAndSourceNotNull(graph, source);     
        initializeAlgoState(graph);
        final int numNodes = graph.getNodes().size();
        Node<V> sourceNode = graph.getNode(source);
        
        if(sourceNode == null)
        {
            throw new IllegalArgumentException("source value not found in graph");
        }
        
        setNodeKey(sourceNode, 0.0);  
        //The shortest path can be at longest |V| - 1 edges
        for(int i = 1; i < numNodes -1; i++)
        {
            for(WeightedEdge<V> edge : graph.getEdges())
            {
                relaxEdge(edge);
            }
        }
        
        //first |V| - 1 passes should have converged on the shortest path
        //any further ability to relax indicates a negative cycle
        for(WeightedEdge<V> edge : graph.getEdges())
        {
            if(relaxEdge(edge))
            {
                return true;
            }
        }
        return false;
    }

    private boolean relaxEdge(WeightedEdge<V> edge)
    {
        final double totalCost = edge.getWeight() + getNodeKey(edge.getFrom()).doubleValue();
        if(totalCost < getNodeKey(edge.getTo()))
        {
            predecessors.put(edge.getTo().getValue(), edge.getFrom().getValue());
            setNodeKey(edge.getTo(), totalCost);
            return true;
        }
        return false;
    }
}
