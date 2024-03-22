# 배열과 연결 리스트의 장단점에 대해 설명해 주세요.

- **배열(`Array`)은 인덱스(`index`)로 해당 원소(`element`)에 접근할 수 있어 찾고자 하는 원소의 인덱스 값을 알고 있으면 `O(1)`에 해당 원소로 접근할 수 있다.**
- **연결 리스트(`LinkedList`)는 각각의 원소들은 자기 자신 다음과 이전에 어떤 원소인지를 기억하고 있기 때문에 이 부분만 다른 값으로 바꿔주면 삽입과 삭제를 `O(1)`로 해결할 수 있다.**

[LinkedList](https://github.com/genesis12345678/TIL/blob/main/dataStructure/linear/linkedList/LinkedList.md#linkedlist)는 [Array](https://github.com/genesis12345678/TIL/blob/main/dataStructure/linear/Array/Array.md#array---%EB%B0%B0%EC%97%B4)에서 삽입 또는 삭제할 때 각 원소들을 `shift` 해주어야 하는 비용이 생겨서 시간복잡도가 `O(N)`이 된다는 문제를 해결할 수 있는 자료구조이다.

**정리하면**, `Array`는 검색이 빠르지만 삽입, 삭제가 느리다. `LinkedList`는 삽입, 삭제가 빠르지만 검색이 느리다.