# QueryDSL 중급 문법 - 2

## 동적 쿼리 - BooleanBuilder

```java
void 동적쿼리_BooleanBuilder() {
    String username = "member1";
    Integer ageParam = null;
    List<Member> result = searchMember1(username, ageParam);
    assertThat(result.size()).isEqualTo(1);
}
private List<Member> searchMember1(String usernameCond, Integer ageParamCond) {
    BooleanBuilder builder = new BooleanBuilder();
    if(usernameCond != null) {
        builder.and(member.username.eq(usernameCond));
    }
    if(ageParamCond != null) {
        builder.and(member.age.eq(ageParamCond));
    }
    return  query
            .selectFrom(member)
            .where(builder)
            .fetch();
}
```
`BooleanBuilder`로 조건에 따라 `where`에 어떤 `and`조건을 추가할지 동적으로 생성할 수 있다. `and`외에 `or`, `andNot`, `orNot`등이 있다.

<br>

## 동적 쿼리 - Where 다중 파라미터 사용
```java
void 동적쿼리_WhereParam() {
    String username = "member1";
    Integer ageParam = null;
    List<Member> result = searchMember2(username, ageParam);
    assertThat(result.size()).isEqualTo(1);
}
private List<Member> searchMember2(String usernameCond, Integer ageCond) {
    return query
            .selectFrom(member)
            .where(usernameEq(usernameCond), ageEq(ageCond))
            .fetch();
}
private BooleanExpression usernameEq(String usernameCond) {
    return usernameCond != null ? member.username.eq(usernameCond) : null;
}
private BooleanExpression ageEq(Integer ageCond) {
    return ageCond != null ? member.age.eq(ageCond) : null;
}
```
- `where`조건에 `null`이 들어가면 무시된다.
- **메서드를 다른 쿼리에서 재사용이 가능하다.**
- 쿼리 자체의 가독성이 높아진다.

자바 코드이기 때문에 조합도 가능하다.
```java
private BooleanExpression allEq(String usernameCond, Integer ageCond) {
    return (usernameCond == null && ageCond == null) ? null  : usernameEq(usernameCond).and(ageEq(ageCond));
}
```
`null`체크에 주의해야 한다.

<br>

## 수정, 삭제 벌크 연산

```java
long count = query
             .update(member)
             .set(member.username, "비회원")
             .where(member.age.lt(28))
             .execute();
// 28살 미만 회원의 이름을 "비회원"으로 변경한다.
em.flush();
em.clear();
List<Member> result = query
                        .selectFrom(member)
                        .fetch();
```
`execute()`로 벌크 연산을 할 수 있다.

```java
query
        .update(member)
        .set(member.age, member.age.add(2))
        .execute();
```
모든 회원의 나이를 2살씩 더한다. 곱하기는 `multiply(x)`
```java
query
        .delete(member)
        .where(member.age.gt(18))
        .execute();
```
18살 초과의 회원들을 모두 삭제한다.

> **벌크 연산은 항상 영속성 컨텍스트를 무시하고 실행되기 때문에 주의해야 한다.** 벌크 연산을 실행하고 나면 항상 영속성 컨텍스트를 깨끗하게 비워내는 것이
> 안전하다.

<br>

## SQL function 호출하기
SQL function은 JPA와 같이 Dialect에 등록된 내용만 호출할 수 있다.

replace 함수 사용
```java
List<String> result = query
                     .select(Expressions.stringTemplate(
                             "function('replace', {0}, {1}, {2})",
                             member.username, "member", "M"))
                     .from(member)
                     .fetch();
```
회원 이름에서 'member' 부분을 'M'으로 변경한다.