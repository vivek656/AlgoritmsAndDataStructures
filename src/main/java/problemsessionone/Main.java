package problemsessionone;

import algorithms.KarpRabinStringSearch;
import datastructures.graph.Graph;
import datastructures.graph.GraphUtils;
import datastructures.graph.GraphVisualize;
import datastructures.graph.search.BFS;
import datastructures.graph.search.DFS;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String LOG_INFO = "{}"; //needed for logging message stub

    public static void main(String[] args) {
       graphTest();

    }

    public static void graphTest(){

        var listOfPairs = new LinkedList<Integer[]>();
        for(int i = 0; i< 100 ; i++){
            listOfPairs.add(new Integer[]{
                    RandomUtils.nextInt(1,60) , RandomUtils.nextInt(70,80)
            });
        }
        var graph = Graph.fromPairs(listOfPairs);
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

}

