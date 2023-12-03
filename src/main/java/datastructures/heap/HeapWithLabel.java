package datastructures.heap;


import common.DSUtil;
import common.ObjectKeyWrapper;
import common.Pair;
import datastructures.array.DynamicArray;
import datastructures.common.PriorityQueue;

import java.text.MessageFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * HeapWithLabel implementation
 * 0 indexed
 *
 * @param <T>
 */
public class HeapWithLabel<T, U> implements PriorityQueue<T> {

    private final DynamicArray<LabelledHeapItem<T, U>> arrayAsCBT;//array as CompleteBinaryTree
    private final Comparator<T> comparator;
    private final HashMap<U, Integer> labelMap = new HashMap<>();
    public HeapWithLabel(List<Pair<T, U>> a, Comparator<T> comparator) {
        this.comparator = comparator;
        var labelledArray = a.stream().map(item -> new LabelledHeapItem<>(item.getFirst(), item.getSecond())).toList();
        arrayAsCBT = new DynamicArray<>(labelledArray);
        for (int i = 0 , len = arrayAsCBT.len(); i < len; i++){
            labelMap.put(arrayAsCBT.getAt(i).getLabel() , i);
        }
        buildHeapWithLabel();
    }

    private ObjectKeyWrapper<Integer, LabelledHeapItem<T, U>> left(Integer size, Integer p) {
        var leftIdx = (2 * p) + 1;
        return leftIdx <= size ? new ObjectKeyWrapper<>(leftIdx, arrayAsCBT.getAt(leftIdx)) : null;
    }

    private ObjectKeyWrapper<Integer, LabelledHeapItem<T, U>> right(Integer size, Integer p) {
        var rightIdx = (2 * p) + 2;
        return rightIdx <= size ? new ObjectKeyWrapper<>(rightIdx, arrayAsCBT.getAt(rightIdx)) : null;
    }

    private ObjectKeyWrapper<Integer, LabelledHeapItem<T, U>> parent(Integer size, Integer c) {
        var parent = (c - 1) / 2;
        return (parent >= 0 || (parent) <= size) ? new ObjectKeyWrapper<>(parent, arrayAsCBT.getAt(parent)) : null;
    }

    private void heapifyDown(int idx, int size) {
        var left = left(size, idx);
        var right = right(size, idx);
        var largest = idx;
        if (left != null && comparator.compare(left.getValue().getValue(), arrayAsCBT.getAt(largest).getValue()) > 0) {
            largest = left.getKey();
        }
        if (right != null && comparator.compare(right.getValue().getValue(), arrayAsCBT.getAt(largest).getValue()) > 0) {
            largest = right.getKey();
        }
        if (largest != idx) {
            swap(largest,idx);
            heapifyDown(largest, size);
        }
    }

    private void heapifyUp(int idx, int size) {
        var parent = parent(size, idx);
        var currentValue = arrayAsCBT.getAt(idx);
        if (parent != null && comparator.compare(parent.getValue().getValue(), currentValue.getValue()) < 0) {
            swap(parent.getKey(), idx);
            heapifyUp(parent.getKey(), size);
        }
    }

    private void swap(int a , int b){
        var itemAtA = arrayAsCBT.getAt(a);
        var itemAtB =  arrayAsCBT.getAt(b);
        DSUtil.swap(arrayAsCBT, a,b);
        labelMap.put(itemAtA.getLabel(), b);
        labelMap.put(itemAtB.getLabel(), a);

    }

    private void buildHeapWithLabel() {
        for (int i = (arrayAsCBT.len() / 2) + 1; i >= 0; i--) {
            heapifyDown(i, arrayAsCBT.len() - 1);
        }
    }

    @Override
    public PriorityQueue<T> build(Iterable<T> items) {
        return null;
    }

    @Override
    public T insert(T item) {
        return null;
    }


    public T insert(T item, U label) {
        var res = arrayAsCBT.insertLast(new LabelledHeapItem<>(item, label));
        heapifyUp(arrayAsCBT.len() - 1, arrayAsCBT.len() - 1);
        return res.value;
    }

    @Override
    public T poll() {
        swap(0, arrayAsCBT.len() - 1);
        var max = arrayAsCBT.deleteLast();
        labelMap.remove(max.label);
        heapifyDown(0, arrayAsCBT.len() - 1);
        return max.value;
    }

    @Override
    public T peek() {
        return arrayAsCBT.getAt(0).value;
    }

    @Override
    public Boolean isEmpty() {
        return arrayAsCBT.len()==0;
    }

    public void updateLabelValue(U label , T value){
        var index = labelMap.get(label);
        Objects.requireNonNull(index , MessageFormat.format("No label with value {0} exist in heap" , label));
        LabelledHeapItem<T,U> objectAtIndex = arrayAsCBT.getAt(index);
        var valueAtIndex = objectAtIndex.value;
        if(comparator.compare(value,valueAtIndex)==0)return;

        objectAtIndex.setValue(value);
        if(comparator.compare(value, valueAtIndex) > 0){
            //if value is bigger than current value , then it is also bigger than its child , so only needed Heapify UP
            heapifyUp(index,arrayAsCBT.len()-1);
        }else if (comparator.compare(value, valueAtIndex) < 0){
            heapifyDown(index , arrayAsCBT.len()-1);
        }
    }

    private static class LabelledHeapItem<T, U> {

        private U label;
        private T value;

        public LabelledHeapItem( T value , U label) {
            this.label = label;
            this.value = value;
        }

        public U getLabel() {
            return label;
        }

        public void setLabel(U label) {
            this.label = label;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}

