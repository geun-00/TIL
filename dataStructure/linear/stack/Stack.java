package dataStructure.linear.stack;
interface Stack<E> {
    boolean isEmpty();
    boolean isFull();
    void push(E item);
    E pop();
    E peek();
    void clear();
    int size();
}