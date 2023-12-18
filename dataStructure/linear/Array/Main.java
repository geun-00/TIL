package dataStructure.linear.Array;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            arrayList.add(i);
        }
        System.out.println(arrayList);

        arrayList.addLast(20);
        System.out.println(arrayList);

        System.out.println(arrayList.size());

        System.out.println(arrayList.remove(10));

        for (int i = 30; i <= 100; i += 10) {
            arrayList.addLast(i);
        }
        System.out.println(arrayList);

        arrayList.clear();
        System.out.println(arrayList);
    }
}