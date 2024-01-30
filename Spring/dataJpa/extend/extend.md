# 확장 기능

## 사용자 정의 레포지토리 구현
> 스프링 데이터 JPA로 레포지토리를 개발하면 인터페이스만 정의하고 구현체는 스프링이 자동생성 해준다. 하지만 다양한 이유로 메서드를 직접 구현해야 할 때가 있는데
> 스프링 데이터 JPA가 제공하는 인터페이스를 직접 구현하면 공통 인터페이스가 제공하는 기능까지 모두 구현을 해야 한다.
> 
> 스프링 데이터 JPA는 이런 문제를 우회해서 필요한 메서드만 구현할 수 있는 방법을 제공한다.

먼저 직접 구현할 메서드를 위한 사용자 정의 인터페이스를 작성해야 한다.
```java
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
```
인터페이스 이름은 자유롭게 지으면 된다.

다음으로 사용자 정의 인터페이스를 구현한 클래스를 작성해야 한다.
```java
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final EntityManager em;
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
```
순수한 JPA를 사용하는 레포지토리다.

이때 클래스 이름을 만드는 규칙이 있다. `레포지토리 이름 + Impl`로 지어야 한다. 이렇게 하면 스프링 데이터 JPA가 사용자 정의 구현 클래스로 인식한다.

마지막으로 사용자 정의 인터페이스를 상속받으면 된다.
```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    ...
}
```

클래스 이름 규칙을 `Impl`대신 다른 이름을 붙이고 싶을 때는 `JavaConfig`에 직접 설정할 수도 있다.
```java
@EnableJpaRepositories(basePackages = "study.datajpa.repository", 
                       repositoryImplementationPostfix = "Impl")
```

> Querydsl이나 SpringJdbcTemplate을 함께 사용할 때 사용자 정의 레포지토리가 유용하다.
> 
> **항상 사용자 정의 레포지토리를 구현할 필요는 없고 그냥 임의의 레포지토리를 만들어도 된다.** 인터페이스가 아닌 클래스로 만들고 스프링 빈으로 등록해서
> 직접 사용해도 된다. 

**최근에는 다른 방법도 지원한다.** 사용자 정의 구현 클래스에 레포지토리 인터페이스 이름 + `Impl` 대신에

사용자 정의 인터페이스 명 + `Impl` 방식도 지원한다. `MemberRepositoryImpl`대신 `MemberRepositoryCustomImpl`같이 구현해도 된다.

```java
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private final EntityManager em;
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
}
```

**이 방식이 기존 방식보다 사용자 정의 인터페이스 이름과 구현 클래스 이름이 비슷하므로 더 직관적이다.** 추가로 여러 인터페이스를 분리해서 구현하는 것도 가능하다.

<br>

## Auditing
> 엔티티를 수정, 변경할 때 변경한 사람과 시간을 추적할 수 있다.

### 순수 JPA
```java
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdDate;// 등록일
    private LocalDateTime updatedDate;// 수정일

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}

@Entity
public class Member extends JpaBaseEntity {
    ...
}
```

테스트 코드
```java
@Test
void JpaEventBaseEntity() throws InterruptedException {
    Member member = new Member("member1");
    memberRepository.save(member); //@PrePersist 발생
        
    Thread.sleep(1000);
    member.setUsername("member2");
    
    em.flush(); //@PreUpdate 발생
    em.clear();
    
    Member findMember = memberRepository.findById(member.getId()).get();
    assertThat(findMember.getCreatedDate()).isBefore(findMember.getLastModifiedDate());
}
```
JPA 주요 이벤트 어노테이션
- `@PrePersist`, `@PostPersist`
- `@PreUpdate`, `@PostUpdate`

### 스프링 데이터 JPA

스프링 부트 설정 클래스에 `@EnableJpaAuditing` 적용
```java
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {...}
```

BaseEntity
```java
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;// 등록일
    
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;// 수정일
    
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;// 등록자
    
    @LastModifiedBy
    private String lastModifiedBy;// 수정자
}
```
등록자, 수정자 이벤트를 사용하려면 등록자, 사용자를 처리해주는 `AuditorAware`를 스프링 빈으로 등록해야 한다.
```java
@EnableJpaAuditing
@SpringBootApplication
public class DataJpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}
	@Bean
	public AuditorAware<String> auditorAware() {
		return () -> Optional.of(UUID.randomUUID().toString());
	}
}
```
**UUID보다는 세션 정보나 스프링 시큐리티 로그인 정보에서 ID를 받아서 사용하는 것이 좋다.**

