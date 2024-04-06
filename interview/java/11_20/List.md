# 연결 리스트와 일반 리스트의 차이는 무엇인가요?

- 자바에서 연결 리스트와 일반 리스트는 인덱스를 이용해 탐색 및 삽입이 가능하고, 차이는 데이터 구조에 있다.
- [일반 리스트(ArrayList)](https://github.com/genesis12345678/TIL/blob/main/dataStructure/linear/Array/Array.md#arraylist)는 배열 구조의 크기를 동적으로 바꿀 수 있는 것이 특징이며, 각 데이터는 인덱스에 따라 순차적으로 저장되는 구조이다.
- [연결 리스트(LinkedList)](https://github.com/genesis12345678/TIL/blob/main/dataStructure/linear/linkedList/LinkedList.md#linkedlist)는 노드(`Node`)끼리 서로 앞뒤 노드의 주소 포인터를 가지는 것이 특징이며, 시작과 끝이 존재한다.

**일반 리스트**는 삽입/삭제 작업보다 탐색을 더 많이 해야할 때 유용하고,<br>
**연결 리스트**는 탐색보다 삽입/삭제 작업을 더 많이 해야할 때 유용하다.