package datastructures.graph.mst;

import datastructures.graph.edge.EdgeWithWeight;

interface MST<T> {

    Iterable<EdgeWithWeight<T,T,Double>> get();
}