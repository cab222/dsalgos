package cabkata.graphs;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @param <V>
 */
public class Graph<V> {
	private final Map<V, Node<V>> nodes = new HashMap<V, Node<V>>();
	private final Set<WeightedEdge<V>> edges = new HashSet<WeightedEdge<V>>();

	public static enum EdgeType{
		DIRECTED,
		UNDIRECTED
	}
	/**
	 * Returns the collection of nodes in the graph
	 * note that modifiying this list has no effect on the graph as it's
	 * copy. Limiting mutable state!
	 * @return Collection<Node<V>>
	 */
	public Collection<Node<V>> getNodes()
	{
		Set<Node<V>> nodeCopy = new HashSet<Node<V>>(nodes.values());
		return nodeCopy;
	}
	
	public Node<V> getNode(V v)
	{
		return nodes.get(v);
	}
	
	/**
	 * Returns the collection of edges in the graph
	 * note that modifiying this list has no effect on the graph as it's
	 * copy. Limiting mutable state!
	 * @return Collection<WeightedEdge<V>>
	 */
	public Collection<WeightedEdge<V>> getEdges()
	{
		Set<WeightedEdge<V>> edgeCopy = new HashSet<WeightedEdge<V>>(edges);
		return edgeCopy;
	}
	
	public static class WeightedEdge<T>{
		private Node<T> from;
		private Node<T> to;
		private double weight;
		
		private WeightedEdge(Node<T> from, Node<T> to, double weight){
			this.from = from;
			this.to = to;
			this.weight = weight;
		}
		
		public Node<T> getTo()
		{
			return to;
		}
		
		public Node<T> getFrom()
		{
			return from;
		}
		
		public double getWeight()
		{
			return weight;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if(this == o)
			{
				return true;
			}
			
			if(!(o instanceof WeightedEdge))
			{
				return false;
			}
			
			@SuppressWarnings("unchecked")
			WeightedEdge<T> obj = (WeightedEdge<T>)o;						
			return this.from.equals(obj.from) && this.to.equals(obj.to);
		}
		
		@Override
		public int hashCode()
		{
			int result = 17;
			result = 37*result + this.from.hashCode();
			result = 37*result + this.to.hashCode();
			return result;
		}
		
		@Override
		public String toString()
		{
			return new StringBuffer("<").append(from.value).append(",").
					append(to.value).append(",").append(weight).append(">").toString();
		}
	}
	
	public static class Node<T>{
		private T value;
		//TODO: is incident the right term? i feel like adjacent refers to the node
		private Set<WeightedEdge<T>> incidentEdges; 
		private Node(T value)
		{
			this.value = value;
			this.incidentEdges = new HashSet<WeightedEdge<T>>();
		}
		
		public T getValue()
		{
			return value;
		}
		
		public Collection<WeightedEdge<T>> getAdjacentyList()
		{
			Set<WeightedEdge<T>> edgeCopy = new HashSet<WeightedEdge<T>>(incidentEdges);
			return edgeCopy;
		}
		
		@Override
		public boolean equals(Object o)
		{
			if(this == o)
			{
				return true;
			}
			
			if(!(o instanceof Node))
			{
				return false;
			}
			
			@SuppressWarnings("unchecked")
			Node<T> obj = (Node<T>)o;						
			return this.value.equals(obj.value);
		}
		
		@Override
		public int hashCode()
		{
			int result = 17;
			result = 37*result + this.value.hashCode();
			return result;
		}
		
		@Override
		public String toString()
		{
			return value.toString();
		}
	}
		
	public static Graph<String> graphFromStringEdges(Collection<String> egdgesAsStrings, EdgeType edgeType)
	{
		Graph<String> graph = new Graph<String>();
		
		for(String edge : egdgesAsStrings)
		{
			String[] parts = edge.split(",");
			if(parts.length < 1 || parts.length > 3)
			{
				throw new IllegalArgumentException("Edges format: {fromValue,toValue,OptionalWeight}");
			}		
			
			//TODO: If I really want this to be generic, I should have some
			//V.valueOf(String string)
			String fromValue = parts.length > 0 ? parts[0] : null;
			String toValue =  parts.length > 1 ? parts[1] : null;
			
			Node<String> fromNode = null;
			Node<String> toNode = null;
			double weight = 0.0;
			
			if(fromValue != null)
			{
				fromNode = createNode(graph, fromValue);
			}
			
			if(toValue != null)
			{
				toNode = createNode(graph, toValue);
			}
			
			if(fromNode != null && toNode != null)
			{
				if(parts.length == 3)
				{
					weight = Double.valueOf(parts[2]);
				}
				addEdge(graph, fromNode, toNode, weight);
				
				if(edgeType.equals(EdgeType.UNDIRECTED))
				{
					addEdge(graph, toNode, fromNode, weight);
				}
			}
		}
		return graph;
	}

	private static void addEdge(Graph<String> graph, Node<String> fromNode,	Node<String> toNode, double weight)
	{
		WeightedEdge<String> edge1 = new WeightedEdge<String>(fromNode, toNode, weight);
		if(!graph.edges.contains(edge1))
		{
			graph.edges.add(edge1);
			fromNode.incidentEdges.add(edge1);
		}
	}

	private static Node<String> createNode(Graph<String> graph, String value)
	{
		Node<String> fromNode;
		if(graph.nodes.containsKey(value))
		{
			fromNode = graph.nodes.get(value);
		}
		else
		{
			fromNode = new Node<String>(value);
			graph.nodes.put(value, fromNode);
		}
		return fromNode;
	}
	
	private Graph()
	{
	}
}
