package datastructures.tree;


import datastructures.tree.node.AVLSearchTreeNode;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * USING AVL Tree to Implement Sequence
 * insertion, deletion O(lgN) , get set o(lgN)
 * @param <T>
 */
public class AVLSequenceBinaryTree<T> implements List<T> {


    private SequenceAVLTreeNode<T> root;

    private static final String SIZE = "size";

    void changeRoot() {
        root = (SequenceAVLTreeNode<T>) root.getHighestParent();
    }


    private static class SequenceAVLTreeNode<T> extends AVLSearchTreeNode<T> {
        public SequenceAVLTreeNode(T data) {
            super(data);
            addAugmentation(SIZE, 0, 1,
                    (a, b, c) -> BinaryTreeUtils.sizeFunction((Integer) a, (Integer) b, (Integer) c));
        }


        Integer getSize() {
            return (Integer) getAugmentedValue(SIZE);
        }

        SequenceAVLTreeNode<T> subtreeAt(int index) {
            Objects.checkIndex(index, getSize());
            int lsize = (getLeft() != null) ?
                    ((SequenceAVLTreeNode<T>) getLeft()).getSize() : 0;
            if (index < lsize) {
                return ((SequenceAVLTreeNode<T>) getLeft()).subtreeAt(index);
            } else if (index > lsize) {
                return ((SequenceAVLTreeNode<T>) getRight()).subtreeAt(index - lsize - 1);
            } else {
                return this;
            }
        }

        SequenceAVLTreeNode<T> subtreeInsertAt(Integer index, T value) {
            var newNode = new SequenceAVLTreeNode<>(value);
            if (index == 0) {
                var node = subtreeFirstNodeInTraversalOrder();
                return (SequenceAVLTreeNode<T>) node.subtreeInsertBefore(newNode);
            } else {
                var nodeOneIndexBefore = subtreeAt(index - 1);
                return (SequenceAVLTreeNode<T>) nodeOneIndexBefore.subtreeInsertAfter(newNode);
            }
        }

        SequenceAVLTreeNode<T> subtreeDeleteAt(Integer index) {
            var node = subtreeAt(index);
            return (SequenceAVLTreeNode<T>) super.deleteNodeFromSubtree(node);
        }

    }


    @Override
    public int size() {
        return root!=null ? root.getSize() : 0;
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public boolean contains(Object o) {
        if(root==null) return false;
        return root.traversalOrder().contains(o);
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return root.traversalOrder().listIterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        if(root==null) return new Object[0];
        return root.traversalOrder().toArray();
    }

    @Override
    public <T1> T1[] toArray(@NotNull T1[] a) {
        if(root==null) return Collections.emptyList().toArray(a);
        return root.traversalOrder().toArray(a);
    }

    @Override
    public boolean add(T t) {
         add(size() , t);
         return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public T get(int index) {
        if(isEmpty() || index >= size()) throw  new IndexOutOfBoundsException();
        return root.subtreeAt(index).getData();
    }

    @Override
    public T set(int index, T element) throws IllegalArgumentException {
        assert root != null;
        root.subtreeAt(index).setData(element);
        return element;
    }

    @Override
    public void add(int index, T element) {
        if (index == 0 && root == null) {
            root = new SequenceAVLTreeNode<>(element);
            return;
        }
        root.subtreeInsertAt(index,element);
        changeRoot();
    }

    @Override
    public T remove(int index) {
        Objects.checkIndex(index, size());
        return root.subtreeDeleteAt(index).getData();
    }

    @Override
    public int indexOf(Object o) {
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        return -1;
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        Objects.requireNonNull(root);
        return root.traversalOrder().listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        Objects.checkIndex(index , size());
        return root.traversalOrder().listIterator(index);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return root.traversalOrder().subList(fromIndex,toIndex);
    }
}
