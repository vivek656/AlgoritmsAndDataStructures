package datastructures.algorithms;

import algorithms.UnionFind;
import org.junit.Test;
import org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.assertThrows;

public class UnionFindTests {

    @Test
    public void createUnionFindTest(){
        int size = 10;
        UnionFind UF = new UnionFind(size);
        assert UF.find(1) == 1;
    }


    @Test
    public void unionFindPostUnion2Items(){
        int size = 10;
        UnionFind UF = new UnionFind(size);
        assert UF.find(1) == 1;
        assert  UF.find(2) == 2;
        UF.union(1,2);
        assert UF.find(1)== UF.find(2);
        assert UF.connected(1,2);
    }

    @Test
    public void unionFIndThrowsErrorIdIndexIsOutOfRange(){
        int size = 10;
        UnionFind UF = new UnionFind(size);

        assertThrows(IllegalArgumentException.class, () -> UF.find(size+2));
        assertThrows(IllegalArgumentException.class, () -> UF.union(1,size+2));
        assertThrows(IllegalArgumentException.class, () -> UF.connected(2,size+2));
    }

    @Test
    public  void getUniqueSetsChangesAsWeUnion(){
        int size = 10;
        UnionFind UF = new UnionFind(size);

        assert UF.getUniqueSets() == size;

        UF.union(1,2);
        UF.union(4,3);

        assert  UF.getUniqueSets() == size - 2;
    }

}
