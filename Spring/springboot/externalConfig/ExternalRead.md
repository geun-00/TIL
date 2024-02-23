# 외부 설정 사용

## Environment

```java
@Slf4j
@Data
@AllArgsConstructor
public class MyDataSource {

    private String url;
    private String username;
    private String password;
    private int maxConnection;
    private Duration timeout;
    private List<String> options;

    @PostConstruct
    public void init() {
        log.info("url = {}", url);
        log.info("username = {}", username);
        log.info("password = {}", password);
        log.info("maxConnection = {}", maxConnection);
        log.info("timeout = {}", timeout);
        log.info("options = {}", options);
    }
}
```
- `application.properties`
```properties
my.datasource.url=local.db.com
my.datasource.username=username
my.datasource.password=password
my.datasource.etc.max-connection=1
my.datasource.etc.timeout=3500ms
my.datasource.etc.options=CACHE, ADMIN
```
> **참고**<br>
> `properties`는 자바의 낙타 표기법이 아니라 소문자와 `-`를 사용하는 캐밥 표기법을 주로 사용한다.<br>
> 이곳에 낙타 표기법을 사용한다고 문제가 되는 것은 아니지만 캐밥 표기법을 권장한다.

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MyDataSourceEnvConfig {

    private final Environment env;

    @Bean
    public MyDataSource myDataSource() {
        String url = env.getProperty("my.datasource.url");
        String username = env.getProperty("my.datasource.username");
        String password = env.getProperty("my.datasource.password");
        int maxConnection = env.getProperty("my.datasource.etc.max-connection", Integer.class);
        Duration timeout = env.getProperty("my.datasource.etc.timeout", Duration.class);
        List<String> options = env.getProperty("my.datasource.etc.options", List.class);

        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }
}
```
- `MyDataSource`를 스프링 빈으로 등록하는 설정 코드다.
- `Environment`를 사용하면 외부 설정의 종류와 관계없이 일관성 있게 외부 설정을 조회할 수 있다.
- `Environment.getProperty(key, Type)`를 호출할 때 타입 정보를 주면 해당 타입으로 변환해준다.(스프링 내부 변환기가 작동한다.)

```text
## 실행 결과

