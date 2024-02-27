# 지연 로딩과 조회 성능 최적화

- `XtoOne(ManyToOne, OneToOne)` 관계 최적화
- Order
- Order -> Member
- Order -> Delivery
- 모든 연관관계는 지연로딩(`LAZY`)으로 설정 되어있음.

### 주문 조회 - V1
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();//Lazy 강제 초기화
            order.getDelivery().getAddress();//Lazy 강제 초기화
        }
        return all;
    }
}
```
- **조회 V1 - 엔티티를 직접 노출**
  - 엔티티를 직접 노출할 때는 양방향 연관관계가 걸린 곳은 한곳을 `@JsonIgnore`처리 해야한다.(안 그러면 양쪽을 서로 호출하면서 무한 루프)
  - 엔티티를 API 응답으로 외부로 노출하는 것은 좋지 않다. DTO로 변환해서 반환하는 것이 더 좋은 방법이다.
  - **지연 로딩(`LAZY`)을 피하기 위해 즉시 로딩(`EAGER`)으로 설정하면 안 된다.**
    - 즉시 로딩 때문에 연관관계가 필요 없는 경우에도 데이터를 항상 조회해서 성능 문제가 발생할 수 있다.
    - 즉시 로딩으로 설정하면 성능 튜닝이 매우 어려워진다.
    - **항상 지연 로딩을 기본으로 하고, 성능 최적화가 필요한 경우에 페치 조인(fetch join)을 사용하자.**

### 주문 조회 - V2
```java
@GetMapping("/v2/simple-orders")
public List<SimpleOrderDto> ordersV2() {
    return orderRepository.findAllByString(new OrderSearch())
                          .stream()
                          .map(SimpleOrderDto::new)
                          .toList();
}

@Data
static class SimpleOrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;


    public SimpleOrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();//지연 로딩
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();//지연 로딩
    }
}
```

- **조회 V2 - 엔티티를 DTO로 변환**
  - 엔티티를 DTO로 변환하는 가장 일반적인 방법이다.
  - 쿼리가 총 `1 + N + N`번 실행된다.
    - `order` 조회 1번(여기서 조회 결과 수가 `N`이 된다.)
    - `order -> member` 지연 로딩 조회 `N`번
    - `order -> delivery` 지연 로딩 조회 `N`번
    - 예를 들어 `order`의 결과가 4개면 최악의 경우 1 + 4 + 4번 실행된다.
      - 지연로딩은 영속성 컨텍스트에서 조회하므로, 이미 조회된 경우 쿼리를 생략할 수 있다.(같은 회원이 있는 경우)

### 주문 조회 - V3
```java
@GetMapping("/v3/simple-orders")
public List<SimpleOrderDto> ordersV3() {
    return orderRepository.findAllWithMemberDelivery()
                          .stream()
                          .map(SimpleOrderDto::new)
                          .toList();
}

@Data
static class SimpleOrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;


    public SimpleOrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();//지연 로딩
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();//지연 로딩
    }
}
```
```java
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("select o from Order o" +
                              " join fetch o.member m" +
                              " join fetch o.delivery d", Order.class)
                .getResultList();
    }
}
```

- **조회 V3 - 엔티티를 DTO로 변환, 페치 조인 최적화**
  - 엔티티를 페치 조인(fetch join)을 사용해서 쿼리 1번에 조회한다.
  - 페치 조인으로 `order -> member`, `order -> delivery`는 이미 조회 된 상태이므로 지연로딩을 하지 않는다.

### 주문 조회 - V4
```java
@GetMapping("/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }
```
```java
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return  em.createQuery(
            "select new jpabook.jpashop.repository.simpleQuery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
            " from Order o" +
            " join o.member m" +
            " join o.delivery d", OrderSimpleQueryDto.class)
                .getResultList();
    }
}
```
- 전용 레포지토리

```java
@Data
public class OrderSimpleQueryDto {
    
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    
    public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate,
                               OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```

- **조회 V4 - JPA에서 DTO로 바로 조회**
  - 일반적인 SQL을 사용할 때처럼 원하는 값만 선택해서 조회한다.
  - `new` 명령어를 사용해서 JPQL의 결과를 DTO로 바로 변환한다.
  - `SELECT`절에서 원하는 데이터를 직접 선택하므로 DB -> 애플리케이션 네트워크 용량을 최적화할 수 있지만 생각보다는 미비하다.
  - **단점**
    - 레포지토리 재사용성이 떨어진다.(`V3` 방식은 다른 곳에서도 재사용할 수 있다.)
    - API 스펙에 맞춘 코드가 레포지토리에 들어간다.(패키지를 분리해서 사용하는 것 추천)


## 정리
엔티티를 DTO로 변환하거나 DTO로 바로 조회하는 방법은 각각 장단점이 있다. 둘중 상황에 따라서 더 나은 방법을 선택하면 된다.<br>
엔티티로 조회하면 레포지토리 재사용성도 좋고, 개발도 단순해진다.

**쿼리 방식 선택 권장 순서**
1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.(`V2`)
2. 필요하면 페치 조인으로 성능 최적화를 한다.(`V3`) -> 대부분의 성능 이슈가 해결된다.
3. 그래도 안되면 DTO로 직접 조회하는 방법을 선택한다.(`V4`)
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.