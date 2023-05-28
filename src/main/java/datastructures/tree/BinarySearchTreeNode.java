package datastructures.tree;

import java.util.Comparator;

@SuppressWarnings("unchecked")
public class BinarySearchTreeNode<T> extends BinaryTreeNode<T>{


    BinarySearchTreeNode(T data) {
        super(data);
    }

    @Override protected BinarySearchTreeNode<T> getLeft() {
        return (BinarySearchTreeNode<T>) left;
    }

    @Override protected BinarySearchTreeNode<T> getRight() {
        return (BinarySearchTreeNode<T>) right;
    }

    BinarySearchTreeNode<T> subtreeFind(T key , Comparator<T> comparator){
        if(comparator.compare(data , key) > 0 && left!=null){
            return getLeft().subtreeFind(key, comparator);
        }else if (comparator.compare(data, key) < 0 && right!=null){
            return getRight().subtreeFind(key, comparator);
        }else if (comparator.compare(data,key)==0){
            return this;
        }else {
            return null;
        }
    }

    BinarySearchTreeNode<T> subtreeFindNext(T key , Comparator<T> comparator){
        if(comparator.compare(data,key) <= 0){
            if(right!=null)return getRight().subtreeFindNext(key, comparator);
            else return null;
        } else if (left!=null) {
            BinarySearchTreeNode<T> nextBiggerInLeftSubtree = getLeft().subtreeFindNext(key,comparator);
            if(nextBiggerInLeftSubtree!=null)return nextBiggerInLeftSubtree;
        }
        return this;
    }

    BinarySearchTreeNode<T> subtreeFindPrev(T key , Comparator<T> comparator){
        if(comparator.compare(data,key) >= 0){
            if(left!=null)return getLeft().subtreeFindNext(key, comparator);
            else return null;
        } else if (right!=null) {
            BinarySearchTreeNode<T> nextSmallerInRightSubtree = getLeft().subtreeFindNext(key,comparator);
            if(nextSmallerInRightSubtree !=null)return nextSmallerInRightSubtree;
        }
        return this;
    }

    BinarySearchTreeNode<T> subtreeInsert(BinarySearchTreeNode<T> nodeToInsert, Comparator<T> comparator){
        if(comparator.compare(data, nodeToInsert.data) > 0){
            if(left!=null) return getLeft().subtreeInsert(nodeToInsert , comparator);
            else return (BinarySearchTreeNode<T>) subtreeInsertBefore(nodeToInsert);
        } else if (comparator.compare(data, nodeToInsert.data) < 0) {
            if(right!=null) return getRight().subtreeInsert(nodeToInsert, comparator);
            else return (BinarySearchTreeNode<T>) subtreeInsertAfter(nodeToInsert);
        }
        else return this;
    }

}
