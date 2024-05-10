# 스프링 MVC 비동기 통합

- 스프링 시큐리티는 **Spring MVC Controller**에서 `Callable`을 실행하는 비동기 스레드에 `SecurityContext`를 자동으로 설정하도록 지원한다.
- 스프링 시큐리티는 `WebAsyncManager`와 통합하여 `SecurityContextHolder`에서 사용 가능한 `SecurityContext`를 **Callable**에서 접근 가능하도록 해준다.

---

## WebAsyncManagerIntegrationFilter

- **SecurityContext**와 `WebAsyncManager` 사이의 통합을 제공하며 `WebAsyncManager`를 생성하고 **SecurityContextCallableProcessingInterceptor**를 `WebAsyncManager`에 등록한다.

---

## WebAsyncManager

- 스레드 풀의 비동기 스레드를 생성하고 `Callable`을 받아 실행시키는 주체로서 등록된 **SecurityContextCallableProcessingInterceptor**를 통해 현재 스레드(부모 스레드)가
    보유하고 있는 `SecurityContext` 객체를 비동시 스레드(자식 스레드)의 `ThreadLocal`에 저장시킨다.

---

## 예제 코드

![img_6.png](image/img_6.png)

- 비동기 스레드(자식 스레드)가 수행하는 `Callable` 영역 내에서 자신의 **ThreadLocal**에 저장된 `SecurityContext`를 참조할 수 있으며 이는 부모 스레드가 가지고 있는 `SecurityContext`와 동일한 객체이다.
- `@Async`나 다른 비동기 기술은 스프링 시큐리티와 통합되어 있지 않기 때문에 비동기 스레드에 `SecurityContext`가 적용되지 않는다.

### 흐름도

![img_7.png](image/img_7.png)

---

## Callable 적용

```java
@RestController
@Slf4j
public class IndexController {

    /*
        ...
    */

    @GetMapping("/callable")
    public Callable<Authentication> call() {
        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().getContext();
        log.info("context = {}", context);
        log.info("Parent Thread = {}", Thread.currentThread().getName());

        return new Callable<Authentication>() {
            @Override
            public Authentication call() throws Exception {
                SecurityContext context = SecurityContextHolder.getContextHolderStrategy().getContext();
                log.info("context = {}", context);
                log.info("Child Thread = {}", Thread.currentThread().getName());

                return context.getAuthentication();
            }
        };
    }
}
```
```text
context = SecurityContextImpl [Authentication=UsernamePasswordAuthenticationToken [Principal=org.springframework.security.core.userdetails.User [Username=user, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]], Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_USER]]]
Parent Thread = http-nio-8080-exec-4
context = SecurityContextImpl [Authentication=UsernamePasswordAuthenticationToken [Principal=org.springframework.security.core.userdetails.User [Username=user, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]], Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_USER]]]
Child Thread = task-1
```

- 스레드는 다르지만, `SecurityContext`는 같은 것을 확인할 수 있다.

## 다른 비동기 기술 적용

```java
@Service
@Slf4j
public class AsyncService {

    @Async
    public void asyncMethod() {
        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().getContext();
        log.info("context = {}", context);
        log.info("Child Thread = {}", Thread.currentThread().getName());
    }
}
```
```java
@RestController
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final AsyncService asyncService;
    
    /*
            ...
     */

    @GetMapping("/async")
    public Authentication async() {

        //부모 쓰레드
        SecurityContext context = SecurityContextHolder.getContextHolderStrategy().getContext();
        log.info("context = {}", context);
        log.info("Parent Thread = {}", Thread.currentThread().getName());

        //자식 쓰레드
        asyncService.asyncMethod();

        return context.getAuthentication();
    }
}
```
```java
@SpringBootApplication
@EnableAsync
public class SpringsecuritymasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringsecuritymasterApplication.class, args);
    }

}
```

- `@EnableAsync`를 활성화해야 `@Async`가 적용이 된다.

```text
context = SecurityContextImpl [Authentication=AnonymousAuthenticationToken [Principal=anonymousUser, Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_ANONYMOUS]]]
Parent Thread = http-nio-8080-exec-1
context = SecurityContextImpl [Null authentication]
Child Thread = task-1
```

- 스레드 간 `SecurityContext`가 공유하지 못하는 것을 확인할 수 있다.
- `Callable`을 사용해야 스레드 간의 `SecurityContext`를 공유할 수 있다.

`Callable`이 아닌 다른 기술로 비동기 통합을 하려면 다음과 같이 설정할 수 있다.

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user").hasAuthority("ROLE_USER")
                        .requestMatchers("/db").hasAuthority("ROLE_DB")
                        .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
        ;

        //부모 스레드로부터 자식 스레드로 보안 컨텍스트가 상속하는 설정
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
        return http.build();
    }
}
```