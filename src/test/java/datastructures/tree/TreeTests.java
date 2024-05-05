package datastructures.tree;

import common.AbstractAlgoAndDsTestBase;
import datastructures.graph.GraphVisualize;
import org.junit.jupiter.api.Test;

import java.util.List;

class TreeTests extends AbstractAlgoAndDsTestBase {

    @Test
    void binarySearchTreeTest() {
        var bst = new SetBinaryTree<Integer>(List.of(5, 8, 2, 4, 3, 6, 1));
        logger.info("BST ORDER_TRAVERSAL: {}", bst.asList());
    }

    @Test
    void avlSearchTreeTest() {
        var bst = new AVLSetBinaryTree<Integer>(List.of(9, 6, 55, 22, 5, 8, 2, 4, 3, 6, 1));
        logger.info("BST ORDER_TRAVERSAL: {}", bst.asList());
        GraphVisualize.of(bst).withName("AVL TREE GRAPH").build()
                .display();
    }

}
