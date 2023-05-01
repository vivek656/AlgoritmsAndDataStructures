package problemsessionone;

import algorithms.KarpRabinStringSearch;
import datastructures.graph.Graph;
import datastructures.graph.search.BFS;
import datastructures.graph.search.DFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String LOG_INFO = "{}"; //needed for logging message stub

    public static void main(String[] args) {

       graphTest();

    }

    public static void graphTest(){

        List<Integer[]> pairs = List.of(new Integer[]{1,2} , new Integer[]{0,1}  , new Integer[]{2,0} , new Integer[]{3,4} ,
        new Integer[]{2,3} ,new Integer[]{5,6} , new Integer[]{7,8} , new Integer[]{6,8}  , new Integer[]{9,0} ,
                new Integer[]{7,9} , new Integer[]{10,13} , new Integer[]{13,20});
        var graph = Graph.fromPairs(pairs);
        logger.info(LOG_INFO , graph);
        var dfs = DFS.of(graph);
        dfs.run();
        var bfs = BFS.of(graph);
        bfs.run(7);
        logger.info(LOG_INFO , bfs.singleSourceShortestPaths());


    }

    public static void karpRabinTest() throws IOException {
        var inputString = "hello how are you , with advent of chatGPT every new thing feels like sunday, i dont know what the hell" +
                " i am writing here but that s write , putting chatGPT here as well just to check chatGPT";
        var searchString = "chatGPT";
        var cell = KarpRabinStringSearch.search(searchString,inputString);
        logger.info(LOG_INFO,cell.offsets);
    }

}

