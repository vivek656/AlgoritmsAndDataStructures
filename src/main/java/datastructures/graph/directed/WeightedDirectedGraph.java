package datastructures.graph.directed;

import common.ObjectKeyWrapper;
import datastructures.graph.edge.GraphEdge;

import java.util.HashMap;
import java.util.function.BiFunction;

public class WeightedDirectedGraph<T> extends DirectedGraph<T> {

    private HashMap<String , BiFunction<T,T , Long>> weightedFunctionMap;
    private static final String  DEFAULT_WEIGHT_FUNCTION_NAME = "default_weight";

    public WeightedDirectedGraph(DirectedGraph<T> graph){
        super();
        graph.asListOfEdges().forEach(this::addEdge);
        if(graph.getClass() == WeightedDirectedGraph.class) {
            initializeWeightedFunctionMap((WeightedDirectedGraph<T>) graph);
        } else initializeWeightedFunctionMap();
    }

    private void  initializeWeightedFunctionMap(){
        this.weightedFunctionMap = new HashMap<>();
        this.weightedFunctionMap.put(DEFAULT_WEIGHT_FUNCTION_NAME ,
                ((a,b) -> 1L));
    }

    private void  initializeWeightedFunctionMap(WeightedDirectedGraph<T> graph) {
        this.weightedFunctionMap = new HashMap<>();
        this.weightedFunctionMap.putAll(graph.weightedFunctionMap);
    }


    public void addWeightedFunction(String functionName , BiFunction<T,T,Long> function){
        if(DEFAULT_WEIGHT_FUNCTION_NAME.equals(functionName))
            throw new IllegalArgumentException(
                    String.format("Invalid function name: %s Name clashes With default weighted function of Graph , provide different name" , functionName)
            );
        weightedFunctionMap.put(functionName , function);
    }

    private Long getEdgeWeight(GraphEdge<T,T> edge , String weightedFunctionName){
        return weightedFunctionMap.getOrDefault(weightedFunctionName , (a,b) -> null)
                .apply(edge.start(), edge.end());
    }

    private Long getEdgeWeight(GraphEdge<T,T> edge){
        return getEdgeWeight(edge, DEFAULT_WEIGHT_FUNCTION_NAME);
    }

    public ObjectKeyWrapper<String , BiFunction<T,T, Long>> getDefaultFunctionWithName(){
        return new ObjectKeyWrapper<>(DEFAULT_WEIGHT_FUNCTION_NAME, weightedFunctionMap.get(DEFAULT_WEIGHT_FUNCTION_NAME));
    }

    public ObjectKeyWrapper<String , BiFunction<T,T, Long>> getFunctionWithName(String functionName){
        return new ObjectKeyWrapper<>(functionName, weightedFunctionMap.get(functionName));
    }

}




