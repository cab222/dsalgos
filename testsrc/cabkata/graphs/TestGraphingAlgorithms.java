package cabkata.graphs;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

import cabkata.graphs.Graph.EdgeType;
import cabkata.graphs.Graph.WeightedEdge;

public final class TestGraphingAlgorithms {

    @Test
    public void testToplogicalSort()
    {
        List<String> edgesAsString = Arrays.asList("watch,",
                "undershorts,shoes", "undershorts,pants", "pants,shoes",
                "pants,belt", "belt,jacket", "shirt,tie", "shirt,belt",
                "tie,jacket", "socks,shoes");

        /*
         * socks <17,18> undershorts <15,16> pants <13,14> watch <11,12> shoes
         * <9,10> shirt <1,8> belt <6,7> tie <2,5> jacket <3,4>
         */
        Graph<String> graph = Graph.graphFromStringEdges(edgesAsString,
                EdgeType.DIRECTED);
        GraphAlgorithms<String> graphAlgorithms = new GraphAlgorithms<String>();
        List<String> topoloicalSortedList = 
                graphAlgorithms.toplogicalSort(graph);
    
        assertEquals("shirt", topoloicalSortedList.get(0));
        assertEquals("undershorts", topoloicalSortedList.get(1));
        assertEquals("socks",  topoloicalSortedList.get(2));
        assertEquals("watch", topoloicalSortedList.get(3));
        assertEquals("pants", topoloicalSortedList.get(4));
        assertEquals("belt", topoloicalSortedList.get(5));
        assertEquals("tie", topoloicalSortedList.get(6));
        assertEquals("shoes", topoloicalSortedList.get(7));
        assertEquals("jacket", topoloicalSortedList.get(8));
        assertEquals(13, graphAlgorithms.getStart("socks"));
        assertEquals(14, graphAlgorithms.getFinish("socks"));
        assertEquals(5, graphAlgorithms.getStart("tie"));
        assertEquals(6, graphAlgorithms.getFinish("tie"));
    }

    @Test
    public void testBreadthFirstSearch()
    {
        List<String> edgesAsString = Arrays.asList("r,s", "r,v", "s,w", "w,t",
                "w,x", "t,x", "t,u", "x,u", "x,y", "u,y");
        Graph<String> graph = Graph.graphFromStringEdges(edgesAsString,
                EdgeType.UNDIRECTED);
        GraphAlgorithms<String> graphAlgorithms = new GraphAlgorithms<String>();
        graphAlgorithms.breadthFirstSearch(graph, "s");
        assertEquals(1, graphAlgorithms.getDistance("r"));
        assertEquals(1, graphAlgorithms.getDistance("w"));
        assertEquals(2, graphAlgorithms.getDistance("t"));
        assertEquals(2, graphAlgorithms.getDistance("x"));
        assertEquals(3, graphAlgorithms.getDistance("u"));
        assertEquals(3, graphAlgorithms.getDistance("y"));
    }

    @Test
    public void testMinimumSpanningTree()
    {
        List<String> edgesAsString = Arrays.asList("a,b,4", "a,h,8", "b,c,8",
                "b,h,11", "c,d,7", "c,f,4", "d,e,9", "d,f,14", "e,f,10",
                "f,g,2", "g,i,6", "g,h,1", "h,i,7", "i,c,2");
        Graph<String> graph = Graph.graphFromStringEdges(edgesAsString,
                EdgeType.UNDIRECTED);
        assertEquals(edgesAsString.size() * 2, graph.getEdges().size());
        GraphAlgorithms<String> graphAlgo = new GraphAlgorithms<String>();
        Set<WeightedEdge<String>> mstEdges = 
                graphAlgo.minimumSpanningTreeKruskal(graph);
        assertEquals(8, mstEdges.size());

        double sum = 0;
        for (WeightedEdge<String> edge : mstEdges)
        {
            sum += edge.getWeight();
        }
        assertEquals(37.0, sum, .0001);

        graphAlgo = new GraphAlgorithms<String>();
        mstEdges = graphAlgo.minimumSpanningTreePrim(graph, "a");
        sum = 0;
        for (WeightedEdge<String> edge : mstEdges)
        {
            sum += edge.getWeight();
        }
        assertEquals(37.0, sum, .0001);

    }
}
