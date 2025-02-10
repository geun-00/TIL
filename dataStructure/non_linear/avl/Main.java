package dataStructure.non_linear.avl;

public class Main {
    public static void main(String[] args) {

        AVLTreeImpl<Integer> avl = new AVLTreeImpl<>();
        avl.add(5);
        avl.add(2);
        avl.add(9);
        avl.add(3);
        avl.add(7);
        avl.add(4);
        avl.add(8);
        avl.add(1);
        avl.add(6);

        avl.traversal();
        avl.remove(9);
        avl.traversal();
    }
}
