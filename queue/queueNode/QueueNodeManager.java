package queue.queueNode;

public class QueueNodeManager {
    QueueNode front, rear;
    public QueueNodeManager() {
        front = null;
        rear = null;
    }

    public boolean isEmpty() {
        return (front == null && rear == null);
    }

    public void push(int value) {
        QueueNode queueNode = new QueueNode(value);
        if (isEmpty()) {
            front = queueNode;
            rear = queueNode;
        } else {
            front.setNextNode(queueNode);
            front = queueNode;
        }
    }

    public QueueNode peek() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return null;
        } else {
            return rear;
        }
    }

    public int size() {
        QueueNode front2 = front;
        QueueNode rear2 = rear;
        int count = 0;
        while (front2 != null && rear2 != null) {
            count++;
            rear2 = rear2.getNextNode();
        }
        return count;
    }
}
