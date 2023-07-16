package problemsessions.problemsessionone;

import algorithms.KarpRabinStringSearch;
import datastructures.common.PriorityQueue;
import datastructures.graph.DirectedGraph;
import datastructures.graph.GraphUtils;
import datastructures.graph.GraphVisualize;
import datastructures.graph.search.BFS;
import datastructures.graph.search.DFS;
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

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String LOG_INFO = "{}"; //needed for logging message stub

    public static void main(String[] args) {
        heapAsPriorityQTest();
    }

    public static void graphTest(){

        var listOfPairs = new LinkedList<Integer[]>();
        for(int i = 0; i< 100 ; i++){
            listOfPairs.add(new Integer[]{
                    RandomUtils.nextInt(1,60) , RandomUtils.nextInt(70,80)
            });
        }
        var graph = DirectedGraph.fromPairs(listOfPairs);
        logger.info("GRAPH {}" , graph);
        var dfs = DFS.of(graph);
        dfs.run();
        var bfs = BFS.of(graph);
        bfs.run(listOfPairs.get(RandomUtils.nextInt(1,listOfPairs.size()))[0]);
        logger.info("BFS_SSSP {}" , bfs.singleSourceShortestPaths());
        logger.info("DFC TOPOLOGICAL_SORT {}" , dfs.topologicalSort());
        logger.info(LOG_INFO , GraphUtils.validateGraphIsADAG(graph));
        var visualGraph = GraphVisualize.of(graph)
                .withName("GRAPH_TEST").build();
        visualGraph.display();
    }

    public static void karpRabinTest() throws IOException {
        var inputString = "hello how are you , with advent of chatGPT every new thing feels like sunday, i dont know what the hell" +
                " i am writing here but that s write , putting chatGPT here as well just to check chatGPT";
        var searchString = "chatGPT";
        var cell = KarpRabinStringSearch.search(searchString,inputString);
        logger.info(LOG_INFO,cell.offsets);
    }

    public static void  radixSortTest(){
        var integersArray = new ArrayList<Integer>(1000);
        for(int i = 0 ; i < 1000 ; i++){
            integersArray.add(RandomUtils.nextInt(10000000,1000000000));
        }
        logger.info("ARRAY:BEFORE SORT: {}", integersArray);
        logger.info("RADIX SORT: {}" , RadixSort.sort(integersArray));

    }

    public static void  heapSortTest(){
        var integersArray = new ArrayList<Integer>(1000);
        for(int i = 0 ; i < 1000 ; i++){
            integersArray.add(RandomUtils.nextInt(100,1000));
        }

        logger.info("ARRAY:BEFORE SORT: {}", integersArray);
        var a = integersArray.toArray(new Integer[]{});
        logger.info("HEAP SORT: {}" , Arrays.toString(HeapSort.sort(a)));
    }

    public static void  heapAsPriorityQTest(){
        var integersArray = List.of(1,2,8,9,10,3,4,5,6,7);
        logger.info("INTEGERS ARRAY: {}" , integersArray);
        PriorityQueue<Integer> q = new MaxHeap<>(integersArray);
        var GET_MAX = "GET MAX {}";
        var DELETE_MAX = "DELETE MAX {}";
        var INSERT_NEW = "INSERT {} new Max {}";
        logger.info(GET_MAX , q.findMax() );
        logger.info(DELETE_MAX, q.deleteMax());
        logger.info(GET_MAX , q.findMax());
        q.insert(12);
        logger.info(INSERT_NEW , 12 , q.findMax());


    }

    public static void binarySearchTreeTest(){
        var bst = new SetBinaryTree<Integer>(List.of(5,8,2,4,3,6,1));
        logger.info("BST ORDER_TRAVERSAL: {}" , bst.asList());
    }

    public static void avlSearchTreeTest(){
        var bst = new AVLSetBinaryTree<Integer>(List.of(9,6,55, 22, 5,8,2,4,3,6,1));
        logger.info("BST ORDER_TRAVERSAL: {}" , bst.asList());
        GraphVisualize.of(bst).withName("AVL TREE GRAPH").build()
                .display();
    }

}

