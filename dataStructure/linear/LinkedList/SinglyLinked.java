package dataStructure.linear.LinkedList;

public interface SinglyLinked<E> {
    void addFirst(E value);
    void addLast(E value);
    boolean add(E value);
    void add(int index, E value);
    E removeFirst();
    E remove();
    E remove(int index);
    boolean remove(Object value);
    E removeLast();
    E get(int index);
    void set(int index, E value);
    int size();
    void clear();
}
