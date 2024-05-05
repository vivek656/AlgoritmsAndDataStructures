package sort;

import common.AbstractAlgoAndDsTestBase;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

class SortTests extends AbstractAlgoAndDsTestBase {

    @Test
    void radixSortTest() {
        var integersArray = new ArrayList<Integer>(1000);
        for (int i = 0; i < 1000; i++) {
            integersArray.add(RandomUtils.nextInt(10000000, 1000000000));
        }
        logger.info("ARRAY:BEFORE SORT: {}", integersArray);
        logger.info("RADIX SORT: {}", RadixSort.sort(integersArray));

    }
}
