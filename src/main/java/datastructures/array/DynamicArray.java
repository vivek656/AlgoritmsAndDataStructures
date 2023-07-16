package datastructures.array;

import datastructures.common.Sequence;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unchecked")
public class DynamicArray<T> implements Sequence<T> {

    private static final double DEFAULT_FILL_RATIO = 0.5;

    private final double fillRatio;

    private Object[] internalArray ;
    private Integer lowerBound;
    private Integer upperBound;

    private Integer size = 0;

    public DynamicArray(){
        this(Collections.emptyList());
    }

    public Object[] asArray(){
        return Arrays.copyOf(internalArray , size);
    }

    public void addALL(Iterable<T> iter){
        for(T t : iter) insertLast(t);
    }

    public DynamicArray(List<T> list){
        this(list, DEFAULT_FILL_RATIO);
    }

    public DynamicArray(List<T> list , double fillRatio){
        if (fillRatio < 1.0) {
            this.internalArray = new Object[0];
            this.fillRatio = fillRatio;
            computeBounds();
            resize(0);
            addALL(list);
        } else {
            throw new AssertionError();
        }
    }

    private void computeBounds(){
        upperBound = internalArray.length;
        lowerBound = (int) Math.floor(internalArray.length * fillRatio * fillRatio);
    }

    private void resize(int numberOfItems){
        if(lowerBound < numberOfItems && numberOfItems < upperBound) return;
        var m = Integer.max(numberOfItems , 1) * Math.ceil(1 / fillRatio);
        Object[] a = new Object[(int) m];
        System.arraycopy(internalArray, 0, a, 0, internalArray.length);
        internalArray = a;
        computeBounds();
    }


    @Override
    public Sequence<T> build(Iterable x) {
        return null;
    }

    @Override
    public Integer len() {
        return size;
    }

    @Override
    public T getAt(Integer index) {
        Objects.checkIndex(index, size);
        return (T) internalArray[index];
    }

    @Override
    public T setAt(Integer index, T value) {
        Objects.checkIndex(index, size);
        internalArray[index] = value;
        return (T) internalArray[index];
    }

    @Override
    public T insertAt(Integer index, T value) {
       Objects.checkIndex(index, size);
       resize(size+1);
       for(int i = size-1; i >= index; i--){
           internalArray[i+1] = internalArray[i];
       }
       internalArray[index] = value;
       size++;
       return (T) internalArray[index];
    }

    @Override
    public T deleteAt(Integer index) {
        Objects.checkIndex(index, size);
        resize(size--);
        var res = internalArray[index];
        for(int i = index+1; i < size-1; i++){
            internalArray[i-1] = internalArray[i];
        }
        internalArray[size-1] = null;
        size--;
        return (T) res;
    }

    @Override
    public T deleteFirst() {
        return deleteAt(0);
    }

    @Override
    public T deleteLast() {
        var res = internalArray[size-1];
        internalArray[size-1] = null;
        size--;
        resize(size);
        return (T) res;
    }

    @Override
    public T insertLast(T value) {
        resize(size+1);
        internalArray[size] = value;
        size++;
        return (T) internalArray[size-1];
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return Arrays.stream(Arrays.copyOf(internalArray, size)).map(a -> (T) a).toList().iterator();
    }
}
