package datastructures.graph.search.weightedsearch;

import datastructures.graph.GraphEdge;
import datastructures.graph.WeightedDirectedGraph;

import java.util.HashMap;
import java.util.Objects;

public abstract class WeightedGraphSearch<T> {

    protected HashMap<T, WeightedVertexAttributes<T>> vertexAttributesMap = new HashMap<>();

    protected WeightedDirectedGraph<T> graph;

    protected String weightedFunctionName;

    //run to initialize search attributes
    public abstract void run(T source);

    public static class WeightedVertexAttributes<E> {
        protected static final Long INFINITE_WEIGHT = Long.MAX_VALUE;
        protected static final Long INFINITE_NEGATIVE_WEIGHT = Long.MIN_VALUE;


        WeightedVertexAttributes<E> predecessor;

        protected Long pathWeight;

        private final E key;

        E getKey() {
            return key;
        }

        WeightedVertexAttributes(E key) {
            this.key = key;
            pathWeight = INFINITE_WEIGHT;
        }

    }

    Boolean tryToRelaxEdge(GraphEdge<T, T> edge) {
        var v = vertexAttributesMap.get(edge.end());
        var u = vertexAttributesMap.get(edge.start());
        var weight = getEdgeWeight(edge);
        //Infinity + weight  = Infinity
        if (Objects.equals(u.pathWeight, WeightedVertexAttributes.INFINITE_WEIGHT)) {
            return false;
        }
        if (v.pathWeight > u.pathWeight + weight) {
            v.predecessor = u;
            v.pathWeight = u.pathWeight + weight;
            return true;
        }
        return false;
    }

    private Long getEdgeWeight(GraphEdge<T, T> edge) {
        return graph.getFunctionWithName(weightedFunctionName).getValue()
                .apply(edge.start(), edge.end());
    }




}
