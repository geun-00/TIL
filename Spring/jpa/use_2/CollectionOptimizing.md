# 컬렉션 조회 최적화

- `OneToMany` 관계 최적화
- Order
- Order -> OrderItems
- OrderItems -> Item
- 모든 연관관계는 지연로딩(`LAZY`)으로 설정 되어있음.

### 조회 - V1
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    
    @GetMapping("/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();//LAZY 강제 초기화
            order.getDelivery().getAddress();//LAZY 강제 초기화

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());//LAZY 강제 초기화
        }
        return all;
    }
}
```
- **조회 V1 - 엔티티 직접 노출**
  - 지연로딩으로 설정 되어 있으므로 강제 초기화를 해주지 않으면 조회가 안 된다.
  - 양방향 연관관계면 무한루프가 걸리지 않도록 한곳에 `@JsonIgnore`를 추가해야 한다.
  - 엔티티를 직접 노출하므로 좋은 방법이 아니다.

### 조회 - V2
```java
@GetMapping("/v2/orders")
public List<OrderDto> ordersV2() {
    return orderRepository.findAllByString(new OrderSearch())
                          .stream()
                          .map(OrderDto::new)
                          .toList();
}

@Data
static class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();//지연 로딩
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();//지연 로딩
        orderItems = order.getOrderItems()//지연 로딩
                          .stream()
                          .map(OrderItemDto::new)
                          .toList();
    }
}

@Data
static class OrderItemDto {

    private String itemName; //상품 명
    private int orderPrice; //주문 가격
    private int count; //주문 수량

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }
}
```
- **조회 V2 - 엔티티를 DTO로 변환**
  - 지연로딩이기 때문에 너무 많은 SQL이 실행된다.
  - SQL 실행 수(최악의 경우)
    - `order` 1번
    - `member`, `address` N번(`order` 조회 수 만큼)
    - `orderItem` N번(`order` 조회 수 만큼)
    - `Item` N번(`order` 조회 수 만큼)


### 조회 - V3
```java
@GetMapping("/v3/orders")
public List<OrderDto> ordersV3(){
    return orderRepository.findAllWithItem()
                          .stream()
                          .map(OrderDto::new)
                          .toList();
}
```
```java
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public List<Order> findAllWithItem() {
        return em.createQuery(
                        "select o from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d" +
                                " join fetch o.orderItems oi" +
                                " join fetch oi.item i", Order.class)
                .getResultList();
    }
}
```

- **조회 V3 - 엔티티를 DTO로 변환, 페치 조인 최적화**
  - 페치 조인으로 SQL이 1번만 실행된다.
  - **단점**
    - **페이징이 불가능하다.**
    - [참고](https://github.com/genesis12345678/TIL/blob/main/Spring/jpa/jpql/jpql_2.md#%ED%8E%98%EC%B9%98-%EC%A1%B0%EC%9D%B8%EC%9D%98-%ED%95%9C%EA%B3%84)

### 조회 - V3.1

- 컬렉션을 페치 조인하면 페이징이 불가능하다.
  - 컬렉션을 페치 조인하면 일대다 조인이 발생하므로 데이터가 예측할 수 없이 증가한다.
  - 일대다에서 `일(1)`을 기준으로 페이징을 하는 것이 목적이다. 그런데 데이터는 `다(N)`를 기준으로 `row`가 생성된다.
  - 예를 들어 `Order`를 기준으로 페이징 하고 싶은데, `다(N)`인 `OrderItem`을 조인하면 `OrderItem`이 기준이 되어버리는 것이다.
- 이 경우 하이버네이트는 경고 로그를 남기고 모든 DB 데이터를 읽어서 메모리에서 페이징을 시도한다.

**그러면 페이징 + 컬렉션 엔티티를 함께 조회하려면 어떻게 해야할까?**
- 먼저 `XToOne` 관계를 모두 페치 조인한다. `XToOne` 관계는 row수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.
- 컬렉션은 지연 로딩으로 조회한다.
- 지연 로딩 성능 최적화를 위해 글로벌 설정이나 개별 설정을 적용한다.
  - 글로벌 설정 : `hibernate.default_batch_fetch_size`
  - 개별 최적화 : `@BatchSize`
  - 이 옵션을 적용하면 컬렉션이나 프록시 객체를 한꺼번에 설정한 `size`만큼 `IN`쿼리로 조회한다.

```java
@GetMapping("/v3.1/orders")
    public List<OrderDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0")int offSet,
                                        @RequestParam(value = "limit", defaultValue = "100")int limit){
        return orderRepository.findAllWithMemberDelivery(offSet, limit)
                              .stream()
                              .map(OrderDto::new)
                              .toList();
    }
