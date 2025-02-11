package dataStructure.non_linear.redblack;

public class Main {
    public static void main(String[] args) {
        RedBlackTreeImpl<Integer> rbt = new RedBlackTreeImpl<>();
        rbt.add(5);
        rbt.add(2);
        rbt.add(9);
        rbt.add(3);
        rbt.add(7);
        rbt.add(4);
        rbt.add(8);
        rbt.add(1);
        rbt.add(6);

        rbt.traversal();
    }
}
