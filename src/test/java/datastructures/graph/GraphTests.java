package datastructures.graph;

import common.AbstractAlgoAndDsTestBase;
import datastructures.graph.search.BFS;
import datastructures.graph.search.DFS;
import datastructures.graph.search.weightedsearch.BellmanFordWeightedSearch;
import datastructures.graph.search.weightedsearch.DAGWeightedSearch;
import datastructures.graph.search.weightedsearch.DijkstraWeightedSearch;
import datastructures.graph.search.weightedsearch.FloyedWarshal;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GraphTests extends AbstractAlgoAndDsTestBase {

    @Test
    void graphTest() {

        var listOfPairs = new LinkedList<Integer[]>();
        for (int i = 0; i < 100; i++) {
            listOfPairs.add(new Integer[]{
                    RandomUtils.nextInt(1, 60), RandomUtils.nextInt(70, 80)
            });

        }
        var graph = DirectedGraph.fromPairs(listOfPairs);
        logger.info("GRAPH {}", graph);
        var dfs = DFS.of(graph);
        dfs.run();
        var bfs = BFS.of(graph);
        bfs.run(listOfPairs.get(RandomUtils.nextInt(1, listOfPairs.size()))[0]);
        logger.info("BFS_SSSP {}", bfs.singleSourceShortestPaths());
        logger.info("DFS TOPOLOGICAL_SORT {}", dfs.topologicalSort());
        logger.info(LOG_INFO, GraphUtils.validateGraphIsADAG(graph));
        var visualGraph = GraphVisualize.of(graph)
                .withName("GRAPH_TEST").build();
        visualGraph.display();
    }

    @Test
    void dfsTest() {
        var listOfPair = List.of(
                new String[]{"a", "b"}, new String[]{"a", "c"},
                new String[]{"a", "d"}, new String[]{"b", "c"},
                new String[]{"c", "e"}, new String[]{"e", "k"},
                new String[]{"l", "a"}, new String[]{"b", "o"},
                new String[]{"c", "n"}, new String[]{"n", "p"},
                new String[]{"o", "f"}, new String[]{"f", "i"}
        );
        var graph = DirectedGraph.fromPairs(listOfPair);
        var dfs = DFS.of(graph);
        IntStream.range(0, 5).forEach(a -> {
            logger.info("DFS RUN no: {}", a);
            dfs.run();
            logger.info("DFS TOPOLOGICAL SORT: {}", dfs.topologicalSort());
        });
        BiFunction<String, String, Long> function = (a, b) -> a.codePoints().sum() + (long) b.codePoints().sum();

        var weightedSearch = DAGWeightedSearch.of(graph, function);

        weightedSearch.run("a");
        logger.info("PATH AND WEIGHT: {}", weightedSearch.createPath("p"));

        var weightedSearch2 = BellmanFordWeightedSearch.of(graph).withWeightFunction(function);
        weightedSearch2.run("a");

        logger.info("BellmanFord PATH AND WEIGHT: {}", weightedSearch2.createPath("p"));
    }

    @Test
    void weightedShortestPathTest() {
        var listOfPair = List.of(
                new String[]{"a", "b"}, new String[]{"a", "c"},
                new String[]{"a", "d"}, new String[]{"b", "c"},
                new String[]{"c", "e"}, new String[]{"e", "k"},
                new String[]{"l", "a"}, new String[]{"b", "o"},
                new String[]{"c", "n"}, new String[]{"n", "p"},
                new String[]{"o", "f"}, new String[]{"f", "i"},
                new String[]{"f" , "b"} , new String[]{"a" , "y"},
                new String[]{"y" , "x"} , new String[]{"y" , "z"}
        );
        var graph = DirectedGraph.fromPairs(listOfPair);
        var weightedMapping = new HashMap<String, Integer>();
        weightedMapping.put("ab", 2);weightedMapping.put("ac", 5);
        weightedMapping.put("ad", -9);weightedMapping.put("ce", 25);
        weightedMapping.put("ek", -5);weightedMapping.put("la", -6);
        weightedMapping.put("bo", 1);weightedMapping.put("cn", 12);
        weightedMapping.put("np", 100);weightedMapping.put("of", -23);
        weightedMapping.put("fb", 15);weightedMapping.put("bc", 5);


        BiFunction<String, String, Long> longFunction = (a, b) -> a.codePoints().sum() + (long) b.codePoints().sum();
        BiFunction<String, String, Long> function = (a, b) -> weightedMapping.getOrDefault(a+b , longFunction.apply(a,b).intValue()).longValue();

        try {
            var weightedSearch = DAGWeightedSearch.of(graph, function);
            weightedSearch.run("a");
            logger.info("PATH AND WEIGHT: {}", weightedSearch.createPath("p"));
        }catch (Exception e){
            logger.error("DFS gave error", e);
        }

        var dijstraWeightedMapping = new HashMap<String,Integer>();
        weightedMapping.forEach((key,value) -> dijstraWeightedMapping.put(key , Math.abs(value)));
        BiFunction<String, String, Long> function2 = (a, b) -> dijstraWeightedMapping.getOrDefault(a+b , longFunction.apply(a,b).intValue()).longValue();

        var weightedSearch2 = BellmanFordWeightedSearch.of(graph).withWeightFunction(function2);
        weightedSearch2.run("a");
        graph.asAdjacencyMap().keySet().forEach(a ->
                logger.info("BellmanFord PATH AND WEIGHT: {}", weightedSearch2.createPath(a))
        );

        var weightedSearch3 = DijkstraWeightedSearch.of(graph).withWeightFunction(function2);
        weightedSearch3.run("a");
        graph.asAdjacencyMap().keySet().forEach(a ->
                logger.info("Dijstra PATH AND WEIGHT: {}", weightedSearch3.createPath(a))
        );

        var floyedWarshallTests = FloyedWarshal.of(graph).withWeightFunction(function2);
        floyedWarshallTests.run("a");
        graph.asAdjacencyMap().keySet().forEach(a ->
                logger.info("Floyed PATH AND WEIGHT: btw a and {} is {}",a,
                        floyedWarshallTests.getPathWeight("a" , a))
        );
    }

}
