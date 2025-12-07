package datastructures.graph.mst;

import datastructures.graph.edge.GraphEdge;

interface MST<T> {

    Iterable<GraphEdge<T,T>> get();
}