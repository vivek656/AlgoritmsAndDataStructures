package datastructures.graph.edge;

import org.jetbrains.annotations.NotNull;

public class EdgeWithWeight<T,R,NUM extends Number> implements Comparable<EdgeWithWeight<T,R,NUM>> {
    public GraphEdge<T, R> edge;
    public NUM weight;

    public EdgeWithWeight(GraphEdge<T, R> edge, NUM weight) {
        this.edge = edge;
        this.weight = weight;
    }

    @Override
    public int compareTo(@NotNull EdgeWithWeight o) {
        double diff = (weight.doubleValue() - o.weight.doubleValue());
        if(diff > 0) {
            return (int) Math.ceil(diff);
        } else if (diff < 0) {
            return (int) Math.floor(diff);
        } else  {
            return 0;
        }
    }

    @Override
    public String toString() {
        return  "edge="+ edge + ", weight=" + weight;
    }
}