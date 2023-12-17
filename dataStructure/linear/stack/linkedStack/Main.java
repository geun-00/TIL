package dataStructure.linear.stack.linkedStack;

public class Main {
    public static void main(String[] args) {
        LinkedStack<Integer> stack = new LinkedStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);

        System.out.println(stack); // [5, 4, 3, 2, 1]

        System.out.println(stack.peek()); // 5
        stack.pop(); // 5
        stack.pop(); // 4
        stack.pop(); // 3
        stack.pop(); // 2
        stack.pop(); // 1
        System.out.println(stack); // []
        stack.peek(); // Stack is Empty
        stack.pop(); // Stack is Empty

    }
}