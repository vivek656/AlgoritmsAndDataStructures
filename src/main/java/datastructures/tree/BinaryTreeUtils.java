package datastructures.tree;

public class BinaryTreeUtils {


    private BinaryTreeUtils(){}

    public static Integer sizeFunction(Integer size , Integer leftTreeSize , Integer rightTreeSize){
        return ((leftTreeSize!=null)? leftTreeSize : 0) +
                (rightTreeSize!=null ? rightTreeSize : 0) + 1;
    }
}
