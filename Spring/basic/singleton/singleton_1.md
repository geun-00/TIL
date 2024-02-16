# 싱글톤 컨테이너 - 1

## 싱글톤 패턴
```java
@Test
@DisplayName("스프링 없는 순수한 DI 컨테이너")
void pureContainer() {
    AppConfig appConfig = new AppConfig();
    
    MemberService memberService1 = appConfig.memberService();
    MemberService memberService2 = appConfig.memberService();
    
    System.out.println("memberService1 = " + memberService1);
    System.out.println("memberService2 = " + memberService2);
    
    // 참조값이 다른 것을 확인
    assertThat(memberService1).isNotSameAs(memberService2);
}
```
직접 만든 스프링 없는 `AppConfig`는 요청을 할 때 마다 객체를 새로 생성해서 반환한다. 요청에 비례해서 객체가 생성이 되고 소멸되기 때문에 메모리 낭비가 심하다.

**싱글톤 패턴을 적용해 해당 객체가 딱 1개만 생성되고 공유하도록 설계해야 한다.**

- 싱글톤 패턴 적용
```java
public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    public static SingletonService getInstance() {
        return instance;
    }

    private SingletonService() {
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
```
- static 영역에 객체 인스턴스를 미리 하나 생성해서 올려둔다.
- 이 객체 인스턴스를 사용하는 코드에서는 `getInstance()` 메서드를 통해서만 조회할 수 있는데 이 메서드를 호출하면 항상 같은 인스턴스를 반환한다.(싱글톤)
- 생성자를 `private`으로 막아서 외부에서 new를 통해 객체 인스턴스가 생성되는 것을 방지한다.

```java
@Test
@DisplayName("싱글톤 패턴을 적용한 객체 사용")
void singletonServiceTest() {
    
    SingletonService singletonService1 = SingletonService.getInstance();
    SingletonService singletonService2 = SingletonService.getInstance();
    
    System.out.println("singletonService1 = " + singletonService1);
    System.out.println("singletonService2 = " + singletonService2);
    
    // 같은 참조값을 반환한다.
    assertThat(singletonService1).isSameAs(singletonService2);
}
```

싱글톤 패턴을 사용하면 이미 만들어진 객체 하나를 공유해서 사용하기 때문에 효율적일 수 있지만 여러가지 문제점들이 있다.
- 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
- 의존관계상 클라이언트가 구체 클래스에 의존한다.
- 테스트하기 어렵다.
- 유연성이 떨어진다.

<br>

## 싱글톤 컨테이너
스프링 컨테이너는 싱글톤 패턴의 문제점들을 해결하면서 객체 인스턴스를 싱글톤으로 관리한다.

```java
@Test
@DisplayName("싱글톤 컨테이너와 싱글톤")
void springContainer() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    
    MemberService memberService1 = ac.getBean("memberService", MemberService.class);
    MemberService memberService2 = ac.getBean("memberService", MemberService.class);
    
    System.out.println("memberService1 = " + memberService1);
    System.out.println("memberService2 = " + memberService2);

    // 참조값이 다른 것을 확인
    assertThat(memberService1).isSameAs(memberService2);
}
```

## 싱글톤 방식 주의점
- 싱글톤 방식은 객체 인스턴스를 딱 하나만 생성해서 여러 클라이언트가 하나의 같은 객체 인스턴스를 공유하기 때문에 **싱글톤 객체는 상태를 유지(`stateful`)하게 설계하면 안 된다.**
- **무상태(`stateless`)로 설계해야 한다.**
  - 특정 클라이언트에 의존적인 필드가 있으면 안 된다.
  - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안 된다.
  - 읽기만 가능해야 한다.

### 문제점 예시
```java
@Getter
public class StatefulService {
    private int price;//상태를 공유하는 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price;//문제 발생
    }
}

@Test
void statefulServiceSingleton() {
    ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
    StatefulService statefulService1 = ac.getBean(StatefulService.class);
    StatefulService statefulService2 = ac.getBean(StatefulService.class);

    statefulService1.order("userA", 10000);
    statefulService2.order("userB", 20000);

    int price = statefulService1.getPrice();
    System.out.println("price = " + price);

    assertThat(statefulService1).isSameAs(statefulService2);
    assertThat(statefulService1.getPrice()).isEqualTo(20000);
}

static class TestConfig {
    @Bean
    public StatefulService statefulService() {
        return new StatefulService();
    }
}
```
`userA`의 주문금액이 10,000이 나올 것을 예상했지만 20,000이 나온다. 싱글톤이기 때문에 나중에 요청한 `userB`의 값으로 바꿔치기 된 것이다.

**공유필드에 주의해야 한다.**