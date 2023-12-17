package dataStructure.linear.linkedList.doubleLinkedList;

import java.util.NoSuchElementException;
import java.util.Objects;

public class DoublyLinkedList<E> implements DoublyLInked<E>{
    private Node<E> head; // 노드의 첫 부분
    private Node<E> tail; // 노드의 끝 부분
    private int size; // 요소 개수

    /**
     * 여러 노드들을 체인처럼 연결<br>
     * inner static class
     * @param <E> Generic<br>
     */
    private static class Node<E> {
        private E data;
        private Node<E> next;
        private Node<E> before;
        Node(Node<E> before, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.before = before;
        }
    }

    /**
     * add와 remove를 구현하기 위한 내부 메소드<br>
     * 인덱스가 시작에 가깝다면 head부터 탐색한다.<br>
     * 인덱스가 끝에 가깝다면 tail부터 탐색한다.<br>
     * 이것은 SinglyLinkedList와는 달리 이전 노드도 참조하고 있기 때문에 가능하다.<br>
     * 노드의 next는 그 다음 노드를 가리키기 때문에 index전까지 탐색한다.
     * @param index 찾을 위치
     * @return 노드
     */
    private Node<E> search(int index) {
        Node<E> n; // 반환할 노드

        if ((size / 2) > index) {
            n = head;
            for (int i = 0; i < index; i++) {
                n = n.next;
            }
        }else{
            n =tail;
            for (int i = size - 1; i > index; i--) {
                n=n.before;
            }
        }
        return n;
    }

    /**
     * 첫 번째 위치에 요소 추가<br>
     * 1. 가장 앞의 요소(head)값을 first 변수에 백업<br>
     * 2. 새 노드 생성(head니까 이전 노드가 null, 데이터 넣고, 기존 head가 새로운 노드에 next노드가 된다.)<br>
     * 3. size +1 , head에 새로운 노드 업데이트<br>
     * 4. 만약 빈 리스트에서 처음 add된 것이면 head와 tail이 가리키는 요소는 같다.<br>
     * 4-1. 그것이 아니라면 추가되기 이전 첫번째 노드에 before를 새 노드로 업데이트
     * @param value 노드에 담길 데이터
     */
    @Override
    public void addFirst(E value) {
        Node<E> first = head;
        Node<E> newNode = new Node<>(null, value, first);
        size++;
        head = newNode;

        if (first == null) {
            tail = newNode;
        } else {
            first.before = newNode;
        }
    }

    /**
     * 마지막 위치에 요소 추가<br>
     * 1. 가장 뒤에 요소(tail)값을 last 변수에 백업<br>
     * 2. 새 노드 생성(기존 tail이 이전 노드가 되고, 데이터 넣고, 마지막 노드니까 next는 null)<br>
     * 3. size +1, tail에 새로운 노드 업데이트<br>
     * 4. 만약 빈 리스트에서 처음 add된 것이면 head와 tail이 가리키는 요소는 같다.<br>
     * 5. 최초 추가가 아니라면 기존 tail의 next노드를 마지막 위치에 새로 추가한 노드로 업데이트
     * @param value 노드에 담길 데이터
     */
    @Override
    public void addLast(E value) {
        Node<E> last = tail;
        Node<E> newNode = new Node<>(last, value, null);
        size++;
        tail = newNode;

        if (last == null) {
            head = newNode;
        } else {
            last.next = newNode;
        }
    }

    /**
     * addLast()와 동일하다.
     * @param value 노드에 담길 데이터
     * @return 성공 시 true
     */
    @Override
    public boolean add(E value) {
        addLast(value);
        return true;
    }

    /**
     * 지정된 위치에 요소 추가(중간 삽입)<br>
     * 1. 인덱스 범위를 체크한다.<br>
     * 2. 목표 인덱스가 처음이나 끝이라면 위에서 구현한 addFirst와 addLast를 재활용한다.<br>
     * 3. 추가하려는 위치를 구하기 위해 위에서 구현한 search 메소드를 사용한다.<br>
     * 4. 추가하려는 위치에 이전 노드와 다음 노드를 백업한다.<br>
     * 5. size +1, 이전 노드에 nextNode를 새로운 노드로 업데이트<br>
     * 6. 다음 노드의 beforeNode를 새로운 노드로 업데이트
     * @param index 중간에 추가할 위치
     * @param value 노드에 담길 데이터
     */
    @Override
    public void add(int index, E value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            addFirst(value);
            return;
        } else if (index == size) {
            addLast(value);
            return;
        }

