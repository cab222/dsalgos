package cabkata.tree;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cabkata.tree.BinarySearchTree.Node;

public final class BinarySearchTreeTest {
    BinarySearchTree<Integer> tree = new BinarySearchTree<Integer>();

    @Before
    public void setUp()
    {
        tree.insert(Integer.valueOf(50));
        tree.insert(Integer.valueOf(20));
        tree.insert(Integer.valueOf(10));
        tree.insert(Integer.valueOf(70));
        tree.insert(Integer.valueOf(90));
        tree.insert(Integer.valueOf(60));
        tree.insert(Integer.valueOf(65));
    }

    @After
    public void tearDown()
    {
        tree = new BinarySearchTree<Integer>();
    }

    @Test
    public void testInorderPrinting()
    {
        StringBuffer buffer = new StringBuffer();
        tree.printInorder(buffer);
        assertEquals(buffer.toString(), "10 20 50 60 65 70 90 ");
    }

    @Test
    public void testMinAndMax()
    {
        assertEquals(tree.minimum().intValue(), 10);
        assertEquals(tree.maximum().intValue(), 90);
    }

    @Test
    public void testInsertion()
    {
        assertEquals(tree.root.parent, null);
        assertEquals(tree.root.value.intValue(), 50);
        assertEquals(tree.root.left.value.intValue(), 20);
        assertEquals(tree.root.right.value.intValue(), 70);
        assertEquals(tree.root.left.parent.value.intValue(), 50);
        assertEquals(tree.root.right.parent.value.intValue(), 50);
    }

    @Test
    public void testFind()
    {
        Node<Integer> node70 = tree.find(Integer.valueOf(70), tree.root);
        assertEquals(node70.value.intValue(), 70);
    }

    @Test
    public void testSuccessor()
    {
        Node<Integer> node70 = tree.find(Integer.valueOf(70), tree.root);
        assertEquals(node70.value.intValue(), 70);
        Node<Integer> successorTo70 = tree.successor(node70);
        assertEquals(successorTo70.value.intValue(), 90);
        assertEquals(
                tree.successor(tree.find(Integer.valueOf(50), tree.root)).value
                        .intValue(),
                60);
    }

    @Test
    public void testRemove()
    {
        tree.remove(Integer.valueOf(70));
        StringBuffer buffer = new StringBuffer();
        tree.printInorder(buffer);
        assertEquals("10 20 50 60 65 90 ", buffer.toString());

        tree.remove(Integer.valueOf(90));
        buffer = new StringBuffer();
        tree.printInorder(buffer);
        assertEquals("10 20 50 60 65 ", buffer.toString());

        tree.remove(Integer.valueOf(50));
        buffer = new StringBuffer();
        tree.printInorder(buffer);
        assertEquals("10 20 60 65 ", buffer.toString());
    }
}
