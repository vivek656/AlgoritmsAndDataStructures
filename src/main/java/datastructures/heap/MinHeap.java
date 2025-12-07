package datastructures.heap;

import java.util.Comparator;
import java.util.List;

public class MinHeap<T extends Comparable<T>> extends Heap<T> {

    public MinHeap(List<T> a) {
        super(a, Comparator.reverseOrder());
    }
}
