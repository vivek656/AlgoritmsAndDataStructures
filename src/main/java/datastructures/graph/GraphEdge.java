package datastructures.graph;

public class GraphEdge<T,R> {
    private final T start ;
    private final R end ;


    GraphEdge(T start, R end){
        this.start = start;
        this.end = end;
    }

    public R getEnd(){
        return end;
    }

    public T getStart(){
        return start;
    }


    @Override
    public String toString() {
        return ("(" + start + "-->" + end + ")");
    }
}

