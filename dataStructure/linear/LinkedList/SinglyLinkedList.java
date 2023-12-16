package dataStructure.linear.LinkedList;

import java.util.Arrays;
import java.util.Objects;

public class SinglyLinkedList<E> implements SinglyLinked<E> {
    private Node<E> head; // 노드의 첫 부분
    private Node<E> tail; // 노드의 마지막 부분
    private int size; // 연결 된 노드의 개수

    public SinglyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    /**
     * 여러 노드들을 체인처럼 연결<br>
     * inner static class
     * @param <E> Generic<br>
     */
    private static class Node<E> {
        private E data; // 노드에 담을 데이터
        private Node<E> next; // 다음 노드를 가리킨다

        public Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    /**
     * add와 remove를 구현하기 위한 내부 메소드<br>
     * 시간복잡도 : O(N)
     * @param index 찾을 위치
     * @return 노드
     */
    private Node<E> search(int index) {
        Node<E> n = head;
        for (int i = 0; i < index; i++) {
            n = n.next;
        }
        return n;
    }

    /**
     * 첫 번째 위치에 요소 추가<br>
     * 1. 가장 앞의 요소(head)값을 first 변수에 백업<br>
     * 2. 새 노드 생성 (데이터를 넣고, 기존 head가 새로운 노드에 next노드가 된다.)<br>
     * 3. head 업데이트 후 size +1 <br>
     * 4. 만약 빈 리스트에서 처음 add된 것이면 head와 tail이 가리키는 요소는 같다.
     * @param value 노드에 담길 데이터
     */
    @Override
    public void addFirst(E value) {
        Node<E> first = head;
        Node<E> newNode = new Node<>(value, first);

        head = newNode;
        size ++;

        if (first == null) {
            tail = newNode;
        }
    }

    /**
     * 마지막 위치에 요소 추가<br>
     * 1. 가장 뒤의 요소(tail)값을 last 변수에 백업<br>
     * 2. 새 노드 생성(데이터를 넣고, 마지막 요소는 next노드가 없기 때문에 null)<br>
     * 3. tail 업데이트 후 size +1<br>
     * 4. 만약 빈 리스트에서 처음 add된 것이면 head와 tail이 가리키는 요소는 같다.<br>
     * 5. 최초 추가가 아니라면 기존 tail의 next노드를 마지막 위치에 새로 추가한 노드로 업데이트
     * @param value 노드에 담길 데이터
     */
    @Override
    public void addLast(E value) {
        Node<E> last = tail;
        Node<E> newNode = new Node<>(value, null);

        tail = newNode;
        size++;

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
     * 4. 이전 노드와 다음 노드를 백업한다.<br>
     * 5. 새로운 노드와 다음 노드를 연결하고 size +1<br>
     * 6. 이전 노드의 nextNode는 새로운 노드로 업데이트
     * @param index 중간에 추가할 위치
     * @param value 노드에 담길 데이터
     */
    @Override
    public void add(int index, E value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            addFirst(value);
            return;
        } else if (index == size - 1) {
            addLast(value);
            return;
        }

        Node<E> beforeNode = search(index - 1); // 추가하려는 위치의 이전 노드
        Node<E> nextNode = beforeNode.next; // 추가하려는 위치의 다음 노드

        Node<E> newNode = new Node<>(value, nextNode); // 새로 추가할 노드
        // 데이터를 넣고, nextNode는 파라미터 index에 위치한 노드와 같다고 볼 수 있기 때문에
        // 기존 노드가 새로운 노드의 nextNode가 된다.

        size++;

        beforeNode.next = newNode;
    }

