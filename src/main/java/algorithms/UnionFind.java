package algorithms;

public class UnionFind {

    private final int[] parent;
    private final int[] size;
    private int uniqueSets;

    public UnionFind(int elements) {
        parent = new int[elements];
        size = new int[elements];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        uniqueSets = parent.length;
    }


    public int getUniqueSets() {
        return uniqueSets;
    }

    public int find(int itemIndex) {
        validate(itemIndex);
        int  root = itemIndex;
        while (root != parent[root]) {
            root = parent[root];
        }
        return root;
    }

    public boolean connected(int x, int y) {
        return  find(x) == find(y);
    }

    public int union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) {
            return rootX;
        }
        if (size[rootX] > size[rootY]) {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
        uniqueSets--;
        return parent[rootY];
    }

    private void validate(int x) {
        if (x < 0 || x >= uniqueSets) {
            throw new IllegalArgumentException("Index: " + x + ", is out of range. size: " + parent.length );
        }
    }
}
