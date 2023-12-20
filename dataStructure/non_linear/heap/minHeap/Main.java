package dataStructure.non_linear.heap.minHeap;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        MinHeap<Integer> maxHeap = new MinHeap<>(10);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            maxHeap.add(random.nextInt(100));
        }
        maxHeap.add(random.nextInt(100));
        System.out.println(maxHeap);

        System.out.println(maxHeap.remove());
        System.out.println(maxHeap);

        System.out.println(maxHeap.remove());
        System.out.println(maxHeap);

        System.out.println(maxHeap.remove());
        System.out.println(maxHeap);

        System.out.println(maxHeap.getSize());
        System.out.println(maxHeap.getCapacity());
    }
}
