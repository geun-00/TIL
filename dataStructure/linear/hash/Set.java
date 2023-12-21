package dataStructure.linear.hash;

public interface Set<E> {
    boolean add(E e); // 요소가 없는 경우 추가
    boolean remove(Object o); // 요소가 있는 경우 삭제
    boolean contains(Object o); // 요소가 있는지 확인
    boolean equals(Object o); // 지정된 객체와 현재 집합이 같은지 확인
    boolean isEmpty(); // 집합이 비어있는지 확인
    int size(); // 집합의 크기 반환
    void clear(); // 집합 비우기

}
