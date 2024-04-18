# 트랜잭션을 추상화한 이유는 무엇인가요?

- 트랜잭션을 시작하는 코드는 데이터 접근 기술마다 모두 다르다. (`JDBC`, `JPA`, `Hibernate` 등)
- 만약 `JDBC` 기술을 사용해 `JDBC` 트랜잭션에 의존하다가 `JPA` 기술로 변경하게 되면 코드 수정이 불가피할 것이다. 
- 이런 문제를 해결하기 위해 스프링은 `PlatformTransactionManager`라는 인터페이스의 `getTransaction()` 메서드로 트랜잭션 추상화 기술을 제공한다.