```
```java
public List<Order> findAllWithMemberDelivery(int offSet, int limit) {
    return em.createQuery("select o from Order o" +
                                  " join fetch o.member m" +
                                  " join fetch o.delivery d", Order.class)
             .setFirstResult(offSet)
             .setMaxResults(limit)
             .getResultList();
}
```
```yaml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 1000
```
- 개별로 설정하려면 `@BatchSize`를 컬렉션은 컬렉션 필드에, 엔티티는 클래스에 적용하면 된다.

- **조회 V3.1 - 엔티티를 DTO로 변환, 페이징 한계 돌파**

- **장점**
  - 쿼리 호출 수가 `1 + N`에서 `1 + 1`로 최적화 된다.
  - 조인보다 DB 데이터 전송량이 최적화 된다.
  - 페치 조인 방식과 비교해서 쿼리 호출 수가 약간 증가하지만, DB 데이터 전송량이 감소한다.
  - **컬렉션 페치 조인은 페이징이 불가능 하지만 이 방법은 페이징이 가능하다.**
- `XToOne` 관계는 페치 조인해도 페이징에 영향을 주지 않는다. 따라서 `XToOne` 관계는 페치 조인으로 쿼리 수를 줄이고, 나머지는
  `hibernate.default_batch_fetch_size`로 최적화 하자.

### 조회 - V4
```java
@GetMapping("/v4/orders")
public List<OrderQueryDto> ordersV4(){
    return orderQueryRepository.findOrderQueryDto();
}
```
```java
@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDto() {

        findOrders().forEach(o -> {
            o.setOrderItems(findOrderItems(o.getOrderId()));
        });
        return findOrders();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" +
                    " join o.member m" +
                    " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }
    
    /**
     * 1:N 관계인 orderItems 조회
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                    " from OrderItem oi" +
                    " join oi.item i" +
                    " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }
}
```
- 전용 레포지토리
```java
@Data
@EqualsAndHashCode(of = "orderId")
public class OrderQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate,
                         OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}
```
```java
@Data
public class OrderItemQueryDto {

    @JsonIgnore
    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
```
- **조회 V4 - JPA에서 DTO로 직접 조회**
  - 쿼리 : 루트 1번, 컬렉션 N번 실행
  - `XToOne`관계들을 먼저 조회하고(`findOrders()`), `XToMany` 관계는 각각 별도로 처리한다.(`findOrderItems()`)
    - `XToOne` 관계는 조인해도 데이터 row 수에 영향이 없다.
    - `XToMany` 관계는 조인하면 row 수가 증가한다.
  - row 수가 증가하지 않는 `XToOne` 관계는 조인으로 최적화 하기 쉬우므로 한번에 조회하고, `XToMany` 관계는 최적화 하기 어려우므로 `findOrderItems()`같은 별도의 메서드로 조회한다.

    
### 조회 - V5
```java
@GetMapping("/v5/orders")
public List<OrderQueryDto> ordersV5(){
    return orderQueryRepository.findAllByDto_optimization();
}
```
```java
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findAllByDto_optimization() {

        //루트 조회(toOne 코드를 모두 한번에 조회)
        List<OrderQueryDto> result = findOrders();

        //orderItem 컬렉션을 MAP 한방에 조회
        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        //루프를 돌면서 컬렉션 추가(추가 쿼리 실행 X)
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) from Order o" +
                    " join o.member m" +
                    " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                                " from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                                               .setParameter("orderIds", orderIds)
                                               .getResultList();

        return orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                     .map(OrderQueryDto::getOrderId)
                     .toList();
    }
}
```

- **조회 V5 - JPA에서 DTO 직접 조회, 컬렉션 조회 최적화**
  - `findOrder()` : `XToOne` 관계 조회
  - `findOrderItemMap()` : `XToMany` 관계를 조회하고 주문 ID를 기준으로 그룹화하여 `Map`으로 반환
  - `toOrderIds()` : DTO에서 주문 ID만 추출해서 리스트로 반환
  - 쿼리 : 루트 1번, 컬렉션 1번
  - `XToOne` 관계들을 먼저 조회하고, 여기서 얻은 식별자 `orderId`로 `XToMany` 관계인 `OrderItem`을 한꺼번에 조회한다.
  - `Map`을 사용해서 매칭 성능을 향상시켰다.(`O(1)`)


### 조회 - V6
```java
import static java.util.stream.Collectors.*;

@GetMapping("/v6/orders")
public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                    .collect(
                        groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                                                          o.getName(),
                                                          o.getOrderDate(),
                                                          o.getOrderStatus(),
                                                          o.getAddress()),

                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),
                                                           o.getItemName(),
                                                           o.getOrderPrice(),
                                                           o.getCount()),
                        toList()))
        
                    ).entrySet()
                     .stream()

                     .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                                                e.getKey().getName(),
                                                e.getKey().getOrderDate(),
                                                e.getKey().getOrderStatus(),
                                                e.getKey().getAddress(),
                                                e.getValue())
                    ).toList();
        }
```
```java
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
            "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                    " from Order o" +
                    " join o.member m" +
                    " join o.delivery d" +
                    " join o.orderItems oi" +
                    " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
```
```java
@Data
@AllArgsConstructor
public class OrderFlatDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;
}
```
```java
@Data
@EqualsAndHashCode(of = "orderId")
@AllArgsConstructor
public class OrderQueryDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemQueryDto> orderItems;
}
```
```java
@Data
@AllArgsConstructor
public class OrderItemQueryDto {

    @JsonIgnore
    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;
}
```
- **조회 V6 - JPA에서 DTO로 직접 조회, 플랫 데이터 최적화**
  - 스트림으로 `flats` 리스트를 그룹화하고 매핑한다.
  - `groupingBy`로 주문 정보를 기준으로 그룹화하고, 같은 주문에 속하는 주문 항목들은 `mapping` 으로 리스트로 수집한다.
  - `entrySet()`으로 그룹화된 맵의 엔트리들을 스트림으로 변환(`stream()`)하고, 각 엔트리를 `OrderQueryDto`로 매핑한다.
  - **장점** : 쿼리 1번
  - **단점**
    - 쿼리는 1번이지만 조인으로 인해 DB에서 애플리케이션에 전달하는 데이터에 중복 데이터가 추가되므로 상황에 따라 `V5`보다 더 느릴 수도 있다.
    - 애플리케이션에서 추가 작업이 크다.
    - 페이징이 불가능하다.

<br>

## 정리

- **엔티티 조회**
  - 엔티티를 조회해서 그대로 반환 : `V1`
  - 엔티티 조회 후 DTO로 변환 : `V2`
  - 페치 조인으로 쿼리 수 최적화 : `V3`
  - 컬렉션 페이징 한계 돌파 : `V3.1`
    - 컬렉션은 페치 조인 시 페이징이 불가능
    - `XToOne` 관계는 페치 조인으로 쿼리 수 최적화
    - 컬렉션은 페치 조인 대신에 지연 로딩을 유지하고, `hibernate.default_batch_fetch_size` 또는 `@BatchSize`로 최적화

- **DTO 직접 조회**
  - JPA에서 DTO를 직접 조회 : `V4`
  - 컬렉션 조회 최적화 - 일대다 관계인 컬렉션은 `IN`절을 활용해서 메모리에 미리 조회해서 최적화 : `V5`
  - 플랫 데이터 최적화 - `JOIN` 결과를 그대로 조회 후 애플리케이션에서 원하는 모양으로 직접 변환 : `V6`


### 권장 순서
1. 엔티티 조회 방식으로 우선 접근
   1. 페치 조인으로 쿼리 수를 최적화
   2. 컬렉션 최적화
      - 페이징 필요 O : `hibernate.default_batch_fetch_size` 또는 `@BatchSize`로 최적화
      - 페이징 필요 X : 페치 조인 사용
2. 엔티티 조회 방식으로 해결이 안되면 DTO 조회 방식 사용
3. DTO 조회 방식으로 해결이 안되면 NativeSQL 또는 스프링 JdbcTemplate 사용

> 엔티티 조회 방식은 페치 조인이나 `hibernate.default_batch_fetch_size` 또는 `@BatchSize` 같이 코드를 거의 수정하지 않고
> 옵션만 약간 변경해서 다양한 성능 최적화를 시도할 수 있다.<br>
> 반면에 DTO를 직접 조회하는 방식은 성능을 최적화 하거나 성능 최적화 방식을 변경할 때 많은 코드를 변경해야 한다.

> **개발자는 성능 최적화와 코드 복잡도를 항상 고민 해야한다.**<br>
> 항상 그런 것은 아니지만 보통 성능 최적화는 단순한 코드를 복잡한 코드로 만든다.<br>
> **엔티티 조회 방식**은 JPA가 많은 부분을 최적화 해주기 때문에 단순한 코드를 유지하면서 성능을 최적화할 수 있다.<br>
> 반면에 **DTO 조회 방식**은 SQL을 직접 다루는 것과 유사하기 때문에 둘 사이를 고민해야 한다.

**DTO 조회 방식의 선택지**
- DTO로 조회하는 방법은 각각 장단점이 있다. `V4`, `V5`, `V6`에서 단순하게 쿼리가 1번 실행된다고 `V6`가 항상 좋은 방법인 것은 아니다.


- `V4`는 코드가 단순하다. 특정 주문 한건만 조회하면 이 방식을 사용해도 성능이 잘 나온다.
  - 예를 들어서 조회한 `Order` 데이터가 1건이면 `OrderItem`을 찾기 위한 쿼리도 1번만 실행하면 된다.


- `V5`는 코드가 복잡하다. 여러 주문을 한꺼번에 조회하는 경우에는 `V4`대신에 이것을 최적화한 `V5` 방식을 사용해야 한다.
  - 예를 들어서 조회한 `Order` 데이터가 1000건인데 `V4` 방식을 사용하면 쿼리가 총 `1 + 1000`번 실행된다.
  - 여기서 `1`은 `Order`를 조회한 쿼리고, `1000`은 조회된 `Order`의 row 수다.
  - `V5` 방식으로 최적화하면 쿼리가 총 `1 + 1`번 실행된다.
  - 상황에 따라 다르겠지만 운영 환경에서 100배 이상의 성능 차이가 날 수 있다.


- `V6`는 완전히 다른 접근 방식이다. 쿼리 한번으로 최적화 되어서 좋아보이지만, `Order`를 기준으로 페이징이 불가능하다.
  - 실무에서는 데이터 수백이나 수천 건 단위로 페이징 처리가 꼭 필요하므로, 이 경우 선택하기 어려운 방법이다.
  - 데이터가 많으면 중복 전송이 증가해서 `V5`와 비교해서 성능 차이도 미비하다.