        Node<E> nextNode = search(index); // 새로 추가할 노드의 다음 노드가 될 노드
        Node<E> beforeNode = nextNode.before; // 새로 추가할 노드의 이전 노드가 될 노드
        Node<E> newNode = new Node<>(beforeNode, value, nextNode); // 새로 추가할 노드
        // 바로 이전 노드와 다음 노드가 연결된다.

        size++;
        beforeNode.next = newNode;
        nextNode.before = newNode;
    }

    /**
     * 맨 앞 요소 제거<br>
     * 1. head ==null이면 삭제할 요소가 없다는 것이므로 에러<br>
     * 2. 새로운 head가 될 기존 head의 다음 노드를 first 변수 임시 저장<br>
     * 3. 제거될 값을 반환해야 하므로 기존 head의 데이터 백업<br>
     * 4. 기존 head 정보 모두 삭제<br>
     * 5. 임시 저장해 둔 first를 새로운 head로 업데이트하고 size -1<br>
     * 6. 마지막 하나 남은 요소를 삭제한 것이라면 head와 tail모두 null<br>
     * 6-1. 아니라면 삭제되기 이전 두번째였던 first가 첫 번째 노드가 되니 before를 null 처리
     * @return 삭제된 노드의 데이터
     */
    @Override
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        E returnValue = head.data;
        Node<E> first = head.next;

        head.next = null;
        head.data = null;
        head = first;
        size--;
        if (first == null) {
            tail = null;
        } else {
            first.before = null;
        }
        return returnValue;
    }

    /**
     * LinkedList의 기본 remove 동작은 add와 달리 첫번째 요소를 처리한다.
     * @return 삭제된 노드의 데이터
     */
    @Override
    public E remove() {
        return removeFirst();
    }

    /**
     * 인덱스 위치에 요소를 제거<br>
     * 1. 인덱스 범위를 체크한다.<br>
     * 2. 인덱스가 처음이나 끝이라면 removeFirst와 removeLast를 실행한다.<br>
     * 3. search메소드로 삭제할 노드를 찾는다.<br>
     * 4. 삭제할 노드의 이전 노드와 다음 노드를 찾아둔다.<br>
     * 5. 삭제할 노드의 데이터를 백업해 둔다.<br>
     * 6. 삭제할 노드의 모든 요소를 모두 삭제하고 size -1.<br>
     * 7. 중간 노드가 삭제됐으니 이전 노드가 다음 노드를, 다음 노드가 이전 노드를 가리키도록 업데이트한다.
     * @param index 삭제시킬 노드의 위치 인덱스 값
     * @return 삭제될 노드의 데이터
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        }

        Node<E> toDelNode = search(index); // 삭제할 노드
        Node<E> beforeNode = toDelNode.before; // 삭제할 노드의 이전 노드
        Node<E> nextNode = toDelNode.next; // 삭제할 노드의 다음 노드

        E returnValue = toDelNode.data;

        toDelNode.data = null;
        toDelNode.before = null;
        toDelNode.next = null;
        size--;

        nextNode.before = beforeNode;
        beforeNode.next = nextNode;

        return returnValue;
    }

    /**
     * 데이터로 노드 삭제<br>
     * 1. 삭제 노드를 저장할 변수를 선언해둔다.<br>
     * 2. 리스트를 순회하기 위해 변수를 선언해둔다.<br>
     * 3. head부터 시작해서 다음 노드를 순회하면서 해당 데이터를 찾는다.<br>
     * 4. 삭제하려는 노드가 처음이나 끝이라면 removeFirst나 removeLast를 사용한다.<br>
     * 5. 삭제할 노드의 이전과 다음 노드를 구한다.<br>
     * 6. 삭제할 노드의 모든 요소를 제거하고 size -1.<br>
     * 7. 이전 노드와 다음 노드끼리 서로 연결한다.
     * @param value 제거할 노드의 데이터
     * @return 성공 시 true
     */
    @Override
    public boolean remove(Object value) {
        Node<E> toDelNode = null;

        Node<E> h = head;
        while (h != null) {
            if (Objects.equals(h.data, value)) {
                toDelNode = h;
                break;
            }
            h = h.next;
        }

        if (toDelNode == null) {
            return false;
        }

        if (toDelNode == head) {
            removeFirst();
            return false;
        } else if (toDelNode == tail) {
            removeLast();
            return false;
        }
        Node<E> beforeNode = toDelNode.before;
        Node<E> nextNode = toDelNode.next;

        toDelNode.data = null;
        toDelNode.before = null;
        toDelNode.next = null;
        size--;

        beforeNode.next = nextNode;
        nextNode.before = beforeNode;
        return true;
    }

    /**
     * 맨 마지막 요소 제거<br>
     * 1. head == null이면 삭제할 요소가 없다는 것이므로 에러
     * 2. 삭제될 마지막 요소의 데이터 백업<br>
     * 3. 기존 마지막 노드의 이전 노드 임시 저장(새로운 tail이 됨)<br>
     * 4. 기존 tail 내부 요소 모두 삭제<br>
     * 5. tail을 last(기존 tail 이전 노드)로 업데이트하고 size -1<br>
     * 6. 만약 유일한 값을 제거해서 빈 리스트가 된 경우 head도 null 처리<br>
     * 6-1. 아니라면 삭제되기 이전 마지막 노드였던 last가 마지막 노드가 되니 next를 null 처리
     * @return 삭제될 노드의 데이터
     */
    @Override
    public E removeLast() {
        if (head == null) {
            throw new NoSuchElementException();
        }

        E returnValue = tail.data;
        Node<E> last = tail.before;

        tail.data = null;
        tail.before = null;

        tail = last;
        size--;

        if (last == null) {
            head = null;
        } else {
            last.next = null;
        }
        return returnValue;
    }

    /**
     * 인덱스 위치에 있는 노드의 데이터를 구한다.<br>
     * 1. 인덱스 범위를 체크한다.<br>
     * 2. search메소드에서 찾은 노드의 데이터를 return한다.
     * @param index 얻고 싶은 노드의 인덱스 값
     * @return 구한 노드의 데이터
     */
    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return search(index).data;
    }

    /**
     * 특정 노드의 데이터를 변경한다.<br>
     * 1. 인덱스 범위를 설정한다.<br>
     * 2. search메소드에서 찾은 노드의 데이터를 변경한다.
     * @param index 데이터를 변경할 노드의 인덱스 값
     * @param value 변경할 데이터
     */
    @Override
    public void set(int index, E value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = search(index);
        node.data = null;
        node.data = value;
    }

    /**
     * head부터 순서대로 검색해서 위치를 반환한다.<br>
     * head부터 순회하면서 찾는 데이터가 담긴 노드의 위치를 구한다.<br>
     * 찾고자 하는 데이터가 없을 경우 -1(=false)를 반환한다.
     * @param value 인덱스 값을 알고 싶은 데이터
     * @return 있다면 인덱스 위치, 없다면 -1(=false)
     */
    @Override
    public int indexOf(Object value) {
        Node<E> h = head;
        int i = 0;
        while (h != null) {
            if (Objects.equals(h.data, value)) {
                return i;
            }
            i++;
            h = h.next;
        }
        return -1;
    }

    /**
     * tail부터 역순으로 검색해서 위치를 반환한다.<br>
     * tail부터 역순회하면서 찾는 데이터가 담긴 노드의 위치를 구한다.<br>
     * 찾고자 하는 데이터가 없을 경우 -1(=false)를 반환한다.
     * @param value 인덱스 값을 알고 싶은 데이터
     * @return 있다면 인덱스 위치, 없다면 -1(=false)
     */
    @Override
    public int lastIndexOf(Object value) {
        Node<E> t = tail;
        int i = size - 1;
        while (t != null) {
            if (Objects.equals(t.data, value)) {
                return i;
            }
            i++;
            t = t.before;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 리스트 초기화<br>
     * 반복문으로 리스트 전체를 순회하여 각 노드에 null을 대입한다.
     */
    @Override
    public void clear() {
        for (Node<E> h = head; h.next != null;) {
            Node<E> nextNode = h.next;

            h.data = null;
            h.before = null;
            h.next = null;

            h = nextNode;
        }
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * 찾고자 하는 데이터가 존재 하는지 안하는지<br>
     * 위에서 구현한 indexOf 메소드가 -1을 반환하면 없다는 것이다.
     * @param value 존재여부 확인 데이터
     * @return 있다면 true
     */
    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }
        Node<E> h = head;
        StringBuilder sb = new StringBuilder();

        sb.append("[\n");

        for (int i = 0; i < size; i++) {
            sb.append("  Node@").append(String.format("%-10s", h.hashCode())).append(" -> ");

            if (h.before != null) {
                sb.append("[").append(h.before.hashCode()).append(" | ");
            } else {
                sb.append("[").append("null").append(" | ");
            }

            sb.append(h.data).append(" | ");

            if (h.next != null) {
                sb.append(h.next.hashCode()).append("]");
            } else {
                sb.append("null").append("]");
            }
            sb.append(", \n");
            h = h.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
