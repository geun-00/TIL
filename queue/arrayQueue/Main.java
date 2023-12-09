package queue.arrayQueue;

public class Main {
    public static void main(String[] args) {
        ArrayQueue arrayQueue = new ArrayQueue();
        // front, rear = 0 | ArrayQueue[1000]

        System.out.println("Is queue empty?" + arrayQueue.isEmpty());

        for (int i = 1; i <= 10; i++) {
            arrayQueue.push(i);
            System.out.printf("%d enqueued\n", i);
        }

        System.out.println("Is queue empty? " + arrayQueue.isEmpty()); // false
        System.out.println("Is queue full? " + arrayQueue.isFull()); // false
        System.out.println("front : " + arrayQueue.front);
        System.out.println("rear : " + arrayQueue.rear);
        System.out.println("Queue size: " + arrayQueue.size()); // rear - front

        System.out.println("Peek: " + arrayQueue.peek()); // Peek: 1

        // Dequeue elements
        while (!arrayQueue.isEmpty()) {
            System.out.println("Dequeued: " + arrayQueue.pop());
        }

        System.out.println("Is the queue empty? " + arrayQueue.isEmpty()); // true
        System.out.println("Is the queue full? " + arrayQueue.isFull()); // false
        System.out.println("Queue size: " + arrayQueue.size()); // 0
    }
}
