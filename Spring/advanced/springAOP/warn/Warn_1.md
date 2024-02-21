# 스프링 AOP 주의사항

## 프록시 내부 호출

스프링은 프록시 방식의 AOP를 사용한다.<br>
AOP를 적용하려면 항상 프록시를 통해서 대상 객체(`Target`)를 호출해야 한다. 이렇게 해야 프록시에서 먼저 어드바이스를 호출하고, 이후에 대상 객체를 호출한다.<br>
만약 프록시를 거치지 않고 대상 객체를 직접 호출하게 되면 AOP가 적용되지 않고 어드바이스도 호출되지 않는다.

**AOP를 적용하면 스프링은 대상 객체 대신에 프록시를 스프링 빈으로 등록한다.** 따라서 스프링은 의존관계 주입 시에 항상 프록시 객체를 주입한다.

프록시 객체가 주입되기 때문에 대상 객체를 직접 호출하는 문제는 일반적으로 발생하지 않는다. **하지만 대상 객체의 내부에서 메서드 호출이 발생하면 프록시를
거치지 않고 대상 객체를 직접 호출하는 문제가 발생한다.**

### 예제
```java
@Slf4j
@Component
public class CallServiceV0 {

    public void external() {
        log.info("call external");
        internal(); //내부 메서드 호출(this.internal())
    }

    public void internal() {
        log.info("call internal");
    }
}
```
```java
@Slf4j
@Aspect
public class CallLogAspect {

    @Before("execution(* hello.aop.internalcall..*.*(..))")
    public void doLog(JoinPoint joinPoint) {
        log.info("aop={}", joinPoint.getSignature());
    }
}
```
```java
@Slf4j
@Import(CallLogAspect.class)
@SpringBootTest
class CallServiceV0Test {

    @Autowired CallServiceV0 callService;//프록시

    @Test
    void isProxy() {
        assertThat(AopUtils.isAopProxy(callService)).isTrue();
    }

    @Test
    void external() {
        callService.external();
    }

    @Test
    void internal() {
        callService.internal();
    }
}
```
```text
## external() 실행 결과

aop=void hello.aop.internalcall.CallServiceV0.external()
call external
call internal

## internal() 실행 결과

aop=void hello.aop.internalcall.CallServiceV0.internal()
call internal
```
**`external()` 실행 결과가 중요하다.**

![img.png](img.png)

- 실행 결과를 보면 `external()`을 실행할 때는 프록시를 호출한다. `CallLogAspect`어드바이스가 호출된 것이다.
- 그리고 AOP Proxy는 `target.internal()`을 호출한다.
- 여기서 문제는 `external()`안에서 `internal()`을 호출할 때 발생한다. 이때는 `CallLogAspect`어드바이스가 호출되지 않는다.

자바 언어에서 메서드 앞에 별도의 참조가 없으면 `this`라는 뜻으로 자기 자신의 인스턴스를 가리킨다. 이떄 `this`는 실제 대상 객체(`target`)의 인스턴스를 뜻한다.
**결과적으로 이러한 내부 호출은 프록시를 거치지 않기 때문에 어드바이스도 적용할 수 없는 것이다.**

![img_1.png](img_1.png)

- 외부에서 호출하는 경우 프록시를 거치지 때문에 `internal()`도 `CallLogAsepct`어드바이스가 적용이 된다.

## 프록시 내부 호출 - 대안 1

### 자기 자신 주입

내부 호출을 해결하는 가장 간단한 방법은 자기 자신을 의존관계 주입 받는 것이다.

```java
/**
 * 생성자 주입은 순환 사이클을 만들기 때문에 실패한다.
 */
@Slf4j
@Component
public class CallServiceV1 {
    
    private CallServiceV1 callServiceV1;//프록시

    // setter 주입
    @Autowired
    public void setCallServiceV1(CallServiceV1 callServiceV1) {
        //callServiceV1 setter=class hello.aop.internalcall.CallServiceV1$$SpringCGLIB$$0
        log.info("callServiceV1 setter={}", callServiceV1.getClass());
        this.callServiceV1 = callServiceV1;
    }

    public void external() {
        log.info("call external");
        callServiceV1.internal(); //외부 메서드 호출
    }

    public void internal() {
        log.info("call internal");
    }
}
```
- 수정자를 통해서 주입을 받았다. 스프링에서 AOP가 적용된 대상을 의존관계 주입 받으면 주입 받은 대상은 실제 자신이 아니라 프록시 객체가 된다.
- 생성자 주입은 본인을 생성하면서 주입해야 하기 때문에 순환 사이클이 만들어진다. 수정자 주입은 스프링이 생성된 이후에 주입할 수 있기 때문에 오류가 발생하지 않는다.

