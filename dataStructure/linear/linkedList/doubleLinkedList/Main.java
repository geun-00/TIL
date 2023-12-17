package dataStructure.linear.linkedList.doubleLinkedList;

public class Main {
    public static void main(String[] args) {
        DoublyLinkedList<Integer> list = new DoublyLinkedList<>();

        list.addFirst(1);
        list.add(2);
        list.addLast(3);
        list.addLast(4);

        list.addFirst(0);

        list.add(5);

        System.out.println(list);

        list.removeFirst();
        list.removeLast();

        System.out.println(list);

        list.remove();
        list.remove(1);

        System.out.println(list);

        list.add(6);
        list.add(8);
        System.out.println(list);

        System.out.println(list.get(3));

        list.set(3, 7);
        System.out.println(list);

        System.out.println(list.indexOf(7));
        System.out.println(list.isEmpty());

        list.clear();
        System.out.println(list);
    }
}
