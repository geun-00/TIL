package dataStructure.linear.linkedList.singleLinkedList;

public class Main {
    public static void main(String[] args) {
        SinglyLinkedList<Number> list = new SinglyLinkedList<>();

        for (int i = 1; i <= 10; i++) {
            list.addFirst(i);
        }
        System.out.println(list); // [10, 9, 8, 7, 6, 5, 4, 3, 2, 1]

        list.addFirst(10);
        list.addFirst(20);
        System.out.println(list); // [20, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]

        list.addLast(15);
        list.add(20);
        System.out.println(list); // [20, 10, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 15, 20]

        System.out.println(list.size()); // 14

//        list.add(14, 30); // IndexOutOfBoundsException

        list.add(10, 30);
        System.out.println(list); // [20, 10, 10, 9, 8, 7, 6, 5, 4, 3, 30, 2, 1, 15, 20]

        System.out.println(list.removeFirst()); // 20
        System.out.println(list.removeFirst()); // 10
        System.out.println(list.removeFirst()); // 10
        System.out.println(list.remove()); // 9
        System.out.println(list.remove()); // 8

        System.out.println(list.size()); // 10

        System.out.println(list); // [7, 6, 5, 4, 3, 30, 2, 1, 15, 20]

        System.out.println(list.remove(1));// index로 삭제, 출력: 6
        System.out.println(list.remove((Number)30)); // value로 삭제, 출력: true

        System.out.println(list); // [7, 5, 4, 3, 2, 1, 15, 20]

        System.out.println(list.removeFirst()); // 7
        System.out.println(list.removeLast()); // 20

        System.out.println(list.size()); // 6

//        System.out.println(list.get(7)); // IndexOutOfBoundsException
        System.out.println(list); // [5, 4, 3, 2, 1, 15]

        System.out.println(list.get(3)); // 2

        list.set(3, 30);
        System.out.println(list.get(3)); // 30

        System.out.println(list); // [5, 4, 3, 30, 1, 15]

        list.clear();
        System.out.println(list); // []
    }
}
