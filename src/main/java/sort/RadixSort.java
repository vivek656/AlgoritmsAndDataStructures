package sort;

import java.util.Arrays;
import java.util.List;

/**
 * radix sort : sorting with help of counting sort
 * splitting integers into separate value according to base (preferably n)
 * will give us O(n) time if total distinct values k will be or order O(n^a)
 * i.e. polynomial in n
 * sorting first least significant digit up-to the most significant one ,
 * maintaining the relative order
 * of keys
 */
public class RadixSort {

    private static int getTotalDigitsForBase(int digit, int b) {
        var pow = 0;
        while (digit >= Math.pow(b, pow)) pow++;
        return pow;
    }

    private RadixSort() {
    }

    public static Integer[] sort(Integer[] keys) {
        var n = Math.max(keys.length, 10);
        if (keys.length == 0) return keys;
        var digits = getTotalDigitsForBase(Arrays.stream(keys).max(Integer::compareTo).orElseGet(() -> keys[0]), n);
        var temp = Arrays.copyOf(keys, keys.length);
        for (int i = 0; i < digits; i++) {
            int finalI = i;
            var countingSort = new CountingSort<Integer>(n, j -> getBitOfZBaseYAtPositionX(j, finalI, n));
            temp = countingSort.sort(temp);
        }
        return temp;
    }

    /**
     * return the bit value of ZbaseY at place x.
     * eg 89base4 = 1121
     * so for args= (89,1,4) result will be 2 (89base4 value at position 1 )
     *
     * @param z no to get bits map of
     * @param x place of bit (O indexed)
     * @param y base
     * @return a bit value at place x
     */
    private static int getBitOfZBaseYAtPositionX(int z, int x, int y) {
        return (int) (((int) (z % Math.pow(y, (x + 1)))) / (Math.pow(y, x)));
    }


    public static List<Integer> sort(List<Integer> keys) {

        var keyAsArray = keys.toArray(new Integer[0]);
        keyAsArray = sort(keyAsArray);
        return Arrays.stream(keyAsArray).toList();

    }
}
