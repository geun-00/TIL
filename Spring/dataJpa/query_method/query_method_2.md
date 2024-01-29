# 쿼리 메서드 - 2

## 페이징과 정렬

- 순수 JPA
```java
public List<Member> findByPage(int age, int offset, int limit) {
   return em.createQuery("select m from Member m where m.age = :age order by m.username desc", Member.class)
            .setParameter("age", age)
            .setFirstResult(offset)
            .setMaxResults(limit)
            .getResultList();
}
public long totalCount(int age) {
    return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
            .setParameter("age", age)
            .getSingleResult();
}
```

- 스프링 데이터 JPA

스프링 데이터 JPA는 쿼리 메서드에 페이징과 정렬 기능을 사용할 수 있도록 2가지 파라미터를 제공한다.
- `org.springframework.data.domain.Sort` : 정렬 기능
- `org.springframework.data.domain.Pageable` : 페이징 기능(내부에 `Sort` 포함)

**반환 타입**
- `org.springframework.data.domain.Page` : 추가 `count` 쿼리 결과를 포함하는 페이징
- `org.springframework.data.domain.Slice` : 추가 `count` 쿼리 없이 다음 페이지만 확인 가능(내부적으로 limit + 1조회)
- `List` : 자바 컬렉션, 추가 `count`쿼리 없이 결과만 반환

사용 예제
```java
Page<Member> findByUsername(String name, Pageable pageable);  //count 쿼리 사용 
Slice<Member> findByUsername(String name, Pageable pageable); //count 쿼리 사용 안함 
List<Member> findByUsername(String name, Pageable pageable);  //count 쿼리 사용 안함 
List<Member> findByUsername(String name, Sort sort);
```
JpaRepository
```java
Page<Member> findByAge(int age, Pageable pageable);
```

테스트 코드
```java
import static org.springframework.data.domain.Sort.Direction.DESC;

@Test
void paging() {
    //given
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 10));
    memberRepository.save(new Member("member5", 10));
    int age = 10;
    PageRequest pageRequest = PageRequest.of(0, 3, DESC, "username");
    //when
    Page<Member> page = memberRepository.findByAge(age, pageRequest);
    List<Member> content = page.getContent();
    long totalElements = page.getTotalElements();
    //then
    assertThat(content.size()).isEqualTo(3);
    assertThat(totalElements).isEqualTo(5);
    assertThat(page.getNumber()).isEqualTo(0);
    assertThat(page.getTotalPages()).isEqualTo(2);
    assertThat(page.isFirst()).isTrue();
    assertThat(page.hasNext()).isTrue();
}
```
- 두 번째 파라미터로 받은 `Pageable`은 인터페이스다. 실제 사용할 때는 해당 인터페이스를 구현한 `PageRequest`객체를 사용한다.
- `PageRequest`는 현재 페이지, 조회할 데이터 수를 파라미터로 받을 수 있고 추가로 정렬 정보(예: username을 DESC로)도 파라미터로 사용할 수 있다.

여기서 반환 타입을 `Slice`로 하면 `count`쿼리는 나가지 않고 내부적으로 `limit`으로 설정한 3보다 +1한 `limit 4`를 실행한다.

Page 인터페이스
```java
public interface Page<T> extends Slice<T> { 
    int getTotalPages();     //전체 페이지 수 
    long getTotalElements(); //전체 데이터 수
    <U> Page<U> map(Function<? super T, ? extends U> converter); //변환기 
}
```

Slice 인터페이스
```java
public interface Slice<T> extends Streamable<T> {
    int getNumber();            //현재 페이지
    int getSize();              //페이지 크기
    int getNumberOfElements();  //현재 페이지에 나올 데이터 수
    List<T> getContent();       //조회된 데이터
    boolean hasContent();       //조회된 데이터 존재 여부
    Sort getSort();             //정렬 정보
    boolean isFirst();          //현재 페이지가 첫 페이지 인지 여부
    boolean isLast();           //현재 페이지가 마지막 페이지 인지 여부
    boolean hasNext();          //다음 페이지 여부
    boolean hasPrevious();      //이전 페이지 여부
    Pageable getPageable();     //페이지 요청 정보
    Pageable nextPageable();    //다음 페이지 객체
    Pageable previousPageable();//이전 페이지 객체
    <U> Slice<U> map(Function<? super T, ? extends U> converter); //변환기
}
```

```java
@Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
Page<Member> findByAge(int age, Pageable pageable);
```
이렇게 `count`쿼리를 불필요한 `join`을 하지 않게 분리할 수도 있다.(**전체 count 쿼리는 매우 무겁다.**)

```java
Page<MemberDto> toMap = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));
```
페이지를 유지하면서 엔티티를 DTO로 변환할 수 있다.

<br>

## 벌크성 수정 쿼리

순수 JPA
```java
public int bulkAgePlus(int age) {
    return  em.createQuery(
            "update Member m set m.age = m.age + 1 " +
                    "where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();
}
```
영향받은 로우의 수를 반환한다.

스프링 데이터 JPA
```java
@Modifying
@Query("update Member m set m.age = m.age + 1 where m.age >= :age")
int bulkAgePlus(@Param("age") int age);
```
`@Modifying`어노테이션으로 벌크성 수정, 삭제 쿼리를 날릴 수 있다. 벌크성 쿼리를 실행하고 나서 영속성 컨텍스트를 초기화 하려면
`@Modifying(clearAutomatically = true)`로 한다.(기본 값: `false`)

> **벌크 연산은 영속성 컨텍스를 무시하고 실행하기 때문에 영속성 컨텍스트에 있는 엔티티의 상태와 DB 엔티티 상태가 달라질 수 있다.**
> 
> 영속성 컨텍스트에 엔티티가 없는 상태에서 벌크 연산을 먼저 실행하거나, 벌크 연산 직후 영속성 컨텍스트를 초기화 해야 한다.

<br>

## @EntityGraph
> 지연로딩(`LAZY`)인 관계를 페치 조인으로 조회할 수 있다.

공통 메서드를 오버라이드 한다.
```java
@Override
@EntityGraph(attributePaths = "team")
List<Member> findAll();
```

JPQL + 엔티티 그래프를 사용할 수도 있다.
```java
@EntityGraph(attributePaths = "team")
@Query("select m from Member m")
List<Member> findMemberEntityGraph();
```

메서드 이름으로 쿼리 생성할 때도 사용할 수 있다. 같이 조회할 일이 많으면 유용하다.
```java
@EntityGraph(attributePaths = "team") 
List<Member> findByUsername(String username)
```

`@NamedEntityGraph`라는 기능도 있다.
```java
@Entity
@NamedEntityGraph(name = "Member.all",
                  attributeNodes = @NamedAttributeNode("team"))
public class Member{...}
    
// JpaRepository
@EntityGraph("Member.all")
List<Member> findGraphByUsername(String username);
```

> **`EntityGraph`는 사실상 페치 조인의 간편 버전이다.**

<br>

## JPA Hint

```java
@QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
Member findReadOnlyByUsername(String username);

@Test
void queryHint() {
    memberRepository.save(new Member("member1", 10));
    em.flush();
    em.clear();
    Member findMember = memberRepository.findReadOnlyByUsername("member1");
    findMember.setUsername("member2");
    em.flush(); // Upate Query를 실행하지 않는다.
}
```
SQL 힌트는 아니고 JPA 구현체에게 제공하는 힌트다.

<br>

## JPA Lock

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
List<Member> findLockByUsername(String username);
```
쿼리 시 락을 걸 수 있다.