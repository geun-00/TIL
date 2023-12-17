package dataStructure.linear.stack.linkedStack;

public interface Stack<E> {
    void push(E value);
    E pop();
    E peek();
    boolean isEmpty();
    void clear();
}