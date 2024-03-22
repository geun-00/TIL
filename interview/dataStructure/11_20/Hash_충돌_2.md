# Hash 충돌 시에 내부 데이터 탐색은 어떻게 하나요?

- **해시 충돌 시 내부 데이터 탐색은 `LinkedList`, `Tree` 등의 방식으로 구현될 수 있다.**
- 해시 코드가 같은 두 개의 키-값 쌍이 추가될 경우 키 값에 대한 값을 저장하는 연결 리스트를 사용하여 충돌을 해결할 수 있다.
- [이런 방식](https://github.com/genesis12345678/TIL/blob/main/dataStructure/linear/hash/hash.md#%ED%95%B4%EC%8B%9C%EC%B6%A9%EB%8F%8C-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%EB%B0%A9%EC%95%88)은 탐색 속도가 느리지만 키-값 쌍을 저장하는 비용이 적다는 장점이 있다.