# JPQL

## 경로 표현식
> .(점)을 찍어 객체 그래프를 탐색하는 것

- **상태 필드** : 단순히 값을 저장하기 위한 필드
- **연관 필드** : 연관관계를 위한 필드, 임베디드 타입 포함
  - **단일 값 연관 필드** : `@ManyToOne`, `@OneToOne`, 대상이 엔티티
  - **컬렉션 값 연관 필드** : `@OneToMany`, `@ManyToMany`, 대상이 컬렉션

**특징**
- **상태 필드 경로** : 경로 탐색의 끝이다. 더 이상 탐색할 수 없다.
- **단일 값 연관 경로** : **묵시적 내부 조인이 발생한다.** 계속 탐색할 수 있다.
- **컬렉션 값 연관 경로** : **묵시적 내부 조인이 발생한다.** 더 이상 탐색할 수 없다.
  - `FROM` 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색이 가능하다.
  - `select t.members.name form Team t` 실패한다.
  - `select m.name from Team t join t.members m` 성공한다.


- **명시적 조인** : `JOIN`을 직접 적어준다.
  - `select m from Member m join m.team t`
- **묵시적 조인** : 경로 표현식에 의해 묵시적으로 조인이 일어난다. 내부 조인만 할 수 있다.
  - `select m.team from Member m`

> **가급적 묵시적 조인 대신에 명시적 조인을 사용하자.** 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어렵다.

<br>

## 페치 조인
> SQL의 조인 종류는 아니고 JPQL에서 성능 최적화를 위해 제공하는 기능이다.
> 
> 연관된 엔티티나 컬렉션을 한 번에 같이 조회하는 기능이다.

- 엔티티 페치 조인
```java
List<Member> result = em.createQuery("select m from Member m join fetch m.team", Member.class)
        .getResultList();
for (Member member : result) {
        System.out.println("member = " + member.getName() + ", " + member.getTeam().getName());
}
```
`join fetch`없이 `select m from Member m`인 상태에서 객체 그래프 탐색을 하면 지연로딩(`LAZY`)으로 설정되어 있기 때문에 사용할 때마다 쿼리를 날리게 된다. 이 때
`N + 1`문제가 발생한다.(회원을 select하는 쿼리 실행(1)에 연관된 팀마다 N번 select 쿼리를 실행한다.)

하지만 페치 조인을 사용하면 지연로딩으로 설정 되어있다 해도 회원과 연관된 팀 엔티티는 프록시가 아닌 실제 엔티티가 반환된다.

- 컬렉션 페치 조인
```java
List<Team> result = em.createQuery("select t from Team t join fetch t.members", Team.class)
        .getResultList();
for (Team team : result) {
    System.out.println("team = " + team.getName() + "|members = " + team.getMembers().size());
    for (Member member : team.getMembers()) {
        System.out.println("    -> member = " + member);
    }
}
```
일대다 관계를 조회할 때는 데이터가 훨씬 많이 조회될 수도 있다.

**하이버네이트6 부터는 `distinct` 자동 적용된다.**

### 페치 조인의 한계

- **페치 조인 대상에는 별칭을 줄 수 없다.**
  - JPA 표준에서는 지원하지 않지만 하이버네이트에서는 지원한다. 별칭을 잘못 사용하면 연관된 데이터 수가 달라져서 데이터 무결성이 깨질 수 있으므로 조심해서 사용해야 한다.(가급적 사용하면 안 된다.)
- **둘 이상의 컬렉션은 페치 조인 할 수 없다.**
  - 컬렉션 * 컬렉션이 되기 때문에 카테시안 곱이 만들어진다.
- **컬렉션을 페치 조인하면 페이징 API를 사용할 수 없다.**
  - 일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인해도 페이징이 가능하다.
  - 하이버네이트는 컬렉션을 페지 조인하고 페이징을 하면 경고 로그를 남기면서 메모리에서 페이징 처리를 한다.(**매우 위험하다.**)
  - 객체 그래프 탐색 방향을 반대로 하거나 `@BatchSize`로 해결할 수도 있다.

> 페치 조인을 사용하면 SQL 한 번으로 연관된 엔티티들을 함께 조회할 수 있어서 SQL 호출 횟수를 줄여 성능 최적화를 할 수 있다. 페치 조인은 엔티티에 직접 적용하는 
> 글로벌 로딩 전략(FetchType.LAZY)보다 우선한다. **글로벌 로딩 전략은 될 수 있으면 모두 지연 로딩을 사용하고 최적화가 필요하면 페치 조인을 적용하는 곳이 효과적이다.**
> 
> **모든 것을 페치 조인으로 해결할 수는 없다.** 페치 조인은 객체 그래프를 유지할 때 사용하면 효과적이다. 
> 
> 여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야 한다면 억지로 페치 조인을 사용하기보다는 일반 조인을 사용하고 여러 테이블에서 필요한
> 필드들만 조회해서 DTO로 반환하는 것이 더 효과적일 수 있다.