    /**
     * 맨 앞 요소 제거<br>
     * 1. head == null 이면 삭제할 요소가 없다는 것이므로 에러<br>
     * 2. 새로운 head가 될 기존 head의 다음 노드를 first 변수 임시 저장<br>
     * 3. 제거될 값을 반환해야 하므로 기존 head의 데이터 백업<br>
     * 4. 기존 head정보 모두 삭제(나중에 GC가 수거해감)<br>
     * 5. 임시 저장해 둔 first를 새로운 head로 업데이트하고 size -1<br>
     * 6. 마지막 하나 남은 요소를 삭제한 것이라면 head와 tail모두 null
     * @return 삭제된 노드의 데이터
     */
    @Override
    public E removeFirst() {
        if (head == null) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> first = head.next;

        E returnValue = head.data;

        head.data = null;
        head.next = null;

        head = first;
        size--;

        if (head == null) { // 리스트의 마지막 하나 남은 값을 삭제해서 빈 리스트가 될 경우
            tail = null;
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
     * 2. 인덱스가 0이라면 removeFirst 메소드를 실행하고 리턴한다.<br>
     * 3. 앞뒤 노드 간의 새로운 연결을 맺어야 하므로 search메소드에서 반환된 노드를 통해 전, 현, 후 노드를 변수에 저장시켜 둔다.<br>
     * 4. 현 노드(삭제될 노드)의 데이터를 백업시켜 둔다.<br>
     * 5. 현 노드(삭제될 노드)의 내부 요소를 모두 삭제시킨다.<br>
     * 6. size -1하고 전 노드가 후 노드를 가리키도록 업데이트한다.
     * @param index 삭제시킬 노드의 인덱스(위치)
     * @return 삭제된 노드의 데이터
     */
    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == 0) {
            return removeFirst();
        }

        Node<E> beforeNode = search(index - 1); // 전 노드
        Node<E> toDelNode = beforeNode.next; //  현 노드(삭제될 노드)
        Node<E> nextNode = toDelNode.next; // 후 노드

        E returnValue = toDelNode.data;

        toDelNode.data = null;
        toDelNode.next = null;

        size--;
        beforeNode.next = nextNode;

        return returnValue;
    }

    /**
     * 요소값이 일치하는 위치의 요소를 제거한다.(중복일 경우 맨 앞의 요소 제거)<br>
     * 1. head == null이면 삭제할 요소가 없다는 것이므로 에러<br>
     * 2. 전 노드, 삭제될 노드, 후 노드를 저장할 변수를 선언해둔다.<br>
     * 3. head부터 시작해서 각 노드의 데이터를 순회한다.<br>
     * 4. 찾은 요소가 없다면 return false<br>
     * 5. 삭제될 노드가 head라면 첫번째 노드를 삭제하는 것이니 removeFirst메소드 실행<br>
     * 6. 후 노드에 삭제될 노드의 next를 대입<br>
     * 7. 삭제될 노드의 요소를 모두 제거 후 size -1<br>
     * 8. 이전 노드의 next노드를 후 노드로 업데이트
     * @param value 삭제시킬 데이터 값
     * @return 성공시 true
     */
    @Override
    public boolean remove(Object value) {
        if (head == null) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> beforeNode = null; // 전 노드
        Node<E> toDelNode = null; // 삭제될 노드
        Node<E> nextNode = null; // 후 노드

        Node<E> h = head;
        while (h != null) {
            if (Objects.equals(h.data, value)) {
                // 노드 데이터와 파라미터 데이터가 같으면 삭제될 노드에 요소를 저장시키고 break
                toDelNode = h;
                break;
            }
            beforeNode = h; // 이전 노드도 노드요소 일일이 업데이트
            h = h.next;
        }
        if (toDelNode == null) {
            return false;
        }
        if (toDelNode == head) {
            removeFirst();
            return true;
        }

        nextNode = toDelNode.next;

        toDelNode.data = null;
        toDelNode.next = null;

        size--;

        beforeNode.next = nextNode;

        return true;
    }

    /**
     * 맨 마지막 요소 제거<br>
     * 위에서 구현한 remove메소드를 실행한다.<br>
     * 시간복잡도 : O(N)
     * @return 삭제될 노드의 데이터
     */
    @Override
    public E removeLast() {
        return remove(size - 1);
    }

    /**
     * 인덱스 위치에 있는 노드 데이터를 구한다.<br>
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
     * 1. 인덱스 범위를 체크한다.<br>
     * 2. search메소드로 노드를 찾고 데이터를 변경한다.
     * @param index 데이터를 변경할 노드의 인덱스 값
     * @param value 변경할 데이터
     */
    @Override
    public void set(int index, E value) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<E> node = search(index);
        node.data = value;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * 리스트를 초기화한다.<br>
     * 1. head == null이라면 이미 비어있는 상태이므로 return<br>
     * 2. head부터 시작해서 하나씩 요소를 제거한다.
     */
    @Override
    public void clear() {
        if (head == null) {
            System.out.println("already empty");
            return;
        }
        Node<E> h = head;
        Node<E> next;
        while (h != null) {
            next = h.next;
            h.data = null;
            h.next = null;
            h = next;
        }
        head = tail = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (head == null) {
            return "[]";
        }

        Object[] array = new Object[size];

        int index = 0;
        Node<E> n = head;
        while (n != null) {
            array[index] = n.data;
            index++;
            n = n.next;
        }

        return Arrays.toString(array);
    }
}

