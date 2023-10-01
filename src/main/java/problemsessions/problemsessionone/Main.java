package problemsessions.problemsessionone;

import algorithms.KarpRabinStringSearch;
import datastructures.common.PriorityQueue;
import datastructures.graph.DirectedGraph;
import datastructures.graph.GraphUtils;
import datastructures.graph.GraphVisualize;
import datastructures.graph.search.BFS;
import datastructures.graph.search.DFS;
import datastructures.graph.search.weightedsearch.BellmanFordWeightedSearch;
import datastructures.graph.search.weightedsearch.DAGWeightedSearch;
import datastructures.graph.search.weightedsearch.DijkstraWeightedSearch;
import datastructures.heap.MaxHeap;
import datastructures.tree.AVLSetBinaryTree;
import datastructures.tree.SetBinaryTree;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sort.HeapSort;
import sort.RadixSort;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String LOG_INFO = "{}"; //needed for logging message stub

    public static void main(String[] args) {
        weightedShortestPathTest();
    }

    public static void graphTest() {

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

    public static void dfsTest() {
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

    public static void weightedShortestPathTest() {
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
        var weightedSearch2 = BellmanFordWeightedSearch.of(graph).withWeightFunction(function);
        weightedSearch2.run("a");
        graph.asAdjacencyMap().keySet().forEach(a ->
                logger.info("BellmanFord PATH AND WEIGHT: {}", weightedSearch2.createPath(a))
        );

        var dijstraWeightedMapping = new HashMap<String,Integer>();
        weightedMapping.forEach((key,value) -> dijstraWeightedMapping.put(key , Math.abs(value)));
        BiFunction<String, String, Long> function2 = (a, b) -> dijstraWeightedMapping.getOrDefault(a+b , longFunction.apply(a,b).intValue()).longValue();

        var weightedSearch3 = DijkstraWeightedSearch.of(graph).withWeightFunction(function2);
        weightedSearch3.run("a");
        graph.asAdjacencyMap().keySet().forEach(a ->
                logger.info("Dijstra PATH AND WEIGHT: {}", weightedSearch3.createPath(a))
        );

    }

    public static void karpRabinTest() throws IOException {
        var inputString = "hello how are you , with advent of chatGPT every new thing feels like sunday, i dont know what the hell" +
                " i am writing here but that s write , putting chatGPT here as well just to check chatGPT";
        var searchString = "chatGPT";
        var cell = KarpRabinStringSearch.search(searchString, inputString);
        logger.info(LOG_INFO, cell.offsets);
    }

    public static void radixSortTest() {
        var integersArray = new ArrayList<Integer>(1000);
        for (int i = 0; i < 1000; i++) {
            integersArray.add(RandomUtils.nextInt(10000000, 1000000000));
        }
        logger.info("ARRAY:BEFORE SORT: {}", integersArray);
        logger.info("RADIX SORT: {}", RadixSort.sort(integersArray));

    }

    public static void heapSortTest() {
        var integersArray = new ArrayList<Integer>(1000);
        for (int i = 0; i < 1000; i++) {
            integersArray.add(RandomUtils.nextInt(100, 1000));
        }

        logger.info("ARRAY:BEFORE SORT: {}", integersArray);
        var a = integersArray.toArray(new Integer[]{});
        logger.info("HEAP SORT: {}", Arrays.toString(HeapSort.sort(a)));
    }

    public static void heapAsPriorityQTest() {
        var integersArray = List.of(1, 2, 8, 9, 10, 3, 4, 5, 6, 7);
        logger.info("INTEGERS ARRAY: {}", integersArray);
        PriorityQueue<Integer> q = new MaxHeap<>(integersArray);
        var GET_MAX = "GET MAX {}";
        var DELETE_MAX = "DELETE MAX {}";
        var INSERT_NEW = "INSERT {} new Max {}";
        logger.info(GET_MAX, q.peek());
        logger.info(DELETE_MAX, q.poll());
        logger.info(GET_MAX, q.peek());
        q.insert(12);
        logger.info(INSERT_NEW, 12, q.poll());


    }

    public static void binarySearchTreeTest() {
        var bst = new SetBinaryTree<Integer>(List.of(5, 8, 2, 4, 3, 6, 1));
        logger.info("BST ORDER_TRAVERSAL: {}", bst.asList());
    }

    public static void avlSearchTreeTest() {
        var bst = new AVLSetBinaryTree<Integer>(List.of(9, 6, 55, 22, 5, 8, 2, 4, 3, 6, 1));
        logger.info("BST ORDER_TRAVERSAL: {}", bst.asList());
        GraphVisualize.of(bst).withName("AVL TREE GRAPH").build()
                .display();
    }

}

