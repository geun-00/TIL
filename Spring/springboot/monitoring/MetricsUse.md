# 모니터링 메트릭 활용

CPU 사용량, 메모리 사용량, 톰캣 쓰레드, DB 커넥션 풀과 같이 공통으로 사용되는 기술 메트릭은 이미 구현되어 있다. 이렇게 이미 구현되어 등록되어 있는 메트릭을 사용해서
대시보드를 구성하고 모니터링 할 수 있다.

여기에 비즈니스에 특화된 부분을 모니터링 하고 싶으면 어떻게 해야할까? 예를 들어 주문 수, 취소 수, 재고 수량 같은 메트릭 같은 경우다. 이 부분은 공통으로
만들 수 있는 부분이 아니고 각각의 비즈니스에 특화된 부분들이다.

비즈니스에 관한 부분은 각 비즈니스 마다 구현이 다르기 때문에 비즈니스 메트릭은 직접 등록하고 확인해야 한다.

**예제 상황**
- 주문 수, 취소 수
  - 상품을 주문하면 주문 수가 증가한다.
  - 상품을 취소하면 주문 수는 유지하되, 취소 수를 증가한다.
- 재고 수량
  - 상품을 주문하면 재고 수량이 감소한다.
  - 상품을 취소하면 재고 수량이 증가한다.
  - 재고 물량이 들어오면 재고 수량이 증가한다.

주문 수, 취소 수는 계속 증가하므로 `카운터`를 사용하고, 재고 수량은 증가하거나 감소하므로 `게이지`를 사용한다.

```java
public interface OrderService {
    void order(); //주문
    void cancel(); //취소
    AtomicInteger getStock(); //재고 수량 확인
}
```
- `AtomicInteger` : 멀티쓰레드 환경에서 동시성을 보장하는 클래스
```java
@Slf4j
public class OrderServiceV0 implements OrderService {

    private AtomicInteger stock = new AtomicInteger(100);
    
    @Override
    public void order() {
      log.info("주문");
      stock.decrementAndGet();
    }

    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
```
- `new AtomicInteger(100);` : 초기값을 100으로 설정, 재고 수량이 100개로 시작한다고 가정한다.

```java
@Configuration
public class OrderConfigV0 {

    @Bean
    public OrderService orderService() {
        return new OrderServiceV0();
    }
}
```
```java
@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/order")
    public String order() {
        log.info("order");
        orderService.order();
        return "order";
    }

    @GetMapping("/cancel")
    public String cancel() {
        log.info("cancel");
        orderService.cancel();
        return "cancel";
    }

    @GetMapping("/stock")
    public int stock() {
        log.info("stock");
        return orderService.getStock().get();
    }
}
```
```java
@SpringBootApplication(scanBasePackages = "hello.controller")
@Import(OrderConfigV0.class)
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }
}
```
- 컨트롤러만 컴포넌트 스캔의 대상이 되도록 했다.
- 이후 여러 버전에 따라서 `@Import`를 수정하면서 하면 된다.

### [카운터]()
- [V1]()
- [V2]()
### [타이머]()
- [V1]()
- [V2]()

## 정리

메트릭은 100% 정확한 숫자를 보는데 사용하는 것이 아니다. 약간의 오차를 감안하고 실시간으로 대략의 데이터를 보는 목적으로 사용한다.

**마이크로미터 핵심 기능**
- `Counter`, `Timer`, `Gauge`, `Tags`

**MeterRegistry**
- 마이크로미터 기능을 제공하는 핵심 컴토넌트
- 스프링을 통해서 주입 받아서 사용하고 이곳을 통해서 카운터, 게이지 등을 등록한다.

**[Counter](https://prometheus.io/docs/concepts/metric_types/#counter)**
- 단조롭게 증가하는 단일 누적 측정항목
  - 단일 값이고, 보통 하나씩 증가한다.
  - 누적이므로 전체 값을 포함한다.(`total`)
  - 프로메테우스에서는 일반적으로 카운터의 이름 마지막에 `_total`을 붙여서 표현한다.
- 값을 증가하거나 0으로 초기화 하는 것만 가능하다.
- 마이크로미터에서 값을 감소하는 기능도 지원하지만 목적에 맞지 않는다.
- 예) HTTP 요청 수

**Timer**
- `Timer`는 조금 특별한 메트릭 측정 도구인데, 시간을 측정하는데 사용된다.
- 카운터와 유사한데 `Timer`를 사용하면 실행 시간도 함께 측정할 수 있다.
- `Timer`는 다음 내용을 한번에 측정해준다.
  - `seconds_count` : 누적 실행 수(`카운터`)
  - `seconds_sum` : 실행 시간의 합(`sum`)
  - `seconds_max` : 최대 실행 시간(가장 오래걸린 실행 시간)(`게이지`)
    - 내부에 타임 윈도우라는 개념이 있어서 1~3분마다 최대 실행 시간이 다시 계산된다.

**[Gauge](https://prometheus.io/docs/concepts/metric_types/#gauge)**
- 게이지는 임의로 오르내릴 수 있는 단일 숫자 값을 나타내는 메트릭이다.
- 값의 현재 상태를 보는데 사용한다.
- 값이 증가하거나 감소할 수 있다.
- 예) CPU 사용량, 메모리 사용량

**카운터와 게이지를 구분할 때는 값이 감소할 수 있는가를 고민해보면 도움이 된다.**

**Tag, 레이블**
- `Tag`를 사용하면 데이터를 나누어서 확인할 수 있다.
- `Tag`는 카디널리티가 낮으면서 그룹화할 수 있는 단위에 사용해야 한다.
  - 예) 성별, 주문 상태, 결제 수단 등
- 카디널리티가 높으면 안 된다.
  - 예) 주민등록번호, PK 같은 것들

## 실무 모니터링 환경 구성 팁

**모니터링은 3단계로 나누어 구성해야 한다.**
- 대시보드
- 애플리케이션 추적
- 로그

### 대시보드
- 전체를 한눈에 볼 수 있는 가장 높은 뷰
- **제품** : 마이크로미터, 프로메테우스, 그라파나 등등
- **모니터링 대상**
  - 시스템 메트릭(CPU, 메모리 등)
  - 애플리케이션 메트릭(톰캣 쓰레드 풀, DB 커넥션 풀, 애플리케이션 호출 수 등)
  - 비즈니스 메트릭(주문수, 취소수 등 비즈니스 로직에 대한 메트릭)

### 애플리케이션 추적
- 주로 각각의 HTTP 요청을 추적해야 할 때
- **제품** : [핀포인트(오픈소스)](https://github.com/pinpoint-apm/pinpoint), 스카우트(오픈소스), 와탭(상용), 제니퍼(상용) 등등

### 로그
- 가장 자세한 추적으로, 원하는대로 커스텀이 가능하다.
- **같은 HTTP 요청을 묶어서 확인할 수 있는 방법이 중요하다.**
  - `MDC`를 검색해보자.

**파일로 직접 로그를 남기는 경우**
- 일반 로그와 에러 로그는 파일을 구분해서 남기는 것이 좋다.
- 에러 로그만 확인해서 문제를 바로 정리할 수 있어야 한다.

**클라우드에 로그를 저장하는 경우**
- 일반 로그와 에러 로그를 검색이 잘 되도록 구분해야 한다.

**모니터링은 각각 용도가 다르다. 관찰을 할 때는 전체에서 점점 좁게 가는 것이 좋다.**