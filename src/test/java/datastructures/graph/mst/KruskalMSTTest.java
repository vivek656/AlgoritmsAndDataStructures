package datastructures.graph.mst;

import datastructures.graph.edge.EdgeWithWeight;
import datastructures.graph.undirected.UndirectedGraph;
import org.junit.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Arrays;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KruskalMSTTest {

    @Test
    public void getEdgesForAMst(){
        String s = """
        4 5 0.35
        4 7 0.37
        5 7 0.28
        0 7 0.16
        1 5 0.32
        0 4 0.38
        2 3 0.17
        1 7 0.19
        0 2 0.26
        1 2 0.36
        1 3 0.29
        2 7 0.34
        6 2 0.40
        3 6 0.52
        6 0 0.58
        6 4 0.93 
        """;

        double[][] edgeWeights = Arrays.stream(s.split("\n"))
                .map(a -> {
                    String[] splits = a.split(" ");
                    return new double[]{Double.parseDouble(splits[0]), Double.parseDouble(splits[1]), Double.parseDouble(splits[2])};
                }).toList().toArray(new double[][]{});
        UndirectedGraph<Integer> g =  new UndirectedGraph<>();
        for (double[] ds : edgeWeights) {
            g.addEdgeWithWeight(Double.valueOf(ds[0]).intValue() , Double.valueOf(ds[1]).intValue() , ds[2]);
        }

        MST<Integer> mst = new KruskalMST<>(g);

        Iterable<EdgeWithWeight<Integer,Integer,Double>> edges = mst.get();
        Double sum = 0.0;
        for (EdgeWithWeight<Integer,Integer,Double> edge : edges) {
            System.out.println(edge);
            sum += edge.weight;
        }
        System.out.println(sum);

    }
}