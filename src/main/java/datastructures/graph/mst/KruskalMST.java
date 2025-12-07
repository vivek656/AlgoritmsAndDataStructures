package datastructures.graph.mst;

import algorithms.UnionFind;
import datastructures.graph.edge.EdgeWithWeight;
import datastructures.graph.undirected.UndirectedGraph;
import datastructures.heap.MinHeap;

import java.util.*;
import java.util.stream.Collectors;

public class KruskalMST<T> implements MST<T> {


    private final UndirectedGraph<T> graph;
    private final HashMap<T, Integer> vertexIntegerMap;
    private final UnionFind UF;
    private final MinHeap<EdgeWithWeight<T,T,Double>> heap;
    private final Queue<EdgeWithWeight<T,T, Double>> mst;


    public KruskalMST(UndirectedGraph<T> graph) {
        this.graph = new UndirectedGraph<>(graph);
        vertexIntegerMap = new HashMap<>();
        int count = 0;
        Set<T> keys = graph.asAdjacencyMap().keySet();
        for (T key : keys) {
            vertexIntegerMap.put(key, count++);
        }
        UF = new UnionFind(vertexIntegerMap.size());
        mst = new LinkedList<>();

        heap = new MinHeap<>(
                graph.asListOfEdges().stream().map(
                        a -> new EdgeWithWeight<T,T,Double>(a, graph.getWeight(a.start(), a.end()))
                ).collect(Collectors.toList())
        );

        run();
    }

    private void run(){
        long vertices = graph.vertices();

        while (!heap.isEmpty() && mst.size() < vertices-1) {
            EdgeWithWeight<T,T,Double> current = heap.poll();
            T start = current.edge.start();
            T end = current.edge.end();
            if(isConnected(start, end)) continue;
            union(start, end);
            mst.add(current);
        }
    }


    private boolean isConnected(T a, T b){
        int aIndex = vertexIntegerMap.get(a);
        int bIndex = vertexIntegerMap.get(b);

        return UF.connected(aIndex, bIndex);
    }

    private void union(T a, T b){
        int aIndex = vertexIntegerMap.get(a);
        int bIndex = vertexIntegerMap.get(b);

        UF.union(aIndex, bIndex);
    }


    @Override
    public Iterable<EdgeWithWeight<T, T, Double>> get() {
        return new LinkedList<>(mst);
    }
}
