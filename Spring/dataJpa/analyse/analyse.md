# 스프링 데이터 JPA 분석

## 스프링 데이터 JPA가 사용하는 구현체

스프링 데이터 JPA가 제공하는 공통 인터페이스는 `SimpleJpaRepository` 클래스가 구현한다.

- `SimpleJpaRepository`
```java
@Repository
@Transactional(readOnly = true)
public class SimpleJpaRepository<T, ID> implements JpaRepositoryImplementation<T, ID> {
    ...
    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null");
        if (entityInformation.isNew(entity)) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }
    ...
}
```
- **@Repository 적용** : 스프링 빈으로 등록하고, **JPA 예외를 스프링이 추상화한 예외로 변환한다.**
- **@Transactional 트랜잭션 적용**
  - **JPA의 모든 변경은 트랜잭션 안에서 이루어져야 한다.**
  - 스프링 데이터 JPA는 데이터를 변경(등록, 수정, 삭제)하는 메서드를 트랜잭션 처리한다.
  - 서비스 계층에서 트랜잭션을 시작하지 않으면 레포지토리에서 트랜잭션을 시작한다.
  - 서비스 계층에서 트랜잭션을 시작했으면 레포지토리도 해당 트랜잭션을 전파받아서 그대로 사용한다.
- **@Transactional(readOnly = true)**
  - 데이터를 단순히 조회만 하고 변경하지 않는 트랜잭션에서 이 옵션을 사용하면 플러시를 생략해서 약간의 성능 향상을 얻을 수 있다.

<br>

## 새로운 엔티티를 구별하는 방법
> 위 `SimpleJpaRepository`클래스 메서드 중에 `save`메서드를 보면 단순히 `em.persist()`만 하는 것이 아닌 조건에 따라 다르게 동작하는 걸 볼 수 있다.
> `save()`메서드에 전략을 알아보자.

간단히 말하면
- 저장할 엔티티가 새로운 엔티티면 저장(`persist`)
- 새로운 엔티티가 아니면 [병합](https://github.com/genesis12345678/TIL/blob/main/Spring/jpa/persistenceContext/flush_detach/flush_detach.md#%EB%B3%91%ED%95%A9-merge)(`merge`)

새로운 엔티티를 판단하는 기본 전략
- 식별자가 객체일 때 `null`로 판단
- 식별자가 자바 기본 타입일 때 `0`으로 판단
- `Persistable` 인터페이스를 구현해서 판단 로직을 변경할 수 있다.

예제
```java
@Entity
public class Item{
    @Id @GeneratedValue
    private Long id;
}

@Test
void save() {
    Item item = new Item();
    itemRepository.save(item);
    
}
```
`save()`메서드에 브레이킹 포인트를 찍고 디버깅 모드로 해보면 새로운 엔티티라 판단하고 `em.persist()`를 호출한다.

왜냐하면 현재 JPA 식별자 생성 전략이 `@GeneratedValue`으로 되어있다. `@GeneratedValue`는 `em.persist()`를 호출한 후에 값이 생성되기 때문에 `save()`를
호출한 시점에는 식별자가 `null`이다.(Long은 객체이다.) 따라서 새로운 엔티티라 판단하고 `em.persist()`를 호출하는 것이다.

**`@Id`만 사용한다면?**
```java
@Entity
@NoArgsConstructor
public class Item{
    @Id private String id;
    
    public Item(String id) {
        this.id = id;
    }
}

@Test
void save() {
    Item item = new Item("A");
    itemRepository.save(item);
}
```
`@Id`만 사용하고 식별자를 직접 할당했다. 이미 식별자 값이 있는 상태로 `save()`를 호출한다.

이 때는 식별자가 `null`이 아니므로 `em.merge()`를 호출한다. `em.merge()`는 **우선 DB를 호출해서 값을 확인하고,** DB에 값이 없으면 새로운 엔티티로 인지하기 때문에
매우 비효율적이다.(쿼리를 두 번 실행한다.)

**`merge`는 최대한 일어나지 않게 하고 데이터 변경은 `변경 감지`로 일어나야 한다.**

`@GeneratedValue`를 사용하면 이것을 고민할 필요는 없지만 **만약 `@GeneratedValue`를 사용하지 않는다면 어떻게 해야할까?**

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String>{

    @Id
    private String id;

    public Item(String id) {
        this.id = id;
    }

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
```
`Persistable` 인터페이스의 `isNew()`를 구현하면 된다. `@CreatedDate`과 조합해서 사용하면 편리하게 새로운 엔티티 여부를 확인할 수 있다.

`@CreatedDate`는 `em.persist()`호출 후에 값이 할당되기 때문에 `save()`를 호출한 시점에는 값이 `null`이 된다. 따라서 새로운 엔티티라고 판단하고 `em.persis()`를 호출한다.