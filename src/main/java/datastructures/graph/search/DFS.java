package datastructures.graph.search;

import datastructures.graph.Graph;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static datastructures.graph.search.GraphSearch.VertexAttributes.VertexColor.*;

public non-sealed class DFS<T> extends GraphSearch<T> {

    private int globalTimer = 0;
    private DFS(Graph<T> g) {
        super(g);
    }


    private void initializeAttributes(){
        if(g == null)
            throw new IllegalStateException("Failed to Initialize run of dfs , without graph or vertex information");
        adjacencyMap = g.asAdjacencyMap();
        vertexAttributesMap.clear();
        for (var v : adjacencyMap.keySet()) {
            vertexAttributesMap.put(v, new DFSVertexAttributes<>(v));
        }
    }

    static class DFSVertexAttributes<E> extends VertexAttributes<E> {

        DFSVertexAttributes<E> predecessor;
        int discoveryTime;
        int finishTime;

        DFSVertexAttributes(E key) {
            super(key);
            predecessor = null;
            discoveryTime = -1;
            finishTime = -1;
        }


    }

    @NotNull
    @Contract("_ -> new")
    public static <E> DFS<E> of(Graph<E> g) {
        return new DFS<>(g);
    }

    public void run() {
        if(g == null)
            throw new IllegalStateException("Cant run dfs , without graph or vertex information");
        initializeAttributes();
        globalTimer = 0;
        for (var entry : vertexAttributesMap.entrySet()){
            if(entry.getValue().colour == WHITE){
                dfsVisit(getAttributesFor((entry.getKey())));
            }
        }
    }

    private DFSVertexAttributes<T> getAttributesFor(T u){
        return (DFSVertexAttributes<T>) vertexAttributesMap.get(u);
    }

    private void dfsVisit(@NotNull DFSVertexAttributes<T> u){
        globalTimer++;
        u.discoveryTime = globalTimer;
        u.colour = GRAY;

        for (var outgoingVertex : adjacencyMap.getOrDefault(u.key , Collections.emptySet())){
            var v = getAttributesFor(outgoingVertex.getEnd());
            if(v.colour==WHITE){
                v.predecessor = u;
                dfsVisit(v);
            }
        }
        globalTimer++;
        u.finishTime = globalTimer;
        u.colour=BLACK;
    }


}
