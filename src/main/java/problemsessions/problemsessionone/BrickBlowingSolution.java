package problemsessions.problemsessionone;


import org.apache.commons.lang3.tuple.MutablePair;

import java.util.Arrays;
import java.util.LinkedList;

/**
  refers to mit oc2 6006 fall 2020 , problem session2 question 3-4
  <a href="https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-spring-2020/resources/mit6_006s20_prob2/">pdf link here </a>
 */

@SuppressWarnings("unchecked")
public class BrickBlowingSolution {
    private MutablePair<Integer,Integer>[] houses ;
    private LinkedList<MutablePair<Integer,Integer>> houseSequence;


    public BrickBlowingSolution(Integer[] houses){
        houseSequence = new LinkedList<>();
        this.houses = Arrays.stream(houses).map(
                val -> new MutablePair<>(val, 1)
        ).toList().toArray(new MutablePair[0]);
        houseSequence.addAll(Arrays.asList(this.houses));

    }

    public void solve(){
        mergeAndSolve(houses, 0, houses.length - 1);
        System.out.println(houseSequence);

    }

    private MutablePair<Integer,Integer>[] mergeAndSolve(MutablePair<Integer,Integer>[] array , int start , int end){
        if(start >= end) return new MutablePair[]{array[start]};
        var middle = (start + end) / 2;
        var left = mergeAndSolve(array, start, middle);
        var right = mergeAndSolve(array, middle+1, end);
        MutablePair<Integer,Integer>[] result = new MutablePair[end - start + 1];
        // 7 8 9 1 3 5
        int l = 0 , r = 0 ,blowned = 0, len = end - start+1;
        for(int i = 0 ; i < len ; i ++){
            if((r < right.length) && (l >= left.length ||  left[l].left>=right[r].left)){
                if(l < left.length && left[l].left>=right[r].left) blowned++;
                result[i] = right[r];
                r++;
            }else {
                left[l].right+=blowned;
                result[i]=left[l];
                l++;
            }
        }
        return result;
    }
}
