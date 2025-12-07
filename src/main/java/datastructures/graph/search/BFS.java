package datastructures.graph.search;

import datastructures.graph.directed.DirectedGraph;
import datastructures.graph.edge.GraphEdge;

import java.util.*;

import static datastructures.graph.search.GraphSearch.VertexAttributes.VertexColor.*;


public non-sealed class BFS<T> extends GraphSearch<T> {

    BFSVertexAttributes<T> source;

    private BFS(DirectedGraph<T> g) {
        super(g);
    }

    public  static <E> BFS<E> of(DirectedGraph<E> g) {
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
        this.source = getAttributesFor(source);
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
                var v = getAttributesFor(graphVertex.end());
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

    public Map<T , List<T>> singleSourceShortestPaths(){
        if(source==null){
            throw new IllegalStateException("Source must not be null , please call run with source vertex");
        }
        var result = new HashMap<T, List<T>>();
        for(T v : adjacencyMap.keySet()){
            result.put(v , getPathFromVertex(v));
        }
        return result;
    }
    protected List<T> getPathFromVertex(T v){
        var resultList = new LinkedList<T>();
        if(!singlePairReachability(v)) return resultList;
        var nextVertex = getAttributesFor(v);
        while(nextVertex!=null && nextVertex.key!=source.key){
            resultList.addFirst(nextVertex.key);
            nextVertex = nextVertex.predecessor;
        }
        resultList.addFirst(source.key);
        return resultList;
    }

    public boolean singlePairReachability(T end){
        return vertexAttributesMap.containsKey(end) && !getAttributesFor(end).isAtInfiniteDistance();
    }

    public List<T> singlePairShortestPath(T end){
        return getPathFromVertex(end);
    }

    private BFSVertexAttributes<T> getAttributesFor(T u){
        return (BFSVertexAttributes<T>) (vertexAttributesMap.get(u));
    }

    static class BFSVertexAttributes<E> extends VertexAttributes<E>{

        private static final int INFINITE_STUB = Integer.MIN_VALUE;
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
