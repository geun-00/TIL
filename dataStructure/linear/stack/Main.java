package dataStructure.linear.stack;

import java.util.Arrays;

class Main {
    public static void main(String[] args) {
        System.out.println("----- integerStack -----");
        Stack<Integer> integerStack = new ArrayStack<>(10);

        for (int i = 1; i <= 10; i++) {
            integerStack.push(i); // 정해진 사이즈 만큼 데이터 추가
        }

        System.out.println(integerStack);

        // 가득 찬 상태에서 push() 시도
        integerStack.push(11); // stack is full

        System.out.println(integerStack.size()); // 10

        System.out.println(integerStack.isFull()); // true

        System.out.println(integerStack.peek()); // 10

        System.out.println(integerStack.pop()); // 10

        System.out.println(integerStack.peek()); // 9

        System.out.println(integerStack.size()); // 9

        integerStack.clear(); // 스택 초기화

        System.out.println(integerStack); // []
        System.out.println(integerStack.isEmpty()); // true

        System.out.println("----- charStack -----");
        Stack<Character> charStack = new ArrayStack<>(10);

        for (char c = 'A'; c <= 'J'; c++) {
            charStack.push(c);
        }

        while (!charStack.isEmpty()) {
            System.out.print(charStack.pop());
        }
        System.out.println();

        System.out.println(charStack.isEmpty());
        System.out.println(charStack.isFull());
    }
}