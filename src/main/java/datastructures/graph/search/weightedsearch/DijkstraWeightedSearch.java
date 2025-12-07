package datastructures.graph.search.weightedsearch;

import common.Pair;
import datastructures.graph.directed.DirectedGraph;
import datastructures.graph.edge.GraphEdge;
import datastructures.graph.directed.WeightedDirectedGraph;
import datastructures.heap.HeapWithLabel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.random.RandomGenerator;

public class DijkstraWeightedSearch<T> extends WeightedGraphSearch<T> {

    HeapWithLabel<Pair<T, Long>, Integer> heap;

    private  final Comparator<Pair<T , Long>> keyWeightPairComparator = (o1,o2)-> o2.getSecond().compareTo(o1.getSecond());

    private Map<T, Set<GraphEdge<T,T>>> adjacencyMap;
    private static final RandomGenerator generator = RandomGenerator.getDefault();


    private DijkstraWeightedSearch(WeightedDirectedGraph<T> graph ){
        this.graph = graph;
    }
    private static String randomName() {
        return String.valueOf(generator.nextLong(1000, 10000000));
    }


     public static <E> DijkstraWeightedSearch<E> of(DirectedGraph<E> graph){
        var weightedGraph = new WeightedDirectedGraph<>(graph);
        return new DijkstraWeightedSearch<>(weightedGraph);
    }

    public <R extends Number> DijkstraWeightedSearch<T> withWeightFunction(BiFunction<T,T,R> weightFunction){
        Objects.requireNonNull(graph , "Graph is not initialized , please initialized graph, by providing it in static initializer");
        String function = (weightedFunctionName == null)? randomName() : this.weightedFunctionName;
        graph.addWeightedFunction(function, (weightFunction::apply));
        this.weightedFunctionName = function;
        return this;
    }

    private void buildHeap(){
        var attributes = vertexAttributesMap.values().stream().map(this::toHeapInput).toList();
        heap = new HeapWithLabel<>(attributes, keyWeightPairComparator);
    }

    @Override
    public void run(T source) {
        initializeAttributes(source);
        int totalvertex = adjacencyMap.size();
        for (int i =0 ; i < totalvertex ; i++){
            var u  = heap.poll();
            for (var v : adjacencyMap.getOrDefault(u.getFirst(), Collections.emptySet())){
                var relaxed = tryToRelaxEdge(v);
                var attibutes = vertexAttributesMap.get(v.end());
                if(Boolean.FALSE == heap.isEmpty() && relaxed==Boolean.TRUE)
                    decreaseDistance((DijkstraWeightedAttribute<T>) attibutes, attibutes.pathWeight);
            }
        }
    }

    private void initializeAttributes(T source){
        adjacencyMap = graph.asAdjacencyMap();
        vertexAttributesMap.clear();
        var indices = 0;
        for (var v : adjacencyMap.keySet())
            vertexAttributesMap.put(v, new DijkstraWeightedAttribute<>(v ,indices++));
        vertexAttributesMap.get(source).pathWeight = 0L;
        buildHeap();
    }
    private void decreaseDistance(DijkstraWeightedAttribute<T> attribute , Long weight){
        heap.updateLabelValue(attribute.id, new Pair<>(attribute.getKey(),weight));

    }

    private Pair<Pair<T, Long>, Integer> toHeapInput(WeightedVertexAttributes<T> item) {
        return new Pair<>(new Pair<>(item.getKey(), item.pathWeight), ((DijkstraWeightedAttribute<T>) item).id);
    }

    private WeightedVertexAttributes<T> getAttributesFor(T u) {
        return vertexAttributesMap.get(u);
    }

    public Pair<String , List<T>> createPath(T end){
        Objects.requireNonNull(end);
        var path = new LinkedList<T>();
        var vertexAttributes = getAttributesFor(end);
        var temp =  vertexAttributes;
        path.addFirst(temp.getKey());
        var  traversedKeySet = new HashSet<T>();
        while (temp.predecessor!=null){
            traversedKeySet.add(temp.getKey());
            path.addFirst(temp.predecessor.getKey());
            temp = temp.predecessor;
            if(traversedKeySet.contains(temp.getKey())) break;
        }
        var weight = vertexAttributes.pathWeight.toString();
        if(Objects.equals(vertexAttributes.pathWeight, WeightedVertexAttributes.INFINITE_WEIGHT)){
            weight = "INF";
        }
        return new Pair<>(weight , path);
    }

    public static class DijkstraWeightedAttribute<E> extends WeightedVertexAttributes<E> implements Comparable<DijkstraWeightedAttribute<E>> {

        int id;
        DijkstraWeightedAttribute(E key , int id) {
            super(key);
            this.id = id;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null || obj.getClass()!=getClass()) return false;
            return ((DijkstraWeightedAttribute<?>) obj).id == this.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public int compareTo(@NotNull DijkstraWeightedSearch.DijkstraWeightedAttribute<E> o) {
            return this.pathWeight.compareTo(o.pathWeight);
        }
    }
}
