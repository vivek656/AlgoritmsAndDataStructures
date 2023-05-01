package sort;


import java.util.Arrays;
import java.util.function.Function;

/**
 * counting sort given array of size n  from set of k no, sort via random addressing
 * in O(n) time , for n ~ k
 * we cant make k too high , so that array size will be very large , upper bounder by word size i.e. 64 bits
 * we also needed this sort to be stable , i.e. relative order of objects (with same key value) must be maintained.<br>
 * <B>mapper</B> needed to map an object to integral key <br>
 * range of mapper function should be non-zero non-negative Integers
 */
public class CountingSort<T>  {
    private final int k;

    private Function<T , Integer> mapper = T::hashCode;
    public CountingSort(Integer bounds , Function<T , Integer> intMapper){
        k = bounds;
        this.mapper = intMapper;
    }
    public CountingSort(Integer bounds ){
        k = bounds;
    }


    private int getKey(T t){
        return mapper.apply(t);
    }


    public T[] sort(T[] keysArray){
        var c = new Integer[k];
        //storing keys at the index of c
        for(T i : keysArray){
            c[getKey(i)] = c[getKey(i)]==null? 1 : c[getKey(i)]+1;
        }
        var runningSum = 0;
        // storing how many elements is <= i in keysArray
        for(int i = 0; i < c.length ; i++){
            runningSum+= (c[i]!=null ? c[i] : 0);
            c[i]=runningSum;
        }
        //doing in place sorting using above info
        var result = Arrays.copyOf(keysArray,keysArray.length);
        for(int i = keysArray.length-1; i >= 0 ; i--){
            var key = getKey(keysArray[i]);
            result[c[key]-1] = keysArray[i];
            c[key]--;
        }
        return result;
    }
}
