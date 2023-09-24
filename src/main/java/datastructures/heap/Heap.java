package datastructures.heap;

import common.DSUtil;
import common.ObjectKeyWrapper;
import datastructures.array.DynamicArray;
import datastructures.common.PriorityQueue;

import java.util.*;

/**
 * Heap implementation
 * 0 indexed
 * @param <T>
 */
public class Heap<T> implements PriorityQueue<T> {

    private final DynamicArray<T> arrayAsCBT ;//array as CompleteBinaryTree
    private final Comparator<T> comparator ;

    Heap(List<T> a , Comparator<T> comparator){
        this.comparator = comparator;
        arrayAsCBT = new DynamicArray<>(a);
        buildHeap(arrayAsCBT , this.comparator);
    }

    private static <T1> ObjectKeyWrapper<Integer , T1> left(DynamicArray<T1> a ,Integer size ,  Integer p){
        var leftIdx = (2*p)+1;
        return leftIdx <= size ? new ObjectKeyWrapper<>(leftIdx , a.getAt(leftIdx)) : null;
    }

    private static <T1> ObjectKeyWrapper<Integer , T1> right(DynamicArray<T1> a ,Integer size ,  Integer p){
        var rightIdx = (2*p)+2;
        return rightIdx <= size ? new ObjectKeyWrapper<>(rightIdx, a.getAt(rightIdx)) : null;
    }

    private static <T1> ObjectKeyWrapper<Integer , T1> parent(DynamicArray<T1> a ,Integer size ,  Integer c){
        var parent = (c-1)/2;
        return  (parent >= 0  || (parent) <= size) ? new ObjectKeyWrapper<>(parent , a.getAt(parent)) : null;
    }

    public static <T1> void heapifyDown(DynamicArray<T1> a , int idx ,int size , Comparator<T1> comparator){
        var left = left(a,size , idx );
        var right = right(a,size,idx);
        var largest = idx;
        if(left!=null && comparator.compare(left.getValue(), a.getAt(largest)) > 0){
            largest = left.getKey();
        }
        if(right!=null && comparator.compare(right.getValue(), a.getAt(largest)) > 0){
            largest = right.getKey();
        }
        if(largest!=idx){
            DSUtil.swap(a,largest,idx);
            heapifyDown(a,largest,size,comparator);
        }
    }

    public static <T1> void heapifyUp(DynamicArray<T1> a , int idx ,int size , Comparator<T1> comparator){
        var parent = parent(a, size , idx);
        if(parent!=null && comparator.compare(parent.getValue(), a.getAt(idx)) < 0){
            DSUtil.swap(a,parent.getKey(),idx);
            heapifyUp(a, parent.getKey(), size , comparator);
        }
    }

    public static <T1> DynamicArray<T1> buildHeap(DynamicArray<T1> a , Comparator<T1> comparator){
        for(int i = (a.len() / 2)+1; i >= 0 ; i-- ){
            heapifyDown(a , i , a.len()-1 , comparator);
        }
        return a;
    }

    @Override
    public PriorityQueue<T> build(Iterable<T> items) {
        var list = new ArrayList<T>();
        items.forEach(list::add);
        return new Heap<>(list, comparator);
    }

    @Override
    public T insert(T item) {
        var res = arrayAsCBT.insertLast(item);
        heapifyUp(arrayAsCBT , arrayAsCBT.len()-1, arrayAsCBT.len()-1 , comparator);
        return res;
    }

    @Override
    public T poll() {
        DSUtil.swap(arrayAsCBT , 0 , arrayAsCBT.len()-1);
        var max = arrayAsCBT.deleteLast();
        heapifyDown(arrayAsCBT, 0, arrayAsCBT.len()-1 , comparator);
        return max;
    }

    @Override
    public T peek() {
        return arrayAsCBT.getAt(0);
    }
}
