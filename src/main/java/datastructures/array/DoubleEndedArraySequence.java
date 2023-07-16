package datastructures.array;


import java.util.*;

/***
 * implementing an array with constant insertion and removal from ends as well as constant(amortized) insertion
 */
@SuppressWarnings("unchecked")
public class DoubleEndedArraySequence<T>  {

    private T[] arrays;
    private int startIndex ;
    private int actualListSize;

    private int lastIndex() {
        return startIndex + actualListSize -1;
    }

    public DoubleEndedArraySequence(T[] array){
        buildInitialArrayWithArray(array);
    }

    public static <E> DoubleEndedArraySequence<E> of(E... objects){
        return new DoubleEndedArraySequence<>(objects);
    }


    public int size() {
        return this.actualListSize;
    }

    public T insertFirst(T t){
        recomputeSize();
        this.arrays[startIndex-1]=t;
        startIndex--;
        actualListSize++;
        return t;
    }

    public T getAtLast(){
        return arrays[lastIndex()];
    }

    public T insertLast(T t){
        recomputeSize();
        this.arrays[lastIndex()+1]=t;
        actualListSize++;
        return t;
    }

    public T removeFomLast(){
        T lastElem = getAtLast();
        this.arrays[lastIndex()]  = null;
        this.actualListSize--;
        return lastElem;
    }

    public T removeFromFront(){
        T lastElem = getFirst();
        this.arrays[startIndex]=null;
        this.startIndex--;
        this.actualListSize--;
        return lastElem;
    }

    private T getFirst() {
        return arrays[startIndex];
    }


    public boolean isEmpty() {
        return this.actualListSize==0;
    }


    public boolean contains(Object o) {
        for(int i = startIndex , lastIndex = startIndex + actualListSize ; i < lastIndex ; i++ ){
            if (arrays[i].equals(o)) return true;
        }
        return false;
    }


    public Iterator<T> iterator() {
        return Arrays.stream(toArray()).iterator();

    }


    public T[] toArray() {
        return (T[]) Arrays.copyOfRange(arrays , startIndex , lastIndex());
    }


    public boolean add(Object o) {
        recomputeSize();
        arrays[lastIndex()+1] = (T) o;
        actualListSize++;
        return true;

    }

    private void recomputeSize() {
        if(actualListSize < arrays.length/6 ||
        startIndex == 0 || lastIndex() == arrays.length) {
            buildInitialArrayWithArray(toArray());
        }
    }

    public boolean addAll(Collection c) {
        for(Object o : c){
            add(o);
        }
        return true;
    }



    private void  buildInitialArrayWithArray(T[] array) {
        this.arrays = (T[]) new Object[array.length*3];
        this.startIndex = array.length;
        for(int i =0 , len = array.length ; i < len ; i++){
            this.arrays[startIndex+i]=array[i];
        }
        this.actualListSize = array.length;
    }



}
