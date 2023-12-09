package queue.arrayQueue;

public class ArrayQueue {
    int MAX = 1000;
    int front; // 머리 쪽에 위치할 index값, pop할 때 참조하는 index
    int rear; // 꼬리 쪽에 위치할 index값, push할 때 참조하는 index
    int [] queue;

    public ArrayQueue() {
        front = 0;
        rear = 0;
        queue = new int[MAX];
    }

    public boolean isEmpty() {
        return front == rear;
    }

    public boolean isFull() {
        return rear == MAX;
    }

    public int size() {
        return rear - front;
    }

    public void push(int value) {
        if (isFull()) {
            System.out.println("Queue is Full");
            return;
        }
        queue[rear++] = value;
    }

    public int pop() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return -1;
        }
        return queue[front++];
    }

    public int peek() {
        if (isEmpty()) {
            System.out.println("Queue is Empty");
            return -1;
        }
        return queue[front];
    }
}
