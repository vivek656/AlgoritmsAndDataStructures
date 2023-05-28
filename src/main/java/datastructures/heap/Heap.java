package datastructures.heap;

import common.DSUtil;
import common.ObjectKeyWrapper;

import java.util.Comparator;
import java.util.List;

/**
 * Heap implementation
 * 0 indexed
 * @param <T>
 */
public class Heap<T> {

    private T[] arrayAsCBT ;//array as CompleteBinaryTree
    private Comparator<T> comparator ;

    private int size;

    Heap(List<T> a , Comparator<T> comparator){
        this.comparator = comparator;
        arrayAsCBT = a.toArray(DSUtil.getGenericArray(a.size()));
        buildHeap(arrayAsCBT , this.comparator);
        this.size = arrayAsCBT.length;
    }


    private static <T1> ObjectKeyWrapper<Integer , T1> left(T1[] a ,Integer size ,  Integer p){
        var leftIdx = (2*p)+1;
        return leftIdx <= size ? new ObjectKeyWrapper<>(leftIdx , a[leftIdx]) : null;
    }

    private static <T1> ObjectKeyWrapper<Integer , T1> right(T1[] a ,Integer size ,  Integer p){
        var rightIdx = (2*p)+2;
        return rightIdx <= size ? new ObjectKeyWrapper<>(rightIdx, a[rightIdx]) : null;
    }

    private static <T1> ObjectKeyWrapper<Integer , T1> parent(T1[] a ,Integer size ,  Integer c){
        var parent = (c-1)/2;
        return  (parent >= 0  || (parent) <= size) ? new ObjectKeyWrapper<>(parent , a[parent]) : null;
    }

    protected ObjectKeyWrapper<Integer , T> left(Integer p){
        return left(arrayAsCBT , size , p);
    }

    protected ObjectKeyWrapper<Integer , T> right(Integer p){
        return right(arrayAsCBT , size , p);
    }

    protected ObjectKeyWrapper<Integer , T> parent(Integer p){
        return parent(arrayAsCBT , size , p);
    }


    public static <T1> void heapifyDown(T1[] a , int idx ,int size , Comparator<T1> comparator){
        var left = left(a,size , idx );
        var right = right(a,size,idx);
        var largest = idx;
        if(left!=null && comparator.compare(left.getValue(), a[largest]) > 0){
            largest = left.getKey();
        }
        if(right!=null && comparator.compare(right.getValue(), a[largest]) > 0){
            largest = right.getKey();
        }
        if(largest!=idx){
            DSUtil.swap(a,largest,idx);
            heapifyDown(a,largest,size,comparator);
        }
    }

    public static <T1> T1[] buildHeap(T1[] a , Comparator<T1> comparator){
        for(int i = (a.length / 2)+1; i >= 0 ; i-- ){
            heapifyDown(a , i , a.length-1 , comparator);
        }
        return a;
    }

}
