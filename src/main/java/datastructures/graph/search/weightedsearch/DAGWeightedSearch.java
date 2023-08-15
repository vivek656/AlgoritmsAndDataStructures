package datastructures.graph.search.weightedsearch;

import datastructures.graph.DirectedGraph;
import datastructures.graph.GraphEdge;
import datastructures.graph.GraphUtils;
import datastructures.graph.WeightedDirectedGraph;
import datastructures.graph.search.DFS;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.BiFunction;
import java.util.random.RandomGenerator;

public class DAGWeightedSearch<T>  {

    private static final RandomGenerator generator = RandomGenerator.getDefault();

    private WeightedDirectedGraph<T> graph;
    private String weightedFunction;
    protected Map<T, Set<GraphEdge<T,T>>> adjacencyMap;

    protected HashMap<T, WeightedVertexAttributes<T>> vertexAttributesMap = new HashMap<>();

     DAGWeightedSearch(WeightedDirectedGraph<T> graph , String functionName){
         if(Boolean.FALSE.equals(GraphUtils.validateGraphIsADAG(graph)))
             throw new IllegalArgumentException("Graph provided is not A DAG");
         if(graph.getFunctionWithName(functionName)==null)
             throw new IllegalArgumentException(String.format(
                     "No function With Name %s Exists in Graph" , functionName));
         this.graph = graph;
         this.weightedFunction = functionName;
     }

     public static  <E> DAGWeightedSearch<E> of(DirectedGraph<E> graph , BiFunction<E,E,Long> weightFunction){
         var weightedGraph = new WeightedDirectedGraph<>(graph);
         var randomName = randomName();
         weightedGraph.addWeightedFunction(randomName, weightFunction);
         return new DAGWeightedSearch<>(weightedGraph , randomName);
     }

     private static String randomName() {
         return String.valueOf(generator.nextLong(1000, 10000000));
     }

     public void run(T source){
         if (graph == null)
             throw new IllegalStateException("Cant run dfs , without graph or vertex information");
         if(!graph.containsVertex(source))
             throw new IllegalArgumentException("Graph does not contain vertex " + source);
         initializeAttributes();
         getAttributesFor(source).pathWeight = 0L;
         var dfs = DFS.of(graph);

         dfs.run();
         var topologicalOrder = dfs.topologicalSort();
         var startIndex = topologicalOrder.indexOf(source);
         for(int i = startIndex ; i < topologicalOrder.size() ; i++){
             for (GraphEdge<T,T> edge : adjacencyMap.getOrDefault(topologicalOrder.get(i), Collections.emptySet())){
                 tryToRelaxEdge(edge);
             }
         }
     }

     public Pair<String , List<T>> createPath(T end){
         Objects.requireNonNull(end);
         var path = new LinkedList<T>();
         var vertexAttributes = getAttributesFor(end);
         var temp =  vertexAttributes;
         path.addFirst(temp.key);
         while (temp.predecessor!=null){
             path.addFirst(temp.predecessor.key);
             temp = temp.predecessor;
         }
         var weight = Objects.equals(vertexAttributes.pathWeight, WeightedVertexAttributes.INFINITE_WEIGHT) ?
                 "INF" : vertexAttributes.pathWeight.toString();
         return new ImmutablePair<>(weight , path);
     }

    private WeightedVertexAttributes<T> getAttributesFor(T u) {
        return vertexAttributesMap.get(u);
    }


     private void initializeAttributes(){
         adjacencyMap = graph.asAdjacencyMap();
         vertexAttributesMap.clear();
         for(var v : adjacencyMap.keySet())
             vertexAttributesMap.put(v, new WeightedVertexAttributes<>(v));
     }

     public static class WeightedVertexAttributes<E> {
         private static final Long INFINITE_WEIGHT = Long.MAX_VALUE;

         WeightedVertexAttributes<E> predecessor;

         private Long pathWeight;

         private E key;

         WeightedVertexAttributes(E key){
             this.key = key;
             pathWeight = INFINITE_WEIGHT;
         }

     }

     private Boolean tryToRelaxEdge(GraphEdge<T,T> edge){
        var v = vertexAttributesMap.get(edge.end());
        var u = vertexAttributesMap.get(edge.start());
        var weight = getEdgeWeight(edge);
        //Infinity + weight  = Infinity
        if(Objects.equals(u.pathWeight, WeightedVertexAttributes.INFINITE_WEIGHT)){
            return false;
        }
        if(v.pathWeight > u.pathWeight + weight){
            v.predecessor = u;
            v.pathWeight = u.pathWeight + weight;
            return true;
        }
        return false;
     }

     private Long getEdgeWeight(GraphEdge<T,T> edge){
         return graph.getFunctionWithName(weightedFunction).getValue()
                 .apply(edge.start(), edge.end());
     }


}
