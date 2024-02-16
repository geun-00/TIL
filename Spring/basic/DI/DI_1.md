# 스프링 의존관계 자동 주입 - 1

## 다양한 의존관계 주입 방법

### 생성자 주입
- 이름 그대로 생성자를 통해서 의존 관계를 주입 받는 방법
- 특징
  - 생성자 호출시점에 딱 1번만 호출되는 것이 보장된다.
  - **불변, 필수** 의존관계에 사용

```java
@Component
public class OrderServiceImpl implements OrderService {
    
    private final MemberRepository memberRepository; 
    private final DiscountPolicy discountPolicy;
    
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository; 
        this.discountPolicy = discountPolicy;
    } 
}
```
**생성자가 딱 1개만 있으면 `@Autowired`를 생략할 수 있다.(스프링 빈에만 해당)**

### 수정자 주입(setter)
- `setter`를 통해 의존관계를 주입한다.
- 특징
  - **선택, 변경** 가능성이 있는 의존관계에 사용
  - 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법

```java
@Component
public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository; 
    private DiscountPolicy discountPolicy;

    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
}
```
- `@Autowired`의 기본 동작은 주입할 대상이 없으면 오류가 발생하는데 주입할 대상이 없어도 동작하게 하려면 `@Autowired(required = false)`로 지정하면 된다.

### 필드 주입
- 이름 그대로 필드에 바로 주입하는 방법이다.
- 특징
  - 코드가 간결해서 편리해 보이지만 외부에서 변경이 불가능해서 테스트 하기 힘들다.
  - DI 프레임워크가 없으면 아무것도 할 수 없다.
  - **사용하지 말자**
    - 애플리케이션의 실제 코드와 관계 없는 테스트 코드나 스프링 설정을 목적으로 하는 `@Configuration`같은 곳에서만 특별한 용도로 사용해야 한다.
```java
@Component
public class OrderServiceImpl implements OrderService { 
    @Autowired private MemberRepository memberRepository; 
    @Autowired private DiscountPolicy discountPolicy; 
}
```
- 순수한 자바 테스트 코드에는 `@Autowired`가 동작하지 않고 `@SpringBootTest`처럼 스프링 컨테이너를 테스트에 통합한 경우에만 가능하다.

### 일반 메서드 주입
- 일반 메서드를 통해서 의존 관계를 주입 받는다.
- 특징
  - 한 번에 여러 필드를 주입 받을 수 있다.
  - 잘 사용하지는 않는다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository; 
    private DiscountPolicy discountPolicy;
    
    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

<br>

## 생성자 주입을 사용하자

최근에는 스프링을 포함한 DI 프레임워크 대부분이 **생성자 주입**을 권장한다.

**불변**
- 대부분의 의존관계는 애플리케이션 종료 전까지 변하면 안 된다.(불변해야 한다.)
- 수정자 주입은 `setter`를 public으로 열어두어야 하기 때문에 누군가 실수로 변경할 수 있다. 좋은 설계가 아니다.
- 생성자 주입은 객체를 생성할 때 딱 1번만 호출되므로 이후에 호출되는 일이 없다. 불변한 설계가 가능하다.

**누락**
- 프레임워크 없이 순수한 자바 코드로 단위 테스트를 수행할 때 수정자 주입(setter)은 의존관계 주입이 누락되어도 실행은 된다. (런타임 오류)
- 생성자 주입을 사용하면 의존관계 주입이 누락됐을 때 **컴파일 오류**가 발생한다. IDE에서 어떤 값을 필수로 주입해야 하는지 알려준다.
  - 생성자 주입 방식은 `final`을 사용할 수 있어 생성자에서 값이 설정되지 않은 오류를 컴파일 시점에 막아준다.

### @RequiredArgsConstructor
> 생성자 의존 관계 주입 방법 사용 시 생성자가 딱 1개면 `@Autowired`를 생략할 수 있다. 여기서 롬복의 `@RequiredArgsConstructor`를 클래스에 적용하면
> 생성자 코드를 자동으로 생성해준다. `@RequiredArgsConstructor`는 `final`이 붙은 필드를 모아서 생성자를 만들어준다.