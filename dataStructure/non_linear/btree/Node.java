package dataStructure.non_linear.btree;

import java.util.ArrayList;
import java.util.List;

public class Node<E extends Comparable<E>> {

    private final List<E> keys;
    private final List<Node<E>> children;

    public Node(int t) {
        this.keys = new ArrayList<>(2 * t);
        this.children = new ArrayList<>(2 * t);
    }

    public List<E> getKeys() {
        return keys;
    }

    public List<Node<E>> getChildren() {
        return children;
    }

    public Node<E> getLastChild() {
        return children.get(children.size() - 1);
    }

    public E getLastKey() {
        return keys.get(keys.size() - 1);
    }
}
