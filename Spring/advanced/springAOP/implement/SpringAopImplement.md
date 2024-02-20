# 스프링 AOP 구현하기

## 예제

```java
@Slf4j
@Repository
public class OrderRepository {

    public String save(String itemId) {
        log.info("[orderRepository] 실행");
        //저장 로직
        if (itemId.equals("ex")) {
            throw new IllegalStateException("예외 발생!");
        }
        return "ok";
    }
}
```
```java
@Slf4j
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void orderItem(String itemId) {
        log.info("[orderService] 실행");
        orderRepository.save(itemId);
    }
}
```
```java
@Slf4j
@SpringBootTest
public class AopTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void aopInfo() {
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));//false
        log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));//false
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    @Test
    void exception() {
        assertThatThrownBy(() -> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class);
    }
}
```
- 현재는 AOP 관련 코드가 전혀 없으므로 당연히 `false`가 나온다.

![img.png](image/img.png)

**이제 버전을 올려보면서 AOP를 구현해보자.**

- [V1 - @Aspect](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_1_3.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v1)
- [V2 - 포인트컷 분리](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_1_3.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v2)
- [V3 - 어드바이스 추가](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_1_3.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v3)
- [V4 - 포인트컷 참조](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_4_6.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v4)
- [V5 - 어드바이스 순서](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_4_6.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v5)
- [V6 - 어드바이스 종류](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_4_6.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v6)