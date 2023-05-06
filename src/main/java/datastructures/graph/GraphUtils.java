package datastructures.graph;

import datastructures.graph.search.DFS;

public class GraphUtils {

    private GraphUtils(){}

    public static <T> Boolean validateGraphIsADAG(Graph<T> g){
        var dfs = DFS.of(g);
        dfs.run();
        return dfs.getALLEdgesOfType(DFS.EdgeType.BACK_EDGE).isEmpty();
    }

}
