package dataStructure.non_linear.redblack;

public class Node<E extends Comparable<E>> {
    E key;
    Node<E> left, right, parent;
    int color = RED;

    public Node(E key) {
        this.key = key;
    }

    static final int RED = 0;
    static final int BLACK = 1;
}
