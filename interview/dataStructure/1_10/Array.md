# Array와 ArrayList의 차이에 대해 설명해 주세요.

- **Array**는 크기가 고정적이고, **ArrayList**는 크기가 가변적이다.
- [Array](https://github.com/genesis12345678/TIL/blob/main/dataStructure/linear/Array/Array.md#array---%EB%B0%B0%EC%97%B4)는 초기화시 메모리에 할당되어 ArrayList 보다 빠르고, [ArrayList](https://github.com/genesis12345678/TIL/blob/main/dataStructure/linear/Array/Array.md#arraylist)는 데이터 추가 및 삭제 시 메모리를 재할당 하기 때문에 속도가 Array 보다 느리다.
- 데이터의 조회와 삽입 연산을 비교해서 조회(탐색)가 더 많다면 `Array`를, 데이터의 수가 가변적이고 삽입/삭제 연산이 자주 이루어지면 `ArrayList`를 적절히 판단해야 한다. 