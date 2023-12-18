package dataStructure.linear.queue.arrayQueue;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class ArrayQueue<E> implements Queue<E> {
    private static final int DEFAULT_CAPACITY = 64; // 최소 용량
    private Object[] array; // 요소가 담길 배열
    private int size; // 요소 개수
    private int front; // 시작 인덱스(빈 공간)
    private int rear; // 마지막 인덱스

    // 초기 용량 할당 안 할 경우 default
    public ArrayQueue() {
        array = new Object[DEFAULT_CAPACITY];
        size = front = rear = 0;
    }
    // 초기 용량 할당 할 경우
    public ArrayQueue(int capacity) {
        array = new Object[capacity];
        size = front = rear = 0;
    }

    private void resize(int newCapacity) {

        int arrayCapacity = array.length; // 현재 용적 크기

        Object[] newArray = new Object[newCapacity]; // 용적을 변경한 배열

        /*
         * i = new array index
         * j = original array
         * index 요소 개수(size)만큼 새 배열에 값 복사
         */
        for (int i = 1, j = front + 1; i <= size; i++, j++) {
            newArray[i] = array[j % arrayCapacity];
        }

        this.array = null;
        this.array = newArray; // 새 배열을 기존 array의 배열로 덮어씌움

        front = 0;
        rear = size;

    }

    @Override
    public boolean offer(E value) {
        // 옹량이 가득 찼을 경우
        if ((rear + 1) % array.length == front) {
            resize(array.length * 2);
        }
        rear = (rear + 1) % array.length;
        array[rear] = value;
        size++;
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E poll() {
        if (size == 0) {
            return null;
        }
        front = (front + 1) % array.length;

        E returnValue = (E) array[front];
        array[front] = null;
        size--;

        if (array.length > DEFAULT_CAPACITY && size < (array.length / 4)) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
        return returnValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        if (size == 0) {
            return null;
        }
        return (E) array[(front + 1) % array.length];
    }

    public E remove() {
        E returnValue = poll();
        if (returnValue == null) {
            throw new NoSuchElementException();
        }
        return returnValue;
    }

    public E element() {
        E returnValue = peek();
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
        int start = (front + 1) % array.length;

        for (int i = 0, idx = start; i < size; i++, idx = (idx + 1) % array.length) {
            if (array[idx].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = front = rear = 0;
    }

    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int start = (front + 1) % array.length;
        for (int i = start; i != rear; i = (i + 1) % array.length) {
            sb.append(array[i]).append(", ");
        }
        sb.append(array[rear]);
        sb.append("]");
        return sb.toString();
    }

    public int getFront() {
        return front;
    }

    public int getRear() {
        return rear;
    }
}
