# 스프링 데이터 JPA 나머지 기능들

## 프로젝션(Projections)
> 전체 엔티티가 아니라 필드 하나만 조회할 수 있다.

```java
public interface UsernameOnly {
    String getUsername();
}
```
조회할 엔티티의 필드를 `getter` 형식으로 지정한다.(인터페이스)

```java
public interface MemberRepository extends JpaRepository<Member, Long>{
    List<UsernameOnly> findProjectionByUsername(String name);
}

@Test
void projections() {
    Team teamA = new Team("teamA");
    em.persist(teamA);

    Member m1 = new Member("m1", 0, teamA);
    Member m2 = new Member("m2", 0, teamA);
    em.persist(m1);
    em.persist(m2);

    em.flush();
    em.clear();
    List<UsernameOnly> result = memberRepository.findProjectionByUsername("m1");
}
```
```sql
select m1_0.username from member m1_0 where m1_0.username='m1';
```
실행된 SQL에서 이름만 조회한다. 이 방법을 **인터페이스 기반 Closed Projections**라고 하고 이렇게 프로퍼티 형식(`getter`)의 인터페이스를 제공하면
구현체는 스프링 데이터 JPA가 프록시 기술로 제공해준다.

**인터페이스 기반 Open Projections**
```sql
public interface UsernameOnly {
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
```
이렇게 스프링의 SpEL 문법도 지원한다. 이 방법은 일단 DB에서 엔티티 필드를 다 조회해온 다음에 계산하기 때문에 Select 절에서 최적화는 안 된다.

**클래스 기반 Projections**
```java
@Getter
public class UsernameOnlyDto {
    private final String username;

    public UsernameOnlyDto(String username) {
        this.username = username;
    }
}
```
인터페이스가 아닌 구체적인 DTO 형식도 가능하다. **생성자의 파라미터 이름으로 매칭한다.** 파라미터 이름이 필드 이름과 다르면 매칭이 안 된다.

실행된 SQL은 `username`만 딱 조회하고 인터페이스처럼 프록시 객체가 아닌 실제 객체를 반환한다.

**동적 Projections**
```java
<T> List<T> findProjectionByUsername(String username, Class<T> type);

//사용 코드
List<UsernameOnlyDto> result = memberRepository.findProjectionByUsername("m1", UsernameOnlyDto.class);
```
타입을 제네릭으로 주면 동적으로 프로젝션 데이터 변경이 가능하다.

**중첩 구조 처리**
```java
public interface NestedClosedProjections {
    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
```
실행 SQL
```sql
select
    m1_0.username,
    t1_0.team_id,
    t1_0.name 
from
    member m1_0 
left join
    team t1_0 
        on t1_0.team_id=m1_0.team_id
where
    m1_0.username=?
```
루트 엔티티인 Member만 Select절을 최적화해서 조회했다.

이렇게 프로젝션 대상이 루트 엔티티면 Select절 최적화가 가능하지만 루트가 아니라면 `Left Join`으로 모든 필드를 Select해서 엔티티로 조회한 다음에 계산한다.

**프로젝션 대상이 루트 엔티티면 유용하다.** 대상이 루트 엔티티를 넘어가면 최적화가 안 된다.

단순할 때만 사용하고 `Querydsl`을 사용하는 것이 좋다.

<br>

## Native Query

```java
@Query(value = "select * from member where username = ?", nativeQuery = true)
Member findByNativeQuery(String username);
```

Projections를 활용하여 DTO를 조회할 수도 있다.
```java
public interface MemberProjection {
    Long getId();
    String getUsername();
    String getTeamName();
}

@Query(value = "select m.member_id as id, m.username, t.name as teamName" +
               " from member m left join team t",
        countQuery = "select count(*) from member",
        nativeQuery = true)
Page<MemberProjection> findByNativeProjection(Pageable pageable);
```
페이징도 가능하다.

**Native Query 제약 사항**
- Sort 파라미터를 통한 정렬이 정상 동작하지 않을 수 있다.
- JPQL처럼 애플리케이션 로딩 시점에 문법 확인이 불가능하다.
- 동적 쿼리가 불가하다.