package datastructures.graph.undirected;

import datastructures.graph.directed.DirectedGraph;
import datastructures.graph.directed.WeightedDirectedGraph;
import datastructures.graph.edge.GraphEdge;

import java.util.*;

public class UndirectedGraph<T> extends WeightedDirectedGraph<T> {

    HashMap<T , HashMap<T,Long>> edgeWeights = new HashMap<>();
    static final String edgeWeightFunction = "UndirectedGraph_edgeWeight";

    public UndirectedGraph() {
        super(DirectedGraph.emptyGraph());
        addWeightedFunction(edgeWeightFunction , this::getEdgeWeight);
    }

    /**
     * Create defensive copy of the graph
     * @param graph
     */
    public UndirectedGraph(UndirectedGraph<T> graph) {
        super(graph);
        // this is needed so that new function correctly call the map in this class
        addWeightedFunction(edgeWeightFunction , this::getEdgeWeight);
        this.edgeWeights.putAll(graph.edgeWeights);
    }

    @Override
    public void addEdge(GraphEdge<T,T> edge){
        addEdge(edge.start(), edge.end());
    }

    public void addEdge(T start, T end) {
        GraphEdge<T,T> edge = new GraphEdge<>(start, end);
        addEdge(edge);
        GraphEdge<T,T> edge2 = new GraphEdge<>(end, start);
        addEdge(edge2);
        edgeWeights.putIfAbsent(start, new HashMap<>());
        edgeWeights.putIfAbsent(end, new HashMap<>());
        edgeWeights.get(start).putIfAbsent(end, 1L);
        edgeWeights.get(end).putIfAbsent(start, 1L);
    }


    private Long getEdgeWeight(T start, T end) {
        HashMap<T,Long> map = edgeWeights.get(start);
        if(map == null) { return 1L;}
        return Optional.ofNullable(map.get(end)).orElse(1L);
    }


    public Long getWeight(T start, T end) {
        return getEdgeWeight(start, end);
    }

    @Override
    public List<GraphEdge<T,T>> asListOfEdges(){
        Set<T> doneVertex = new HashSet<>();
        Map<T,Set<GraphEdge<T,T>>> map = asAdjacencyMap();
        ArrayList<GraphEdge<T,T>> list = new ArrayList<>();
        for(Map.Entry<T,Set<GraphEdge<T,T>>> entry : map.entrySet()){
            entry.getValue().forEach(edge -> {
                if(!doneVertex.contains(edge.end())){
                    list.add(edge);
                }
            });
            doneVertex.add(entry.getKey());
        }
        return list;
    }

    @Override
    public long vertices() {
        return super.vertices() / 2;
    }

    @Override
    public long edges() {
        return super.edges() / 2;
    }

}
