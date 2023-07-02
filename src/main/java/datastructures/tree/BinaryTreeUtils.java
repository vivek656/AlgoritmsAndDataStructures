package datastructures.tree;

public class BinaryTreeUtils {


    private BinaryTreeUtils(){}

    public static Integer sizeFunction(Integer size , Integer leftTreeSize , Integer rightTreeSize){
        return ((leftTreeSize!=null)? leftTreeSize : 0) +
                (rightTreeSize!=null ? rightTreeSize : 0) + 1;
    }

    public static Integer heightFunction(Object size , Object leftTreeHeight , Object rightTreeHeight){
        return Integer.max(
                (leftTreeHeight!= null && leftTreeHeight instanceof Integer) ? (Integer) leftTreeHeight : 0,
                (rightTreeHeight != null && leftTreeHeight instanceof Integer ) ? (Integer) rightTreeHeight : 0
        ) + 1;
    }
}
