# HashMap은 어떤 식으로 동작하나요?

- **HashMap**은 [해시 테이블](https://github.com/genesis12345678/TIL/blob/main/dataStructure/linear/hash/hash.md#%ED%95%B4%EC%8B%9Chash) 기반의
    `Map` 인터페이스 구현체이다.
- **키와 값**을 저장하고 키를 사용하여 값을 검색할 수 있다.
- 해싱 기법을 사용하여 키를 해시 코드로 변환하여 해시 테이블에 빠르게 검색할 수 있도록 구현되어 있다.
- 키의 해시 충돌이 일어날 경우 링크드 리스트 드의 방식으로 관리한다.