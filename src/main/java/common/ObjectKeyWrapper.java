package common;

import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * simple wrapper class that takes an objects and map it to a key (usually integer) , it's augmenting the object with key,
 * for various random access data structures  objects are compared using only keys.
 * @see datastructures.SequenceHashTable
 */

public class ObjectKeyWrapper<K extends Comparable<K> , T> implements Comparable<ObjectKeyWrapper<K,T>> {

    private T value;
    private K key;

    public ObjectKeyWrapper(K key, T value) {
        this.value = value;
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    @Override
    public int compareTo(@NotNull ObjectKeyWrapper<K, T> o) {
        if(!o.value.getClass().equals(this.value.getClass())){
            throw new IllegalArgumentException("values must be of same type");
        }
        return key.compareTo(o.key);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectKeyWrapper<?, ?> that = (ObjectKeyWrapper<?, ?>) o;
        return Objects.equal(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, key);
    }
}
