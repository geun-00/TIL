# B-트리

## 기본 개념

**B-트리**는 기본적으로 이진 탐색 트리로 동작하지만, 이진 트리와 다른 의도와 개념을 가진 트리이다.
한 노드가 여러 개의 데이터(키)를 가질 수 있고 여러 자식 노드를 가질 수 있다. 그리고 **분기 요소**라는 개념을 통해
한 노드가 가질 수 있는 데이터와 자식 수가 달라진다.

![img.png](image/img.png)

---

## B-트리 규칙

### 1. 특정 키를 기준으로 왼쪽 자식은 특정 키보다 작은 키들의 모음이고, 오른쪽 자식은 큰 키들의 모음이다.

![img_1.png](image/img_1.png)

### 2. 모든 리프 노드는 같은 깊이를 가진다.

> 모든 리프 노드가 같은 깊이를 가진다는 것은 균형 있는 트리이며, 대부분의 키가 리프 노드에 존재하기 때문에
> 키 탐색 시간이 어느 정도 균일하다는 것을 의미한다.

![img_2.png](image/img_2.png)

### 3. 분기 요소(t)가 존재한다. (2 <= t)

> B-트리는 분기 요소(`t`)에 의해 노드가 보유할 수 있는 키의 개수가 정해지므로 분기 요소(`t`)가 커질수록
> 더 많은 양의 키를 보유할 수 있고, 트리의 높이는 줄어든다.

![img_3.png](image/img_3.png)

![img_4.png](image/img_4.png)

![img_5.png](image/img_5.png)

### 4. 키의 개수는 최대 2t - 1개이다.

> 노드가 보유할 수 있는 키의 개수 범위는 `t-1 <= keys <= 2t - 1`이다. 키 삽입시 개수가 
> **2t**가 되면 분할을 통해 규칙을 유지한다.

![img_6.png](image/img_6.png)

### 5. 키의 개수는 최소 t-1개다.

> 노드가 보유할 수 있는 키의 개수 범위는 `t-1 <= keys <= 2t - 1`이다. 키 삭제시 개수가
> **t-1 미만**이 되면 회전 또는 병합을 통해 규칙을 유지한다.

![img_7.png](image/img_7.png)

![img_8.png](image/img_8.png)

### 6. 노드의 키의 개수가 k일 때, 자식 노드의 개수는 k+1이며 t <= 자식 노드 수 <= 2t이다.

> 예를 들어 1개의 키가 존재 시 2개의 자식이 존재하며, 2개의 키가 존재 시 3개의 자식이 존재해야 한다.(리프 노드 제외)
> 이러한 규칙을 일반화하면 자식 노드의 개수는 **t <= children <= 2t**가 된다.

![img_9.png](image/img_9.png)

---

## B-트리 탐색

B-트리의 탐색은 이진 탐색 트리와 유사하다. 각 노드는 정렬된 데이터로 배치되기 때문에 루트 노드의 키부터 자식 노드의
키까지 탐색한다.

![img_10.png](image/img_10.png)

![img_11.png](image/img_11.png)

---

## B-트리 삽입

노드의 키는 항상 정렬된 상태로 있어야 하며, 노드가 최대로 보유할 수 있는 키(2t-1)를 초과할 때까지 저장하다가
초과하면 노드를 분할한다. 내부 노드 분할 시 트리의 높이가 늘어나지 않으나 루트 노드 분할 시 트리의 높이가 늘어난다.

**B-트리의 키 삽입은 항상 리프 노드에 추가**하며, 순서는 다음과 같다.

1. 삽입될 키의 위치를 찾아 노드에 키를 삽입한다.
2. 재귀적으로 노드의 키 개수가 **2t**가 되면 분할한다.
3. 루트 노드 분할 시 새로운 루트 노드로 변경한다.

![img_12.png](image/img_12.png)

![img_13.png](image/img_13.png)

![img_14.png](image/img_14.png)

![img_15.png](image/img_15.png)

![img_16.png](image/img_16.png)

![img_17.png](image/img_17.png)

![img_18.png](image/img_18.png)

![img_19.png](image/img_19.png)

![img_20.png](image/img_20.png)

> 이러한 과정을 거치며 B-트리의 삽입이 이루어지며 모든 리프 노드가 항상
> 같은 깊이를 가지도록 상향식 분할을 한다.

---

## B-트리 삭제

