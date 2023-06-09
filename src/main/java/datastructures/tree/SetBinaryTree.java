package datastructures.tree;

import common.TripleConsumer;
import datastructures.common.Graph;
import datastructures.graph.GraphEdge;
import datastructures.tree.node.BinarySearchTreeNode;
import datastructures.tree.node.BinaryTreeNode;
import datastructures.tree.node.TreeAugmentation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unchecked")
public class SetBinaryTree<T> implements Iterable<T> , Graph<T> {

    protected Comparator<T> comparator;
    protected BinarySearchTreeNode<T> root;

    HashMap<String , TreeAugmentation> augmentations;

    private static final TripleConsumer<Integer,Integer,Integer,Integer> sizeFunction =
            BinaryTreeUtils::sizeFunction;
    private static final String SIZE_KEY = "SIZE";
    public SetBinaryTree(){
        this.augmentations = new HashMap<>();
        TreeAugmentation sizeAugment = new TreeAugmentation(
                SIZE_KEY, 1, 0
        ) {
            @Override
            public Object recompute(Object parentValue, Object leftValue, Object rightValue) {
                return sizeFunction.apply((Integer) parentValue, (Integer) leftValue, (Integer) rightValue);
            }

        };
        augmentations.put(sizeAugment.key , sizeAugment);
    }

    public SetBinaryTree(Comparator<T> comparator){
        this();
        this.comparator = comparator;
    }

    public <E extends Comparable<E>> SetBinaryTree(E rootValue){
        this((Comparator<T>) Comparator.naturalOrder());
        this.root = newNode((T) rootValue);
    }

    public <E extends Comparable<E>> SetBinaryTree(List<E> a){
        this((Comparator<T>) Comparator.naturalOrder());
        build((List<T>) a);
    }

    protected BinarySearchTreeNode<T> newNode(T data){
        BinarySearchTreeNode<T> node = new BinarySearchTreeNode<>(data);
        BinaryTreeNode.setAugmentationsToNode(node,augmentations);
        return node;
    }

    public List<T> asList(){
        if(root==null)return Collections.emptyList();
        else return root.traversalOrder();
    }

    void build(List<T> a){
        a.forEach(this::insert);
    }

    Integer size(){
        if(root==null) return 0;
        return (Integer) root.getAugmentedValue(SIZE_KEY);
    }
    Boolean insert(T data){
        BinarySearchTreeNode<T> node = newNode(data);
        if(root == null){
            this.root = node;
            return true;
        }
       return root.subtreeInsert(node, comparator)!=null;
    }

    Boolean remove(T data){
        if(root==null) return false;
        BinarySearchTreeNode<T> node = root.subtreeFind(data , comparator);
        if(node==null)return false;
        node = (BinarySearchTreeNode<T>) root.deleteNodeFromSubtree(node);
        return node!=null;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return asList().iterator();
    }

    @Override
    public Map<T, Set<GraphEdge<T, T>>> asAdjacencyMap() {
        if(root==null) return Collections.emptyMap();
        else return root.asAdjacencyMap();
    }
}
