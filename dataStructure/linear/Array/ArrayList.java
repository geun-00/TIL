package dataStructure.linear.Array;

import java.util.Arrays;

public class ArrayList<E> implements List<E> {

    private static final int DEFAULT_CAPACITY = 10; // 최소(기본) 크기
    private static final Object[] EMPTY_ARRAY = {};
    private int size; // 배열에 담긴 요소의 개수
    Object[] array; // 요소를 담을 배열

    public ArrayList() { // 초기 공간 할당 X
        array = EMPTY_ARRAY;
        size = 0;
    }

    public ArrayList(int capacity) { // 초기 공간 할당 O
        array = new Object[capacity];
        size = 0;
    }

    private void resize() {
        int arrayCapacity = array.length;

        // 용량이 0일 경우
        if (Arrays.equals(array, EMPTY_ARRAY)) {
            array = new Object[DEFAULT_CAPACITY];
            return;
        }
        // 용량이 꽉 찰 경우
        if (size == arrayCapacity) {
            int newCapacity = arrayCapacity * 2;
            array = Arrays.copyOf(array, newCapacity);
            return;
        }
        // 용량의 절반 미만으로 요소가 차 있을 경우
        if (size < (arrayCapacity / 2)) {
            int newCapacity = arrayCapacity / 2;
            array = Arrays.copyOf(array, Math.max(newCapacity, DEFAULT_CAPACITY));
        }
    }

    @Override
    public void add(E value) {
        addLast(value);
    }

    public void addLast(E value) {
        if (size == array.length) {
            resize();
        }
        array[size] = value;
        size++;
    }

    @Override
    public void add(int index, E value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size) { // index가 마지막 위치라면 addLast
            addLast(value);
        } else {
            if (size == array.length) {  // 꽉 찬 경우 용량 재할당
                resize();
            }
            for (int i = size; i > index; i--) { // index위치 이후 한 칸씩 뒤로 밀기
                array[i] = array[i - 1];
            }
            array[index] = value;
            size++;
        }
    }

    @Override
    public void addFirst(E value) {
        add(0, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        return (E) array[index];
    }

    @SuppressWarnings("unchecked")
    @Override
    public E set(int index, E value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }else
            array[index] = value;
        return (E) array[index];
    }

    @Override
    public int indexOf(Object value) {
        // '==' 주소 비교
        // equals 객체값 비교
        for (int i = 0; i < size; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return  -1;
    }

    @Override
    public int lastIndexOf(Object value) {
        for (int i = size - 1; i >= 0; i--) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object value) {
        return indexOf(value) >= 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        E returnValue = (E) array[index];
        array[index] = null;

        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
            array[i + 1] = null;
        }
        size--;
        resize();
        return returnValue;
    }

    @Override
    public boolean remove(Object value) {
        int index = indexOf(value);

        if (index == -1) {
            return false;
        }
        remove(index);
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
        resize();
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }else
            return Arrays.toString(array);
    }
}
