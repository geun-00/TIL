# JdbcTemplate
> SQL을 직접 사용하는 경우에 스프링이 제공하는 `JdbcTemplate`은 JDBC를 매우 편리하게 사용할 수 있게 도와준다.

- 장점
  - 설정
    - `JdbcTemplate`은 `spring-jdbc`라이브러리에 포함되어 있는데 이 라이브러리는 스프링으로 JDBC를 사용할 때 기본으로 사용되는 라이브러리다. 별도의 복잡한 설정 없이 바로 사용할 수 있다.
  - 반복문제 해결
    - `템플릿 콜백 패턴`을 사용해서 JDBC를 직접 사용할 때 발생하는 대부분의 반복 작업을 대신 처리해준다.
      - 커넥션 획득, 트랜잭션을 위한 커넥션 동기화
      - 예외 발생 시 스프링 예외 변환기 실행
      - `커넥션`, `statement`,`resultset` 종료
    - 개발자는 SQL을 작성하고 파라미터를 정의하고 응답 값을 매핑하기만 하면 된다.
- 단점
  - 동적 SQL을 해결하기 어렵다.


- build.gradle 설정
```properties
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
```

- [V1](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/jdbcTemplate/v1/jdbcTemplate_v1.md)
- [V2](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/jdbcTemplate/v2/jdbcTemplate_v2.md)
- [V3](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/jdbcTemplate/v3/jdbcTemplate_v3.md)

JdbcTemplate이 제공하는 주요 기능

- `JdbcTempalte` : 순서 기반 파라미터 바인딩 지원
- `NamedParameterJdbcTemplate` : 이름 기반 파라미터 바인딩 지원(권장)
- `SimpleJdbcInsert` : INSERT SQL을 편리하게 사용할 수 있다.
- `SimpleJdbcCall` : 스토어드 프로시저를 편리하게 호출할 수 있다.

[스프링 JdbcTemplate 사용 방법 공식 매뉴얼](https://docs.spring.io/spring-framework/reference/data-access/jdbc/core.html#jdbc-JdbcTemplate)