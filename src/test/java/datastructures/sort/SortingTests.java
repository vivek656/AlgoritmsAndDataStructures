package datastructures.sort;

import common.Pair;
import org.junit.Test;
import sort.CountingSort;

import java.util.Arrays;
import java.util.List;

public class SortingTests {

    @Test
    public void countingSortTest(){
        List<Pair<Integer,String>> list2 = List.of(
                new Pair<>(4,"a"), new Pair<>(2,"a") ,
                new Pair<>(3,"a") ,
                new Pair<>(1,"a"), new Pair<>(3,"b")
        );
        Pair<Integer,String>[] array = list2.toArray(new Pair[0]);
        var sort = new CountingSort<Pair<Integer,String>>(10, Pair::getFirst);
        var result = sort.sort(array);
        System.out.println(Arrays.toString(result));
    }
}
