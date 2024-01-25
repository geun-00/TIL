# 트랜잭션 옵션

```java
public @interface Transactional {
	@AliasFor("transactionManager")
	String value() default "";

	@AliasFor("value")
	String transactionManager() default "";

	String[] label() default {};

	Propagation propagation() default Propagation.REQUIRED;

	Isolation isolation() default Isolation.DEFAULT;

	int timeout() default TransactionDefinition.TIMEOUT_DEFAULT;

	String timeoutString() default "";

	boolean readOnly() default false;

	Class<? extends Throwable>[] rollbackFor() default {};

	String[] rollbackForClassName() default {};

	Class<? extends Throwable>[] noRollbackFor() default {};

	String[] noRollbackForClassName() default {};
}
```
- **value, transactionManager**
  - 트랜잭션을 사용하려면 먼저 스프링 빈에 등록된 어떤 트랜잭션 매니저를 사용할지 알아야 한다. 사용할 트랜잭션 매니저를 지정할 때는 `value`,`transactionManager` 둘 중 하나에
    트랜잭션 매니저의 스프링 빈의 이름을 적어주면 된다. 생략하면 기본으로 등록된 트랜잭션 매니저를 사용하기 때문에 대부분 생략한다.

- **rollbackFor**
  - 예외 발생 시 스프링 트랜잭션의 기본 정책
    - 언체크 예외인 `RuntimeException`, `Error`와 그 하위 예외가 발생하면 **롤백**한다.
    - 체크 예외인 `Exception`과 그 하위 예외들은 **커밋**한다.
    - 이 옵션은 기본 정책에 추가로 어떤 예외가 발생했을 때 롤백할지 지정할 수 있다.
```java
@Transactional(rollbackFor = Exception.class)
```
이렇게 지정하면 체크 예외인 `Exception`과 그 하위 예외들이 발생해도 롤백하게 된다. `rollbackFor`는 이렇게 예외 클래스를 직접 지정하고 `rollbackForClassName`은 
예외 이름을 문자로 넣는다.

- **noRollbackFor**
  - `rollbackFor`와 반대로 기본 정책에 추가로 어떤 예외가 발생했을 때 롤백하면 안되는지 지정할 수 있다. 마찬가지로 예외 이름을 문자로 넣을 수 있는 `noRollbackForClassName`도 있다.

- **propagation**
  - 트랜잭션 전파에 대한 옵션

- **isolation**
  - 트랜잭션 격리 수준을 지정할 수 있다. 기본 값은 DB에서 설정한 트랜잭션 격리 수준을 사용하는 `DEFAULT`이며 대부분 DB에서 설정한 기준을 따른다. 애플리케이션 개발자가 직접
    트랜잭션 격리 수준을 지정하는 경우는 드물다.
  - `DEFAULT` : DB에서 설정한 격리 수준을 따른다.
  - `READ_UNCOMMITTED` : 커밋되지 않은 읽기
  - `READ_COMMITTED` : 커밋된 읽기
  - `REPEATABLE_READ` : 반복 가능한 읽기
  - `SERIALIZABLE` : 직렬화 가능

- **timeout**
  - 트랜잭션 수행 시간에 대한 타임아웃을 초 단위로 지정한다. 기본 값은 트랜잭션 시스템의 타임아웃을 사용한다.
  - `timeoutString`은 숫자 대신 문자 값으로 지정한다.

- **label**
  - 트랜잭션 어노테이션에 있는 값을 직접 읽어서 어떤 동작을 하고 싶을 때 사용할 수 있다.
  - 일반적으로 사용하지는 않는다.

- **readOnly**
  - 트랜잭션은 기본적으로 읽기 쓰기가 모두 가능한 트랜잭션이 생성 된다.
  - `readOnly=true` 옵션을 사용하면 읽기 전용 트랜잭션이 생성 되며 이 경우 등록, 수정, 삭제가 안되고 읽기 기능만 동작한다.
  - 읽기에서 다양한 성능 최적화가 발생할 수 있다.

`readOnly` 옵션은 크게 3곳에서 적용 된다.
- **프레임워크**
  - `JdbcTemplate`은 읽기 전용 트랜잭션 안에서 변경 기능을 실행하면 예외를 던진다.
  - `JPA(하이버네이트)`는 읽기 전용 트랜잭션의 경우 커밋 시점에 플러시를 호출하지 않는다. 읽기 전용이니 변경에 사용되는 플러시를 호출할 필요가 없다. 변경이 필요 없으니
    변경 감지를 위한 스냅샷 객체도 생성하지 않는다. JPA에서는 다양한 최적화가 발생한다.

- **JDBC 드라이버**
  - 읽기 전용 트랜잭션에서 변경 쿼리가 발생하면 예외를 던진다.
  - 읽기, 쓰기(슬레이브, 마스터) DB를 구분해서 요청한다. 읽기 전용 트랜잭션의 경우 읽기(슬레이브) DB의 커넥션을 획득해서 사용한다.

- **데이터베이스**
  - DB에 따라 읽기 전용 트랜잭션의 경우 읽기만 하면 되므로 내부에서 성능 최적화가 발생한다.
