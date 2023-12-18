package dataStructure.linear.queue;

import java.util.LinkedList;
import java.util.Queue;
public class Main {
    public static void main(String[] args) {
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);
        queue.offer(4);
        queue.offer(5);
        System.out.println(queue); // [1, 2, 3, 4, 5]

        System.out.println(queue.remove()); // 1
        System.out.println(queue.poll()); // 2
        System.out.println(queue); // [3, 4, 5]

        System.out.println(queue.element()); // 3
        System.out.println(queue.peek()); // 3
    }
}