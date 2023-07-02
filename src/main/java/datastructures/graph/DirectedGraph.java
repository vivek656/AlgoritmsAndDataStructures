package datastructures.graph;


import datastructures.common.Graph;

import java.util.*;

/**
 * graph is a collection of ordered pair , V -> vertices
 * (u,v) one order pair is called edge
 * Directed Graph
 */
public class DirectedGraph<T> implements Graph<T> {

    /**
     * maintaining a map , with vertices and their outgoing vertices .
     */
    private final HashMap<T , Set<GraphEdge<T,T>>> adjacencyMap;



    public static <E> DirectedGraph<E> fromPairs(List<E[]> pairs){
        return new DirectedGraph<>(pairs);
    }
    private DirectedGraph(){
        adjacencyMap = new HashMap<>();
    }

    private DirectedGraph(List<T[]> pairs){
        this();
        for(T[] pair : pairs){
            addEdge(new GraphEdge<>(pair[0],pair[1]));
        }
    }

    private void addVertex(T v){
        if(adjacencyMap.containsKey(v)) return;
        adjacencyMap.put(v , new LinkedHashSet<>());
    }

    private void addEdge(GraphEdge<T,T> edge){
        addVertex(edge.getStart());
        addVertex(edge.getEnd());
        adjPlus(edge.getStart()).add(edge);
    }

    /**
     * adjPlus mean outgoing vertices, that represented by set.
     */
    private Set<GraphEdge<T,T>> adjPlus(T u){
        return this.adjacencyMap.getOrDefault(u , new LinkedHashSet<>());
    }

    public List<List<T>> asListOfEdges(){
        var res = new  LinkedList<List<T>>();
        for(Map.Entry<T,Set<GraphEdge<T,T>>> entry : this.adjacencyMap.entrySet()){
            entry.getValue().forEach(val -> res.add(List.of(entry.getKey() , val.getEnd())));
        }
        return res;
    }

    @Override
    public Map<T , Set<GraphEdge<T,T>>> asAdjacencyMap(){
        return Map.copyOf(adjacencyMap);
    }


    @Override
    public String toString() {
        var sb = new StringBuilder("{");
        sb.append("\r\n");
        adjacencyMap.forEach((key, value) -> sb.append("  ").append(key).append("=").append(value).append("\r\n"));
        sb.append("}");
        return sb.toString();
    }
}