**B-트리의 삭제는 두 가지 규칙을 유지하면서 삭제해야 한다.**

1. 노드의 최소 보유 키 개수(t-1)를 유지해야 한다.
2. 모든 리프 노드에서 트리의 깊이가 같아야 한다.

![img_21.png](image/img_21.png)

![img_22.png](image/img_22.png)

B-트리에서는 키를 삭제하기 위해 다음 행위들을 재귀적으로 반복한다.

1. 삭제할 키가 내부 노드인 경우 리프 노드로 옮긴다.
2. 노드의 최소 보유 키 개수를 유지하기 위해 키를 이동시키거나 노드를 병합한다.
3. 리프 노드의 키를 삭제한다. **(항상 키는 리프 노드에서 삭제한다.)**

### Case 1

**삭제할 키가 노드 `x`에 있을 때 `x`가 리프 노드인 경우**

![img_23.png](image/img_23.png)

### Case 2

**삭제할 키가 노드 `x`에 있을 때 `x`가 내부 노드인 경우**

위와 같은 경우일 때 또 세 가지 상황으로 구분할 수 있다.

- 삭제할 키가 노드 `x`에 있을 때 `x`가 내부 노드이며, `왼쪽 자식 노드의 키의 개수 >= t`인 경우
- 삭제할 키가 노드 `x`에 있을 때 `x`가 내부 노드이며, `오른쪽 자식 노드의 키의 개수 >= t`인 경우
- 삭제할 키가 노드 `x`에 있을 때 `x`가 내부 노드이며, `오른쪽/왼쪽 자식 노드의 키의 개수 < t`인 경우

**Case 2-a. 삭제할 키가 노드 `x`에 있을 때 `x`가 내부 노드이며, `왼쪽 자식 노드의 키의 개수 >= t`인 경우**

키를 리프 노드에서 삭제하기 위해 이동시킨다.

![img_24.png](image/img_24.png)

![img_25.png](image/img_25.png)

![img_26.png](image/img_26.png)

**Case 2-b. 삭제할 키가 노드 `x`에 있을 때 `x`가 내부 노드이며, `오른쪽 자식 노드의 키의 개수 >= t`인 경우**

1번 상황과 대치되며 키를 리프 노드에서 삭제하기 위해 이동시킨다.

![img_27.png](image/img_27.png)

![img_28.png](image/img_28.png)

![img_29.png](image/img_29.png)

**Case 2-c. 삭제할 키가 노드 `x`에 있을 때 `x`가 내부 노드이며, `오른쪽/왼쪽 자식 노드의 키의 개수 < t`인 경우**

좌우측 모두 키의 개수가 부족하다면 삭제할 키를 아래로 내리며 병합해야 한다. 이 행위는 **Case 3-b**를 할 수밖에 없는
원인을 만든다.

![img_30.png](image/img_30.png)

![img_31.png](image/img_31.png)

![img_32.png](image/img_32.png)

### Case 3

**삭제할 키가 노드 `x`에 존재하지 않고 내부 노드인 경우**

위와 같은 경우일 때 또 두 가지 상황으로 구분할 수 있다.

- **(Case 3-a)** 삭제할 키가 노드 `x`에 존재하지 않고 내부 노드이며,
  - `x` 노드의 자식 노드를 부모 노드로 하는 서브 트리에 키가 존재하고,
  - 자식 노드가 `t-1`개의 키를 가지고 있으며,
  - 자식 노드의 형제 노드가 `t`개 이상의 키를 가지고 있는 경우

![img_33.png](image/img_33.png)

![img_34.png](image/img_34.png)

- **(Case 3-b)** 삭제할 키가 노드 `x`에 존재하지 않고 내부 노드이며,
  - `x` 노드의 자식 노드의 인접한 형제 노드가 모두 `t-1`개의 키를 가지고 있는 경우

![img_35.png](image/img_35.png)

![img_36.png](image/img_36.png)

---

## Case 3-a가 존재해야 하는 이유

- **Case 3-a**는 삭제할 키가 있는 노드의 인접한 형제 노드한테 키를 빌려와서 키의 개수를 늘린다.
- 만약 삭제할 키가 리프 노드에 존재하고 **리프 노드의 키의 개수가 최소 보유 키 개수인 경우** 그냥 제거하면
**최소 보유 키 규칙**과, **모든 리프 노드는 같은 깊이를 가져야 한다는 규칙**을 어긴다.

