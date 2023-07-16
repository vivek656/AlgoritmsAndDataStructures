package common;

import datastructures.array.DynamicArray;

import java.lang.reflect.Array;

public class DSUtil {

    private DSUtil(){}

    public static <T> void swap(DynamicArray<T> a, Integer i , Integer j){
        var temp = a.getAt(i);
        a.setAt(i , a.getAt(j));
        a.setAt(j , temp);
    }
}
