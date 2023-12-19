package dataStructure.linear.heap.minHeap;

public class MinHeap<T extends Comparable<T>> {
    private Object[] heapArray;
    private int size;
    private int capacity;
    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.heapArray = new Object[capacity + 1]; // 0번 인덱스는 사용하지 않음
    }

    private int parent(int index) {
        return index / 2;
    }
    private int leftChild(int index) {
        return index * 2;
    }
    private int rightChild(int index) {
        return index * 2 + 1;
    }

    private void resize() {
        Object[] newArray = new Object[capacity * 2 + 1];
        System.arraycopy(heapArray, 1, newArray, 1, size);
        heapArray = null;
        heapArray = newArray;
        capacity *= 2;
    }
    private void swap(int index1, int index2) {
        Object temp = heapArray[index1];
        heapArray[index1] = heapArray[index2];
        heapArray[index2] = temp;
    }

    @SuppressWarnings("unchecked")
    public void add(T value) {
        if (size + 1 > capacity) resize();
        heapArray[++size] = value;

        int currentIndex = size;
        while (currentIndex > 1 && ((T) heapArray[currentIndex]).compareTo((T) heapArray[parent(currentIndex)]) < 0) {
            swap(currentIndex, parent(currentIndex));
            currentIndex = parent(currentIndex);
        }
    }
    @SuppressWarnings("unchecked")
    public T remove() {
        if(size == 0) return null;
        T returnValue = (T)heapArray[1];
        heapArray[1] = heapArray[size--];

        int index = 1;
        while(leftChild(index) <= size) {
            int child;
            if(rightChild(index) <= size && ((T)heapArray[rightChild(index)]).compareTo((T)heapArray[leftChild(index)]) < 0) {
                child = rightChild(index);
            } else {
                child = leftChild(index);
            }
            if(((T)heapArray[index]).compareTo((T)heapArray[child]) < 0) break;
            swap(index, child);
            index = child;
        }
        return returnValue;
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for (int i = 1; i <= size; i++) {
            result.append(heapArray[i]);
            if (i < size) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    public int getSize() {
        return size;
    }
    public int getCapacity() {
        return capacity;
    }
}
