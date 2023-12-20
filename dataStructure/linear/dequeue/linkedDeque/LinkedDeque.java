package dataStructure.linear.dequeue.linkedDeque;

import dataStructure.linear.queue.arrayQueue.Queue;

import java.util.NoSuchElementException;

public class LinkedDeque<E> implements Queue<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    public LinkedDeque() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    private static class Node<E> {
        private E item;
        private Node<E> before;
        private Node<E> next;

        public Node(E item) {
            this.item = item;
            this.before = null;
            this.next = null;
        }
    }

    public boolean offerFirst(E value) {
        Node<E> newNode = new Node<>(value);
        newNode.next = head;

        if (head != null) {
            head.before = newNode;
        }
        head = newNode;
        size++;

        if (head.next == null) {
            tail = head;
        }

        return true;
    }

    @Override
    public boolean offer(E value) {
        return offerLast(value);
    }

    public boolean offerLast(E value) {
        if(size == 0) {
            return offerFirst(value);
        }
        Node<E> newNode = new Node<>(value);
        tail.next = newNode;
        newNode.before = tail;
        tail = newNode;
        size++;

        return true;
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        E returnValue = head.item;

        Node<E> nextNode = head.next;
        head.item = null;
        head.next = null;

        if(nextNode != null) {
            nextNode.before = null;
        }
        head = null;
        head = nextNode;
        size--;

        if (size == 0) {
            tail = null;
        }
        return returnValue;
    }

    public E remove() {
        return removeFirst();
    }

    public E removeFirst() {
        E returnValue = poll();
        if(returnValue == null) {
            throw new NoSuchElementException();
        }
        return returnValue;
    }

    public E pollLast() {
        if (size == 0) {
            return null;
        }
        E returnValue = tail.item;

        Node<E> beforeNode = tail.before;
        tail.item = null;
        tail.before = null;

        if(beforeNode != null) {
            beforeNode.next = null;
        }
        tail = null;
        tail = beforeNode;
        size--;

        if (size == 0) {
            head = null;
        }
        return returnValue;
    }

    public E removeLast() {
        E returnValue = pollLast();
        if(returnValue == null) {
            throw new NoSuchElementException();
        }
        return returnValue;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    public E peekFirst() {
        if (size == 0) {
            return null;
        }
        return head.item;
    }

    public E peekLast() {
        if (size == 0) {
            return null;
        }
        return tail.item;
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        E returnValue = peek();
        if(returnValue == null) {
            throw new NoSuchElementException();
        }
        return returnValue;
    }

    public E getLast() {
        E returnValue = peekLast();
        if(returnValue == null) {
            throw new NoSuchElementException();
        }
        return returnValue;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object value) {

        while(head != null) {
            if (head.item.equals(value)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }

    public void clear() {
        while(head != null) {
            Node<E> nextNode = head.next;
            head.item = null;
            head.next = null;
            head.before = null;
            head = nextNode;
        }
        head = tail = null;
        size = 0;
    }
}