![img_37.png](image/img_37.png)

- 따라서 포인터 `x`가 리프 노드로 내려가기 전에 미리 체크하여, **삭제할 키가 있는 노드의 키 보유 개수를
유지시켜주기 위해 인접한 형제 노드한테 키를 빌려온다.**
- 그러나 키를 빌려오지 못하는 경우 **Case 3-b**를 실행해야 한다.

![img_38.png](image/img_38.png)

## Case 3-b가 존재해야 하는 이유

![img_39.png](image/img_39.png)

- 만약 키 5를 삭제한다면, **노드의 최소 보유 키 개수 규칙**에 어긋나기 때문에 인접 형제 노드한테 키를 빌려와서 삭제해야 한다.
- 그러나 인접 형제 노드의 키의 개수도 `t-1`인 경우 키를 빌려 올 수 없다.

![img_40.png](image/img_40.png)

- 또한 리프 노드와 병합할 수도 없다.
- **모든 리프 노드의 깊이는 동일**해야 하는 규칙을 어긴다.

![img_41.png](image/img_41.png)

- 따라서 삭제할 키를 탐색하면서 삭제할 키와 연관된 서브 트리의 자식 노드 키의 개수가 `t-1`인 경우 미리 노드를 병합한다.
- **미리 노드를 병합**함으로써, 리프 노드의 키를 삭제할 경우 **최소 보유 키 규칙**을 유지할 수 있다.

![img_42.png](image/img_42.png)

- 부모 노드의 키의 개수가 3개이므로 3번 키를 리프 노드로 내려 병합 후, 이후에 삭제할 수 있다.

![img_43.png](image/img_43.png)

## Case 2-c가 존재해야 하는 이유

**Case 2-a** 또는 **Case 2-b**를 실행하는 방식으로 삭제할 키를 리프 노드로 내리는 경우
리프 노드에는 최소 보유 키 개수만 가지고 있기 때문에 이후에 **Case 3-b**를 실행해야 한다.

![img_44.png](image/img_44.png)

즉 **Case 2-a** 또는 **Case 2-b** 이후에 **Case 3-b**를 실행할 바에 삭제할 키를 아래로 내리며
병합하는 방식인 **Case 2-c**로 최적화한 것이다.

![img_45.png](image/img_45.png)

## Case 2-c와 Case 3-b의 차이

- **Case 2-c**는 삭제할 키를 내리며 병합하고,
- **Case 3-b**는 삭제할 키를 찾기 위한 연관 키를 내리며 병합한다.

![img_46.png](image/img_46.png)

![img_47.png](image/img_47.png)

![img_48.png](image/img_48.png)

![img_49.png](image/img_49.png)

## B-트리는 삭제 시 부모 노드의 키 개수를 t개 유지하면서 삭제한다.

- 삭제할 키가 리프 노드에 존재하고, 해당 노드의 키의 개수가 `t-1`개이고, 인접 형제 노드의 키의 개수도
`t-1`개일 수도 있다는 것을 가정하기 때문에 **Case 3-b**가 존재한다.
- 그러나 **Case 3-b**를 할 경우 부모 노드의 키를 하나 줄이기 때문에 **노드의 최소 보유 키 개수 규칙**을 위배할 수 있다.
- 따라서 삭제할 키를 탐색하는 과정에서 부모 노드의 키 개수를 `t`개 유지하면서 진행된다.

**루트 노드의 키 개수는 `t-1`개 규칙에서 제외된다.**

---

## 시간복잡도

- 트리의 노드 수: `N`

|    | 평균      | 최악   |
|----|---------|------|
| 검색 | O(logN) | O(logN) |
| 삽입 | O(logN) | O(logN) |
| 삭제 | O(logN) | O(logN) |

---

# B-트리 구현

```java
public class Node<E extends Comparable<E>> {

    private final List<E> keys;
    private final List<Node<E>> children;

    public Node(int t) {
        this.keys = new ArrayList<>(2 * t);
        this.children = new ArrayList<>(2 * t);
    }

    public List<E> getKeys() {
        return keys;
    }

    public List<Node<E>> getChildren() {
        return children;
    }

    public Node<E> getLastChild() {
        return children.get(children.size() - 1);
    }

    public E getLastKey() {
        return keys.get(keys.size() - 1);
    }
}
```