등록일, 수정일은 필요하지만 등록자, 수정자는 필요 없을 수도 있다. 이럴 때는 Base 타입을 분리하고 원하는 타입을 선택해서 상속할 수도 있다.
```java
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;
}

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
```
등록일, 수정일은 기본적으로 자주 쓰이니 `BaseEntity`가 `BaseTimeEntity`를 상속받아서 사용할 수 있다.

<br>

## Web 확장
> 스프링 데이터 프로젝트는 스프링 MVC에서 사용할 수 있는 편리한 기능을 제공한다.
> 
> 식별자로 도메인 클래스를 바로 바인딩 해주는 `도메인 클래스 컨버터 기능`과 `페이징과 정렬 기능`을 제공한다.

### 도메인 클래스 컨버터 기능
> HTTP 파라미터로 넘어온 엔티티의 아이디로 엔티티 객체를 찾아서 바인딩 해준다.

- 도메인 클래스 컨버터 사용 전
```java
@GetMapping("/members/{id}")
public String findMember(@PathVariable("id") Long id) {
    Member member = memberRepository.findById(id).get();
    return member.getUsername();
}
```

- 도메인 클래스 컨버터 사용 후
```java
@GetMapping("/members/{id}")
public String findMember2(@PathVariable("id") Member member) {
    return member.getUsername();
}
```
- HTTP 요청은 회원 `Id`를 받지만 도메인 클래스 컨버터가 중간에 동작해서 회원 엔티티 객체를 반환한다.
- 도메인 클래스 컨버터도 레포지토리를 사용해서 엔티티를 찾는다.

> **도메인 클래스 컨버터로 엔티티를 파라미터로 받으면 이 엔티티는 단순 조회용으로만 사용해야 한다.**<br>
> 트랜잭션이 없는 범위에서 엔티티를 조회했으므로 엔티티를 변경해도 DB에 반영되지 않는다.

### 페이징과 정렬 기능
```java
@GetMapping("/members")
public Page<Member> list(Pageable pageable) {
    Page<Member> page = memberRepository.findAll(pageable);
    return page;
}
```
파라미터로 `Pageable`을 받을 수 있다. 이는 인터페이스고 다음 요청 파라미터 정보로 `PageRequest`객체를 생성해준다.
- page : 현재 페이지, **`0`부터 시작**
- size : 한 페이지에 노출할 데이터 건수
- sort : 정렬 조건을 정의한다.
- `/members?page=0&size=3&sort=id,desc&sort=username,desc`
  - `id`와 `username`을 내림차순으로 첫 번째 페이지를 반환한다.
  - `sort`는 `asc`가 기본값이기 때문에 `asc`는 생략 가능하다.

`Pageable`의 기본값은 `page=0`, `size=20`이다. 변경 가능하다.
- 글로벌 설정
```properties
spring.data.web.pageable.default-page-size=20 //기본 페이지 사이즈
spring.data.web.pageable.max-page-size=2000 //최대 페이지 사이즈
```

- 개별 설정
```java
@GetMapping("/members")
public Page<Member> list(@PageableDefault(size = 5, sort = "username", direction = Sort.Direction.DESC) Pageable pageable) {
    Page<Member> page = memberRepository.findAll(pageable);
    return page;
}
```

**접두사**<br>
페이징 정보가 둘 이상이면 접두사를 사용해서 구분할 수 있다. `@Qualifier`에 접두사명을 추가하고 `{접두사명}_`으로 구분한다.
```java
public String list(
    @Qualifier("member") Pageable memberPageable, 
    @Qualifier("order") Pageable orderPageable, ...
```
URL : `/members?member_page=0&order_page=1`

**Page 내용을 DTO로 변환**<br>
지금처럼 API로 엔티티를 직접 노출하면 좋지 않기 때문에 DTO로 변환해야 한다. Page는 `map()`을 지원해서 내부 데이터를 변경할 수 있다.
```java
@GetMapping("/members")
public Page<MemberDto> list(@PageableDefault(size = 5, sort = "username") Pageable pageable) {
    Page<Member> page = memberRepository.findAll(pageable);
    return page.map(MemberDto::new);
}

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Member member) {
        id = member.getId();
        username = member.getUsername();
    }
}
```