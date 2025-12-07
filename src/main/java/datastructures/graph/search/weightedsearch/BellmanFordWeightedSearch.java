package datastructures.graph.search.weightedsearch;

import datastructures.graph.directed.DirectedGraph;
import datastructures.graph.edge.GraphEdge;
import datastructures.graph.directed.WeightedDirectedGraph;
import datastructures.graph.search.BFS;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BellmanFordWeightedSearch<T> extends WeightedGraphSearch<T> {


    protected Map<T, Set<GraphEdge<T, T>>> adjacencyMap;


    private BellmanFordWeightedSearch(WeightedDirectedGraph<T> graph){
        this.graph = graph;
    }

    private static final RandomGenerator generator = RandomGenerator.getDefault();

    private Boolean graphContainsNegativeCycle = false;

    private Set<T> negativeWeightWitnesses = new HashSet<>();


    private static String randomName() {
        return String.valueOf(generator.nextLong(1000, 10000000));
    }


    public static <E> BellmanFordWeightedSearch<E> of(DirectedGraph<E> graph){
        var weightedGraph = new WeightedDirectedGraph<>(graph);
        return new BellmanFordWeightedSearch<>(weightedGraph);
    }

    public  BellmanFordWeightedSearch<T> withWeightFunction(BiFunction<T,T,Number> weightFunction){
        Objects.requireNonNull(graph , "Graph is not initialized , please initialized graph, by providing it in static initializer");
        String function = (weightedFunctionName == null)? randomName() : this.weightedFunctionName;
        graph.addWeightedFunction(function, weightFunction);
        this.weightedFunctionName = function;
        return this;
    }

    public  void withWeightFunctionAndName(BiFunction<T,T,Number> weightFunction , String weightedFunctionName){
        Objects.requireNonNull(graph , "Graph is not initialized , please initialized graph, by providing it in static initializer");
        this.weightedFunctionName = weightedFunctionName;
        graph.addWeightedFunction(this.weightedFunctionName, weightFunction);
    }



    @Override
    public void run(T source) {
        if (graph == null)
            throw new IllegalStateException("Cant run search , without graph or vertex information");
        if(!graph.containsVertex(source))
            throw new IllegalArgumentException("Graph does not contain vertex " + source);
        //if weighted function not given we will generate weight by default weight function
        if(weightedFunctionName==null){
            this.weightedFunctionName = graph.getDefaultFunctionWithName().getKey();
        }
        initializeAttributes();
        var connectedAdjacencyMap = getConnectedAdjacencyMap(source);
        var keySet = connectedAdjacencyMap.keySet();
        var vertexSize = connectedAdjacencyMap.size();


        getAttributesFor(source).pathWeight = 0L;
        IntStream.range(0, vertexSize-1).forEach(number -> { //V times
            for(T u : keySet){ //v times
                for(GraphEdge<T,T> v : connectedAdjacencyMap.getOrDefault(u , Collections.emptySet())){ // |adj+(V)| times (V max)
                    tryToRelaxEdge(v); // O(1)
                }
            }
        });

        for(T u : keySet){
            for(GraphEdge<T,T> v : connectedAdjacencyMap.getOrDefault(u , Collections.emptySet())){ // V max
                if(Boolean.TRUE.equals(tryToRelaxEdge(v))){
                    //negative weight cycle sadly
                    graphContainsNegativeCycle = true;
                    negativeWeightWitnesses.add(v.end());
                    getAttributesFor(v.end()).pathWeight = WeightedVertexAttributes.INFINITE_NEGATIVE_WEIGHT;
                }
            }
        }

    }

    private WeightedVertexAttributes<T> getAttributesFor(T u) {
        return vertexAttributesMap.get(u);
    }


    private Map<T, Set<GraphEdge<T, T>>> getConnectedAdjacencyMap(T source) {
        BFS<T> bfs = BFS.of(graph);
        bfs.run(source);
        var vertexNotReachable = adjacencyMap.keySet().stream().filter(a -> !bfs.singlePairReachability(a)).collect(Collectors.toSet());
        Map<T, Set<GraphEdge<T, T>>> newMap = HashMap.newHashMap(adjacencyMap.size());
        adjacencyMap.forEach((key, value) -> {
            if(!vertexNotReachable.contains(key)){
                var setToAdd = value.stream().filter(a -> !vertexNotReachable.contains(a.end())).collect(Collectors.toSet());
                newMap.put(key, setToAdd);
            }
        });
        return newMap;
    }

    private void initializeAttributes() {
        adjacencyMap = graph.asAdjacencyMap();
        vertexAttributesMap.clear();
        for (var v : adjacencyMap.keySet())
            vertexAttributesMap.put(v, new WeightedVertexAttributes<>(v));
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
        } else if (Objects.equals(vertexAttributes.pathWeight, WeightedVertexAttributes.INFINITE_NEGATIVE_WEIGHT) ||
                traversedKeySet.stream().anyMatch( a-> negativeWeightWitnesses.contains(a))
        ) {
            weight = "-INF";
        }
        return new ImmutablePair<>(weight , path);
    }


}
