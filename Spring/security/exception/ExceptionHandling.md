# 예외 처리

- 예외 처리는 필터 체인 내에서 발생하는 예외를 의미하며 크게 인증 예외(`AuthenticationException`)와 인가 예외(`AccessDeniedException`)로 나눌 수 있다.
- 예외를 처리하는 필터로서 `ExceptionTranslationFilter`가 사용되며 사용자의 인증 및 인가 상태에 따라 로그인 재시도, 401, 403 코드 등으로 응답할 수 있다.

---

## 예외 처리 유형

### AuthenticationException
1. **SecurityContext 에서 인증 정보 삭제**
   - 기존의 `Authentication`이 더 이상 유효하지 않다고 판단하고 `Authentication`을 초기화 한다.
2. **AuthenticationEntryPoint 호출**
   - `AuthenticationException`이 감지되면 필터는 `AuthenticationEntryPoint`를 실행하고 이를 통해 인증 실패를 공통적으로 처리할 수 있으며 일반적으로 인증을 시도할 수 있는 화면으로 이동한다.
3. **인증 프로세스의 요청 정보를 저장하고 검색**
   - `RequestCache & SavedRequest` - 인증 프로세스 동안 전달되는 요청을 세션 혹은 쿠키에 저장
   - 사용자가 인증을 완료한 후 요청을 검색하여 재사용할 수 있다. 
   - 기본 구현은 `HttpSessionRequestCache` 이다.

### AccessDeniedException

- **AccessDeniedHandler 호출**
  - `AccessDeniedException`이 감지되면 필터는 사용자가 익명 사용자인지 여부를 판단하고 익명 사용자인 경우 인증 예외처리가 실행되고 익명 사용자가 아닌 경우
    필터는 **AccessDeniedHandler** 에게 위임한다.

---

## exceptionHandling()

![img.png](image/img.png)

- **AuthenticationEntryPoint 는 인증 프로세스마다 기본적으로 제공되는 클래스들이 설정된다.** 
  - `UsernamePasswordAuthenticationFilter` - `LoginUrlAuthenticationEntryPoint`
  - `BasicAuthenticationFilter` - `BasicAuthenticationEntryPoint`
  - 아무런 인증 프로세스가 설정 되지 않으면 기본적으로 `Http403ForbiddenEntryPoint`가 사용된다.
  - 사용자 정의 `AuthenticationEntryPoint` 구현이 가장 우선적으로 수행되며 이 때는 **기본 로그인 페이지 생성이 무시된다.**
- **AccessDeniedHandler 는 기본적으로 AccessDeniedHandlerImpl 클래스가 사용된다.**

---

```java
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.info(authException.getMessage());
                            response.sendRedirect("/login");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.info(accessDeniedException.getMessage());
                            response.sendRedirect("/denied");
                        })
                )
        ;

        return http.build();
    }
}
```

---

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/main.md)

[다음 ↪️ - 예외 펄티(`ExceptionTranslationFilter`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/exception/ExceptionTranslationFilter.md)