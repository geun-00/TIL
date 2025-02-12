package dataStructure.non_linear.btree;

public class Main {
    public static void main(String[] args) {
        BTreeImpl<Integer> bTree = new BTreeImpl<>(3); //생성자에서 t 전달

        bTree.add(3);
        bTree.add(4);
        bTree.add(5);
        bTree.add(1);
        bTree.add(2);
        bTree.add(6);
        bTree.add(8);
        bTree.add(9);
        bTree.add(7);
        bTree.add(10);
        bTree.add(12);
        bTree.add(13);
        bTree.add(11);
        bTree.add(14);
        bTree.add(15);
        bTree.traversal();

        bTree.remove(5);
        bTree.remove(6);
        bTree.remove(7);
        bTree.remove(4);
        bTree.remove(3);
        bTree.remove(1);

        bTree.traversal();
    }
}
