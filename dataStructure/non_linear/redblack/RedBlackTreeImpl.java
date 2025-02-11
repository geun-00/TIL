package dataStructure.non_linear.redblack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RedBlackTreeImpl<E extends Comparable<E>> implements RedBlackTree<E> {

    private Node<E> root;

    @Override
    public boolean isRed(Node<E> node) {
        /*해당 노드가 빨강 노드인지 확인*/
        return isExists(node) && node.color == Node.RED;
    }

    @Override
    public boolean isBlack(Node<E> node) {
        /*해당 노드가 검정 노드인지 확인*/
        return !isExists(node) || node.color == Node.BLACK;
    }

    private boolean isExists(Node<E> node) {
        return node != null;
    }

    @Override
    public void swapColor(Node<E> nodeA, Node<E> nodeB) {
        /*두 노드의 색깔을 swap*/
        int temp = nodeA.color;
        nodeA.color = nodeB.color;
        nodeB.color = temp;
    }

    @Override
    public void LL_rotate(Node<E> node) {
        /*LL 회전(Right-Rotation)*/

        Node<E> GP = node.parent;
        Node<E> L = node.left;
        Node<E> LR = L.right;

        L.right = node;
        node.left = LR;
        node.parent = L;
        if (isExists(LR)) {
            LR.parent = node;
        }
        L.parent = GP;

        if (!isExists(GP)) {
            this.root = L;
        } else {
            if (GP.right == node) {
                GP.right = L;
            } else if (GP.left == node) {
                GP.left = L;
            }
        }
    }

    @Override
    public void RR_rotate(Node<E> node) {
        /*RR 회전 (Left-Rotate)*/

        Node<E> GP = node.parent;
        Node<E> R = node.right;
        Node<E> RL = R.left;

        R.left = node;
        node.right = RL;
        node.parent = R;
        if (isExists(RL)) {
            RL.parent = node;
        }
        R.parent = GP;

        if (!isExists(GP)) {
            this.root = R;
        } else {
            if (GP.right == node) {
                GP.right = R;
            } else if (GP.left == node) {
                GP.left = R;
            }
        }
    }

    @Override
    public void add(E key) {
        /*키를 추가*/
        Node<E> newNode = new Node<>(key);
        if (null == root) {
            root = newNode;
        } else {
            insertNode(root, newNode);
        }
        root.color = Node.BLACK;
    }

    private void insertNode(Node<E> node, Node<E> newNode) {
        int compare = node.key.compareTo(newNode.key);
        if (compare > 0) {
            if (!isExists(node.left)) {
                //이중 연결이기 때문에 자식과 부모를 연결
                node.left = newNode;
                newNode.parent = node;
            } else {
                insertNode(node.left, newNode);
            }
        } else if (compare < 0) {
            if (!isExists(node.right)) {
                //이중 연결이기 때문에 자식과 부모를 연결
                node.right = newNode;
                newNode.parent = node;
            } else {
                insertNode(node.right, newNode);
            }
        }

        insertFixup(node); //재귀적으로 노드 재배치를 하면서 위로 올라간다
    }

    @Override
    public void insertFixup(Node<E> node) {
        /*삽입시 노드를 재배치*/

        /*우측 Double Red*/
        //삼촌 검정, 오른쪽-왼쪽 Double Red
        if (isRed(node.right) && isRed(node.right.left) && isBlack(node.left)) {
            LL_rotate(node.right);
            swapColor(node, node.right);
            RR_rotate(node);
        }
        //삼촌 검정, 오른쪽-오른쪽 Double Red
        else if (isRed(node.right) && isRed(node.right.right) && isBlack(node.left)) {
            swapColor(node, node.right);
            RR_rotate(node);
        }
        //삼촌 빨강, 오른쪽-오른쪽 or 오른쪽-왼쪽 Double Red
        else if (isRed(node.right) && (isRed(node.right.right) || isRed(node.right.left))) {
            node.color = Node.RED;
            node.right.color = Node.BLACK;
            node.left.color = Node.BLACK;
        }
        /*우측 Double Red*/

        /*좌측 Double Red*/
        //삼촌 검정, 왼쪽-오른쪽 Double Red
        else if (isRed(node.left) && isRed(node.left.right) && isBlack(node.right)) {
            RR_rotate(node.left);
            swapColor(node, node.left);
            LL_rotate(node);
        }
        //삼촌 검정, 왼쪽-왼쪽 Double Red
        else if (isRed(node.left) && isRed(node.left.left) && isBlack(node.right)) {
            swapColor(node, node.left);
            LL_rotate(node);
        }
        //삼촌 빨강, 왼쪽-왼쪽 or 왼쪽-오른쪽 Double Red
        else if (isRed(node.left) && (isRed(node.left.left) || isRed(node.left.right))) {
            node.color = Node.RED;
            node.left.color = Node.BLACK;
            node.right.color = Node.BLACK;
        }
        /*좌측 Double Red*/
    }

    @Override
    public void remove(E key) {
        removeNode(root, key);
    }

    private void removeNode(Node<E> node, E key) {
        if (node == null) {
            throw new RuntimeException("Not exists node");
        }

        /*삭제할 키를 찾는 과정*/
        int compare = node.key.compareTo(key);

        if (compare > 0) {
            removeNode(node.left, key);
        } else if (compare < 0) {
            removeNode(node.right, key);
        }
        /*삭제할 키를 찾는 과정*/
        else {
            /*삭제할 키를 찾으면 swap 가능한 선임자 또는 후임자 값을 찾는 과정*/
            if (isExists(node.left)) {
                Node<E> predecessor = getLargestNode(node.left); //왼쪽 서브 트리 중 가장 큰 값
                swap(node, predecessor); //swap
                removeNode(node.left, key);
            } else if (isExists(node.right)) {
                Node<E> successor = getSmallestNode(node.right); //오른쪽 서브 트리 중 가장 큰 값
                swap(node, successor); //swap
                removeNode(node.right, key);
            }
            /*삭제할 키를 찾으면 swap 가능한 선임자 또는 후임자 값을 찾는 과정*/

            //삭제할 노드 리프 노드 도착
            else {
                //삭제할 노드가 검정 노드인 경우 = 재배치
                if (isBlack(node)) {
                    removeFixup(node);
                }
                //부모 노드에서 자식 노드의 연결을 끊는다.
                if (node.parent.left == node) {
                    node.parent.left = null;
                } else if (node.parent.right == node) {
                    node.parent.right = null;
                }

                //자식 노드에서 부모 노드의 연결을 끊는다.
                node.parent = null;
            }
        }
    }

    private void swap(Node<E> node, Node<E> alterNode) {
        E temp = node.key;
        node.key = alterNode.key;
        alterNode.key = temp;
    }

    private Node<E> getLargestNode(Node<E> node) {
        if (node.right == null) {
            return node;
        }
        return getLargestNode(node.right);
    }

    private Node<E> getSmallestNode(Node<E> node) {
        if (node.left == null) {
            return node;
        }
        return getSmallestNode(node.left);
    }

    @Override
    public void removeFixup(Node<E> x) {
        /*삭제시 노드를 재배치*/

        while (x != root && isBlack(x)) { //루트 노드가 되거나, 빨강 노드가 될 때까지

            //삭제할 노드가 왼쪽 자식 노드인 경우
            if (x.parent.left == x) {

                Node<E> w = x.parent.right; //삭제할 노드의 형제 노드

                /*Case 1 - 형제 노드가 빨강 노드*/
                if (isRed(w)) {
                    swapColor(x.parent, w); //부모와 형제의 색깔을 swap
                    RR_rotate(x.parent);    //부모 노드 기준 RR 회전
                    w = x.parent.right;     //바뀐 구조의 새로운 형제 노드 변경
                }
                /*Case 1*/

                //Case 1을 해결하고 Case 2, 3, 4 중에 하나로 해결

                /*Case 2 - 형제 노드가 검정 && 형제 노드의 자식이 모두 검정*/
                if (isBlack(w.left) && isBlack(w.right)) {
                    w.color = Node.RED; //형제 노드의 색깔을 빨강으로 변경
                    x = x.parent;       //x 노드를 부모 노드로 변경
                }
                /*Case 2*/
                else {
                    /*Case 3 - 형제 노드가 검정 && 형제 노드의 왼쪽이 빨강, 오른쪽이 검정*/
                    if (isBlack(w.right)) {
                        swapColor(w, w.left);   //형제와 형제의 왼쪽 자식 색깔 swap
                        LL_rotate(w);           //형제 노드 기준 LL 회전
                        w = x.parent.right;     //바뀐 구조의 새로운 형제 노드 변경
                    }
                    /*Case 3*/

                    /*
                     * Case 3에 의해 새로운 형제 노드의 오른쪽 자식 노드의 색깔은
                     * 빨강색이 된다. 즉 Case 4의 상황으로 해결할 수 있다.
                     */

                    /*Case 4 - 형제 노드의 오른쪽 자식이 빨강*/
                    swapColor(w, x.parent);     //형제 노드와 부모 노드의 색깔을 swap
                    w.right.color = Node.BLACK; //형제 노드의 오른쪽 자식을 검정으로 변경
                    RR_rotate(x.parent);        //부모 노드 기준 RR 회전
                    break;                      //Case 4가 해결되면 재배치 종료
                    /*Case 4*/
                }
            }
            //삭제할 노드가 왼쪽 자식 노드인 경우
            else if (x == x.parent.right) {

                Node<E> w = x.parent.left;

                if (isRed(w)) {
                    swapColor(w, x.parent);
                    LL_rotate(x.parent);
                    w = x.parent.left;
                }
                if (isBlack(w.left) && isBlack(w.right)) {
                    w.color = Node.RED;
                    x = x.parent;
                } else {
                    if (isBlack(w.left)) {
                        swapColor(w, w.right);
                        RR_rotate(w);
                        w = w.parent.left;
                    }
                    swapColor(w, x.parent);
                    w.left.color = Node.BLACK;
                    LL_rotate(x.parent);
                    break;
                }
            }
        }

        //마지막으로 x 노드를 검정 노드로 변경
        x.color = Node.BLACK;
    }

    @Override
    public E search(E key) {
        return searchNode(root, key).key;
    }

    private Node<E> searchNode(Node<E> node, E key) {
        if (node == null) throw new RuntimeException("Not exists node");

        int compare = node.key.compareTo(key);

        if (compare > 0) {
            return searchNode(node.left, key);
        } else if (compare < 0) {
            return searchNode(node.right, key);
        }

        return node;
    }

    //오름차순 순회(중위 순회)
    public void traversal() {
        List<String> result = new ArrayList<>();
        inOrder(this.root, result);
        System.out.println(
            String.join(" => ", result)
        );
    }

    private void inOrder(Node<E> node, List<String> result) {
        if (node == null) {
            return;
        }
        inOrder(node.left, result);
        result.add(node.key.toString() + (node.color == Node.BLACK ? " [BLACK]" : " [RED]"));
        inOrder(node.right, result);
    }
}
