package problemsessionone;

import algorithms.KarpRabinStringSearch;
import datastructures.graph.Graph;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

       //graphTest();
        karpRabinTest();
       // parentChildTest();

    }

    public static void graphTest(){

        List<Integer[]> pairs = List.of(new Integer[]{1,2} , new Integer[]{0,1}  , new Integer[]{2,0} , new Integer[]{3,4} );
        var graph = Graph.fromPairs(pairs);
        System.out.println(graph.breadthFirstSearch(0).levels);
        System.out.println(graph.unweightedShortestPath(0,4));
        var dfsOutput = graph.depthFirstSearch(0);
        var bfsOutput = graph.breadthFirstSearch(0);
        System.out.println(dfsOutput.unweightedShortestPath(0,4));
        System.out.println(bfsOutput.unweightedShortestPath(0,4));
        System.out.println(graph.asListOfEdges());
        System.out.println(graph);
        System.out.println(dfsOutput.order);
        System.out.println(bfsOutput.levels);
        var fulldfs = graph.fullDFS();
        System.out.println(fulldfs.parent);
        System.out.println(fulldfs.order);

    }

    public static void karpRabinTest() throws IOException {
        var inputString = "hello how are you , with advent of chatGPT every new thing feels like sunday, i dont know what the hell" +
                " i am writing here but that s write , putting chatGPT here as well just to check chatGPT";
        var searchString = "chatGPT";
        var cell = KarpRabinStringSearch.search(searchString,inputString);
        System.out.println(cell.offsets);
    }

    public static void parentChildTest() throws IOException {
        parent p = new child();
        p.funtionUsage();
        p.fun();
        System.out.println(p.parentVariable);

    }
}

class parent{
    String parentVariable;

    public parent() {
        this.parentVariable = "parent value";
    }

    public void funtionUsage(){
        this.fun();
    }

    public void fun(){
        System.out.println("parents function");
    }
}

class child extends parent{


    public child() {
        super();
        parentVariable = "child value";
    }

    @Override
    public void fun(){
        System.out.println("childs function");
    }
}