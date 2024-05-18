# HTTP Basic 인증

- **HTTP**는 액세스 제어와 인증을 위한 프레임워크를 제공하며 가장 일반적인 인증 방식은 `Basic` 인증 방식이다.
- RFC 7235 표준이며 인증 프로토콜은 HTTP 인증 헤더에 기술되어 있다.

![img_4.png](image/img_4.png)

---
## httpBasic()

- `HttpBasicConfigurer` 설정 클래스를 통해 여러 API들을 설정할 수 있다.
- 내부적으로 `BasicAuthenticationFilter`가 생성되어 기본 인증 방식의 인증 처리를 담당하게 된다.

![img_5.png](image/img_5.png)

```java
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(basic -> basic.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

        return http.build();
    }
}
```
```java

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("WWW-Authenticate", "Basic realm=security");
        response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }
}
```

---

## BasicAuthenticationFilter

- 기본 인증 서비스를 제공하는데 사용되는 필터이다.
- `BasicAuthenticationConverter`를 사용해서 요청 헤더에 기술된 인증 정보의 유효성을 체크하며 Base64 인코딩된 `username`과 `password`를 추출한다.
- 인증 이후 세션을 사용하는 경우와 사용하지 않는 경우에 따라 처리되는 흐름에 차이가 있다.
  - **세션을 사용하는 경우** : 매 요청 마다 인증 과정을 거치지 않는다.
  - **세션을 사용하지 않는 경우** : 매 요청 마다 인증 과정을 거쳐야 한다.

![img_6.png](image/img_6.png)

---

[이전 ↩️ - 폼 인증(formLogin())](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/AuthenticationProcess/FormLogin.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/main.md)

[다음 ↪️ - 기억하기 인증(rememberMe())](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/AuthenticationProcess/RememberMe.md)