<br>

## 다형성 쿼리
- type
```java
//JPQL
select i from Item i
where type(i) in (book, movie)

//SQL
select i from Item i
where i.dtype in ('B', 'M')
```
엔티티의 상속 구조에서 조회 대상을 특정 자식 타입으로 한정할 때 주로 사용한다. Item 중에 `Book`, `Movie`를 조회한다.

- treat
```java
//JPQL
select i from Item i
where treat(i as Book).author = 'kim'

//SQL
select i.* from Item i
where i.dtype='B' 
      and i.author='kim'
```
자바의 타입 캐스팅과 비슷하다. 상속 구조에서 부모 타입을 특정 자식 타입으로 다룰 때 사용한다. 부모 타입인 `Item`을 자식 타입인 `Book`으로 다룬다.

<br>

## 엔티티 직접 사용

- 기본 키 값
  - 객체 인스턴스는 참조 값으로 식별하고 테이블 로우는 기본 키 값으로 식별한다. JPQL에서 엔티티 객체를 직접 사용하면 SQL에서는 해당 엔티티의 기본 키 값을 사용한다.

```java
select count(m.id) from Member m //엔티티의 ID를 사용
select count(m) from Member m //엔티티를 직접 사용
        
//실행된 SQL, 둘 다 같다.
select count(m.id) as cnt from Member m
```
엔티티를 파라미터로 직접 받아도 식별자 값(`Id`)을 직접 파라미터로 받는 것이랑 똑같다.

- 외래 키 값 : 외래 키 값도 기본 키 값과 똑같은 원리로 동작한다.

<br>

## Named 쿼리
> 미리 정의해서 이름을 부여해두고 사용하는 JPQL이다. 정적 쿼리이다.

```java
@Entity
@NamedQueries({
        @NamedQuery(name = "Member.findByUsername", query = "select m from Member m where m.name = :name")
})
public class Member{
  ...
}

List<Member> result = em.createNamedQuery("Member.findByUsername", Member.class)
                        .setParameter("name", "회원1")
                        .getResultList();
```
**Named 쿼리는 애플리케이션 로딩 시점에 문법을 체크하고 미리 파싱해 둔다.** 그래서 오류를 빨리 확인할 수 있고 사용하는 시점에는 파싱된 결과를 재사용한다. 
그리고 Named 쿼리는 변하지 않는 정적 SQL이 생성되므로 DB의 조회 성능 최적화에도 도움이 된다.

(인텔리제이 옵션 중에 `Add Named Query`를 사용하면 편리하게 Named 쿼리를 만들어준다.)

스프링 데이터 JPA에서는 쿼리 메서드 위에 Named 쿼리를 정의할 수 있다.

<br>

## 벌크 연산
> 엔티티를 수정하려면 영속성 컨텍스트의 변경 감지 기능이나 병합을 사용하고 삭제하려면 `em.remove()`메서드를 사용한다. 하지만 이 방법으로 수백 개 이상의 엔티티를
> 하나씩 처리하기에는 시간이 너무 오래 걸린다. 이럴 때 여러 건을 한 번에 수정할 수 있는 벌크 연산을 사용하면 된다.

```java
int resultCount = em.createQuery("update Product p " +
                                 "set p.price = p.price * 1.1 " +
                                 "where p.stockAmount < :stockAmount")
                    .setParameter("stockAmount", 10)
                    .executeUpdate();
```
재고가 10개 미만인 모든 상품의 가격을 10% 상승 시켰다. 이때 벌크 연산을 수행하기 위해 `executeUpdate()`메서드를 사용한다. 반환값은 벌크 연산으로 영향받은 
로우의 수다. 삭제도 같은 메서드를 사용한다.

**벌크 연산은 영속성 컨텍스트를 무시하고 DB에 직접 쿼리하기 때문에 주의해야 한다.**

영속성 컨텍스트를 무시한다는 것은 1차 캐시에 남아있는 엔티티는 그대로 있고 DB에만 반영한다는 것이다. 즉 벌크 연산 수행 후 데이터를 조회하면 1차 캐시는 그대로이기 때문에
수정 전 데이터가 조회되는 것이다.

그래서 벌크 연산을 가장 먼저 실행을 하거나 **벌크 연산 수행 후 영속성 컨텍스트를 초기화**해 주어야 한다.