package dataStructure.non_linear.btree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {

    private final int t;
    private Node<E> root;

    public BTreeImpl(int t) {
        this.t = t;
    }

    @Override
    public void add(E key) {
        /*키를 추가*/
        if (root == null) {
            root = new Node<>(t);
            root.getKeys().add(key);
        } else {
            root = insertKey(root, key);
        }
    }

    private Node<E> insertKey(Node<E> x, E key) {
        int keyIndex = findKeyIndex(x, key);
        duplicateCheck(x, key, keyIndex); //동일한 키가 있는지 확인

        //리프 노드인 경우 키를 추가
        if (isLeafNode(x)) {
            x.getKeys().add(keyIndex, key);
        } else {
            //자식 노드가 아닌 경우 적절한 위치에 있는 자식 노드에 추가 시도
            Node<E> child = insertKey(x.getChildren().get(keyIndex), key);
            if (child.getKeys().size() == 2 * t) {
                //자식 노드에서 최대 키 보유 개수 초과시 분할
                x = splitNode(x, child, keyIndex);
            }
        }

        //루트 노드에서 최대 키 보유 개수 초과시 새로운 노드를 부모 노드로 하고 분할
        if (x == root && x.getKeys().size() == 2 * t) {
            x = splitNode(new Node<>(t), x, 0);
        }

        return x;
    }

    private void duplicateCheck(Node<E> node, E key, int keyIndex) {
        if (keyIndex < node.getKeys().size() && node.getKeys().get(keyIndex).equals(key)) {
            throw new RuntimeException("동일한 키 존재");
        }
    }

    @Override
    public void remove(E key) {
        /*키를 삭제*/
        removeKey(root, key);
    }

    private void removeKey(Node<E> x, E key) {
        int keyIndex = findKeyIndex(x, key);
        if (keyIndex < x.getKeys().size() && x.getKeys().get(keyIndex).equals(key)) {
            if (isLeafNode(x)) {
                case1(x, keyIndex); //삭제할 키가 노드 x에 있고, x가 리프 노드
            } else {
                case2(x, keyIndex); //삭제할 키가 노드 x에 있고, x가 내부 노드
            }
        } else {
            //삭제할 키가 노드 x에 존재하지 않는데 x가 리프 노드인 경우
            if (isLeafNode(x)) {
                throw new RuntimeException("키가 존재하지 않음");
            }
            //삭제할 키가 노드 x에 존재하지 않고, x가 내부 노드이며,
            //keyIndex에 있는 자식의 키의 개수가 최소 보유 키 개수 규칙에 어긋난 경우
            if (x.getChildren().get(keyIndex).getKeys().size() < t) {
                case3(x, key, keyIndex);
            }
            else {
                //KeyIndex에 해당하는 자식 노드부터 다시 탐색
                removeKey(x.getChildren().get(keyIndex), key);
            }
        }
    }

    private void case1(Node<E> node, int keyIndex) {
        node.getKeys().remove(keyIndex);
    }

    private void case2(Node<E> x, int keyIndex) {
        E key = x.getKeys().get(keyIndex);
        Node<E> leftChild = x.getChildren().get(keyIndex);
        Node<E> rightChild = x.getChildren().get(keyIndex + 1);

        //왼쪽 자식의 키의 개수 >= t
        if (leftChild.getKeys().size() >= t) {
            E precedingKey = getLargestKey(leftChild); //왼쪽 서브 트리 중 가장 큰 값(선행키)
            x.getKeys().set(keyIndex, precedingKey);   //x 노드에 선행키를 복사
            removeKey(leftChild, precedingKey);        //선행키로 removeKey 다시 호출
        }
        //오른쪽 자식의 키의 개수 >= t
        else if (rightChild.getKeys().size() >= t) {
            E successorKey = getSmallestKey(rightChild);//오른쪽 서브 트리 중 가장 작은 값(후행키)
            x.getKeys().set(keyIndex, successorKey);    //x 노드에 후행키를 복사
            removeKey(rightChild, successorKey);        //후행키로 removeKey 다시 호출
        }
        //오른쪽/왼쪽 자식의 키의 개수 < t
        else {
            //병합한 후 병합된 노드부터 다시 remove 호출
            Node<E> newNode = merge(x, keyIndex);
            removeKey(newNode, key);
        }
    }

    private E getLargestKey(Node<E> node) {
        if (isLeafNode(node)) {
            return node.getLastKey();
        }
        return getLargestKey(node.getLastChild());
    }

    private E getSmallestKey(Node<E> node) {
        if (isLeafNode(node)) {
            return node.getKeys().get(0);
        }
        return getLargestKey(node.getChildren().get(0));
    }

    private void case3(Node<E> x, E key, int keyIndex) {
        boolean isExistsLeftChild = keyIndex > 0;
        boolean isExistsRightChild = keyIndex < x.getChildren().size() - 1;

        //왼쪽 형제가 있고, 키의 개수 >= t
        if (isExistsLeftChild && x.getChildren().get(keyIndex - 1).getKeys().size() >= t) {

            Node<E> leftChild = x.getChildren().get(keyIndex - 1); //삭제할 키의 왼쪽 형제 노드
            Node<E> removeChild = x.getChildren().get(keyIndex);   //삭제할 키가 존재하는 서브 트리

            E parentKey = x.getKeys().get(keyIndex - 1); //부모 노드의 키
            E lastKey = leftChild.getLastKey();          //형제 노드의 마지막 키

            x.getKeys().set(keyIndex - 1, lastKey);          //형제 노드의 마지막 키를 부모 노드에 덮어쓴다
            leftChild.getKeys().remove(lastKey);             //형제 노드의 마지막 키 제거
            removeChild.getKeys().add(0, parentKey);   //부모 노드의 키를 자식 노드 처음에 추가

            //형제 노드가 리프 노드가 아닌 경우 = 자식 노드가 있다
            if (!isLeafNode(leftChild)) {
                //형제 노드의 자식 노드를 옮겨준다
                Node<E> lastChild = leftChild.getLastChild();
                removeChild.getChildren().add(0, lastChild);
                leftChild.getChildren().remove(lastChild);
            }

            removeKey(removeChild, key);
        }
        //오른쪽 형제가 있고, 키의 개수 >= t
        else if (isExistsRightChild && x.getChildren().get(keyIndex + 1).getKeys().size() >= t) {

            Node<E> removeChild = x.getChildren().get(keyIndex);    //삭제할 키가 존재하는 서브 트리
            Node<E> rightChild = x.getChildren().get(keyIndex + 1); //삭제할 키의 오른쪽 형제 노드

            E parentKey = x.getKeys().get(keyIndex);    //부모 노드의 키
            E firstKey = rightChild.getKeys().get(0);   //형제 노드의 첫번째 키

            x.getKeys().set(keyIndex, firstKey);    //형제 노드의 첫번째 키를 부모 노드에 덮어쓴다
            rightChild.getKeys().remove(firstKey);  //형제 노드의 첫번째 키 제거
            removeChild.getKeys().add(parentKey);   //부모 노드의 키를 자식 노드 마지막에 추가

            //형제 노드가 리프 노드가 아닌 경우 = 자식 노드가 있다
            if (!isLeafNode(rightChild)) {
                //형제 노드의 자식 노드를 옮겨준다
                Node<E> firstChild = rightChild.getChildren().get(0);
                removeChild.getChildren().add(firstChild);
                rightChild.getChildren().remove(firstChild);
            }

            removeKey(removeChild, key);
        }
        //오른쪽, 왼쪽 형제 모두 키의 개수가 부족
        else {
            Node<E> newNode = merge(x, keyIndex);
            removeKey(newNode, key);
        }
    }

    private Node<E> merge(Node<E> x, int keyIndex) {
        Node<E> leftChild = x.getChildren().get(keyIndex);
        Node<E> rightChild = x.getChildren().get(keyIndex + 1);

        //keyIndex에 해당하는 키를 왼쪽 자식으로 내림
        E key = x.getKeys().remove(keyIndex);
        leftChild.getKeys().add(key);

        //오른쪽 자식에 있는 키와 자식 노드들을 왼쪽 자식으로 이동
        leftChild.getKeys().addAll(rightChild.getKeys());
        leftChild.getChildren().addAll(rightChild.getChildren());

        //부모 노드에서 오른쪽 자식을 제거
        x.getChildren().remove(rightChild); //오른쪽 노드 GC

        //노드 x가 루트 노드인 경우
        //노드 x에 키가 없는 경우는 최소 보유 키 규칙에 해당되지 않는 루트 노드만 있음
        if (x.getKeys().isEmpty()) {
            x.getChildren().clear();
            root = leftChild; //새로운 루트 노드로 승격
        }
        return leftChild;
    }

    @Override
    public E search(E key) {
        Node<E> node = searchNode(root, key);
        int keyIndex = node.getKeys().indexOf(key);
        return node.getKeys().get(keyIndex);
    }

    private Node<E> searchNode(Node<E> node, E key) {
        if (!node.getKeys().contains(key)) {
            //해당 노드에 키가 없고, 마지막 리프 노드
            if (isLeafNode(node)) {
                throw new RuntimeException("찾는 값이 존재하지 않음");
            }
            //해당 노드에 키가 없다면 해당 키가 있을만한 자식 노드로 다시 검색
            int nodeIndex = findKeyIndex(node, key);
            return searchNode(node.getChildren().get(nodeIndex), key);
        }
        //해당 노드에 키가 있다
        return node;
    }

    @Override
    public boolean isLeafNode(Node<E> node) {
        /*리프 노드인지 확인*/
        return node.getChildren().isEmpty();
    }

    @Override
    public int findKeyIndex(Node<E> node, E key) {
        /*키와 연관된 index 검색*/
        int index = 0;
        while (index < node.getKeys().size() && node.getKeys().get(index).compareTo(key) < 0) {
            index++;
        }
        return index;
    }

    /**
     * @param parent 분할 대상 노드의 부모 노드
     * @param child 분할 대상 노드
     * @param childNodeIndex 부모 노드의 분할 대상 노드 index
     */
    @Override
    public Node<E> splitNode(Node<E> parent, Node<E> child, int childNodeIndex) {
        /*노드를 분할*/

        Node<E> newNode = new Node<>(t);        //분할 되어 새로 생긴 노드
        E medianKey = child.getKeys().get(t);   //중간키

        //새로운 노드의 중간 키 이후의 키들을 저장
        newNode.getKeys()
               .addAll(child.getKeys().subList(t + 1, child.getKeys().size()));
        //기존 노드의 키 제거
        child.getKeys()
             .subList(t, child.getKeys().size()) //주의-t를 포함해서
             .clear();

        //자식이 리프 노드가 아니면 = 자식 노드가 존재
        //기존 노드의 자식을 새로운 노드의 자식으로 연결한다.
        if (!isLeafNode(child)) {
            //새로운 노드에 자식 노드 연결
            newNode.getChildren()
                   .addAll(child.getChildren().subList(t + 1, child.getChildren().size()));
            //기존 노드의 자식 노드 제거
            child.getChildren()
                 .subList(t + 1, child.getChildren().size())
                 .clear();
        }

        //분할한 노드가 루트 노드
        if (child == root) {
            parent.getKeys().add(medianKey);
            parent.getChildren().add(child);
            parent.getChildren().add(newNode);
        } else {
            parent.getKeys().add(childNodeIndex, medianKey);
            parent.getChildren().add(childNodeIndex + 1, newNode);
        }

        return parent;
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
        for (int i = 0; i < node.getKeys().size(); i++) {
            if (!node.getChildren().isEmpty()) {
                inOrder(node.getChildren().get(i), result);
            }
            result.add(node.getKeys().get(i));
        }
        if (!node.getChildren().isEmpty()) {
            inOrder(node.getLastChild(), result);
        }
    }

}
