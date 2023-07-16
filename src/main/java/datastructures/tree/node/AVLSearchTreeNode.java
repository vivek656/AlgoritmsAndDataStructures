package datastructures.tree.node;

import datastructures.tree.BinaryTreeUtils;

import java.util.HashMap;

public class AVLSearchTreeNode<T> extends BinarySearchTreeNode<T> {

    private static final String HEIGHT = "HEIGHT";
    private static final Integer DEFAULT_HEIGHT = -1;


    public AVLSearchTreeNode(T data) {
        super(data);
        if(this.augmentations==null || this.augmentations.isEmpty()) this.augmentations = new HashMap<>();
        addAugmentation(HEIGHT , DEFAULT_HEIGHT , 0 , BinaryTreeUtils::heightFunction);
    }

    private Integer getHeight(){
        return (Integer) getAugmentedValue(HEIGHT);
    }
    private Integer getHeight(BinaryTreeNode<T> node){
        if(node == null) return DEFAULT_HEIGHT;
        return (Integer) node.getAugmentedValue(HEIGHT);
    }

    @Override
    protected AVLSearchTreeNode<T> getLeft() {
        return (AVLSearchTreeNode<T>) super.getLeft();
    }

    @Override
    protected AVLSearchTreeNode<T> getRight() {
        return (AVLSearchTreeNode<T>) super.getRight();
    }

    public void maintain(){
        reBalance();
        if(parent!=null)((AVLSearchTreeNode<T>)parent).maintain();
    }

    @Override
    public synchronized BinaryTreeNode<T> deleteNodeFromSubtree(BinaryTreeNode<T> nodeToDelete){
        if (nodeToDelete.left!=null || nodeToDelete.right!=null){
            BinaryTreeNode<T> temp = (nodeToDelete.left!=null)? nodeToDelete.predecessorInTraversalOrder()
                    : nodeToDelete.successorInTraversalOrder();
            swapData(temp, nodeToDelete);
            return deleteNodeFromSubtree(temp);
        }else if (parent!=null){//its a leaf!
            if(parent.left==this)parent.left = null;
            else parent.right = null;
            parent.maintainAugmentationWayUp();
            ((AVLSearchTreeNode<T>)parent).maintain();
        }
        nodeToDelete.detach();
        return nodeToDelete;
    }

    @Override public BinaryTreeNode<T> subtreeInsertBefore(BinaryTreeNode<T> nodeToAdd){
        var node = super.subtreeInsertBefore(nodeToAdd);
        ((AVLSearchTreeNode<T>) node).maintain();
        return node;
    }

    @Override public BinaryTreeNode<T> subtreeInsertAfter(BinaryTreeNode<T> nodeToAdd){
        var node = super.subtreeInsertAfter(nodeToAdd);
        ((AVLSearchTreeNode<T>) node).maintain();
        return node;
    }

    public void reBalance(){
        maintainAugmentationWayUp();
        if(getHeight(getLeft()) >= getHeight(getRight()) + 2){
            if(getHeight(getLeft().getLeft()) >= getHeight(getLeft().getRight())){
                rotateRight();
            }else {
                getLeft().rotateLeft();
                rotateRight();
            }
        } else if (getHeight(getRight()) >= 2 + getHeight(getLeft())) {
            if(getHeight(getRight().getRight()) >= getHeight(getRight().getLeft())){
                rotateLeft();
            }else {
                getRight().rotateRight();
                rotateLeft();
            }
        }
    }
}
