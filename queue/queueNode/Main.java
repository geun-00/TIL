package queue.queueNode;

public class Main {
    public static void main(String[] args) {
        QueueNodeManager queueManager = new QueueNodeManager();

        // Enqueue some elements
        for (int i = 1; i <= 5; i++) {
            queueManager.push(i);
            System.out.printf("%d enqueued\n", i);
        }

        System.out.println("Is the queue empty? " + queueManager.isEmpty()); // false
        System.out.println("Queue size: " + queueManager.size()); // 5

        System.out.println("Peek: " + queueManager.peek().getValue()); // Peek: 1

        // Dequeue elements (not applicable for this implementation)

        System.out.println("Is the queue empty? " + queueManager.isEmpty()); // false
        System.out.println("Queue size: " + queueManager.size()); // 5
    }
}
