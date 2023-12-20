package dataStructure.linear.dequeue.arrayDeque;

public class Main {
    public static void main(String[] args) {
        /**
         * Deque
         * 삽입(용량 제한 없음)
         * offer, offerLast : 뒤에 요소 추가
         * offerFirst : 앞에 요소 추가
         *
         * 삭제(비어있을 경우 null 반환)
         * poll, pollFirst : 앞에 요소 반환 후 제거
         * pollLast : 뒤에 요소 반환 후 제거
         * 삭제(비어있을 경우 예외 발생)
         * remove, removeFirst : 앞에 요소 제거
         * removeLast : 뒤에 요소 제거
         *
         * 조회(비어있을 경우 null 반환)
         * peek, peekFirst : 앞에 요소 반환
         * peekLast : 뒤에 요소 반환
         * 조회(비어있을 경우 예외 발생)
         * element, getFirst : 앞에 요소 반환
         * getLast : 뒤에 요소 반환
         */
        ArrayDeque<Integer> deque = new ArrayDeque<>(20);

        for (int i = 1; i <= 10; i++) {
            deque.offerFirst(i);
        }
        System.out.println(deque); // [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
        for (int i = 11; i <= 20; i++) {
            deque.offerLast(i);
        }
        System.out.println(deque); // [10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]
        System.out.println(deque.front()); // 0
        System.out.println(deque.rear()); // 20

        Integer removeFirst = deque.removeFirst(); // pollFirst와 동일
        System.out.println("removeFirst = " + removeFirst); // 10

        Integer poll = deque.poll(); // pollFirst와 동일
        System.out.println("poll = " + poll); // 9

        Integer removeLast = deque.removeLast(); // pollLast와 동일
        System.out.println("removeLast = " + removeLast); // 20

        Integer pollLast = deque.pollLast(); // pollLast와 동일
        System.out.println("pollLast = " + pollLast); // 19

        System.out.println(deque); // [8, 7, 6, 5, 4, 3, 2, 1, 11, 12, 13, 14, 15, 16, 17, 18]

        deque.removeLast(); // 18
        deque.removeLast(); // 17
        deque.removeLast(); // 16
        deque.removeLast(); // 15
        deque.removeLast(); // 14

        deque.removeFirst(); // 8
        deque.removeFirst(); // 7
        deque.removeFirst(); // 6
        deque.removeFirst(); // 5
        deque.removeFirst(); // 4

        System.out.println(deque); // [3, 2, 1, 11, 12, 13]

        System.out.println(deque.peek()); // 3
        System.out.println(deque.peekLast()); // 13
    }
}
