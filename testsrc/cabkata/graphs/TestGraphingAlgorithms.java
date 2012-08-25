package cabkata.graphs;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;

import org.junit.Test;

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
		Graph<String> graph = Graph.graphFromStringEdges(edgesAsString);
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
}
