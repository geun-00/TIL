# QueryDSL 기본 문법 - 2

## 집합
```java
/**
 * JPQL
 * select
 *    COUNT(m),   //회원수
 *    SUM(m.age), //나이 합
 *    AVG(m.age), //평균 나이
 *    MAX(m.age), //최대 나이
 *    MIN(m.age)  //최소 나이
 * from Member m
 */
@Test
void aggregation() {
    List<Tuple> result = query
                        .select(member.count(),
                                member.age.sum(),
                                member.age.avg(),
                                member.age.max(),
                                member.age.min())
                        .from(member)
                        .fetch();
    Tuple tuple = result.get(0);
    assertThat(tuple.get(member.count())).isEqualTo(4);
    assertThat(tuple.get(member.age.sum())).isEqualTo(10 + 20 + 30 + 40);
    assertThat(tuple.get(member.age.avg())).isEqualTo((double) (10 + 20 + 30 + 40) / 4);
    assertThat(tuple.get(member.age.max())).isEqualTo(40);
    assertThat(tuple.get(member.age.min())).isEqualTo(10);
}
```
JPQL이 제공하는 모든 집합 함수를 제공한다. `tuple`이라는 것이 반환된다.

### Group By
```java
@Test
void group() {
    List<Tuple> result = query
                        .select(team.name, member.age.avg())
                        .from(member)
                        .join(member.team, team)
                        .groupBy(team.name)
                        .fetch();
    Tuple teamA = result.get(0);
    Tuple teamB = result.get(1);
    assertThat(teamA.get(team.name)).isEqualTo("teamA");
    assertThat(teamA.get(member.age.avg())).isEqualTo((double) (10 + 20) / 2);
    assertThat(teamB.get(team.name)).isEqualTo("teamB");
    assertThat(teamB.get(member.age.avg())).isEqualTo((double) (30 + 40) / 2);
}
```
팀의 이름과 각 팀의 평균 연령을 구하는 쿼리가 실행된다.

그룹화된 결과를 제한하려면 `having`을 쓸 수 있다.
```java
    .groupBy(item.price)
    .having(item.price.gt(1000))
```
가격이 1,000원 이상인 상품들만 그룹화한다.

<br>

## 조인

### 기본 조인
조인에 기본 문법은 첫 번째 파라미터에 조인 대상을 지정하고 두 번쨰 파라미터에 별칭(`alias`)으로 사용할 Q 타입을 지정한다.
```java
@Test
void join() {
    List<Member> result = query
                          .selectFrom(member)
                          .join(member.team, team)
                          .where(team.name.eq("teamA"))
                          .fetch();
    assertThat(result).extracting("username")
                      .containsExactly("member1", "member2");
}
```
teamA에 소속된 모든 회원을 찾는 쿼리가 실행된다. `join()`, `innerJoin()`, `leftJoin()`, `rightJoin()`이 있다.

### 세타 조인
```java
@Test
void theta_join() {
    em.persist(new Member("teamA"));
    em.persist(new Member("teamB"));
    em.persist(new Member("teamC"));
    List<Member> result = query
                          .select(member)
                          .from(member, team)
                          .where(member.username.eq(team.name))
                          .fetch();
    assertThat(result)
            .extracting("username")
            .containsExactly("teamA", "teamB");
}
```
회원의 이름과 팀의 이름이 같은 회원을 조회하는 쿼리가 실행된다. `from()`절에 연관관계가 없는 필드로 여러 엔티티를 선택하면 세타 조인(`crossJoin`)이 실행된다.
외부 조인이 불가능하다.

### 조인 - on절

- **조인 대상 필터링**
```java
@Test
void join_on_filtering() {
    List<Tuple> result = query
                        .select(member, team)
                        .from(member)
                        .leftJoin(member.team, team).on(team.name.eq("teamA"))
                        .fetch();
}
```
회원과 팀을 조인(`leftJoin`)하는데 팀 이름이 "teamA"인 팀만 조회한다. 회원은 모두 조회된다.(`leftJoin`이니까)
> `on`절로 조인 대상을 필터링할 때 내부조인(`inner join`)을 사용하면 `where`절에서 필터링 하는 것과 기능이 동일하다. 조인 대상 필터링을 할 때 내부조인이면
> 익숙한 `where`절로 해결하고 외부조인이 필요한 경우에 `on`절을 쓰는 것이 좋다.

- **연관관계 없는 엔티티 외부 조인**
```java
@Test
void join_on_no_relation() {
    em.persist(new Member("teamA"));
    em.persist(new Member("teamB"));
    em.persist(new Member("teamC"));
    List<Tuple> result = query
                         .select(member, team)
                         .from(member)
                         .leftJoin(team).on(member.username.eq(team.name))
                         .fetch();
}
```
회원의 이름과 팀의 이름이 같이 대상을 **외부 조인**한다.

`on`을 사용해서 서로 관계가 없는 필드로 외부 조인을 할 수 있다.(내부 조인도 가능)
- 일반 조인: `leftJoin(member.team, team)`
- `on`조인 : `from(member).leftJoin(team).on(...)` 일반 조인과 다르게 엔티티 하나만 들어간다.

<br>

## 페치 조인

- **페치 조인 미적용**
```java
@Autowired EntityManagerFactory emf;

@Test
void fetchJoinNo() {
    em.flush();
    em.clear();
    Member findMember = query
                        .selectFrom(member)
                        .where(member.username.eq("member1"))
                        .fetchOne();
    // Lazy 이기 때문에 로딩이 안됐다.
    boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
    assertThat(loaded).as("페치 조인 미적용").isFalse();
}
```

- **페치 조인 적용**
```java
@Autowired EntityManagerFactory emf;

@Test
void fetchJoinUse() {
    em.flush();
    em.clear();
    Member findMember = query
            .selectFrom(member)
            .join(member.team).fetchJoin()
            .where(member.username.eq("member1"))
            .fetchOne();
    // Lazy이지만 페치 조인으로 로딩이 됐다.
    boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
    assertThat(loaded).as("페치 조인 적용").isTrue();
}
```
`join()`, `leftJoin()`등 조인 기능 뒤에 `fetchJoin()`이라고 추가하면 된다.