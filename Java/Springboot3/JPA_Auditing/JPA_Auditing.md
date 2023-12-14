# JPA Auditing

<br>

> 엔티티에는 해당 데이터의 생성시간과 수정시간 컬럼을 포함하는 것이 좋다.
> 왜냐하면 언제 만들어졌는지와 언제 수정되었는지 등의 정보는 유지보수에 있어
> 중요한 정보이며 데이터 분석에 유용할 수 있기 때문이다.

**하지만 모든 엔티티마다 생성시간과 수정시간 컬럼을 넣는다면 상당히
비효율적이고 객체지향적이지 못할 것이다.**<br>
**이러한 문제를 ``JPA Auditing``을 사용하여 쉽게 구현할 수 있다.

<br>

## BaseTimeEntity 생성

BaseTimeEntity란 추상 클래스를 생성해 엔티티에 상위 클래스로 지정해준다.

```java
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

- **@MappedSuperclass** : 엔티티 클래스들이 이 클래스를 상속할 경우 필드들을 컬럼으로
인식하도록 한다.(createdAt과 updatedAt)
- **@EntityListeners(AuditingEntityListener.class)** : 엔티티에 대한 이벤트 리스너 클래스를 등록하는데 사용된다.
이 어노테이션으로 상속받은 클래스에서는 엔티티가 생성, 수정이 될 때 자동으로 갱신이 가능하다.
- **@CreatedDate, @LastModifiedDate** : 엔티티가 언제 생성되었는지, 언제 마지막으로 수정되었는지를 의미한다.

<br>

## BaseTimeEntity 사용

```java
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Builder
@AllArgsConstructor
@DynamicUpdate
@DynamicInsert
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    /**
     * 생략
     */
}
```

++
- **@DynamicUpdate**와 **@DynamicInsert** : 변경되는 컬럼만 포함해 쿼리를 생성한다.
  
> 언뜻 보면 불필요한 쿼리를 생성하지 않기 때문에 효율적일수 있지만 무조건 그렇지는 않다.
> JPA의 구현체인 hibernate는 기본적으로 엔티티를 수정할 때 모든 필드를 수정한다. 컬럼 하나에 null값이 등록이 되거나 수정되지 않아도
해당 컬럼을 포함한 쿼리가 생성되는 것이다. <br>
> hibernate는 모든 필드 사용 시 insert, update 쿼리가 항상 같기 때문에 쿼리를 미리 생성해두고
> 재사용이 가능하다.<br>
> 하지만 위 어노테이션을 사용하면 엔티티를 업데이트할 때마다 새로운 쿼리를 생성하기 때문에 쿼리 실행 
> 속도를 저하시킬 수 있다.

**그렇다면 언제 @DynamicUpdate와 @DynamicInsert를 사용하는 것이 좋을까?**
- 필드(컬럼)가 많은 경우
- 몇 개의 필드만 자주 수정하는 경우

<br>

## @EnableJpaAuditing
<br>

```java
@SpringBootApplication
@EnableJpaAuditing
public class Project3Application {
    public static void main(String[] args) {
        SpringApplication.run(Project3Application.class, args);
    }

}
```

@SpringbootApplication에 ``@EnableJpaAuditing``을 추가해준다. 

<br>

## 생성, 수정 후 DB 확인

생성

![img.png](img.png)

수정

![img_1.png](img_1.png)