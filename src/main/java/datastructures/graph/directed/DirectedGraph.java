package datastructures.graph.directed;


import datastructures.common.Graph;
import datastructures.graph.edge.GraphEdge;

import java.util.*;

/**
 * graph is a collection of ordered pair , V -> vertices
 * (u,v) one order pair is called edge
 * Directed Graph
 */
public class DirectedGraph<T> implements Graph<T> {

    private Long edges;
    /**
     * maintaining a map , with vertices and their outgoing vertices .
     */
    private final HashMap<T , Set<GraphEdge<T,T>>> adjacencyMap;

    public static <E> DirectedGraph<E> fromPairs(List<E[]> pairs){
        return new DirectedGraph<>(pairs);
    }

    public static  <E> DirectedGraph<E> emptyGraph() {
        return new DirectedGraph<>();
    }
    protected DirectedGraph(){
        adjacencyMap = new HashMap<>();
    }

    private DirectedGraph(List<T[]> pairs){
        this();
        for(T[] pair : pairs){
            addEdge(new GraphEdge<>(pair[0],pair[1]));
        }
    }

    public void addVertex(T v){
        if(adjacencyMap.containsKey(v)) return;
        adjacencyMap.put(v , new LinkedHashSet<>());
    }

    public void addEdge(GraphEdge<T,T> edge){
        addVertex(edge.start());
        addVertex(edge.end());
        adjPlus(edge.start()).add(edge);
        calcEdges();
    }

    /**
     * adjPlus mean outgoing vertices, that represented by set.
     */
    private Set<GraphEdge<T,T>> adjPlus(T u){
        return this.adjacencyMap.getOrDefault(u , new LinkedHashSet<>());
    }

    public List<GraphEdge<T,T>> asListOfEdges(){
        var res = new  LinkedList<GraphEdge<T,T>>();
        for(Map.Entry<T,Set<GraphEdge<T,T>>> entry : this.adjacencyMap.entrySet()){
            res.addAll(entry.getValue());
        }
        return res;
    }

    @Override
    public Map<T , Set<GraphEdge<T,T>>> asAdjacencyMap(){
        return Map.copyOf(adjacencyMap);
    }

    @Override
    public long vertices() {
        return adjacencyMap.size();
    }

    @Override
    public long edges() {
        return edges;
    }

    public boolean containsVertex(T t){
        return adjacencyMap.containsKey(t);
    }


    @Override
    public String toString() {
        var sb = new StringBuilder("{");
        sb.append("\r\n");
        adjacencyMap.forEach((key, value) -> sb.append("  ").append(key).append("=").append(value).append("\r\n"));
        sb.append("}");
        return sb.toString();
    }

    private void calcEdges(){
        this.edges = adjacencyMap.values()
                .stream()
                .map(a -> Long.valueOf(a.size()))
                .reduce(Long::sum)
                .orElse(0L);

    }


}
