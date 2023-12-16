package dataStructure.linear.stack;

import java.util.Arrays;

class ArrayStack<E> implements Stack<E> {

    private Object[] array;
    private int top;
    private int size;

    public ArrayStack(int size) {
        this.size = size;
        array = new Object[size];
        top = -1;
    }

    @Override
    public boolean isEmpty() {
        return top == -1;
    }

    @Override
    public boolean isFull() {
        return top == size - 1;
    }

    @Override
    public void push(E item) {
        if (isFull()) {
            System.out.println("stack is full");
            return;
        }
        array[++top] = item;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E pop() {
        if (isEmpty()) {
            System.out.println("stack is Empty");
            return null;
        }
        size--;
        return (E) array[top--];
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        if (isEmpty()) {
            System.out.println("stack is Empty");
            return null;
        }
        return (E) array[top];
    }

    @Override
    public void clear() {
        if (isEmpty()) {
            System.out.println("stack is already empty");
            return;
        } else {
            top = -1;
            size = 0;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        else return Arrays.toString(array);
    }
}
