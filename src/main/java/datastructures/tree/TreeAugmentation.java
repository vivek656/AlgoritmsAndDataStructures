package datastructures.tree;

import com.sun.source.tree.Tree;

public abstract class TreeAugmentation<T extends Object> {
    final String key;
    T value;
    T defaultValue = null;

    protected TreeAugmentation(String key , T value , T defaultValue) {
        this.key = key;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public void recompute(BinaryTreeNode tree){
        TreeAugmentation<T> currentVal = getAugmentation(tree);
        if(currentVal == null)return;
        TreeAugmentation<T> leftTree = getAugmentation(tree.left);
        TreeAugmentation<T> rightTree = getAugmentation(tree.right);
        recompute(currentVal.getOrDefaultValue() , leftTree.getOrDefaultValue(), rightTree.getOrDefaultValue());
    }
    public T getOrDefaultValue(){
        if(value!= null)return value;
        return defaultValue;
    }

    public abstract void recompute(T parentValue, T leftValue, T rightValue);

    TreeAugmentation<T> getAugmentation(BinaryTreeNode tree){
        if(tree==null)return null;
        if(tree.augmentations == null) return null;
        if(tree.augmentations.isEmpty()) return null;
        return (TreeAugmentation<T>) tree.augmentations.stream().filter(a -> a.equals(this)).findFirst().orElse(null);
    }


}
