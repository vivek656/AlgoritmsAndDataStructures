package datastructures.tree;

import datastructures.tree.node.AVLSearchTreeNode;
import datastructures.tree.node.BinarySearchTreeNode;
import datastructures.tree.node.BinaryTreeNode;

import java.util.Comparator;
import java.util.List;

@SuppressWarnings("unchecked")
public class AVLSetBinaryTree<T> extends SetBinaryTree<T>{

    public AVLSetBinaryTree(){
        super();
    }

    public AVLSetBinaryTree(Comparator<T> comparator){
        super(comparator);
    }

    public <E extends Comparable<E>> AVLSetBinaryTree(E rootValue){
        this((Comparator<T>) Comparator.naturalOrder());
        this.root = newNode((T) rootValue);
    }

    public <E extends Comparable<E>> AVLSetBinaryTree(List<E> a){
        this((Comparator<T>) Comparator.naturalOrder());
        build((List<T>) a);
    }

    private AVLSearchTreeNode<T> getRoot(){
        return (AVLSearchTreeNode<T>) root;
    }

    @Override
    Boolean insert(T data) {
        AVLSearchTreeNode<T> node = newNode(data);
        if(root==null){
            this.root = node;
            return true;
        }
        var addedNode = root.subtreeInsert(node,comparator);
        if(addedNode==null) return false;
        ((AVLSearchTreeNode<T>) addedNode).maintain();
        setNewRoot();
        return true;
    }

    private void setNewRoot(){
        root = (BinarySearchTreeNode<T>) root.getHighestParent();
    }

    @Override
    Boolean remove(T data) {
        if(root==null) return false;
        BinarySearchTreeNode<T> node = root.subtreeFind(data , comparator);
        if(node==null)return false;
        node = (BinarySearchTreeNode<T>) getRoot().deleteNodeFromSubtree(node);
        return node !=null;
    }

    @Override
    protected AVLSearchTreeNode<T> newNode(T data) {
        AVLSearchTreeNode<T> node = new AVLSearchTreeNode<>(data);
        BinaryTreeNode.setAugmentationsToNode(node,augmentations);
        return  node;
    }
}
