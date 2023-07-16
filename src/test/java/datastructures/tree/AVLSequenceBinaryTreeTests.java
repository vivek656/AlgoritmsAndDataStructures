package datastructures.tree;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AVLSequenceBinaryTreeTests {


    @Test
    void testAVLSequenceBinaryTreeWithNInserts() {
        var sequence = new AVLSequenceBinaryTree<Integer>();
        sequence.addAll(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        assert(sequence.get(0) == 1);
    }

}
