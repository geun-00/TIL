# 로그 추적기 - V3

```java
public interface LogTrace {
    TraceStatus begin(String message);
    void end(TraceStatus status);
    void exception(TraceStatus status, Exception e);
}

@Slf4j
public class FieldLogTrace implements LogTrace {
    public static final String START_PREFIX = "-->";
    public static final String COMPLETE_PREFIX = "<--";
    public static final String EX_PREFIX = "<X-";

    private TraceId traceHolder; // traceId 동기화, 동시성 이슈 발생

    @Override
    public TraceStatus begin(String message) {
        syncTraceId();
        TraceId traceId = traceHolder;
        Long startTimeMs = System.currentTimeMillis();
        log.info("[{}] {}{}",
                traceId.getId(),
                addSpace(START_PREFIX, traceId.getLevel()),
                message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    private void syncTraceId() {
        if (traceHolder == null) {//최초 호출이면 새로 생성(시작 로그)
            traceHolder = new TraceId();
        } else {//직전 로그가 있으면 해당 로그의 TraceId를 참고해서 동기화하고 레벨도 하나 증가
            traceHolder = traceHolder.createNextId();
        }
    }

    @Override
    public void end(TraceStatus status) {
        complete(status, null);
    }

    @Override
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();

        TraceId traceId = status.getTraceId();
        if (e == null) {
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs);
        } else {
            log.info("[{}] {}{} time={}ms ex={}" ,
                    traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs,
                    e.toString());
        }
        releaseTraceId();
    }

    private void releaseTraceId() {
        if (traceHolder.isFirstLevel()) {//최초 호출이면 내부에서 관리하는 TraceId를 제거한다.
            traceHolder = null; //destroy
        } else {//메서드 호출이 끝나면 레벨을 하나 감소한다.
            traceHolder = traceHolder.createPreviousId();
        }
    }

    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|    ");
        }
        return sb.toString();
    }
}
```
- `TraceId`를 동기화 할 때 파라미터를 넘기는 것이 아닌 필드를 사용한다. 직전 로그는 파라미터로 전달되지 않고 필드에 저장된다.

```java
@Configuration
public class LogTraceConfig { 
    
    @Bean
    public LogTrace logTrace() { 
        return new FieldLogTrace();
    } 
}
```

```java
@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {
    private final OrderServiceV3 orderService;
    private final LogTrace trace;

    @GetMapping("/v3/request")
    public String request(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);

            return "OK";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {
    private final OrderRepositoryV3 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);

        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
    }
}

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV3 {

    private final LogTrace trace;

    public void save(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderRepository.save()");
            // 저장 로직
            if (itemId.equals("ex")) {
                throw new IllegalStateException("예외 발생!");
            }
            sleep();
            trace.end(status);

        } catch (Exception e) {
            trace.exception(status, e);
            throw e;
        }
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

> V3 문제점
> 
> 동시에 여러 사용자가 요청하면 여러 쓰레드가 동시에 애플리케이션 로직을 호출한다. 이때 여러 로그가 섞여서 나올텐데 이때 동시성 문제가 발생한다.
> 
> `FieldLogTrace`는 싱글톤으로 등록된 스프링 빈이다. 하나만 있는 인스턴스의 `traceHolder` 필드를 여러 쓰레드가 동시에 접근한다.<br>
> 그래서 요청을 전혀 구분하지 못하고 요청이 오는 대로 로그가 누적 되서 출력이 된다.