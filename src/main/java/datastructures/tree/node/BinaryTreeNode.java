package datastructures.tree.node;

import common.TripleConsumer;
import datastructures.common.Graph;
import datastructures.graph.GraphEdge;

import java.util.*;

@SuppressWarnings("unchecked")
public class BinaryTreeNode<T> implements Graph<T> {
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

    public BinaryTreeNode<T> getHighestParent(){
        if(parent!=null)return parent.getHighestParent();
        else return this;
    }

    /*
    TraversalOrder , for each binary tree node ,everything in left subtree comes first in the order
    everything in right subtree comes next in the order
     */
    public List<T> traversalOrder(){
        LinkedList<T> result = new LinkedList<>();
        traversalOrderRecursive(result);
        return result;
    }

    public void addAugmentation(String key, Object defaultVal , Object startVal , TripleConsumer<Object, Object, Object, Object> computeFunction){
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

    public static <E> void setAugmentationsToNode(BinaryTreeNode<E> node , HashMap<String , TreeAugmentation> augmentationMap){
        augmentationMap.values().forEach(aug -> {
            var clazz = aug.value.getClass();
            node.addAugmentation(aug.key , clazz.cast(aug.defaultValue) , clazz.cast(aug.value) ,
                    aug::recompute);
        });
    }

    public Object getAugmentedValue(String key){
        return augmentations.get(key)!=null ? augmentations.get(key).value : null;
    }
    private void traversalOrderRecursive(List<T> a){
        if(left!=null)left.traversalOrderRecursive(a);
        if(data!=null)a.add(data);
        if(right!=null)right.traversalOrderRecursive(a);
    }

    public List<T> levelOrder(){
        var result = new LinkedList<T>();
        Queue<BinaryTreeNode<T>> q = new LinkedList<>();
        q.add(this);
        var currentNode = q.peek();
        while (!q.isEmpty()){
            currentNode = q.poll();
            result.addLast(currentNode.data);
            if(currentNode.left!=null)q.add(currentNode.left);
            if(currentNode.right!=null)q.add(currentNode.right);
        }
        return result;

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

    protected void maintainAugmentationWayUp(){
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
        }
        nodeToDelete.detach();
        return nodeToDelete;
    }

    public void rotateRight(){
        if(getLeft()==null) return;
        var leftChild = getLeft();
        setLeft(leftChild.getRight());
        leftChild.parent = parent;
        if(parent!=null){
            if(parent.right == this)parent.right = leftChild;
            else parent.left = leftChild;
        }
        parent = leftChild;
        leftChild.right = this;
        if(getLeft()!=null)getLeft().maintainAugmentationWayUp();
        maintainAugmentationWayUp();
    }

    public void rotateLeft(){
        var rightChild = getRight();
        if(rightChild ==null)return;
        //setting rightChild.left as rightChild of this node
        setRight(rightChild.getLeft());
        //setting parent as rightChild parent
        rightChild.parent = parent;
        if(parent!=null){
            if(parent.right == this)parent.right = rightChild;
            else parent.left = rightChild;
        }
        //setting rightChild as parent
        parent = rightChild;
        rightChild.left = this;

        //maintaining augmentations
        if(getRight()!=null) getRight().maintainAugmentationWayUp();
        maintainAugmentationWayUp();
    }

    @Override
    public Map<T, Set<GraphEdge<T, T>>> asAdjacencyMap() {
        Queue<BinaryTreeNode<T>> q = new LinkedList<>();
        q.add(this);
        Map<T, Set<GraphEdge<T,T>>> adjacencyMap = new HashMap<>();
        BinaryTreeNode<T> currentNode = q.peek();
        while (!q.isEmpty()){
            currentNode = q.poll();
            adjacencyMap.put(currentNode.data, new HashSet<>());
            if(currentNode.left!=null) {
                adjacencyMap.get(currentNode.data).add(
                       new GraphEdge(currentNode.data , currentNode.left.data)
                );
                q.add(currentNode.left);
            }
            if(currentNode.right!=null){
                adjacencyMap.get(currentNode.data).add(
                        new GraphEdge<>(currentNode.data,currentNode.right.data)
                );
                q.add(currentNode.right);
            }
        }
        return adjacencyMap;
    }
}
