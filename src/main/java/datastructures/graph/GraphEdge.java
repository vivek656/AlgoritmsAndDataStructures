package datastructures.graph;

public record GraphEdge<T, R>(T start, R end) {


    @Override
    public String toString() {
        return ("(" + start + "-->" + end + ")");
    }
}

