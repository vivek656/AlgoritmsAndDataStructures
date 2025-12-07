package datastructures.graph.edge;

public record GraphEdge<T, R>(T start, R end) {

    

    @Override
    public String toString() {
        return ("(" + start + "-->" + end + ")");
    }
}

