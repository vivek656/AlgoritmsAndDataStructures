package datastructures.heap;

import common.AbstractAlgoAndDsTestBase;
import datastructures.common.PriorityQueue;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import sort.HeapSort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class HeapTests extends AbstractAlgoAndDsTestBase {

    @Test
    void heapSortTest() {
        var integersArray = new ArrayList<Integer>(1000);
        for (int i = 0; i < 1000; i++) {
            integersArray.add(RandomUtils.nextInt(100, 1000));
        }

        logger.info("ARRAY:BEFORE SORT: {}", integersArray);
        var a = integersArray.toArray(new Integer[]{});
        logger.info("HEAP SORT: {}", Arrays.toString(HeapSort.sort(a)));
    }

    @Test
    void heapAsPriorityQTest() {
        var integersArray = List.of(1, 2, 8, 9, 10, 3, 4, 5, 6, 7);
        logger.info("INTEGERS ARRAY: {}", integersArray);
        PriorityQueue<Integer> q = new MaxHeap<>(integersArray);
        var GET_MAX = "GET MAX {}";
        var DELETE_MAX = "DELETE MAX {}";
        var INSERT_NEW = "INSERT {} new Max {}";
        logger.info(GET_MAX, q.peek());
        logger.info(DELETE_MAX, q.poll());
        logger.info(GET_MAX, q.peek());
        q.insert(12);
        logger.info(INSERT_NEW, 12, q.poll());


    }

}
