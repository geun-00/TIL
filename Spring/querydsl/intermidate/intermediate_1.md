# QueryDSL 중급 문법 - 1

## 프로젝션 결과 반환 - 기본
프로젝션 : `select` 대상을 지정할 수 있다.

```java
List<String> result = query
                      .select(member.username)
                      .from(member)
                      .fetch();
```
프로젝션 대상이 하나면 타입을 명확하게 지정할 수 있어 간단하다.

프로젝션 대상이 둘 이상이면 `튜플`이나 `DTO`로 조회한다.
```java
List<Tuple> result = query
                     .select(member.username, member.age)
                     .from(member)
                     .fetch();
for (Tuple tuple : result) {
    String username = tuple.get(member.username);
    Integer age = tuple.get(member.age);
    System.out.println("username = " + username);
    System.out.println("age = " + age);
}
```

> `Tuple`은 `com.querydsl.core`이다. 즉 QueryDSL에 종속적인 기술이다. 그래서 `Tuple`을 레포지토리 계층을 벗어나서 서비스나 컨트롤러 계층까지 넘어가면 좋지 않은 설계다.
> 
> 그래서 레포지토리에서만 사용하고 외부로 보낼 때는 DTO로 변환하는 등 하는 것이 좋고 `Tuple`을 그대로 내보내는 방법은 좋지 않다. 

<br>

## 프로젝션 결과 반환 - DTO
엔티티에서 딱 필요한 필드만 조회해서 바로 DTO로 변환

**순수 JPA DTO 조회**
```java
@Data
@AllArgsConstructor
public class MemberDto {
    private String username;
    private int age;
}

List<MemberDto> result = em.createQuery(
                "select new study.querydsl.dto.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                            .getResultList();
```
`new` 명령어를 사용해서 DTO의 Package 이름을 다 적어주어야 한다. 그래서 코드가 지저분해지며 생성자 방식만 지원한다.

QueryDSL은 3가지 방법을 지원한다.

**프로퍼티 접근 - Setter**(`setter`를 통해 값 할당)
```java
List<MemberDto> result = query
                         .select(Projections.bean(MemberDto.class,
                                 member.username,
                                 member.age))
                         .from(member)
                         .fetch();
```

**필드 직접 접근**(`setter`없이 필드로 바로 접근)
```java
List<MemberDto> result = query
                         .select(Projections.fields(MemberDto.class,
                                 member.username,
                                 member.age))
                         .from(member)
                         .fetch();
```

`프로퍼티`나 `필드 접근` 생성 방식에서 이름이 다르면 별칭(`as`)을 주어 해결할 수 있다.
```java
@Data
public class UserDto {
    private String name;
    private int age;
}

List<UserDto> result = query
                        .select(Projections.fields(UserDto.class,
                                member.username.as("name"),
                                //서브 쿼리
                                ExpressionUtils.as(
                                        JPAExpressions
                                                .select(memberSub.age.max())
                                                .from(memberSub), "age")
                                ))
                        .from(member)
                        .fetch();
```
- `member`는 원래 username이고 `UserDto`는 name이다. 이 때 `as()`로 별칭을 줄 수 있다.
- `ExpressionUtils.as(source, alias)` : 필드나 서브 쿼리에 별칭을 적용한다.(가장 많은 나이를 "age"에 매칭한다.)

**생성자 사용**
```java
List<MemberDto> result = query
                        .select(Projections.constructor(MemberDto.class,
                                member.username,
                                member.age))
                        .from(member)
                        .fetch();
```

<br>

## 프로젝션 결과 반환 - @QueryProjection
```java
@Data
public class MemberDto {
    private String username;
    private int age;
    
    @QueryProjection
    public MemberDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
```
DTO 클래스 생성자에 `@QueryProjection`을 적용하고 `compileJava`를 하면 DTO도 QClass로 만들어진다.

```java
List<MemberDto> result = query
                         .select(new QMemberDto(member.username, member.age))
                         .from(member)
                         .fetch();
```
JPQL의 `new`명령어와 같은 기능인데 JPQL과 비교했을 때 패키지명을 적어줄 필요가 없으니 깔끔해진다. 

그리고 프로젝션 생성자 사용과 비교했을 때 일반 생성자 사용은 런타임 시 에러를 낸다. 예를 들어 생성자 파라미터에 DTO에 없는 필드를 넣으면 생성자 사용은 바로 에러를 잡아주지 않고
런타임 시 에러를 잡아준다.(안 좋은 설계)

하지만 `@QueryProject`은 컴파일 시에 에러를 잡아준다.

**@QueryProject 단점**
- DTO까지 Q 파일을 생성해야 한다.
- DTO가 순수해지지 못하고 QueryDSL 기술에 의존적이게 된다.