package dataStructure.non_linear.avl;

public class Node<E extends Comparable<E>> {
    E key;
    Node<E> left;
    Node<E> right;
    int height = 0;

    public Node(E key) {
        this.key = key;
    }
}
