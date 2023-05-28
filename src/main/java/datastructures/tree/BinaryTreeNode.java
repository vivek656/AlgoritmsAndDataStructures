package datastructures.tree;

import common.TripleConsumer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class BinaryTreeNode<T> {
    protected BinaryTreeNode<T> parent;
    protected BinaryTreeNode<T> left;
    protected BinaryTreeNode<T> right;
    protected T data;

    protected HashMap<String , TreeAugmentation> augmentations = new HashMap<>();

    BinaryTreeNode(T data){
        this.data = data;
    }

    protected <X extends BinaryTreeNode<T>> X getLeft() {
        return (X) left;
    }

    protected <X extends BinaryTreeNode<T>> X getRight() {
        return (X) right;
    }

    protected <X extends BinaryTreeNode<T>> X setLeft(X node) {
        this.left = node;
        return node;
    }

    protected <X extends BinaryTreeNode<T>> X setRight(X node) {
        this.right = node;
        return node;
    }

    /*
    TraversalOrder , for each binary tree node ,everything in left subtree comes first in the order
    everything in right subtree comes next in the order
     */
    List<T> traversalOrder(){
        LinkedList<T> result = new LinkedList<>();
        traversalOrderRecursive(result);
        return result;
    }

    void addAugmentation(String key, Object defaultVal , Object startVal , TripleConsumer<Object, Object, Object, Object> computeFunction){
        TreeAugmentation newAugmentation = new TreeAugmentation(
                key , startVal , defaultVal
        ){
            @Override
            public Object recompute(Object parentValue, Object leftValue, Object rightValue) {
                return computeFunction.apply(parentValue, leftValue , rightValue);
            }
        };
        augmentations.put(newAugmentation.key , newAugmentation);
    }

    Object getAugmentedValue(String key){
        return augmentations.get(key)!=null ? augmentations.get(key).value : null;

    }
    private void traversalOrderRecursive(List<T> a){
        if(left!=null)left.traversalOrderRecursive(a);
        if(data!=null)a.add(data);
        if(right!=null)right.traversalOrderRecursive(a);
    }

    private BinaryTreeNode<T> subtreeFirstNodeInTraversalOrder(){
        if(left!=null) return left.subtreeFirstNodeInTraversalOrder();
        else return this;
    }

    private BinaryTreeNode<T> subtreeLastNodeInTraversalOrder(){
        if(right!=null) return right.subtreeLastNodeInTraversalOrder();
        else return this;
    }

    BinaryTreeNode<T> successorInTraversalOrder(){
        if(right!=null) return right.subtreeFirstNodeInTraversalOrder();
        BinaryTreeNode<T> tempParent  = this;
        while(tempParent.parent != null && tempParent.parent.right == tempParent){
            tempParent = tempParent.parent;
        }
        return parent;
    }

    BinaryTreeNode<T> predecessorInTraversalOrder(){
        if(left!=null) return left.subtreeFirstNodeInTraversalOrder();
        BinaryTreeNode<T> tempParent  = this;
        while(tempParent.parent != null && tempParent.parent.left == tempParent){
            tempParent = tempParent.parent;
        }
        return parent;
    }

    private void maintainAugmentationWayUp(){
        augmentations.values().forEach(augmentation ->
            augmentation.recompute(this)
        );
        if(parent!=null)parent.maintainAugmentationWayUp();
    }

    protected BinaryTreeNode<T> subtreeInsertBefore(BinaryTreeNode<T> nodeToAdd){
        if(left!=null) {
            BinaryTreeNode<T> lastNodeInLeftSubtree = left.subtreeLastNodeInTraversalOrder();
            lastNodeInLeftSubtree.right = nodeToAdd;
            nodeToAdd.parent= lastNodeInLeftSubtree;
            nodeToAdd.maintainAugmentationWayUp();
        }
        else {
            left = nodeToAdd;
            nodeToAdd.parent= this;
            nodeToAdd.maintainAugmentationWayUp();
        }
        return nodeToAdd;
    }

    protected BinaryTreeNode<T> subtreeInsertAfter(BinaryTreeNode<T> nodeToAdd){
        if(right!=null) {
            BinaryTreeNode<T> firstNodeInRightSubtree = right.subtreeFirstNodeInTraversalOrder();
            firstNodeInRightSubtree.left = nodeToAdd;
            nodeToAdd.parent= firstNodeInRightSubtree;
            nodeToAdd.maintainAugmentationWayUp();
        }
        else {
            right = nodeToAdd;
            nodeToAdd.parent= this;
            nodeToAdd.maintainAugmentationWayUp();
        }
        return nodeToAdd;
    }

    void swapData(BinaryTreeNode<T> a , BinaryTreeNode<T> b){
        T temp = a.data;
        a.data = b.data;
        b.data = temp;
    }

    void detach(){
        parent = right = left = null;
    }

    synchronized BinaryTreeNode<T> deleteNodeFromSubtree(BinaryTreeNode<T> nodeToDelete){
        if (nodeToDelete.left!=null || nodeToDelete.right!=null){
            BinaryTreeNode<T> temp = (nodeToDelete.left!=null)? nodeToDelete.predecessorInTraversalOrder()
                    : nodeToDelete.successorInTraversalOrder();
            swapData(temp, nodeToDelete);
            return deleteNodeFromSubtree(temp);
        }else if (parent!=null){//its a leaf!
            if(parent.left==this)parent.left = null;
            parent.right = null;
            parent.maintainAugmentationWayUp();
        }
        nodeToDelete.detach();
        return nodeToDelete;
    }
}
