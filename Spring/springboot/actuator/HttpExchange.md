# HTTP 요청 응답 기록

HTTP 요청과 응답의 과거 기록을 확인하고 싶다면 `httpexchanges` 엔드포인트를 사용하면 된다.

`HttpExchangeRepository`인터페이스의 구현체를 스프링 빈으로 등록하면 `httpexchanges` 엔드포인트를 사용할 수 있다.(해당 빈을 등록하지 않으면 엔드포인트가 활성화 되지 않는다.)

스프링 부트는 기본으로 `InMemoryHttpExchangeRepository` 구현체를 제공한다.

```java
@SpringBootApplication
public class ActuatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActuatorApplication.class, args);
    }

    @Bean
    public InMemoryHttpExchangeRepository httpExchangeRepository() {
        return new InMemoryHttpExchangeRepository();
    }
}
```
- 이 구현체는 최대 100개의 HTTP 요청을 제공한다.
- 최대 요청이 넘어가면 과거 요청을 삭제한다.
- `setCapacity()`로 최대 요청 수를 변경할 수 있다.

이 기능은 매우 단순하고 기능에 재한이 많기 때문에 개발 단계에서만 사용하고, 실제 운영 서비스에서는 전문적인 모니터링 툴이나 다른 기술을 사용하는 것이 좋다.(핀포인트, Zipkin 등)