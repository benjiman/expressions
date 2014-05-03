package uk.co.benjiweber.expressions;

import org.junit.Test;
import uk.co.benjiweber.expressions.caseclass.Case2;

import static org.junit.Assert.assertEquals;
import static uk.co.benjiweber.expressions.Leaf.leaf;
import static uk.co.benjiweber.expressions.Node.node;

public class CaseTreeTest {

    @Test
    public void counting_nodes_example() {
        Tree tree =
            node(
                5,
                node(1, leaf, leaf),
                node(
                    3,
                    leaf,
                    node(4, leaf, leaf)
                )
            );

        assertEquals(4, countNodes(tree));
    }

    int countNodes(Tree tree) {
        return tree.match()
            .when(Leaf.class, n -> 0)
            .when(Node.class, n -> 1 + countNodes(n.left()) + countNodes(n.right()));
    }
}

    interface Tree extends Case2<Leaf, Node> {}
    interface Leaf extends Tree {
        static Leaf leaf = new Leaf() {};
    }
    interface Node<T> extends Tree {
        Tree left();
        Tree right();
        T value();
        static <T> Node<T> node(T value, Tree left, Tree right) {
            return new Node<T>() {
                public T value() { return value; }
                public Tree left() { return left; }
                public Tree right() { return right; }
            };
        }
    }

