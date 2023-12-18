package dataStructure.linear.queue.arrayQueue;

public interface Queue<E> {
    boolean offer(E value); // 요소 추가
    E poll(); // 첫번째 요소 삭제 후 요소 반환
    E peek(); // 첫번째 요소 반환
}
