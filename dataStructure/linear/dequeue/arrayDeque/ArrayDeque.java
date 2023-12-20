package dataStructure.linear.dequeue.arrayDeque;

import dataStructure.linear.queue.arrayQueue.Queue;

import java.util.NoSuchElementException;

public class ArrayDeque<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 64;

    private Object[] dequeueArray; // 요소가 담길 배열
    private int size; // 요소의 개수
    private int front; // 시작 인덱스(빈 공간)
    private int rear; // 마지막 인덱스

    public ArrayDeque() {
        dequeueArray = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }
    public ArrayDeque(int capacity) {
        dequeueArray = new Object[capacity];
        this.size = 0;
        this.front = 0;
        this.rear = 0;
    }

    private void resize(int newCapacity) {
        int curLength = dequeueArray.length;
        Object[] newArray = new Object[newCapacity];

        for (int i = 1, j = front + 1; i <= size; i++, j++) {
            newArray[i] = dequeueArray[j % curLength];
        }
        dequeueArray = null;
        dequeueArray = newArray;
        front = 0;
        rear = size;
    }

    @Override
    public boolean offer(E value) {
        return offerLast(value);
    }

    public boolean offerLast(E value) {
        // 용량이 가득 찼을 경우
        if ((rear + 1) % dequeueArray.length == front) {
            resize(dequeueArray.length * 2);
        }
        rear = (rear + 1) % dequeueArray.length; // rear를 한 칸 뒤로 이동

        dequeueArray[rear] = value;
        size++; // 요소 개수 증가
        return true;
    }

    public boolean offerFirst(E value) {
        // 용량이 가득 찼을 경우
        if ((front - 1 + dequeueArray.length) % dequeueArray.length == rear) {
            resize(dequeueArray.length * 2);
        }
        dequeueArray[front] = value;
        front = (front - 1 + dequeueArray.length) % dequeueArray.length; // front를 한 칸 뒤로 이동
        size++; // 요소 개수 증가
        return true;
    }

    @Override
    public E poll() {
        return pollFirst();
    }
    @SuppressWarnings("unchecked")
    public E pollFirst() {
        if (size == 0) {
            return null;
        }
        front = (front + 1) % dequeueArray.length; // front를 한 칸 앞으로 이동

        E returnValue = (E) dequeueArray[front];
        dequeueArray[front] = null;
        size--;

        // 요소 개수가 배열 크기의 1/4 이하일 경우
        if(dequeueArray.length>DEFAULT_CAPACITY && size <= dequeueArray.length/4) {
            // 배열 크기를 1/2로 축소
            // 최소용량(64) 보다 작아지지 않도록 함
            resize(Math.max(DEFAULT_CAPACITY, dequeueArray.length/2));
        }
        return returnValue;
    }

    public E remove() {
        return removeFirst();
    }

    public E removeFirst() {
        E returnValue = pollFirst();
        if (returnValue == null) {
            throw new NoSuchElementException();
        }
        return returnValue;
    }

    public E pollLast() {
        if(size == 0) {
            return null;
        }
        E returnValue = (E)dequeueArray[rear];
        dequeueArray[rear] = null;
        rear = (rear - 1 + dequeueArray.length) % dequeueArray.length; // rear를 한 칸 앞으로 이동
        size--;

        // 요소 개수가 배열 크기의 1/4 이하일 경우
        if (dequeueArray.length > DEFAULT_CAPACITY && size <= dequeueArray.length / 4) {
            // 배열 크기를 1/2로 축소
            // 최소용량(64) 보다 작아지지 않도록 함
            resize(Math.max(DEFAULT_CAPACITY, dequeueArray.length / 2));
        }
        return returnValue;
    }

    public E removeLast() {
        E returnValue = pollLast();
        if (returnValue == null) {
            throw new NoSuchElementException();
        }
        return returnValue;
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @SuppressWarnings("unchecked")
    public E peekFirst() {
        if (size == 0) {
            return null;
        }
        return (E) dequeueArray[(front + 1) % dequeueArray.length];
    }

    @SuppressWarnings("unchecked")
    public E peekLast() {
        if (size == 0) {
            return null;
        }
        return (E) dequeueArray[rear];
    }

    public E element() {
        return getFirst();
    }

    public E getFirst() {
        E returnValue = peek();
        if (returnValue == null) {
            throw new NoSuchElementException();
        }
        return returnValue;
    }

    public E getLast() {
        E returnValue = peekLast();
        if (returnValue == null) {
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
        if (size == 0) {
            return false;
        }
        int start = (front + 1) % dequeueArray.length;

        /**
         * i : 요소의 개수만큼 반복
         * idx : 요소의 인덱스
         */
        for (int i = 0, idx = start; i < size; i++, idx = (idx + 1) % dequeueArray.length)
        {  // idx가 배열의 끝에 도달하면 다시 배열의 처음으로 돌아감
            if (dequeueArray[idx].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            dequeueArray[i] = null;
        }
        size = front = rear = 0;
    }

    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int start = (front + 1) % dequeueArray.length;
        for (int i = start; i != rear; i = (i + 1) % dequeueArray.length) {
            sb.append(dequeueArray[i]).append(", ");
        }
        sb.append(dequeueArray[rear]);
        sb.append("]");
        return sb.toString();
    }

    public int front() {
        return front;
    }

    public int rear() {
        return rear;
    }
}
