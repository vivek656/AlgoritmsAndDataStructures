package datastructures.sort;

import common.Pair;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import sort.CountingSort;
import sort.RadixSort;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

public class SortingTests {

    private final Logger logger = Logger.getLogger("SORTING_TEST");

    @Test
    public void countingSortTest() {
        List<Pair<Integer, String>> list2 = List.of(
                new Pair<>(4, "a"), new Pair<>(2, "a"),
                new Pair<>(3, "a"),
                new Pair<>(1, "a"), new Pair<>(3, "b")
        );
        Pair<Integer, String>[] array = list2.toArray(new Pair[0]);
        var sort = new CountingSort<Pair<Integer, String>>(10, Pair::getFirst);
        var result = sort.sort(array);
        System.out.println(Arrays.toString(result));
    }

    @Test
    public void radixSortTest() {
        var integersArray = new ArrayList<Integer>(1000);
        for (int i = 0; i < 1000; i++) {
            integersArray.add(RandomUtils.nextInt(10000000, 1000000000));
        }
        logger.info(MessageFormat.format("ARRAY:BEFORE SORT: {0}", integersArray));
        logger.info(MessageFormat.format("RADIX SORT: {0}", RadixSort.sort(integersArray)));

    }
}

