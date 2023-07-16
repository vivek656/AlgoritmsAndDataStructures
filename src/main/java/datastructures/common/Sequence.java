package datastructures.common;

public interface Sequence<T> extends Iterable<T> {

    Sequence<T> build(Iterable x);

    Integer len();

    T getAt(Integer index);

    T setAt(Integer integer , T value);

    T insertAt(Integer index, T value);

    T deleteAt(Integer index);

    T deleteFirst();

    T deleteLast();

    public T insertLast(T value);


}
