package dataStructure.non_linear.avl;

import dataStructure.non_linear.bst.BinarySearch;

public interface AVLTree<E extends Comparable<E>> extends BinarySearch<E> {
    int getHeight(Node<E> node);            //노드의 높이를 계산
    int getBalance(Node<E> node);           //노드의 균형도를 계산
    void changeNodeHeight(Node<E> node);    //노드의 높이를 변경
    Node<E> LL_rotate(Node<E> node);        //LL 회전
    Node<E> RR_rotate(Node<E> node);        //RR 회전
    Node<E> rotate(Node<E> node);           //균형을 맞추기 위해 회전
}
