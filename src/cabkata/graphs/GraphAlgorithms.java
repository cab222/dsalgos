package cabkata.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import cabkata.graphs.Graph.Node;
import cabkata.graphs.Graph.WeightedEdge;

public class GraphAlgorithms<V> {
	private final Map<V, NodeSate> nodeState = new HashMap<V, NodeSate>();
    private final Map<V, V> predecessors = new HashMap<V, V>();
    private final Map<V, Integer> finishTime = new HashMap<V, Integer>(); 
    private final Map<V, Integer> startTime = new HashMap<V, Integer>();
    private final Map<V, Integer> distances = new HashMap<V, Integer>();
    private int time = 0;
    private static enum NodeSate
    {
    	UNVISITED,
    	VISITED,
    	FINISHED
    }
	
	/**
	 * Performs a topological sort
	 * 
	 * @param graph
	 * @return toplogical sort of graph elements V
	 */
	public List<V> toplogicalSort(Graph<V> graph){
		List<V> topologicalSort = new ArrayList<V>();
		initializeGraph(graph);

		for(Node<V> node : graph.getNodes())
		{
			if(isNodeUnvisted(node))
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
		for(WeightedEdge<V> edge : node.getAdjacentyList())
		{
			Node<V> adjacentNode = edge.getTo(); 
			if(isNodeUnvisted(adjacentNode))
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
		
		for(Node<V> node : graph.getNodes())
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
		
		while(!q.isEmpty())
		{
			Node<V> currentNode = q.remove();
			for(WeightedEdge<V> edge : currentNode.getAdjacentyList())
			{
				Node<V> adjacentNode = edge.getTo();
				if(isNodeUnvisted(adjacentNode))
				{
					nodeState.put(adjacentNode.getValue(), NodeSate.VISITED);
					markPredecessor(adjacentNode, currentNode);
					distances.put(adjacentNode.getValue(), 
							Integer.valueOf(distances.get(currentNode.getValue()).intValue() + 1));
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
}
