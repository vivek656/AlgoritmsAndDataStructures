package datastructures.heap;

import java.util.Comparator;
import java.util.List;

public class MaxHeap<T extends Comparable<T>> extends Heap<T> {

    MaxHeap(List<T> a){
       super(a, Comparator.naturalOrder());
    }
}
