package datastructures.common;

import datastructures.graph.GraphEdge;

import java.util.Map;
import java.util.Set;

public interface Graph<T> {
    Map<T , Set<GraphEdge<T,T>>> asAdjacencyMap();
}
