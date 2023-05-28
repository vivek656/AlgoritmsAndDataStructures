package datastructures;

import common.ObjectKeyWrapper;
import org.apache.commons.lang3.RandomUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * using hashtable to implement sequence interface , using a keyed object with key equals to position in sequence <br>
 * [getAt] [setAt] will be o(1) <br>
 * insert and deletion will be o(n) <br>
 * refer to mit 6.006 fall 2020 Problem [3-2].
 * link to question and solution <a href = "https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-spring-2020/resources/mit6_006s20_prob3sol/">here</a>
 */
public class SequenceHashTable<T> implements Iterable<T>{

    private T stubValue = null;
    private final HashTableSet<ObjectKeyWrapper<Integer, T>> keySet;
    private int indexOffset = 0;
    public SequenceHashTable(){
        this.keySet = new HashTableSet<>(ObjectKeyWrapper::getKey);
        this.indexOffset = RandomUtils.nextInt(99999 , 999999);
    }

    public SequenceHashTable<T> build(List<T> collection){
        collection.iterator().forEachRemaining(
                this::insertAtLast
        );
        return this;
    }

    int getSize() {
        return keySet.size();
    }

    int lastIndexPlusOne(){
        return indexOffset + getSize();
    }

    private ObjectKeyWrapper<Integer,T> searchObject(int index){
        return new ObjectKeyWrapper<>(index , stubValue);
    }
    T getAt(int i){
        if(i >= getSize()) throw  new IndexOutOfBoundsException(i);
        return getObjectAt(i).getValue();
    }

    boolean setAt(int i , T value ){
        if(i >= getSize()) throw  new IndexOutOfBoundsException(i);
        getObjectAt(i).setValue(value);
        return true;
    }

    private ObjectKeyWrapper<Integer,T> getObjectAt(int i){
        return keySet.find( searchObject(indexOffset + i));
    }
    T insertAt(int i , T value){
        if(i>=getSize()) throw new IndexOutOfBoundsException(i);
        ObjectKeyWrapper<Integer,T> atJ = null;
        var newValue = value;
        T currentValue = null;
        for(int j = i , size = getSize() ; j < size ; j++){
            atJ = getObjectAt(j);
            currentValue = atJ.getValue();
            atJ.setValue(newValue);
            newValue = currentValue;
        }
        keySet.insert(new ObjectKeyWrapper<>(lastIndexPlusOne() , newValue));
        return value;
    }

    void insertAtLast(T value){
        keySet.insert(new ObjectKeyWrapper<>(lastIndexPlusOne() , value));
    }
    T deleteAt(int i){
        if(i>=getSize()) throw new IndexOutOfBoundsException(i);
        ObjectKeyWrapper<Integer,T> previousPair = getObjectAt(i);
        T deletedValue =previousPair.getValue();
        ObjectKeyWrapper<Integer,T> currentPair = null;
        for(int j = i+1 , size = getSize() ; j < size ; j++){
            currentPair = getObjectAt(j);
            previousPair.setValue(currentPair.getValue());
            previousPair = currentPair;
        }
        keySet.delete(currentPair);
        return deletedValue;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        ArrayList<T> tempArray = new ArrayList<>(getSize());
        for(int i = 0 , size = getSize() ; i < size ; i++){
            tempArray.add(getAt(i));
        }
        return tempArray.iterator();
    }

}
