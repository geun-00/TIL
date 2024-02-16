# 빈 생명주기 콜백

DB 커넥션 풀이나 네트워크 소켓처럼 애플리케이션 시작 시점에 필요한 연결을 미리 해두고 애플리케이션 종료 시점에 연결을 모두 종료하는
작업을 하려면 객체의 초기화와 종료 작업이 필요하다.

```java
public class NetworkClient {
    private String url; 
    
    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url); 
        connect();
        call("초기화 연결 메시지");
    }
    public void setUrl(String url) { 
        this.url = url;
    }
    //서비스 시작시 호출 
    public void connect() {
        System.out.println("connect: " + url);
    }
    
    public void call(String message) {
        System.out.println("call: " + url + " message = " + message);
    }

    //서비스 종료시 호출
    public void disconnect() {
        System.out.println("close: " + url);
    }
}
```

문자로 외부 네트워크와 연결하는 예제다. 애플리케이션 시작 시점에 `connect()`로 연결을 맺고, 애플리케이션이 종료되면 `disConnect()`로 연결을 끊어야 한다.

```java
@Test
void lifeCycleTest() {
    ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
    NetworkClient client = ac.getBean(NetworkClient.class);
    ac.close();
}

@Configuration
static class LifeCycleConfig{
    @Bean
    public NetworkClient networkClient() {
        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("www.naver.com");
        return networkClient;
    }
}

//출력
생성자 호출, url = null 
connect: null
call: null message = 초기화 연결 메시지
```

생성자에 url 정보 없이 connect가 호출이 된다. 객체를 생성하는 단계에서는 url이 없고 객체를 생성한 다음에 외부에서 수정자 주입(setter)을 통해서 url이 존재할 수 있다.

스프링 빈은 객체를 생성하고 의존관계 주입이 다 끝난 다음에 필요한 데이터를 사용할 수 있는 준비가 완료된다. 따라서 초기화 작업은 의존관계 주입이 모두 완료되고 난 다음에
호출해야 한다. 개발자가 의존관계 주입이 모두 완료된 시점을 어떻게 알 수 있을까?

**스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려주는 다양한 기능을 제공한다.** 또한 **스프링은 스프링 컨테이너가 종료되기
직전에 소멸 콜백을 주기 때문에** 안전하게 종료 작업을 진행할 수 있다.

- *스프링 빈의 이벤트 라이프 사이클*
  - **스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 실행 로직 -> 소멸전 콜백 -> 스프링 종료**
  - **초기화 콜백** : 빈이 생성되고 빈의 의존관계 주입이 완료된 후 호출
  - **소멸전 콜백** : 빈이 소멸되기 직전에 호출

> **객체의 생성과 초기화를 분리하자.**
> 
> 생성자는 필수 정보(파라미터)를 받고 메모리를 할당해서 객체를 생성하는 책임을 가진다. 반면 초기화는 이렇게 생성된 값들을 활용해서 외부 커넥션을 연결하는 등 무거운 작업을 수행한다.
> 따라서 생성자 안에서 무거운 초기화 작업을 함께 하는 것 보다는 **객체를 생성하는 부분과 초기화 하는 부분을 명확하게 나누는 것이 유지보수 관점에서 좋다.**
> 
> 초기화 작업이 내부 값들만 변경하는 정보로 단순한 경우에는 생성자에서 한 번에 다 처리하는 것이 더 나을 수 있다.

<br>

## 스프링이 지원하는 빈 생명주기 콜백

### 인터페이스 InitializingBean, DisposableBean

```java
public class NetworkClient implements InitializingBean, DisposableBean {
    private String url;
    
    ...
    
    @Override
    public void afterPropertiesSet() throws Exception {
        connect();
        call("초기화 연결 메시지");
    }
    
    @Override
    public void destroy() throws Exception {
        disConnect();
    }
}
```
- `InitializingBean`은 `afterPropertiesSet()` 메서드로 초기화를 지원한다.
- `DisposableBean`은 `destroy()` 메서드로 소멸을 지원한다.
- 이 인터페이스들은 스프링 전용 인터페이스로 해당 코드가 스프링 전용 인터페이스에 의존하게 된다.

### 빈 등록 초기화, 소멸 메서드 지정
```java
public class NetworkClient {
    private String url;
    
    ...

    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
}

@Configuration
static class LifeCycleConfig {
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public NetworkClient networkClient() {
        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("www.naver.com");
        return networkClient;
    }
}
```
- 설정 정보에 초기화, 소멸 메서드를 지정할 수 있다.
- 메서드 이름을 자유롭게 줄 수 있으며 스프링 빈이 스프링 코드에 의존하지 않는다.

### 어노테이션 적용
```java
public class NetworkClient {
    private String url;
    
    ...

    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disConnect();
    }
}
```
- `@PostConstruct` , `@PreDestroy`
  - 스프링에 종속적인 기술이 아니라 자바 표준 기술이기 때문에 스프링이 아닌 다른 컨테이너에서도 동작한다.