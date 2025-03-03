# SessionManagementFilter

- 요청이 시작된 이후 사용자가 인증되었는지 감지하고, 인증된 경우에는 세션 고정 보호 메커니즘을 활성화 하거나 동시 다중 로그인을
    확인하는 등 세션 관련 활동을 수행하기 위해 설정된 세션 인증 전략(`SessionAuthenticationStrategy`)을 호출하는 필터 클래스이다.
- 스프링 시큐리티 6 이상에서는 `SessionManagementFilter`가 기본적으로 설정 되지 않으며 세션 관리 API 설정을 통해 생성할 수 있다.

![img_6.png](image/img_6.png)

---

# ConcurrentSessionFilter

- 각 요청에 대해 `SessionRegistry`에서 **SessionInformation**을 검색하고 세션이 만료로 표시되었는지 확인하고, 만료로 표시된 경우 로그아웃 처리를 수행한다.(세션 무효화)
- 각 요청에 대해 `SessionRegistry.refreshLastRequest(String)`를 호출하여 등록된 세션들이 **항상 마지막 업데이트 날짜/시간**을 가지도록 한다.

![img_7.png](image/img_7.png)

- **시퀀스 다이어그램**

![img_8.png](image/img_8.png)

---

## SessionRegistry 사용 예제

- `SessionRegistry`는 현재 접속되어 있는 사용자의 세션 정보를 가져올 수 있는 API를 제공한다.

![img.png](image_1/img.png)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .maximumSessions(2)
                        .maxSessionsPreventsLogin(false)
                )
        ;

        return http.build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
```
```java
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionInfoService {

    private final SessionRegistry sessionRegistry;

    public void sessionInfo() {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
            for (SessionInformation session : allSessions) {
                log.info("사용자 = {}", principal);
                log.info("세션 ID = {}", session.getSessionId());
                log.info("최종 요청 시간 = {}", session.getLastRequest());
                log.info("----------------------------------------------");
            }
        }
    }
}
```

---

# 세션 설정 초기화 과정 디버깅

## 1. SessionManagementConfigurer

- 설정 클래스에서 `maximumSessions`를 설정할 시 `SessionManagementConfigurer`의 `configure()` 과정에 중요한
영향을 준다.
- `propertiesThatRequireImplicitAuthentication`이라는 `Set<String>` 자료형에 데이터를 추가하는 로직을 기억하자.

![img_1.png](image_1/img_1.png)

![img_2.png](image_1/img_2.png)

## 2. SessionManagementConfigurer - init()

- 세션 생성 정책이 `stateless`인지에 따라 초기화되는 클래스가 달라진다.
- 그리고 `getSessionAuthenticationStrategy()`를 통해 각각의 세션 인증 전략을 초기화하고 저장한다.

![img_3.png](image_1/img_3.png)

![img_4.png](image_1/img_4.png)

## 3. SessionManagementConfigurer - configure()

- 이 과정에서 설정 클래스에서 `maximumSessions`를 설정하는 이유가 나온다.
- `maximumSessions`를 설정하지 않는다면 세션 인증에 필요한 필터, 핸들러 등이 전혀 설정되지 않는다.
- 따라서 명시적인 `maximumSessions` 설정이 필요하다.
- 그리고 세션 생성 정책(`SessionCreationPolicy`)가 `ALWAYS`면 `ForceEagerSessionCreationFilter`를 추가하는
것을 확인할 수 있다.

![img_5.png](image_1/img_5.png)

![img_6.png](image_1/img_6.png)

![img_7.png](image_1/img_7.png)

---

# 인증 이후 과정 디버깅 (maximumSessions 미만)

## 1. AbstractAuthenticationProcessingFilter

- 폼 인증 등의 인증 과정이 끝나고 최종 인증 성공의 작업을 하기 전에 `sessionStrategy.onAuthentication()`을 호출한다.
- 여기서 `sessionStrategy`는 `CompositeSessionAuthenticationStrategy` 클래스로 시큐리티 초기화 과정에서
인증 전략 관련 클래스들이 저장되어 있다.

![img_8.png](image_1/img_8.png)

![img_9.png](image_1/img_9.png)

![img_10.png](image_1/img_10.png)

## 2. ConcurrentSessionControlAuthenticationStrategy

- 위의 과정에서 주의깊게 봐야 할 세션 인증 전략은 이 클래스이다.
- 현재 사용자의 세션이 설정 클래스에서 설정한 `maximumSessions`보다 작으면 전혀 문제가 없는 것이므로 바로 로직을 종료한다.

![img_11.png](image_1/img_11.png)

## 3. ConcurrentSessionFilter

- 이 필터의 역할은 등록된 세션을 항상 마지막 업데이트 시간으로 갱신하는 것이다.
- 정상적인 흐름이므로 문제 없이 세션을 업데이트할 수 있다.

![img_12.png](image_1/img_12.png)

---

# 인증 이후 과정 디버깅 (maximumSessions 이상)

## 1. AbstractAuthenticationProcessingFilter

![img_13.png](image_1/img_13.png)

## 2. ConcurrentSessionControlAuthenticationStrategy

- 같은 사용자의 세션이 하나 추가되어 최대 세션 허용 수와 같아졌다.
- 예외 상황이므로 예외 처리 로직을 수행하게 된다.
- 다만 등록된 세션 중에 현재 세션과 같은 세션 ID가 있으면 현재 사용 중인 사용자가 사용하고 있는 것이므로 이 경우 예외 처리 로직을 수행하지 않는다.
- 그런 세션이 없다면 동일한 계정을 다른 세션 ID로 접근했다고 판단하고 예외 처리 로직을 수행한다.

![img_14.png](image_1/img_14.png)

- `allowableSessionsExceeded()` 메서드는 설정 클래스에서 설정한 `maxSessionsPreventsLogin` 값에 따라 다르게 수행된다.
- 만약 `maxSessionsPreventsLogin` 값이 `false`면, 최근 생성된 세션들을 제외하고 나머지 세션들은 **세션 만료 설정**을 해준다.
- 만약 `maxSessionsPreventsLogin` 값이 `false`면, 바로 예외를 던지게 된다. 이 예외는 인증 필터까지 다시 전달되어
인증 실패 로직을 처리하게 된다.

![img_15.png](image_1/img_15.png)

(`maxSessionsPreventsLogin == false`)

![img_19.png](image_1/img_19.png)

![img_20.png](image_1/img_20.png)

## 3. ConcurrentSessionFilter

- 위 과정에서 세션이 만료로 설정된 세션으로 재요청을 하면 다음 필터로 넘어가지 못하고 로그아웃 처리를 해버린다.

![img_16.png](image_1/img_16.png)

![img_18.png](image_1/img_18.png)

![img_17.png](image_1/img_17.png)

---

[이전 ↩️ - 세션 생성 정책(`sessionCreationPolicy()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/SessionManagement/SessionCreationPolicy.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/main.md)