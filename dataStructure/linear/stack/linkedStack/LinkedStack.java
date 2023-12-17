package dataStructure.linear.stack.linkedStack;

public class LinkedStack<E> implements Stack<E> {
    private Node<E> topNode;

    private static class Node<E> {
        private E data;
        private Node<E> next;

        Node(E data, Node<E> next){
            this.data = data;
            this.next = next;
        }
    }

    @Override
    public void push(E value) {
        topNode = new Node<>(value, topNode);
    }

    @Override
    public E pop() {
        if(isEmpty()){
            System.out.println("Stack is Empty");
            return null;
        }else {
            E returnData = topNode.data;
            topNode = topNode.next;

            return returnData;
        }
    }

    @Override
    public E peek() {
        if (topNode == null) {
            System.out.println("Stack is Empty");
            return null;
        }
        else return topNode.data;
    }

    @Override
    public boolean isEmpty() {
        return topNode == null;
    }

    @Override
    public void clear() {
        if (isEmpty()) {
            System.out.println("Stack is Empty");
            return;
        } else {
            Node<E> top = topNode;
            while (top != null) {
                Node<E> next = topNode.next;
                topNode.data = null;
                topNode.next = null;
                top = next;
            }
        }
        topNode = null;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        } else {
            StringBuilder sb = new StringBuilder("[");
            Node<E> top = topNode;
            while (top != null) {
                sb.append(top.data);
                if (top.next != null) {
                    sb.append(", ");
                }
                top = top.next;
            }
            sb.append("]");
            return sb.toString();
        }
    }
}