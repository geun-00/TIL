# QueryDSL 기본 문법 - 3

## 서브 쿼리
`JPAExpressions`를 사용하면 된다.


```java
@Test
void subQuery() {
    QMember memberSub = new QMember("memberSub");
    List<Member> result = query
                          .selectFrom(member)
                          .where(member.age.eq(
                                  JPAExpressions
                                          .select(memberSub.age.max())
                                          .from(memberSub)
                          ))
                          .fetch();
    assertThat(result).extracting("age").containsExactly(40);
}
```
나이가 가장 많은 회원을 조회한다.

서브 쿼리는 `alias`가 겹치면 안 되기 때문에 Q 타입을 하나 추가로 생성해준다.

```java
@Test
void subQueryGoe() {
    QMember memberSub = new QMember("memberSub");
    List<Member> result = query
                          .selectFrom(member)
                          .where(member.age.goe(
                                  JPAExpressions
                                          .select(memberSub.age.avg())
                                          .from(memberSub)
                          ))
                          .fetch();
    assertThat(result).extracting("age").containsExactly(30,40);
}
```
나이가 평균 이상(`goe`, `>=`)인 회원을 조회한다.

```java
@Test
void subQueryIn() {
    QMember memberSub = new QMember("memberSub");
    List<Member> result = query
                          .selectFrom(member)
                          .where(member.age.in(
                                  JPAExpressions
                                          .select(memberSub.age)
                                          .from(memberSub)
                                          .where(memberSub.age.gt(10))
                          ))
                          .fetch();
    assertThat(result).extracting("age").containsExactly(20, 30, 40);
}
```
`in`절도 가능하다. 10살 초과(`gt`, `>`)인 회원을 조회한다.

```java
@Test
void selectSubQuery() {
    QMember memberSub = new QMember("memberSub");
    List<Tuple> result = query
                         .select(member.username,
                                 JPAExpressions
                                         .select(memberSub.age.avg())
                                         .from(memberSub))
                         .from(member)
                         .fetch();
}
```
`select`절에 서브 쿼리도 가능하다. 모든 회원의 이름하고 평균 나이가 조회된다.

> `JPAExpressions`는 `static import` 가능하다.

<br>

## CASE 문

**단순한 조건**
```java
List<String> result = query
                      .select(member.age
                              .when(10).then("열살")
                              .when(20).then("스무살")
                              .otherwise("기타"))
                      .from(member)
                      .fetch();
```

**복잡한 조건**
```java
List<String> result = query
                      .select(new CaseBuilder()
                              .when(member.age.between(0, 20)).then("0~20살")
                              .when(member.age.between(21, 30)).then("21~30살")
                              .otherwise("기타"))
                      .from(member)
                      .fetch();
```

QueryDSL은 자바 코드로 작성하기 때문에 다음처럼도 가능하다.
```java
@Test
void rankPath() {
    NumberExpression<Integer> rankPath = new CaseBuilder()
            .when(member.age.between(0, 20)).then(2)
            .when(member.age.between(21, 30)).then(1)
            .otherwise(3);
    
    List<Tuple> result = query
            .select(member.username, member.age, rankPath)
            .from(member)
            .orderBy(rankPath.desc())
            .fetch();
    
    for (Tuple tuple : result) {
        String username = tuple.get(member.username);
        Integer age = tuple.get(member.age);
        Integer rank = tuple.get(rankPath);
        System.out.println("username = " + username + ", age = " + age + ", rank = " + rank);
    }
}
```
`rankPath`에 복잡한 조건을 변수로 선언해서 `select`절, `orderBy`절에서 함께 사용했다.

0-30살이 아닌 회원을 가장 먼저 출력하고, 다음에 0-20살, 다음에 21~30살을 출력한다.
```java
//실행 결과
username = member4, age = 40, rank = 3
username = member1, age = 10, rank = 2
username = member2, age = 20, rank = 2
username = member3, age = 30, rank = 1
```

<br>

## 상수, 문자 더하기
`Expressions.constant()`를 사용한다.

```java
List<Tuple> result = query
                     .select(member.username, Expressions.constant("A"))
                     .from(member)
                     .fetch();
//실행 결과
[member1, A]
[member2, A]
[member3, A]
[member4, A]
```
이름과 문자를 함께 출력한다.

```java
List<String> result = query
                      .select(member.username.concat("_").concat(member.age.stringValue()))
                      .from(member)
                      .where(member.username.eq("member1"))
                      .fetch();
//실행 결과
member1_10
```
이름 + `-` + 나이를 출력한다.

`member.age.stringValue()` : 문자가 아닌 다른 타입들은 `StringValue()`로 문자로 변환할 수 있다. `ENUM`을 처리할 때 유용하다.
