package sort;

import java.util.Arrays;
/**
radix sort : sorting with help of counting sort
 * splitting integers into separate value according to base (preferably n)
 * will give us O(n) time if total distinct values k will be or order O(n^a)
 * i.e. polynomial in n
 * sorting first least significant digit up-to the most significant one ,
 * maintaining the relative order
 * of keys
 */
public class RadixSort {

    private static int getTotalDigitsForBase(int digit , int b){
        var pow = 0 ;
        while(digit >= Math.pow(b, pow)) pow++;
        return pow;
    }

    private RadixSort(){}

    public static Integer[] sort(Integer [] keys){
        var n = Math.max(keys.length, 10);
        if(keys.length == 0) return keys;
        var digits = getTotalDigitsForBase(Arrays.stream(keys).max(Integer::compareTo).orElseGet(() -> keys[0]) , n);
        var temp = Arrays.copyOf(keys , keys.length);
        for(int i = 1 ; i <= digits ; i++){
            int finalI = i;
            var countingSort = new CountingSort<Integer>(n , j -> (int) (((int)(j % Math.pow(n , finalI))) / (Math.pow(n , finalI-1.0))));
            temp = countingSort.sort(temp);

        }
        return temp;
    }
}