- B-트리 구현에 사용될 노드 객체로, 유연하게 다양한 형태의 객체들을 저장할 수 있도록 `Comparable`을 구현한 객체들이 저장될 수 있도록 한다.
- 한 노드는 최소`t-1`개, 최대 `2t-1`개의 키를 가질 수 있다.
  - 그럼에도 배열의 크기를 `2t`로 초기화하는 이유는 최대 키의 보유 개수 범위를 초과했을 때
    마지막 키를 가져와 분할하기 위해서다.
- 한 노드는 항상 **키의 개수 + 1개의 자식 노드**를 가진다.
- 분할, 병합 등의 과정에서 사용할 보조 메서드 `getLastChild()`와 `getLastKey()`를 정의했다.

```java
public interface BTree<E extends Comparable<E>> {
    void add(E key);                        //키를 추가
    void remove(E key);                     //카를 삭제
    E search(E key);                        //키를 검색
    boolean isLeafNode(Node<E> node);       //리프 노드인지 확인
    int findKeyIndex(Node<E> node, E key);  //키와 연관된 index 검색
    Node<E> splitNode(Node<E> x, Node<E> y, int childNodeIndex);    //노드를 분할
}
```

B-트리는 이진 탐색 트리와 비슷하게 동작한다. 하지만 B-트리는 노드당 2개 이상의
자식 노드를 가질 수 있고, 항상 균형을 유지해야 하기 때문에 추가적인 구현이 필요하다.

## 노드를 분할

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
    
    private final int t;

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
             .subList(t, child.getKeys().size()) //주의-t를 포함
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
}
```

- 키의 삽입 과정에서 최대 보유 키 수를 초과하면 `splitNode` 메서드를 호출한다.
- 중간키를 위로 올리며 기존 노드와의 연결을 끊고, 새로운 노드와 연결 해준다.

## 리프 노드인지 확인

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
    @Override
    public boolean isLeafNode(Node<E> node) {
        /*리프 노드인지 확인*/
        return node.getChildren().isEmpty();
    }
}
```

- 자식 노드가 없으면 리프 노드이다.

## 키와 연관된 index 검색

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
    
    @Override
    public int findKeyIndex(Node<E> node, E key) {
        /*키와 연관된 index 검색*/
        int index = 0;
        while (index < node.getKeys().size() && node.getKeys().get(index).compareTo(key) < 0) {
            index++;
        }
        return index;
    }
}
```

- 키를 삽입하거나, 삭제하거나, 검색할 때 적절한 위치의 index를 반환한다.
- 더 큰 값이 나오기 전까지 오른쪽으로 `index`를 이동한다.

## 키를 추가

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
    
    private final int t;
    private Node<E> root;

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
}
```

- **키는 항상 리프 노드에 추가되어야 한다.**
- 리프 노드가 아닐 경우 삽입될 위치에 있는 적절한 노드를 재귀호출한다.
- 만약 자식 노드에 키를 추가했을 때 최대 키 보유 개수 범위를 초과했다면, 자식 노드를 분할해야 한다.
- 노드를 분할하고 노드 `x`가 루트 노드이고 최대 키 보유 개수 범위를 초과했다면, 새로운 노드를 부모 노드로 하고
분할을 해주어야 한다.
- **분할 과정에서 루트 노드가 변경될 수 있으므로 키를 추가하는 과정이 끝나면 루트 노드를 갱신해주어야 한다.**

## 키를 삭제

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
    
    private final int t;
    private Node<E> root;

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
            } else {
                //KeyIndex에 해당하는 자식 노드부터 다시 탐색
                removeKey(x.getChildren().get(keyIndex), key);
            }
        }
    }
}
```

- 삽입 과정에서 B-트리는 항상 균형을 맞추고, 각 노드(루트 노드 제외)는 항상 최소 키 개수 이상의 키를 가지고 있는다.
- 그리고 삭제 과정에서 삭제하려는 키를 찾으면서 필요시 병합을 하기 때문에 리프 노드에서 키가 삭제됐을 때
리프 노드는 항상 최소 키 보유 개수를 만족한다.

## Case 1 해결

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
    private void case1(Node<E> node, int keyIndex) {
        node.getKeys().remove(keyIndex);
    }
}
```

- 삭제할 키가 노드 `x`에 있고, `x`가 리프 노드면 노드에서 키를 바로 삭제한다.

