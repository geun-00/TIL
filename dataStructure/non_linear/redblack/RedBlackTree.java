package dataStructure.non_linear.redblack;

import dataStructure.non_linear.bst.BinarySearch;

public interface RedBlackTree<E extends Comparable<E>> extends BinarySearch<E> {
    boolean isRed(Node<E> node);                    //해당 노드가 빨강 노드인지 확인
    boolean isBlack(Node<E> node);                  //해당 노드가 검정 노드인지 확인
    void swapColor(Node<E> nodeA, Node<E> nodeB);   //두 노드의 색깔을 swap
    void LL_rotate(Node<E> node);                   //LL 회전
    void RR_rotate(Node<E> node);                   //RR 회전
    void insertFixup(Node<E> node);                 //삽입시 노드를 재배치
    void removeFixup(Node<E> node);                 //삭제시 노드를 재배치
}