```java
@SpringBootTest
@Import(CallLogAspect.class)
class CallServiceV1Test {

    @Autowired CallServiceV1 callServiceV1;

    @Test
    void external() {
        callServiceV1.external();
    }
}
```
```text
## 실행 결과

aop=void hello.aop.internalcall.CallServiceV1.external()
call external
aop=void hello.aop.internalcall.CallServiceV1.internal()
call internal
```

![img_2.png](img_2.png)

- `internal`을 호출할 때 자기 자신의 인스턴스를 호출하는 것이 아니라 프록시 인스턴스를 통해서 호출하기 때문에 AOP가 잘 적용된다.

## 프록시 내부 호출 - 대안 2

### 지연 조회

자기 자신 주입에서 생성자 주입이 실패하는 이유는 자기 자신을 생성하면서 주입해야 하기 때문이다. 이 경우 수정자 주입을 사용하거나 지연 조회를 사용하면 된다.

스프링 빈을 지연해서 조회하면 되는데 `ObjectProvider`, `ApplicationContext`를 사용하면 된다.

```java
/**
 * ObjectProvider(Provider), ApplicationContext를 사용해서 지연(LAZY) 조회
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV2 {

//    private final ApplicationContext ac;
    private final ObjectProvider<CallServiceV2> callServiceProvider;

    public void external() {
        log.info("call external");
//        CallServiceV2 callServiceV2 = ac.getBean(CallServiceV2.class);
        CallServiceV2 callServiceV2 = callServiceProvider.getObject();
        callServiceV2.internal(); //외부 메서드 호출
    }

    public void internal() {
        log.info("call internal");
    }
}
```
- `ApplicationContext`는 너무 거대한 개념이고 너무 많은 기능을 제공한다.
- `ObjectProvider`는 객체를 스프링 컨테이너에서 조회하는 것을 스프링 빈 생성 시점이 아니라 실제 객체를 사용하는 시점으로 지연할 수 있다.
- `getObject()`를 호출하는 시점에 스프링 컨테이너에서 빈을 조회한다.

```java
@SpringBootTest
@Import(CallLogAspect.class)
class CallServiceV2Test {

    @Autowired CallServiceV2 callServiceV2;

    @Test
    void external() {
        callServiceV2.external();
    }
}
```
```text
## 실행 결과

aop=void hello.aop.internalcall.CallServiceV2.external()
call external
aop=void hello.aop.internalcall.CallServiceV2.internal()
call internal
```

## 프록시 내부 호출 - 대안 3

### 구조 변경

프록시 내부 호출 문제를 해결하기 위해 자기 자신을 주입하거나 `Provider`를 사용했는데 가장 깔끔한 대안은 내부 호출이 발생하지 않도록 
구조 자체를 변경하는 것이다. 가장 권장하는 방법이다.

```java
@Slf4j
@Component
public class InternalService {

    public void internal() {
        log.info("call internal");
    }
}

/**
 * 구조를 변경(분리)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CallServiceV3 {

    private final InternalService internalService;

    public void external() {
        log.info("call external");
        internalService.internal(); //외부 메서드 호출
    }
}
```
```java
@SpringBootTest
@Import(CallLogAspect.class)
class CallServiceV3Test {

    @Autowired CallServiceV3 callServiceV3;

    @Test
    void external() {
        callServiceV3.external();
    }
}
```
```text
## 실행 결과

aop=void hello.aop.internalcall.CallServiceV3.external()
call external
aop=void hello.aop.internalcall.InternalService.internal()
call internal
```

![img_3.png](img_3.png)