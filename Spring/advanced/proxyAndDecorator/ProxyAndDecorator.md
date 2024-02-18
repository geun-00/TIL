# 프록시 패턴과 데코레이터 패턴

다양한 상황에서 프록시를 사용할 수 있다.

1. V1 - 인터페이스와 구현 클래스(스프링 빈 수동 등록)
2. V2 - 인터페이스 없는 구체 클래스(스프링 빈 수동 등록)
3. V3 - 컴포넌트 스캔 스프링 빈 자동 등록

## V1
```java
@RestController//스프링은 @Controller 또는 @RestController가 있어야 컨트롤러로 인식
public interface OrderControllerV1 {
    @GetMapping("/v1/request")
    String request(@RequestParam("itemId") String itemId);

    @GetMapping("/v1/no-log")
    String noLog();
}

@RequiredArgsConstructor
public class OrderControllerV1Impl implements OrderControllerV1{
    private final OrderServiceV1 orderService;

    @Override
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "OK";
    }

    @Override
    public String noLog() {
        return "OK";
    }
}
```
```java
public interface OrderServiceV1 {
    void orderItem(String itemId);
}

@RequiredArgsConstructor
public class OrderServiceV1Impl implements OrderServiceV1{
    private final OrderRepositoryV1 orderRepositoryV1;

    @Override
    public void orderItem(String itemId) {
        orderRepositoryV1.save(itemId);
    }
}
```
```java
public interface OrderRepositoryV1 {
    void save(String itemId);
}

public class OrderRepositoryV1Impl implements OrderRepositoryV1{
    @Override
    public void save(String itemId) {
        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외 발생!");
        }
        sleep();
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
```java
@Configuration
public class AppV1Config {

    @Bean
    public OrderControllerV1 orderControllerV1() {
        return new OrderControllerV1Impl(orderServiceV1());
    }

    @Bean
    public OrderServiceV1 orderServiceV1() {
        return new OrderServiceV1Impl(orderRepositoryV1());
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1() {
        return new OrderRepositoryV1Impl();
    }
}
```
## V2
```java
@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {
    private final OrderServiceV2 orderService;

    @GetMapping("/v2/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "OK";
    }

    @GetMapping("/v2/no-log")
    public String noLog() {
        return "OK";
    }
}

@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepositoryV2;

    public void orderItem(String itemId) {
        orderRepositoryV2.save(itemId);
    }
}

public class OrderRepositoryV2 {

    public void save(String itemId) {
        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외 발생!");
        }
        sleep();
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
```java
@Configuration
public class AppV2Config {

    @Bean
    public OrderControllerV2 orderControllerV2() {
        return new OrderControllerV2(orderServiceV2());
    }

    @Bean
    public OrderServiceV2 orderServiceV2() {
        return new OrderServiceV2(orderRepositoryV2());
    }

    @Bean
    public OrderRepositoryV2 orderRepositoryV2() {
        return new OrderRepositoryV2();
    }
}
```

## V3

```java
@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {
    private final OrderServiceV3 orderService;

    @GetMapping("/v3/request")
    public String request(String itemId) {
        orderService.orderItem(itemId);
        return "OK";
    }

    @GetMapping("/v3/no-log")
    public String noLog() {
        return "OK";
    }
}

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepositoryV3;

    public void orderItem(String itemId) {
        orderRepositoryV3.save(itemId);
    }
}

@Repository
public class OrderRepositoryV3 {

    public void save(String itemId) {
        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외 발생!");
        }
        sleep();
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
```java
@Import({AppV1Config.class, AppV2Config.class})
@SpringBootApplication(scanBasePackages = "hello.proxy.app.v3") //주의
public class ProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProxyApplication.class, args);
    }
}
```
- `V3`만 컴포넌트 스캔으로 자동 빈 등록을 한다.

**이제 원본 코드를 전혀 수정하지 않고 다양한 케이스에 로그 추적기를 적용해야 한다.**

- [프록시 패턴 예제](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/proxy/Proxy.md#%ED%94%84%EB%A1%9D%EC%8B%9C-%ED%8C%A8%ED%84%B4)
- [데코레이터 패턴 예제](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/proxy/Proxy.md#%EB%8D%B0%EC%BD%94%EB%A0%88%EC%9D%B4%ED%84%B0-%ED%8C%A8%ED%84%B4)


- [인터페이스 기반 프록시](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/Proxy.md#%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4-%EA%B8%B0%EB%B0%98-%ED%94%84%EB%A1%9D%EC%8B%9C)
- [구체 클래스 기반 프록시](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/Proxy.md#%EA%B5%AC%EC%B2%B4-%ED%81%B4%EB%9E%98%EC%8A%A4-%EA%B8%B0%EB%B0%98-%ED%94%84%EB%A1%9D%EC%8B%9C)