# @Profile

프로필과 외부 설정을 사용해서 각 환경마다 설정값을 다르게 적용을 할 수 있다.<br>
그런데 설정값은 다른 정도가 아니라 **각 환경마다 서로 다른 빈을 등록하려면 어떻게 해야할까?**<br>
예를 들어 결제 기능을 개발하는데 로컬 개발 환경에서는 가짜 결제 기능을, 운영 환경에서는 실제 결제 기능을 제공하는 스프링 빈을 등록한다고 해보자.

```java
public interface PayClient {
    void pay(int money);
}
```
```java
@Slf4j
public class LocalPayClient implements PayClient{
    @Override
    public void pay(int money) {
        log.info("로컬 결제 money={}", money);
    }
}
```
```java
@Slf4j
public class ProdPayClient implements PayClient{
    @Override
    public void pay(int money) {
      log.info("운영 결제 money={}", money);
    }
}
```
```java
@Service
@RequiredArgsConstructor
public class OrderService {
    private final PayClient payClient;

    public void order(int money) {
        payClient.pay(money);
    }
}
```
```java
@Slf4j
@Configuration
public class PayConfig {

    @Bean
    @Profile("default")
    public LocalPayClient localPayClient() {
        log.info("LocalPayClient 빈 등록");
        return new LocalPayClient();
    }

    @Bean
    @Profile("prod")
    public ProdPayClient prodPayClient() {
        log.info("ProdPayClient 빈 등록");
        return new ProdPayClient();
    }
}
```
- `@Profile` 어노테이션을 사용하면 해당 프로필이 활성화된 경우에만 빈을 등록한다.

```java
@Component
@RequiredArgsConstructor
public class OrderRunner implements ApplicationRunner {
    private final OrderService orderService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        orderService.order(1000);
    }
}
```
- `ApplicationRunner`인터페이스를 구현하면 스프링은 빈 초기화가 모두 끝나고 애플리케이션 로딩이 완료되는 시점에 `run(args)`메서드를 호출해준다.

**@Profile 정체**
```java
...
@Conditional(ProfileCondition.class)
public @interface Profile {
	String[] value();
}

class ProfileCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        MultiValueMap<String, Object> attrs = metadata.getAllAnnotationAttributes(Profile.class.getName());
        if (attrs != null) {
            for (Object value : attrs.get("value")) {
                if (context.getEnvironment().acceptsProfiles(Profiles.of((String[]) value))) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}
```
- `@Profile`은 특정 조건에 따라서 해당 빈을 등록할지 말지 선택한다.
- `@Conditional` 기능을 활용해서 개발자가 편리하게 사용할 수 있는 `@Profile` 기능을 제공하는 것이다.