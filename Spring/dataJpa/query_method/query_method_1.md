# 쿼리 메서드 기능 - 1

> 스프링 데이터 JPA가 제공하는 쿼리 메서드 기능
> 1. 메서드 이름으로 쿼리 생성
> 2. 메서드 이름으로 JPA NamedQuery 호출
> 3. `@Query` 어노테이션을 사용해서 레포지토리 인터페이스에 쿼리 직접 정의

<br>

## 메서드 이름으로 쿼리 생성

순수 JPA
```java
public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
    return em.createQuery("select m from Member m where m.username = :username and m.age > :age",Member.class)
            .setParameter("username", username)
            .setParameter("age", age)
            .getResultList();
}
```

스프링 데이터 JPA
```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
```

스프링 데이터 JPA가 메서드 이름을 분석해서 JPQL을 생성하고 실행한다. 물론 정해진 규칙이 있다.<br>
[스프링 데이터 JPA 공식 문서](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation)

> **엔티티의 필드명이 변경되면 인터페이스의 정의한 메서드 이름도 꼭 함께 변경되어야 한다. 그렇지 않으면 애플리케이션을 시작하는 시점에 오류가 발생한다.**
> 
> 애플리케이션 로딩 시점에 오류를 잡을 수 있는 것이 스프링 데이터 JPA의 매우 큰 장점이다.

<br>

## JPA NamedQuery

엔티티에 NamedQuery 정의
```java
@Entity
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
public class Member
```

JpaRepository
```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
//    @Query(name = "Member.findByUsername")
//    List<Member> findByUsername(@Param("username") String username);
   
    List<Member> findByUsername(@Param("username") String username);
}
```
`@Query`를 사용하거나 생략해서 사용할 수 있다.

스프링 데이터 JPA는 전략이 있는데
- 선언한 "도메인 클래스 + `.`(점) + 메서드 이름"으로 NamedQuery를 찾아서 실행한다.
- 실행할 NamedQuery가 없으면 메서드 이름으로 쿼리 생성 전략을 사용한다.

> 이렇게 직접 NamedQuery를 등록해서 사용하는 일은 드물고 보통은 `@Query`를 사용해서 레포지토리 메서드에 직접 정의하는 방식을 많이 사용한다.

<br>

## @Query, 레포지토리 메서드에 쿼리 정의
> 실행할 메서드에 정적 쿼리를 직접 작성하는 방식이다. 이름 없는 NamedQuery인 셈이다.

메서드에 JPQL 쿼리 작성
```java
public interface MemberRepository extends JpaRepository<Member, Long>{
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
```
메서드 이름으로 쿼리 생성 기능은 파라미터가 증가하면 자연스럽게 메서드 이름이 길어질 수밖에 없으므로 메서드 이름이 지저분해진다. `@Query`로 메서드 이름을 줄일 수 있다.

> **애플리케이션 실행 시점에 문법 오류를 발견할 수 있다.**

### @Query로 값 조회하기
```java
@Query("select m.username from Member m")
List<String> findUsernameList();
```
JPA 값 타입(`@Embedded`)도 이 방식으로 조회할 수 있다.

### @Query로 DTO 조회하기
```java
@Data
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String username;
    private String teamName;
}

@Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
List<MemberDto> findMemberDto();
```
JPA 사용방식과 동일하게 `new` 명령어로 DTO로 직접 조회할 수 있다.

<br>

## 파라미터 바인딩
스프링 데이터 JPA는 위치 기반, 이름 기반 파라미터 바인딩을 지원한다.
```java
select m from Member m where m.name = ?1 // 위치 기반
select m from Member m where m.name = :name // 이름 기반
```
기본 값은 위치 기반이고 이름 기반은 `@Param`으로 매핑한다.<br>
**이름 기반 파라미터를 사용하자.** 코드 가독성도 좋고 무엇보다 유지보수에 용이해진다.

- `Collection`타입으로 `in`절도 지원한다.
```java
@Query("select m from Member m where m.username in :names")
List<Member> findByNames(@Param("names") List<String> names);
```

<br>

## 반환 타입
스프링 데이터 JPA는 유연한 반환 타입을 지원한다. 
```java
List<Member> findByUsername(String name); // 컬렉션
Member findByUsername(String name); // 단건
Optional<Member> findByUsername(String name); // 단건 - Optional
```
**조회 결과가 많거나 없을 때**
- 컬렉션
  - 결과 없음 : 빈 컬렉션을 반환한다.
- 단건 조회
  - 결과 없음 : `null`을 반환한다.
  - 결과가 두 건 이상 : `jakarta.persistence.NonUniqueResultException`예외가 발생한다.

> 단건으로 지정한 메서드를 호출하면 스프링 데이터 JPA는 내부에서 JPQL의 `Query.getSingleResult()` 메서드를 호출한다. 이 메서드를 호출했을 때
> 조회 결과가 없으면 `jakarta.persistence.NoResultException`예외가 발생한다. 
> 
> 이 때 스프링 데이터 JPA는 단건을 조회할 때 이 예외가 발생하면 예외를 무시하고 대신에 `null`을 반환한다.