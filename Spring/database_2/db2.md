# DB2 - 데이터 접근 기술

- SQLMapper
  - [JdbcTemplate](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/jdbcTemplate/jdbcTemplate.md)
  - [MyBatis](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/myBaits/myBatis.md)
  - 개발자가 SQL만 작성하면 SQL의 결과를 객체로 편리하게 매핑해준다.
  - JDBC를 직접 사용할 때 발생하는 여러가지 중복을 제거해주고 편리한 기능을 제공한다.
- ORM
  - [JPA, Hibernate](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/jpa/jpa.md)
  - [스프링 데이터 JPA](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/springJpa/springJpa.md)
  - [Querydsl](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/querydsl/querydsl.md)
  - 기본적인 SQL은 JPA가 대신 작성하고 처리해준다. 개발자는 저장하고 싶은 객체를 마치 자바 컬렉션에 저장하고 조회하듯이 사용하면 ORM 기술이 DB에 해당 객체를 저장하고 조회해준다.
  - `JPA`는 자바 진영의 ORM 표준이고 `Hibernate`는 JPA에서 가장 많이 사용하는 구현체이다. 자바에서 ORM을 사용할 때는 JPA 인터페이스를 사용하고 그 구현체가 하이버네이트인 것이다.
  - 스프링 데이터 JPA, Querydsl은 **JPA를 더 편리하게 사용할 수 있게 도와주는 프로젝트이다.** 
- [테스트](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/test/dbTest.md)
- [활용 방안](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/tradeOff/tradeOff.md)
- [스프링 트랜잭션](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/transaction/transaction.md)
- [트랜잭션 전파-1](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/propagation/tx_propagation_1/tx_propagation.md)
- [트랜잭션 전파-2](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/propagation/tx_propagation_2/tx_propagation.md)