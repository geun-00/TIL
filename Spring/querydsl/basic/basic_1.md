# QueryDSL 기본 문법 - 1

## JPQL vs QueryDSL

```java
@Test
void startJPQL() {
    //member1 조회
    String qlString = "select m from Member m where m.username = :username";
    Member findMember = em.createQuery(qlString, Member.class)
                          .setParameter("username", "member1")
                          .getSingleResult();
    assertThat(findMember.getUsername()).isEqualTo("member1");
}

@Test
void startQuerydsl() {
    Member findMember = query
                        .select(member)
                        .from(member)
                        .where(member.username.eq("member1"))
                        .fetchOne();
    assertThat(findMember.getUsername()).isEqualTo("member1");
}
```
**JPQL과 QueryDSL의 대표적인 차이점**
- 오타 입력 시 JPQL은 문자이기 때문에 실행 시점에 오류가 난다. `QueryDSL`은 자바 코드이기 때문에 컴파일 시점에 오류가 난다.
- JPQL은 파라미터 바인딩을 직접 한다. `QueryDSL`은 파라미터 바인딩이 자동으로 처리된다.

<br>

## 검색 조건 쿼리
```java
@Test
void search() {
    Member findMember = query
                        .selectFrom(member)
                        .where(
                                member.username.eq("member1")
                                .and(member.age.eq(10)))
                        .fetchOne();
    assertThat(findMember.getUsername()).isEqualTo("member1");
}
```
`selectFrom` : `select`,`from`을 합칠 수 있다.<br>
검색 조건은 `.and()`, `.or()`를 메서드 체인으로 연결할 수 있다.

`where`은 `...`문법으로 파라미터를 받기 때문에 다음처럼도 가능하다.
```java
@Test
void searchAndParam() {
 Member findMember = query
         .selectFrom(member)
         .where(
                 member.username.eq("member1"),
                 member.age.eq(10))
         .fetchOne();
 assertThat(findMember.getUsername()).isEqualTo("member1");
}
```
`where()`에 파라미터로 검색조건을 추가하면 `AND`조건이 추가된다. **만약 `null`값 이라면 무시된다.** 메서드 추출을 활용해서 동적 쿼리를 만들 수 있다.

**JPQL이 제공하는 모든 검색 조건**
```java
member.username.eq("member1") // username = 'member1' equal
member.username.ne("member1") //username != 'member1' not equal
member.username.eq("member1").not() // username != 'member1'
member.username.isNotNull() //이름이 is not null
        
member.age.in(10, 20) // age in (10,20)
member.age.notIn(10, 20) // age not in (10, 20)
member.age.between(10,30) //between 10, 30
        
member.age.goe(30) // age >= 30 greater or equal
member.age.gt(30) // age > 30 greater
member.age.loe(30) // age <= 30 less or equal
member.age.lt(30) // age < 30 little
        
member.username.like("member%") //like 검색
member.username.contains("member") // like ‘%member%’ 검색
member.username.startsWith("member") //like ‘member%’ 검색
```

<br>

## 결과 조회
- `fetch()` : 리스트 조회, 데이터가 없으면 빈 리스트를 반환한다.
- `fetchOne()` : 단건 조회
  - 결과가 없으면 `null`
  - 결과가 둘 이상이면 `NonUniqueResultException`예외가 발생한다.
- `fetchFirst()` : 내부에서 `limit(1).fetchOne()`을 실행한다.
- `fetchResults()` : 페이징 정보를 포함해서 total count 쿼리를 추가로 실행한다.
- `fetchCount()` : count 쿼리로 변경해서 count만 조회한다.
```java
@Test
void resultFetch() {
    // List 조회
    List<Member> fetch = query
            .selectFrom(member).fetch();
    // 단건 조회
    Member fetchOne = query
            .selectFrom(member).fetchOne();
    // 처음 한건 조회
    Member fetchFirst = query
            .selectFrom(member).fetchFirst();
    // 페이징에서 사용, count 쿼리 추가 실행
    QueryResults<Member> results = query
            .selectFrom(member).fetchResults();
    // 페이징 정보
    results.getTotal();
    List<Member> content = results.getResults();
    results.getOffset();
    results.getLimit();
    
    // 카운트 쿼리만 실행
    long total = query.selectFrom(member).fetchCount();
}
```

<br>

## 정렬
```java
@Test
void sort() {
    em.persist(new Member(null, 100));
    em.persist(new Member("member5", 100));
    em.persist(new Member("member6", 100));
    
    List<Member> result = query
                          .selectFrom(member)
                          .where(member.age.eq(100))
                          .orderBy(member.age.desc(), member.username.asc().nullsLast())
                          .fetch();
    
    Member member5 = result.get(0);
    Member member6 = result.get(1);
    Member memberNull = result.get(2);
    
    assertThat(member5.getUsername()).isEqualTo("member5");
    assertThat(member6.getUsername()).isEqualTo("member6");
    assertThat(memberNull.getUsername()).isNull();
}
```
1순위는 나이를 내림차순, 2순위는 이름을 오름차순으로 한다. 만약 이름이 없으면 마지막에 출력한다.(`nullsLast()`)
- `desc()`, `asc()` : 일반 정렬
- `nullsLast()`, `nullsFirst()` : `null` 데이터 순서 부여

<br>

## 페이징
```java
@Test
void paging1() {
    List<Member> result = query
                          .selectFrom(member)
                          .orderBy(member.username.desc())
                          .offset(1) // 0부터 시작
                          .limit(2) // 최대 2건 조회
                          .fetch();
    assertThat(result.size()).isEqualTo(2);
}
```

**전체 조회 수가 필요할 때**
```java
@Test
void paging2() {
    QueryResults<Member> queryResults = query
                                        .selectFrom(member)
                                        .orderBy(member.username.desc())
                                        .offset(1)
                                        .limit(2)
                                        .fetchResults();
    assertThat(queryResults.getTotal()).isEqualTo(4);
    assertThat(queryResults.getOffset()).isEqualTo(1);
    assertThat(queryResults.getLimit()).isEqualTo(2);
    assertThat(queryResults.getResults().size()).isEqualTo(2);
    assertThat(queryResults.getResults())
            .extracting(Member::getUsername).containsExactly("member3", "member2");
}
```
> **count 쿼리가 추가로 실행된다. 성능상 좋지 않을 수 있다.**
> 
> 페이징 쿼리를 작성할 때 데이터를 조회하는 쿼리는 여러 테이블을 조인해야 하지만 count 쿼리는 조인이 필요 없는 경우가 있다. 이렇게 자동화된 count 쿼리는
> 원본 쿼리와 같이 모두 조인을 해버린다. count 쿼리에 조인이 필요 없다면 count 전용 쿼리를 별도로 작성해야 한다.