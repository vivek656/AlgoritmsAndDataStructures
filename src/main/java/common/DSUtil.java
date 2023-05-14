package common;

public class DSUtil {

    private DSUtil(){}

    @SuppressWarnings("unchecked")
    public static <T> T[]getGenericArray(int length){
        return (T[]) (new Object[length]);
    }

    public static <T> void swap(T[] a, Integer i , Integer j){
        var temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}