url=local.db.com 
username=local_user 
password=local_pw 
maxConnection=1 
timeout=PT3.5S 
options=[CACHE, ADMIN
```

`application.properties`에 필요한 외부 설정을 추가하고 `Environment`를 통해서 해당 값들을 읽어서 `MyDataSource`를 만들었다.<br>
향후 설정 데이터(`application.properties`)를 사용하다가 커맨드 라인 옵션 인수나 자바 시스템 속성으로 변경하는 외부 설정 방식이 달라져도
애플리케이션 코드를 그대로 유지할 수 있다.

**하지만 이 방식의 단점은 `Environment`를 직접 주입 받고 `env.getProperty(key)`를 통해서 값을 꺼내는 과정을 반복해야 한다는 점이다.<br>
스프링은 `@Value`라는 더욱 편리한 기능을 제공한다.**

## @Value

```java
import org.springframework.beans.factory.annotation.Value;
...

@Slf4j
@Configuration
public class MyDataSourceValueConfig {

    @Value("${my.datasource.url}")
    private String url;
    @Value("${my.datasource.username}")
    private String username;
    @Value("${my.datasource.password}")
    private String password;
    @Value("${my.datasource.etc.max-connection}")
    private int maxConnection;
    @Value("${my.datasource.etc.timeout}")
    private Duration timeout;
    @Value("${my.datasource.etc.options}")
    private List<String> options;

    @Bean
    public MyDataSource myDataSource1() {
        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }

    @Bean
    public MyDataSource myDataSource2(
            @Value("${my.datasource.url}") String url,
            @Value("${my.datasource.username}") String username,
            @Value("${my.datasource.password}") String password,
            @Value("${my.datasource.etc.max-connection}") int maxConnection,
            @Value("${my.datasource.etc.timeout}") Duration timeout,
            @Value("${my.datasource.etc.options}") List<String> options ) {

        return new MyDataSource(url, username, password, maxConnection, timeout, options);
    }
}
```
- `@Value`에 `${}`를 사용해서 외부 설정의 키 값을 주면 원하는 값을 주입 받을 수 있다.
- `@Value`는 필드에 사용할 수도 있고(`myDataSource1`), 파라미터에 사용할 수도 있다.(`myDataSource2`)

**기본값**<br>
만약 키를 찾지 못할 경우 코드에서 기본값을 사용하려면 `:`뒤에 기본값을 적어주면 된다.
- 예) `@Value("${my.datasource.etc.max-connection:1}")` : `key`가 없는 경우 `1`을 사용한다.

**이 방식의 단점은 `@Value`로 하나하나 외부 설정의 키 값을 입력받고 주입 받아와야 하는 부분이 번거롭다는 점이다. 그리고 설정 데이터를 보면
분리되어 있는 것이 아니라 정보의 묶음으로 되어있다.(`my.datasource`)<br>
이런 부분을 객체로 변환해서 사용할 수 있다면 더 편리하고 좋을 것 이다.**

## @ConfigurationProperties

**Type-safe Configuration Properties**<br>
스프링은 외부 설정의 묶음 정보를 객체로 변환하는 기능을 제공한다. 이것을 **타입 안전한 설정 속성**이라고 한다.<br>
**객체를 사용하면 타입을 사용할 수 있다.** 실수로 잘못된 타입이 들어오는 문제도 방지할 수 있고 객체를 통해서 활용할 수 있는 부분들이 많아진다.

```java
@Data
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV1 {

    private String url;
    private String username;
    private String password;
    private Etc etc;

    @Data
    public static class Etc {
        private int maxConnection;
        private Duration timeout;
        private List<String> options = new ArrayList<>();
    }
}
```
- 외부 설정을 주입 받을 객체에 각 필드를 외부 설정의 키 값에 맞춰 준비한다.
- `@ConfigurationProperties`이 있으면 외부 설정을 주입 받는 객체 라는 뜻이다. 여기에 외부 설정 `key`의 묶음 시작점을 적어주면 된다.
- 기본 주입 방식은 자바빈 프로퍼티 방식이다. `Getter`, `Setter`가 필요하다.

```java
@EnableConfigurationProperties(MyDataSourcePropertiesV1.class)
@RequiredArgsConstructor
public class MyDataSourceConfigV1 {

    private final MyDataSourcePropertiesV1 properties;

    @Bean
    public MyDataSource myDataSource() {
        return new MyDataSource(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getEtc().getMaxConnection(),
                properties.getEtc().getTimeout(),
                properties.getEtc().getOptions());
    }
}
```
- `@EnableConfigurationProperties(class)`
  - 스프링에게 사용할 `@ConfigurationProperties`를 지정해주어야 한다. 이렇게 하면 해당 클래스는 스프링 빈으로 등록되고 필요한 곳에서 주입 받아서 사용할 수 있다.

**@ConfigurationPropertiesScan**
- `@ConfigurationProperties`를 하나씩 직접 등록할 때는 `@EnableConfigurationProperties(class)`를 사용한다.
- `@ConfigurationProperties`를 특정 범위로 자동 등록할 때는 `@ConfigurationPropertiesScan(package)`을 사용하면 된다.
```java
@SpringBootApplication
@ConfigurationPropertiesScan("com.example.app")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExternalReadApplication.class, args);
    }
}
```
- 빈을 직접 등록하는 것과 컴포넌트 스캔을 사용하는 차이와 비슷하다.

**문제**<br>
`MyDataSourcePropertiesV1`은 스프링 빈으로 등록된다. 근데 `Setter`를 가지고 있기 때문에 누군가 실수로 값을 변경하는 문제가 발생할 수 있다.
여기에 있는 값들은 외부 설정값을 사용해서 초기에만 설정되고 이후에는 변경하면 안된다. <br>
이럴 때 `Setter`를 제거하고 대신에 생성자를 사용하면 중간에 데이터를 변경하는 실수를 방지할 수 있다.<br>
**좋은 프로그램은 제약이 있는 프로그램이다.**

## @ConfigurationProperties 생성자

```java
@Getter
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV2 {

    private String url;
    private String username;
    private String password;
    private Etc etc;

    public MyDataSourcePropertiesV2(String url, String username, String password, @DefaultValue Etc etc) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.etc = etc;
    }

    @Getter
    public static class Etc {
        private int maxConnection;
        private Duration timeout;
        private List<String> options;

        public Etc(int maxConnection, Duration timeout, @DefaultValue("DEFAULT") List<String> options) {
            this.maxConnection = maxConnection;
            this.timeout = timeout;
            this.options = options;
        }
    }
}
```
- 생성자를 통해서 설정 정보를 주입한다.
- `@DefaultValue` : 해당 값을 찾을 수 없는 경우 기본값을 사용한다.
  -  `@DefaultValue Class class` : 객체를 찾을 수 없을 경우 객체를 생성하고 내부에 들어가는 값은 비워둔다.(`null`, `0`)

> **참고**<br>
> 생성자가 둘 이상인 경우에는 사용할 생성자에 `@ConstructorBinding`어노테이션을 적용해야 한다. 생성자가 하나면 생략할 수 있다.

```java
@EnableConfigurationProperties(MyDataSourcePropertiesV2.class)
@RequiredArgsConstructor
public class MyDataSourceConfigV2 {

    private final MyDataSourcePropertiesV2 properties;

    @Bean
    public MyDataSource myDataSource() {
        return new MyDataSource(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getEtc().getMaxConnection(),
                properties.getEtc().getTimeout(),
                properties.getEtc().getOptions());
    }
}
```

이제 `Setter`가 없으므로 중간에 값이 변경되는 문제가 발생하지 않는다.

## @ConfigurationProperties 검증

`@ConfigurationProperties`에도 자바 빈 검증기를 사용할 수 있다.

```groovy
implementation 'org.springframework.boot:spring-boot-starter-validation' //추가
```

```java
@Getter
@ConfigurationProperties("my.datasource")
@Validated
public class MyDataSourcePropertiesV3 {

    @NotEmpty
    private String url;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    
    private Etc etc;

    public MyDataSourcePropertiesV3(String url, String username, String password, Etc etc) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.etc = etc;
    }

    @Getter
    public static class Etc {
        @Min(1)
        @Max(999)
        private int maxConnection;

        @DurationMin(seconds = 1)
        @DurationMax(seconds = 60)
        private Duration timeout;
        
        private List<String> options;

        public Etc(int maxConnection, Duration timeout, List<String> options) {
            this.maxConnection = maxConnection;
            this.timeout = timeout;
            this.options = options;
        }
    }
}
```
```java
@EnableConfigurationProperties(MyDataSourcePropertiesV3.class)
@RequiredArgsConstructor
public class MyDataSourceConfigV3 {

    private final MyDataSourcePropertiesV3 properties;

    @Bean
    public MyDataSource myDataSource() {
        return new MyDataSource(
                properties.getUrl(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getEtc().getMaxConnection(),
                properties.getEtc().getTimeout(),
                properties.getEtc().getOptions());
    }
}
```
- 이제 값이 검증 범위를 넘어가게 설정하면 오류가 발생한다.

**가장 좋은 예외는 컴파일 예외, 애플리케이션 로딩 시점에 발생하는 예외이다.** 가장 나쁜 예외는 고객 서비스 중에 발생하는 런타임 예외이다.