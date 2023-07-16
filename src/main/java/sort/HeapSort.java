package sort;

import common.DSUtil;
import datastructures.array.DynamicArray;
import datastructures.heap.Heap;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class HeapSort {

    private HeapSort(){}

    public static <E extends Comparable<E>> E[] sort(E[] a){
        var dynamicA = new DynamicArray<>(Arrays.asList(a));
        var size = dynamicA.len()-1;
        if(size <= 1) return a;
        Heap.buildHeap(dynamicA, Comparable::compareTo);
        for(int i = 0 ; size >= 1 ; size--){
            Heap.heapifyDown(dynamicA, i , size , Comparable::compareTo);
            DSUtil.swap(dynamicA, i , size);
        }
        var resArray = Arrays.asList(a).toArray(a);
        var res = dynamicA.asArray();

        for(int i = 0 ; i < res.length ; i++){
            resArray[i] = (E) res[i];
        }
        return resArray;
    }

}
