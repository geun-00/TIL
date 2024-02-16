# 스프링 의존관계 자동 주입 - 2

## 조회한 빈이 2개 이상일 때

`@Autowired`는 기본적으로 타입(Type)으로 조회한다.

```java
@Component
public class FixDiscountPolicy implements DiscountPolicy {}

@Component
public class RateDiscountPolicy implements DiscountPolicy {}

@Autowired
private DiscountPolicy discountPolicy
```
`DiscountPolicy` 인터페이스의 하위 타입 2개를 둘 다 스프링 빈으로 등록했다.<br>
`NoUniqueBeanDefinitionException`오류가 발생한다.

스프링 빈을 수동 등록해서 문제를 해결할 수도 있지만 의존 관계 자동 주입에서 해결할 수 있는 여러 방법이 있다.

### @Autowired 필드명
```java
@Autowired 
private DiscountPolicy rateDiscountPolicy
```
필드명을 스프링 빈 이름으로 변경했다.

- 1차적으로 타입 매칭을 시도한다.
- 이 때 여러 빈이 있으면 필드 이름, 파라미터 이름으로 빈 이름을 추가 매칭한다.

### @Qualifier

추가 구분자를 붙여주는 방법이다.

```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy {}

@Autowired
public OrderServiceImpl(MemberRepository memberRepository,
                        @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```
빈 등록 시에 `@Qualifier`를 붙여 주고 이름을 부여해준다. 의존 관계 주입 시에는 `@Qualifier`를 붙여주고 등록한 이름을 적어준다.

### @Primary

우선순위를 정하는 방법이다. @Autowired 시에 여러 빈이 매칭되면 `@Primary`가 우선권을 가진다.

```java
@Component 
@Primary
public class RateDiscountPolicy implements DiscountPolicy {} 

@Component
public class FixDiscountPolicy implements DiscountPolicy {}
```

이렇게 하면 `DiscountPolicy`를 의존 관계 주입 받는 코드에서는 `RateDiscountPolicy`가 주입이 된다.

**우선순위**
- `@Primary`보다 `@Qualifier`가 더 상세하기 때문에 `@Qualifier`가 더 높은 우선권을 가진다.

### 어노테이션 직접 만들기
`@Quailfier`는 문자를 직접 적기 때문에 컴파일 시 타입 체크가 안 된다.

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}

@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy{ ... }

@Autowired
public OrderServiceImpl(MemberRepository memberRepository,
                        @MainDiscountPolicy DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```
어노테이션에는 상속이라는 개념은 없고 이렇게 여러 어노테이션을 모아서 사용하는 기능은 스프링이 지원해주는 기능이다.