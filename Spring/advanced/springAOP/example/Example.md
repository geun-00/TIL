# 스프링 AOP 실전 예제

```java
@Repository
public class ExamRepository {

    private static int seq = 0;

    /**
     * 5번에 1번 실패하는 요청
     */
    public String save(String itemId) {
        seq++;
        if (seq % 5 == 0) {
            throw new IllegalStateException("예외 발생");
        }
        return "ok";
    }
}

@Service
@RequiredArgsConstructor
public class ExamService {
    private final ExamRepository repository;

    public void request(String itemId) {
        repository.save(itemId);
    }
}
```
```java
@Slf4j
@SpringBootTest
public class ExamTest {

    @Autowired ExamService service;

    @Test
    void test() {
        for (int i = 0; i < 5; i++) {
            log.info("client request i={}", i);
            service.request("data" + i);
        }
    }
}
```

## 로그 출력 AOP

`@Trace` 어노테이션이 메서드에 붙어 있으면 호출 정보가 출력되는 기능을 만들어보자.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Trace {
}

@Aspect
@Slf4j
public class TraceAspect {

    @Before("@annotation(hello.aop.exam.annotation.Trace)")
    public void doTrace(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("[trace] {} args={}", joinPoint.getSignature(), args);
    }
}
```

```java
/**
 * 5번에 1번 실패하는 요청
 */
@Trace //추가
public String save(String itemId) {
    seq++;
    if (seq % 5 == 0) {
        throw new IllegalStateException("예외 발생");
    }
    return "ok";
}
```
```java
@Trace //추가
public void request(String itemId) {
    repository.save(itemId);
}
```
```java
@Slf4j
@Import(TraceAspect.class)//추가
@SpringBootTest
public class ExamTest { ... }
```
```text
## 실행 결과

client request i=0
[trace] void hello.aop.exam.ExamService.request(String) args=[data0]
[trace] String hello.aop.exam.ExamRepository.save(String) args=[data0]
client request i=1
[trace] void hello.aop.exam.ExamService.request(String) args=[data1]
[trace] String hello.aop.exam.ExamRepository.save(String) args=[data1]
client request i=2
[trace] void hello.aop.exam.ExamService.request(String) args=[data2]
[trace] String hello.aop.exam.ExamRepository.save(String) args=[data2]
client request i=3
[trace] void hello.aop.exam.ExamService.request(String) args=[data3]
[trace] String hello.aop.exam.ExamRepository.save(String) args=[data3]
client request i=4
[trace] void hello.aop.exam.ExamService.request(String) args=[data4]
[trace] String hello.aop.exam.ExamRepository.save(String) args=[data4]
```

## 재시도 AOP

`@Retry` 어노테이션이 있으면 예외가 발생했을 때 다시 시도해서 문제를 복구할 수 있다.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    int value() default 3;
}

@Slf4j
@Aspect
public class RetryAspect {

@Around("@annotation(retry)")
public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
    log.info("[retry] {} retry={}", joinPoint.getSignature(), retry);

    int maxRetry = retry.value();
    Exception exceptionHolder = null;

    for (int i = 1; i <= maxRetry; i++) {
        try {
            log.info("[retry] try count={}/{}", i, maxRetry);
            return joinPoint.proceed();
        } catch (Exception e) {
            exceptionHolder = e;
        }
    }
    throw exceptionHolder;
    }
}
```
- 기본값 3은 재시도 횟수로 사용할 값이다.
- `@annotation(retry), Retry retry`를 사용해서 어드바이스에 어노테이션을 파라미터로 전달한다.
- 예외가 발생해서 결과가 정상 반환되지 않으면 `retry.value()`만큼 재시도를 한다.

```java
/**
 * 5번에 1번 실패하는 요청
 */
@Trace
@Retry(4)//추가
public String save(String itemId) {
    seq++;
    if (seq % 5 == 0) {
        throw new IllegalStateException("예외 발생");
    }
    return "ok";
}
```
```java
@Slf4j
//@Import(TraceAspect.class)
@Import({TraceAspect.class, RetryAspect.class})//추가
@SpringBootTest
public class ExamTest {

    @Autowired ExamService service;

    @Test
    void test() {
        for (int i = 0; i < 5; i++) {
            log.info("client request i={}", i);
            service.request("data" + i);
        }
    }
}
```
- 실행을 해보면 5번째에서 문제가 발생했을 때 재시도 덕분에 문제가 복구되고 정상 응답이 된다.

> **참고** : 스프링이 제공하는 `@Transactional`은 가장 대표적인 AOP 이다. 