package dataStructure.non_linear.bst;

public class Node<E extends Comparable<E>> {
    E key;
    Node<E> left;
    Node<E> right;

    public Node(E key) {
        this.key = key;
    }

    public void insert(Node<E> node) {
        int compare = node.key.compareTo(this.key);

        if (compare < 0) {
            if (this.left == null) {
                this.left = node;
            } else {
                this.left.insert(node);
            }
        } else if (compare > 0) {
            if (this.right == null) {
                this.right = node;
            } else {
                this.right.insert(node);
            }
        }
    }
}
