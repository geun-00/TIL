package dataStructure.linear.queue.linkedQueue;

import dataStructure.linear.queue.arrayQueue.Queue;

public class LinkedListQueue<E> implements Queue<E> {
    private Node<E> head;
    private Node<E> tail;
    private int size;
    public LinkedListQueue() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    private static class Node<E> {
        private E data;
        private Node<E> next;
        public Node(E data) {
            this.data = data;
            this.next = null;
        }
    }

    @Override
    public boolean offer(E value) {
        Node<E> newNode = new Node<>(value);
        if (size == 0) {
            head=newNode;
        }
        else tail.next = newNode;

        tail = newNode;
        size++;
        return true;
    }

    @Override
    public E poll() {
        if(size == 0) return null;

        E value = head.data;
        Node<E> nextNode = head.next;
        head.next = null;
        head.data = null;
        head = nextNode;
        size--;
        return value;
    }
    @Override
    public E peek() {
        if(size == 0) return null;
        return head.data;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(E value) {
        Node<E> node = head;
        while(node != null) {
            if(node.data.equals(value)) return true;
            node = node.next;
        }
        return false;
    }

    public void clear() {
        Node<E> node = head;
        while(node != null) {
            Node<E> nextNode = node.next;
            node.next = null;
            node.data = null;
            node = nextNode;
        }
        head = null;
        tail = null;
        size = 0;
    }
    public String toString() {
        if(size == 0) return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Node<E> node = head;
        while(node != null) {
            sb.append(node.data);
            if(node.next != null) sb.append(", ");
            node = node.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
