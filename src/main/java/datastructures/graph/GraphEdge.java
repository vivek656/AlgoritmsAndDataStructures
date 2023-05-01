package datastructures.graph;

public class GraphEdge<T,R> {
    T start ;
    R end ;

    GraphEdge(T start, R end){
        this.start = start;
        this.end = end;
    }


    @Override
    public String toString() {
        return ("(" + start + "-->" + end + ")");
    }
}

