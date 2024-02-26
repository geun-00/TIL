# 주문 도메인 개발

### 주문 엔티티 비즈니스 로직 추가
```java
@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    /**
     * 연관 관계 편의 메서드
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /**
     * 생성 메서드
     */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    /**
     * 비즈니스 로직
     */
    // 주문 취소
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);

        orderItems.forEach(OrderItem::cancel);
/*
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
*/
    }

    /**
     * 조회 로직
     */
    // 전체 주문 가격 조회
    public int getTotalPrice() {
        return orderItems.stream()
                         .mapToInt(OrderItem::getTotalPrice)
                         .sum();
    }
}
```
**생성 메서드**
- 주문 엔티티를 생성할 때 사용한다. 
- 주문 회원, 배송정보, 주문 상품의 정보를 받아서 실제 주문 엔티티를 생성한다.
- 주문에 대한 변화가 생기면 이 메서드만 변경하면 된다.

**주문 취소**
- 주문 취소시 사용한다.
- 주문 상태를 취소로 변경하고 주문상품에 주문 취소를 알린다.
- 이미 배송을 완료한 상품이면 예외가 발생한다.

**전체 주문 가격 조회**
- 주문 시 사용한 전체 주문 가격을 조회한다.
- 전체 주문 가격을 알려면 각각의 주문 상품 가격을 알아야 한다.
- 연관된 주문 상품들의 가격을 조회해서 더한 값을 반환한다.
- 실무에서는 주로 주문에 전체 주문 가격 필드를 두고 역정규화 한다.

### 주문 상품 엔티티 비즈니스 로직 추가
```java
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량

    /**
     * 생성 메서드
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }

    /**
     * 비즈니스 로직
     */
    // 주문 취소
    public void cancel() {
        getItem().addStock(count);
    }

    /**
     * 조회 로직
     * @return 주문 상품 전체 가격
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
```
**생성 메서드**
- 주문 상품, 가격, 수량 정보를 사용해서 주문상품 엔티티를 생성한다.
- 주문한 수량만큼 상품의 재고를 줄인다.

**주문 취소** 
- 취소한 주문 수량 만큼 상품의 재고를 증가시킨다.

### 레포지토리
```java
@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }
}
```

### 서비스
```java
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepositoryOld.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성(static 메서드)
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성(static 메서드)
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }
}
```
**주문**
- 주문하는 회원 식별자, 상품 식별자, 주문 수량 정보를 받아서 실제 주문 엔티티를 생성한 후 저장한다.
- `Order`의 `Delivery`와 `OrderItem` 필드는 `cascade = CascadeType.ALL`로 되어있기 때문에 `Order`만 저장을 해도 `Delivery`, `OrderItem`도 같이 저장된다.
- `Order`와 `OrderItem` 생성 메서드는 `static`으로 되어있다. 그런데 다른 누군가가 `new`로 생성을 해서 `setter`로 값을 변경하려고 할 수도 있다.
  - 일관성과 유지보수성을 위해 기본 생성자를 막아두는 것이 좋다.
  - JPA는 `protected`까지 기본 생성자 범위를 허용한다.

**취소**
- 주문 식별자를 받아서 주문 엔티티를 조회한 후 주문 엔티티에 주문 취소를 요청한다.

> **참고**<br>
> 비즈니스 로직 대부분이 엔티티에 있다. 서비스 계층은 단순히 엔티티에 필요한 요청을 위임하는 역할을 한다.<br>
> 이렇게 엔티티가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것을 **도메인 모델 패턴**이라고 한다.<br>
> 반대로 엔티티에는 비즈니스 로직이 거의 없고 서비스 계층에서 대부분의 비즈니스 로직을 처리하는 것을 `트랜잭션 스크립트 패턴`이라 한다.


### 테스트 코드
```java
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    
    @Test
    void 상품_주문() {
        // given
        Member member = createMember();
        Book book = createBook("혼공S", 24_000, 10);
        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.ORDER);//상품 주문시 주문 상태는 ORDER
        assertThat(getOrder.getOrderItems().size()).isEqualTo(1);//주문한 상품 수가 정확해야 한다.
        assertThat(getOrder.getTotalPrice()).isEqualTo(book.getPrice() * orderCount);//주문 가격은 가격 * 수량 이다.
        assertThat(book.getStockQuantity()).isEqualTo(8);//주문 수량만큼 재고가 줄어야 한다.
    }

    @Test
    void 주문_취소() {
        // given
        Member member = createMember();
        Book item = createBook("알고리즘 코딩 테스트", 32_000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);//주문

        // when
        orderService.cancelOrder(orderId);//주문 취소

        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertThat(getOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);//상품 주문 취소시 주문 상태는 CANCEL
        assertThat(item.getStockQuantity()).isEqualTo(10);//주문이 취소된 상품은 그만큼 재고가 증가해야 한다. 10 -> 8 -> 10
    }
    
    @Test
    void 상품주문_재고수량_초과() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 43_000, 15);

        int orderCount = 16;

        // when
        //15개보다 많은 16개 주문을 시도하면 NotEnoughStockException 예외가 발생해야 한다.
        assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);
        // then
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("member1");
        member.setAddress(new Address("경기", "부천시", "14551"));
        em.persist(member);
        return member;
    }
}
```