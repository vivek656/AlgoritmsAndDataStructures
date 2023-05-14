package sort;

import common.DSUtil;
import datastructures.heap.Heap;

public class HeapSort {

    public static <E extends Comparable<E>> E[] sort(E[] a){
        var size = a.length-1;
        if(size <= 1) return a;
        a = Heap.buildHeap(a , Comparable::compareTo );
        for(int i = 0 ; size >= 1 ; size--){
            Heap.heapifyDown(a, i , size , Comparable::compareTo);
            DSUtil.swap(a, i , size);
        }
        return a;
    }

}
