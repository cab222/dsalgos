package cabkata.graphs;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import cabkata.graphs.Graph.EdgeType;
import cabkata.graphs.Graph.WeightedEdge;

public class TestGraphingAlgorithms {

	@Test
	public void testToplogicalSort()
	{
		List<String> edgesAsString = Arrays.asList(
				"watch,",
				"undershorts,shoes",
				"undershorts,pants",
				"pants,shoes",
				"pants,belt",
				"belt,jacket",
				"shirt,tie",
				"shirt,belt",
				"tie,jacket",
				"socks,shoes"
				 );

		/*
		 * socks <17,18>
		  undershorts <15,16>
		  pants <13,14>
		  watch <11,12>
          shoes <9,10>
		  shirt <1,8>
		  belt <6,7>
		  tie <2,5>		
          jacket <3,4>
        */		
		Graph<String> graph = Graph.graphFromStringEdges(edgesAsString, EdgeType.DIRECTED);
		GraphAlgorithms<String> graphAlgorithms = new GraphAlgorithms<String>();
		List<String> topoloicalSortedList = graphAlgorithms.toplogicalSort(graph);
		assertEquals(topoloicalSortedList.get(0), "socks");
		assertEquals(topoloicalSortedList.get(1), "undershorts");
		assertEquals(topoloicalSortedList.get(2), "pants");
		assertEquals(topoloicalSortedList.get(3), "watch");
		assertEquals(topoloicalSortedList.get(4), "shoes");
		assertEquals(topoloicalSortedList.get(5), "shirt");
		assertEquals(topoloicalSortedList.get(6), "belt");
		assertEquals(topoloicalSortedList.get(7), "tie");
		assertEquals(topoloicalSortedList.get(8), "jacket");
		assertEquals(graphAlgorithms.getStart("socks"), 17);
		assertEquals(graphAlgorithms.getFinish("socks"), 18);
		assertEquals(graphAlgorithms.getStart("tie"), 2);
		assertEquals(graphAlgorithms.getFinish("tie"), 5);
	}
	
	@Test
	public void testBreadthFirstSearch()
	{
		List<String> edgesAsString = Arrays.asList(
				"r,s", 
				"r,v", 
				"s,w", 
				"w,t", 
				"w,x", 
				"t,x", 
				"t,u", 
				"x,u", 
				"x,y", 
				"u,y"
				 );
		Graph<String> graph = Graph.graphFromStringEdges(edgesAsString, EdgeType.UNDIRECTED);
		GraphAlgorithms<String> graphAlgorithms = new GraphAlgorithms<String>();
		graphAlgorithms.breadthFirstSearch(graph, "s");
		assertEquals(graphAlgorithms.getDistance("r"), 1);
		assertEquals(graphAlgorithms.getDistance("w"), 1);
		assertEquals(graphAlgorithms.getDistance("t"), 2);
		assertEquals(graphAlgorithms.getDistance("x"), 2);
		assertEquals(graphAlgorithms.getDistance("u"), 3);
		assertEquals(graphAlgorithms.getDistance("y"), 3);	
	}
	
	@Test 
	public void testMinimumSpanningTreeKruskal()
	{
		List<String> edgesAsString = Arrays.asList(
				"a,b,4", 
				"a,h,8", 
				"b,c,8",
				"b,h,11",
				"c,d,7",
				"c,f,4",
				"d,e,9",
				"d,f,14",
				"e,f,10",  
				"f,g,2", 
				"g,i,6", 
				"g,h,1",
				"h,i,7",
				"i,c,2"
				 );
		Graph<String> graph = Graph.graphFromStringEdges(edgesAsString, EdgeType.UNDIRECTED);	
		assertEquals(edgesAsString.size()*2, graph.getEdges().size());
		GraphAlgorithms<String> graphAlgo = new GraphAlgorithms<String>();
		Set<WeightedEdge<String>> mstEdges = graphAlgo.minimumSpanningTreeKruskal(graph);
		assertEquals(mstEdges.size(), 8);
		
		double sum = 0;
		for(WeightedEdge<String> edge : mstEdges)
		{
			System.out.println(edge);
			sum += edge.getWeight();
		}
		
		assertEquals(37.0, sum, .0001);
	}
}
