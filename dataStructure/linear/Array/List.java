package dataStructure.linear.Array;

public interface List<E> {
    void add(E value);
    void add(int index, E value);
    void addFirst(E value);
    E get(int index);
    E set(int index, E value);
    int indexOf(Object value);
    int lastIndexOf(Object value);
    boolean contains(Object value);
    E remove(int index);
    boolean remove(Object value);
    int size();
    boolean isEmpty();
    void clear();
}