package dataStructure.linear.hash.hashSet;

import dataStructure.linear.hash.Set;

public class HashSet<E> implements Set<E> {
    private final static int DEFAULT_CAPACITY = 1 << 4; // 16, 최송 용량
    private final static float DEFAULT_LOAD_FACTOR = 0.75f; // 최대 로드 팩터
    Node<E>[] table; // 요소의 정보를 담고 있는 Node를 저장할 배열
    private int size; // 요소의 개수
    @SuppressWarnings("unchecked")
    public HashSet() {
        table = new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static final int hash(Object key) {
        int hash;
        // key가 null인 경우 0을 반환
        // 아닌 경우 key의 해시코드를 반환
        // 해시코드는 int형이므로 32비트이다.
        // 32비트 중 16비트를 반환한다.
        // 16비트를 반환하는 이유는 해시 충돌을 최소화하기 위해서이다.
        return (key == null) ? 0 : Math.abs((hash = key.hashCode())) ^ (hash >>> 16);
    }

    public boolean add(E e) {
        return add(hash(e), e) == null;
    }

    private E add(int hash, E key) {
        int index = hash % table.length;
        // index에 해당하는 Node가 없는 경우
        if(table[index] == null) {
            table[index] = new Node<E>(hash, key, null);
        }
        /**
         * index에 해당하는 Node가 있는 경우 == 해시충돌
         * 1. 객체가 같은 경우
         * 2. 얻어진 index가 같은 경우
         */
        else {
            Node<E> temp = table[index]; // 현재위치 노드
            Node<E> before = null; // 이전위치 노드

            while(temp != null) {
                /**
                 * 객체가 같은 경우는 중복을 허용하면 안 되므로
                 * key를 반납(반환)
                 */
                if((temp.hash==hash) && (temp.key==key || temp.key.equals(key))) {
                    return key;
                }
                before = temp;
                temp = temp.next;
            }
            before.next = new Node<E>(hash, key, null);
        }
        size++;
        if(size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = table.length * 2;
        final Node<E>[] newTable = new Node[newCapacity];
        for (int i = 0; i < table.length; i++) {
            Node<E> value = table[i];
            if (value == null) {
                continue;
            }
            table[i] = null;
            Node<E> nextNode;

            while(value != null) {
                /**
                 * 새로 담을 index에 노드가 존재할 경우
                 * 새로 담을 newTable에 index값이 겹칠 경우 == 해시충돌
                 */
                int index = value.hash % newCapacity; // 새로운 index
                if (newTable[index] != null) {
                    Node<E> tail = newTable[index];

                    while (tail.next != null) {
                        tail = tail.next;
                    }
                    nextNode = value.next;
                    value.next = null;
                    tail.next = value;
                }
                // 충돌되지 않는다면 = 노드 추가
                else {
                    nextNode = value.next;
                    value.next = null;
                    newTable[index] = value;
                }
                value = nextNode;
            }
        }
        table = null;
        table = newTable;
    }

    @Override
    public boolean remove(Object o) {
        return remove(hash(o), o) != null;
    }

    private Object remove(int hash, Object key) {
        int index = hash % table.length;
        Node<E> node = table[index];
        Node<E> removedNode = null;
        Node<E> before = null;

        if(node == null) {
            return null;
        }
        while(node != null) {
            if((node.hash == hash) && (node.key == key || node.key.equals(key))) {
                removedNode = node;
                if(before == null) {
                    table[index] = node.next;
                    node = null;
                }
                else {
                    before.next = node.next;
                    node = null;
                }
                size--;
                break;
            }
            before = node;
            node = node.next;
        }
        return removedNode;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        int index = hash(0) % table.length;
        Node<E> temp = table[index];

        while(temp != null) {
            if (o == temp.key || (o != null && (o.equals(temp.key)))) {

                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    @Override
    public void clear() {
        if(table!=null&& size > 0) {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            size = 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o==this) {
            return true;
        }

        if(!(o instanceof HashSet)) {
            return false;
        }

        HashSet<E> oSet;
        try{
            oSet = (HashSet<E>) o;
            if (oSet.size() != size) {
                return false;
            }
            for (int i = 0; i < oSet.table.length; i++) {
                Node<E> oTable = oSet.table[i];

                while(oTable != null) {
                    if(!contains(oTable.key)) {
                        return false;
                    }
                    oTable = oTable.next;
                }
            }
        }catch (ClassCastException e) {
            return false;
        }
        return true;
    }
}
