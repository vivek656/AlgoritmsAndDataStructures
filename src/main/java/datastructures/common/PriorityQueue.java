package datastructures.common;

public interface PriorityQueue<T> {
    PriorityQueue<T> build(Iterable<T> items);

    T insert(T item);

    T poll();

    T peek();

}