## Case 2 해결

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
    
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
}
```

- 삭제할 키가 노드 `x`에 있고 `x`가 내부 노드인 경우, 오른쪽/왼쪽 자식 노드의 키의 개수를 확인한다.
- 오른쪽 또는 왼쪽 자식 노드에서 키를 빌려올 수 있다면 선행키 또는 후행키를 찾는다. 이는 이진 탐색 트리에서의 과정과 같다.
- x 노드에 선행키 또는 후행키를 덮어쓰고, `removeKey()`를 호출한다.
- `removeKey()`에서 `case2()`를 호출하고, 다시 `case2()`에서 `removeKey()`를 호출할 수 있는 것이다.
- 즉 오른쪽 또는 왼쪽 자식 노드에서 다시 삭제하는 과정을 시작하는 것이다. 
- 만약 오른쪽 또는 왼쪽 자식 노드 모두 키가 부족하다면 병합 후 새로 반환된 노드부터 다시 `removeKey()`로 삭제 과정을 시작한다.

## Case 3 해결

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {

    private void case3(Node<E> x, E key, int keyIndex) {
        boolean isExistsLeftChild = keyIndex > 0;
        boolean isExistsRightChild = keyIndex < x.getChildren()
                                                 .size() - 1;

        //왼쪽 형제가 있고, 키의 개수 >= t
        if (isExistsLeftChild && x.getChildren().get(keyIndex - 1).getKeys().size() >= t) {

            Node<E> leftChild = x.getChildren().get(keyIndex - 1); //삭제할 키의 왼쪽 형제 노드
            Node<E> removeChild = x.getChildren().get(keyIndex);   //삭제할 키가 존재하는 서브 트리

            E parentKey = x.getKeys().get(keyIndex - 1); //부모 노드의 키
            E lastKey = leftChild.getLastKey();          //형제 노드의 마지막 키

            x.getKeys().set(keyIndex - 1, lastKey);          //형제 노드의 마지막 키를 부모 노드에 덮어쓴다
            leftChild.getKeys().remove(lastKey);             //형제 노드의 마지막 키 제거
            removeChild.getKeys().add(0, parentKey);         //부모 노드의 키를 자식 노드 처음에 추가

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
}
```

- 삭제할 키가 노드 `x`에 존재하지 않고 내부 노드인 경우, 오른쪽/왼쪽 자식 노드의 키의 개수를 확인한다.
- 오른쪽 또는 왼쪽 형제 노드에서 키를 빌려올 수 있다면 키를 빌려와 최소 보유 키 개수를 만족한 다음
다시 `removeKey()`를 호출해 삭제를 계속 진행한다.
- 만약 두 형제 모두 키가 부족하다면 병합 후 새로 반환된 노드부터 다시 `removeKey()`로 삭제 과정을 시작한다.

## 병합

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
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
}
```

- 오른쪽 노드에 있는 키와 자식 노드들을 모두 왼쪽 노드로 옮긴 후, 특별히 값을 제거할 필요는 없다.
- 부모 노드에서 오른쪽 자식의 참조를 제거하는 것만으로 GC의 대상이 된다.
- 만약 부모 노드에서 키를 내리고, 키가 남아있지 않을 수 있는데 이는 루트 노드에만 해당한다.
- 왜냐하면 루트 노드는 최소 키 보유 개수 규칙에 해당하지 않기 때문에 더 적은 키를 가지고 있을 수 있다.

## 키를 검색

```java
public class BTreeImpl<E extends Comparable<E>> implements BTree<E> {
    private Node<E> root;

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
}
```

- 이진 탐색 트리와 비슷하지만 B-트리는 한 노드에 여러 키와 여러 자식 노드를 가질 수 있다.

## 전체 구현 및 사용 코드

```java
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
```
```java
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
```
```text
1 => 2 => 3 => 4 => 5 => 6 => 7 => 8 => 9 => 10 => 11 => 12 => 13 => 14 => 15
2 => 8 => 9 => 10 => 11 => 12 => 13 => 14 => 15
```

---

### 참고

- [자료구조 시각화 사이트](https://www.cs.usfca.edu/~galles/visualization/BTree.html)
- [유튜브](https://www.youtube.com/watch?v=bqkcoSm_rCs&list=PLcXyemr8ZeoR82N8uZuG9xVrFIfdnLd72&index=12)
- [사이트](https://www.codelatte.io/courses/java_data_structure/3MSPGL8T18PU9AW5)
