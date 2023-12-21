package dataStructure.linear.hash.hashSet;

class Node<E> {
    final int hash;
    final E key;
    Node<E> next;
    Node(int hash, E key, Node<E> next) {
        this.hash = hash;
        this.key = key;
        this.next = next;
    }
}
