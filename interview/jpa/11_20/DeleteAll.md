# deleteAll과 deleteAllInBatch는 무슨 차이가 있나요?

- `deleteAll()`과 `deleteAllInBatch()`는 테스트 코드에서 `tearDown`으로 주로 사용되는 N개의 엔티티를 삭제하는 메서드인데, **성능 차이가 존재한다.**
- `deleteAll()`의 내부 메서드를 보면, `findAll()`을 통해 조회한 엔티티들에 대해 하나씩 `delete()`를 수행한다. 즉 N번의 `delete()`가 발생한다.
- 반면 `deleteAllInBatch()`의 경우 `DELETE FROM {TABLE}`로 동작하기 때문에 한 방 쿼리로 데이터를 삭제한다.
- 따라서 `deleteAll()`보다는 `deleteAllInBatch()`가 성능상 더 유리하다.
- 참고로 `tearDown`에서 데이터를 삭제할 때 **참조 무결성 제약 조건**을 위배할 수 있기 때문에 삭제하는 순서에 신경 써야 한다.(외래 키를 갖고 있는 테이블을 먼저 삭제)