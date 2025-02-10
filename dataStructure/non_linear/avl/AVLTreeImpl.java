package dataStructure.non_linear.avl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AVLTreeImpl<E extends Comparable<E>> implements AVLTree<E> {

    private Node<E> root;

    @Override
    public int getHeight(Node<E> node) {
        /*노드의 높이를 계산*/
        int leftChildHeight = (node.left != null) ? node.left.height : -1;
        int rightChildHeight = (node.right != null) ? node.right.height : -1;

        return Math.max(leftChildHeight, rightChildHeight) + 1;
    }

    @Override
    public int getBalance(Node<E> node) {
        /*노드의 균형도를 계산*/
        int leftChildHeight = (node.left != null) ? node.left.height : -1;
        int rightChildHeight = (node.right != null) ? node.right.height : -1;

        return leftChildHeight - rightChildHeight;
    }

    @Override
    public void changeNodeHeight(Node<E> node) {
        /*노드의 높이를 변경*/
        node.height = getHeight(node);
    }

    @Override
    public Node<E> LL_rotate(Node<E> node) {
        /*LL 회전*/
        Node<E> L = node.left; //왼쪽 자식 노드
        Node<E> LR = L.right;  //왼쪽 자식 노드의 오른쪽 자식 노드

        L.right = node;     //부모 노드를 왼쪽 자식 노드의 오른쪽 자식 노드로 연결
        node.left = LR;     //왼쪽 자식 노드의 오른쪽 자식 노드를 부모의 왼쪽 자식 노드로 연결

        //높이에 영향을 받은 노드 높이 재계산
        changeNodeHeight(node);
        changeNodeHeight(L);

        return L; //새로운 부모 노드가 될 왼쪽 자식 노드를 반환
    }

    @Override
    public Node<E> RR_rotate(Node<E> node) {
        /*RR 회전*/
        Node<E> R = node.right; //오른쪽 자식 노드
        Node<E> RL = R.left;    //오른쪽 자식 노드의 왼쪽 자식 노드

        R.left = node;      //부모 노드를 오른쪽 자식 노드의 왼쪽 자식 노드로 연결
        node.right = RL;    //오른쪽 자식 노드의 왼쪽 자식 노드를 부모 노드의 오른쪽 자식 노드로 연결

        //높이에 영향을 받은 노드 높이 재계산
        changeNodeHeight(node);
        changeNodeHeight(R);

        return R; //새로운 부모 노드가 될 오른쪽 자식 노드를 반환
    }

    @Override
    public Node<E> rotate(Node<E> node) {
        int balance = getBalance(node);

        //균형도가 절댓값 2 이상일 때만 회전한다.
        if (Math.abs(balance) > 1) {
            /*회전으로 인해 변경된 새로운 node를 반환*/

            if (balance > 1) {
                //부모의 균형도가 양수 && 왼쪽 자식 노드의 균형도가 양수 == LL 문제
                if (getBalance(node.left) == 1) {
                    node = LL_rotate(node);
                }
                //부모의 균형도가 양수 && 왼쪽 자식 노드의 균형도가 음수 == LR 문제
                else if (getBalance(node.left) == -1) {
                    node.left = RR_rotate(node.left);
                    node = LL_rotate(node);
                }
            }
            else if (balance < -1) {
                //부모의 균형도가 음수 && 오른쪽 자식 노드의 균형도가 음수 == RR 문제
                if (getBalance(node.right) == -1) {
                    node = RR_rotate(node);
                }
                //부모의 균형도가 음수 && 오른쪽 자식 노드의 균형도가 양수 == RL 문제
                else if (getBalance(node.right) == 1) {
                    node.right = LL_rotate(node.right);
                    node = RR_rotate(node);
                }
            }
        }

        //균형도가 {-1, 0, 1}이면 아무 일도 하지 않는다.
        return node;
    }

    @Override
    public void add(E key) {
        /*키를 추가*/
        root = insertNode(root, key);
    }

    private Node<E> insertNode(Node<E> node, E key) {
        if (node == null) {
            return new Node<>(key);
        }
        int compare = node.key.compareTo(key);

        if (compare > 0) {
            node.left = insertNode(node.left, key);
        } else if (compare < 0) {
            node.right = insertNode(node.right, key);
        }

        //AVL 트리 추가
        changeNodeHeight(node);
        return rotate(node);
    }

    @Override
    public void remove(E key) {
        /*키를 삭제*/
        this.root = removeNode(root, key);
    }

    private Node<E> removeNode(Node<E> node, E key) {
        if (node == null) {
            throw new RuntimeException("Not exists node");
        }

        /*삭제할 키를 찾는 과정*/
        int compare = node.key.compareTo(key);

        if (compare > 0) {
            node.left = removeNode(node.left, key);
        } else if (compare < 0) {
            node.right = removeNode(node.right, key);
        }
        /*삭제할 키를 찾는 과정*/

        else {
            /*삭제할 키를 찾으면 swap 가능한 선임자 또는 후임자 값을 찾는 과정*/
            if (node.left != null) {
                Node<E> predecessor = getLargestNode(node.left); //왼쪽 서브 트리 중 가장 큰 값
                swap(node, predecessor); //swap
                node.left = removeNode(node.left, key);
            }
            else if (node.right != null) {
                Node<E> successor = getSmallestNode(node.right); //오른쪽 서브 트리 중 가장 큰 값
                swap(node, successor); //swap
                node.right = removeNode(node.right, key);
            }
            /*삭제할 키를 찾으면 swap 가능한 선임자 또는 후임자 값을 찾는 과정*/

            else {
                return null; //리프 노드의 경우 연결을 끊는다.
            }
        }

        //AVL 트리 추가
        changeNodeHeight(node);
        return rotate(node);
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
    public E search(E key) {
        /*키 검색*/
        return searchNode(root, key).key;
    }

    private Node<E> searchNode(Node<E> node, E key) {
        if (node == null) throw new RuntimeException("Not exists node");

        int compare = node.key.compareTo(key);

        if (compare > 0) {
            return searchNode(node.left, key);
        }
        else if (compare < 0) {
            return searchNode(node.right, key);
        }

        return node;
    }

    //오름차순 순회(중위 순회)
    public void traversal() {
        List<E> result = new ArrayList<>();
        inOrder(this.root, result);

        System.out.println(
            result.stream()
                  .map(E::toString)
                  .collect(Collectors.joining(" => "))
        );
    }

    private void inOrder(Node<E> node, List<E> result) {
        if (node == null) {
            return;
        }
        inOrder(node.left, result);
        result.add(node.key);
        inOrder(node.right, result);
    }
}
