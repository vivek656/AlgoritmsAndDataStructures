package datastructures.tree;


import java.util.Objects;

public abstract class TreeAugmentation {
    final String key;
    Object value;
    Object defaultValue;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TreeAugmentation that)) return false;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    protected TreeAugmentation(String key, Object value, Object defaultValue) {
        this.key = key;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public <E> void recompute(BinaryTreeNode<E> tree) {
        TreeAugmentation leftAug = getAugmentation(tree.left);
        TreeAugmentation rightAug = getAugmentation(tree.right);
        Object leftVal = (leftAug!=null && leftAug.getOrDefaultValue().getClass()==getOrDefaultValue().getClass()) ? leftAug.value : defaultValue;
        Object rightVal = (rightAug!=null && rightAug.getOrDefaultValue().getClass()==getOrDefaultValue().getClass()) ? rightAug.getOrDefaultValue() : defaultValue;

        value = recompute(
                getOrDefaultValue(),
                leftVal,
                rightVal
        );
    }

    public Object getOrDefaultValue() {
        if (value != null) return value;
        return defaultValue;
    }

    public abstract Object recompute(Object parentValue, Object leftValue, Object rightValue);

    <E> TreeAugmentation getAugmentation(BinaryTreeNode<E> tree) {
        if (tree == null) return null;
        if (tree.augmentations == null) return null;
        if (tree.augmentations.isEmpty()) return null;
        return tree.augmentations.getOrDefault(key , null);
    }
}
