package datastructures.graph;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

import static datastructures.graph.GraphSearch.VertexAttributes.VertexColor.*;

public non-sealed class BFS<T> extends GraphSearch<T>{

    BFSVertexAttributes<T> source;

    private BFS(Graph<T> g) {
        super(g);
    }

    public  static <E> BFS<E> of(Graph<E> g) {
        return new BFS<>(g);
    }

    private void initializeAttributes(T source){
        if(g == null)
            throw new IllegalStateException("Failed to Initialize run of bfs , without graph or vertex information");
        adjacencyMap = g.asAdjacencyMap();
        if(!adjacencyMap.containsKey(source)){
            throw new IllegalArgumentException("Source " + source + "is not a vertex in graph");
        }
        for (var v : adjacencyMap.keySet()) {
            vertexAttributesMap.put(v, new BFSVertexAttributes<>(v));
        }
    }

    public void run(T source){
        initializeAttributes(source);
        BFSVertexAttributes<T> sourceAttributes = getAttributesFor(source);
        sourceAttributes.distance = 0;
        Deque<BFSVertexAttributes<T>> vertexQueue = new LinkedList<>();
        vertexQueue.push(sourceAttributes);
        while (!vertexQueue.isEmpty()){
            var u = vertexQueue.poll();
            for (GraphEdge<T,T> graphVertex : adjacencyMap.getOrDefault(u.key , Collections.emptySet()) ){
                var v = getAttributesFor(graphVertex.end);
                if(v.colour == WHITE){
                    v.colour = GRAY;
                    v.distance = u.distance +1;
                    v.predecessor = u;
                    vertexQueue.push(v);
                }
            }
            u.colour = BLACK;
        }
    }

    private BFSVertexAttributes<T> getAttributesFor(T u){
        return (BFSVertexAttributes<T>) (vertexAttributesMap.get(u));
    }

    static class BFSVertexAttributes<E> extends VertexAttributes<E>{

        private static final int INFINITE_STUB = -1;//-1 is takes as substitute for INFINITE
        BFSVertexAttributes<E> predecessor;
        int distance;

        BFSVertexAttributes(E key) {
            super(key);
            predecessor = null;
            distance = INFINITE_STUB;//-1 taken as inf
        }

        boolean isAtInfiniteDistance() {
            return distance == INFINITE_STUB;
        }
    }
}
