# 로그 추적기 - V1

```java
@Slf4j
@Component
public class HelloTraceV1 {

    public static final String START_PREFIX = "-->";
    public static final String COMPLETE_PREFIX = "<--";
    public static final String EX_PREFIX = "<X-";

    //로그 시작, 로그 메시지를 파라미터로 받는다.
    //현재 로그 상태를 반환한다.
    public TraceStatus begin(String message) {
        TraceId traceId = new TraceId();//생성할 때 UUID(트랜잭션 ID)와 level=0으로 초기화
        Long startTimeMs = System.currentTimeMillis();//프로세스 실행 시작 시간
        log.info("[{}] {}{}",
                traceId.getId(),//현재 트랜잭션 ID
                addSpace(START_PREFIX, traceId.getLevel()),
                message);

        return new TraceStatus(traceId, startTimeMs, message);
    }

    //로그 정상 종료, 정상 흐름에서 호출한다.
    public void end(TraceStatus status) {
        complete(status, null);
    }

    //로그 예외 종료, 예외 발생 시 호출한다.
    public void exception(TraceStatus status, Exception e) {
        complete(status, e);
    }

    //로그 종료 공통 처리 로직
    private void complete(TraceStatus status, Exception e) {
        Long stopTimeMs = System.currentTimeMillis();
        long resultTimeMs = stopTimeMs - status.getStartTimeMs();//status가 시작 시간을 알고 있다.

        TraceId traceId = status.getTraceId();
        if (e == null) { //예외가 발생하지 않았을 때
            log.info("[{}] {}{} time={}ms",
                    traceId.getId(),
                    addSpace(COMPLETE_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs
                    );

        } else { //예외가 발생했을 때
            log.info("[{}] {}{} time={}ms ex={}",
                    traceId.getId(),
                    addSpace(EX_PREFIX, traceId.getLevel()),
                    status.getMessage(),
                    resultTimeMs,
                    e.getMessage()//예외 출력
                    );
        }
    }

    //level=0
    //level=1 |-->
    //level=2 |   |-->

    //level=2 ex |   |<x-
    //level=1 ex |<x-
    private String addSpace(String prefix, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append((i == level - 1) ? "|" + prefix : "|    ");
        }
        return sb.toString();
    }
}
```

```java
@RestController
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderServiceV1 orderService;
    private final HelloTraceV1 trace;

    @GetMapping("/v1/request")
    public String request(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);

            return "OK";
        } catch (Exception e) {
            trace.exception(status, e);
            throw e;//예외를 다시 던져주어야 한다. 로그 때문에 예외가 처리되면 안 된다.
        }
    }
}
```
- HelloTrace1은 컴포넌트 스캔의 대상이 되기 때문에 의존관계 주입이 가능하다.
- 어떤 메서드가 호출되었는지 수작업으로 적어준다.

```java
@Service
@RequiredArgsConstructor
public class OrderServiceV1 {
    private final OrderRepositoryV1 orderRepository;
    private final HelloTraceV1 trace;

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
public class OrderRepositoryV1 {

    private final HelloTraceV1 trace;

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

> V1의 문제점
> 
> 비즈니스 로직의 각 메서드가 따로따로 `begin`을 호출하기 때문에 같은 HTTP 요청이 오더라도 다른 트랜잭션 ID(UUID)가 생성이 되고 깊이도 따로따로 생긴다.
> 동기화 해주는 작업이 필요하다.