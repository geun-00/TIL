package dataStructure.non_linear.btree;

public interface BTree<E extends Comparable<E>> {
    void add(E key);                        //키를 추가
    void remove(E key);                     //카를 삭제
    E search(E key);                        //키를 검색
    boolean isLeafNode(Node<E> node);       //리프 노드인지 확인
    int findKeyIndex(Node<E> node, E key);  //키와 연관된 index 검색
    Node<E> splitNode(Node<E> x, Node<E> y, int childNodeIndex);    //노드를 분할
}
