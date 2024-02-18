# 로그 추적기 - V5

## 템플릿 콜백 패턴

```java
public interface Callback {
    void call();
}

@Slf4j
public class TimeLogTemplate {

    public void execute(Callback callback) {
        long startTime = System.currentTimeMillis();

        callback.call();

        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;

        log.info("resultTime = {}", resultTime);
    }
}
```

```java
/**
* 템플릿 콜백 패턴 - 익명 내부 클래스
*/
@Test
void callbackV1() {
    TimeLogTemplate template = new TimeLogTemplate();
    template.execute(new Callback() {
        @Override
        public void call() {
             log.info("비즈니스 로직1 실행");
        }
    });
    template.execute(new Callback() {
        @Override
        public void call() {
        log.info("비즈니스 로직2 실행");
        }
    });
}

/**
* 템플릿 콜백 패턴 - 람다
*/
@Test
void callbackV2() {
    TimeLogTemplate template = new TimeLogTemplate();
    template.execute(() -> log.info("비즈니스 로직1 실행"));
    template.execute(() -> log.info("비즈니스 로직2 실행"));
}
```

**콜백 정의**
- 프로그래밍에서 콜백 또는 콜 애프터 함수는 다른 코드의 인수로서 넘겨주는 실행 가능한 코드를 말한다. 콜백을 넘겨받는 코드는 이 콜백을 필요에 따라 
  즉시 실행할 수도 있고 나중에 실행할 수도 있다.
- `callback`은 코드가 호출(`call`)은 되는데 코드를 넘겨준 곳의 뒤(`back`)에서 실행된다는 뜻이다.

**자바에서 콜백**
- 자바에서 실행 가능한 코드를 인수로 넘기려면 객체가 필요하다. 자바8부터는 람다를 사용할 수 있다.

**템플릿 콜백 패턴**
- 템플릿 콜백 패턴은 GOF 패턴은 아니고 스프링 내부에서 이런 방식을 자주 사용하기 때문에 스프링 안에서만 이렇게 부른다. 전략 패턴에서 템플릿과 콜백 부분이
  강조된 패턴인 것이다.
- 스프링에서 이름에 `XxxTemplate`이 있다면 템플릿 콜백 패턴으로 만들어져 있는 것이다.
  - `JdbcTemplate`, `RedisTemplate`, `TransactionTemplate` 등..

![img.png](img.png)

## 템플릿 콜백 패턴 적용

```java
public interface TraceCallback<T> {
    T call();
}

@RequiredArgsConstructor
public class TraceTemplate {
    private final LogTrace trace;

    public <T> T execute(String message, TraceCallback<T> callback) {
        TraceStatus status = null;

        try {
            status = trace.begin(message);
            //로직 호출
            T result = callback.call();
            trace.end(status);

            return result;
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}

@Configuration
public class LogTraceConfig {

    @Bean
    public LogTrace logTrace() {
        return new ThreadLocalLogTrace();
    }

    @Bean
    public TraceTemplate traceTemplate() {
        return new TraceTemplate(logTrace());
    }
}
```

```java
@RestController
@RequiredArgsConstructor
public class OrderControllerV5 {
    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

//    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
//        this.orderService = orderService;
//        this.template = new TraceTemplate(trace);
//    }

    @GetMapping("/v5/request")
    public String request(String itemId) {

        return template.execute("OrderController.request()", () -> {
            orderService.orderItem(itemId);
            return "OK";
        });
    }
}
```
- `TraceTemplate`을 직접 스프링 빈으로 등록하지 않고 생성 시점에 의존관계 주입을 받을 수도 있다.

```java
@Service
@RequiredArgsConstructor
public class OrderServiceV5 {
    private final OrderRepositoryV5 orderRepository;
    private final TraceTemplate template;

    public void orderItem(String itemId) {
        template.execute("OrderService.orderItem()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }
}

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV5 {
    private final TraceTemplate template;

    public void save(String itemId) {
        template.execute("OrderRepository.save()", () -> {
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep();
            return null;
        });
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```