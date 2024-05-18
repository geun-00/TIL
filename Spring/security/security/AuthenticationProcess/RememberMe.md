# 기억하기 인증

---
## RememberMe 인증

- 사용자가 웹 사이트나 애플리케이션에 로그인할 때 자동으로 인증 정보를 기억하는 기능이다.
- `UsernamePasswordAuthenticationFilter`와 함께 사용되며, `AbstractAuthenticationProcessingFilter` 슈퍼 클래스에서 훅을 통해 구현된다.
- 인증 성공 시 `RemeberMeServices.loginSuccess()`를 통해 **RememberMe 토큰**을 생성하고 쿠키로 전달한다.
- 인증 실패 시 `RemeberMeServices.loginFail()`을 통해 쿠키를 지운다.
- **LogoutFilter** 와 연계해서 로그아웃 시 쿠키를 지운다.

---
## 토큰 생성

- 기본적으로 암호화된 토큰으로 생성 되어지며 브라우저에 쿠키를 보내고, 향후 세션에서 이 쿠키를 감지하여 자동 로그인이 이루어지는 방식으로 달성된다.

![img_7.png](image/img_7.png)
- `username` : **UserDetailsService**로 식별 가능한 사용자 이름
- `password` : 검색된 **UserDetails**에 일치하는 비밀번호
- `expirationTime` : **remember-me** 토큰이 만료되는 날짜와 시간, 밀리초로 표현
- `key` : **remember-me** 토큰의 수정을 방지하기 위한 개인 키
- `algorithmName` : **remember-me** 초큰 서명을 생성하고 검증하는 데 사용되는 알고리즘(기본적으로 SHA-256 알고리즘을 사용)

---
## RememberMeServices 구현체
- **TokenBasedRememberMeServices** : 쿠키 기반 토큰의 보안을 위해 해싱을 사용한다.(메모리 기반)
- **PersistentTokenBasedRememberMeServices** : 생성된 토큰을 저장하기 위해 데이터베이스나 다른 영구 저장 매체를 사용한다.
- 두 구현 모두 사용자의 정보를 검색하기 위한 `UserDetailsService`가 필요하다.

---
## rememberMe()

- `RememberMeConfigurer` 설정 클래스를 통해 여러 API 들을 설정할 수 있다.
- 내부적으로 `RememberMeAuthenticationFilter`가 생성되어 자동 인증 처리를 담당하게 된다.

![img_8.png](image/img_8.png)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .rememberMe(rememberBe -> rememberBe
                        .alwaysRemember(true)
                        .tokenValiditySeconds(3600)
                        .userDetailsService(userDetailsService())
                        .rememberMeParameter("remember")
                        .rememberMeCookieName("remember")
                        .key("security")
                );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}1111")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
```

---

## RememberMeAuthenticationFilter

- `SecurityContextHolder`에 `Authentication`이 포함되지 않는 경우 실행되는 필터이다.
- 세션이 만료되었거나 애플리케이션 종료로 인해 인증 상태가 소멸된 경우 토큰 기반 인증을 사용해 유효성을 검사하고 토큰이 검증되면 자동 로그인 처리를 수행한다.

![img_9.png](image/img_9.png)

---

[이전 ↩️ - 기본 인증(httpBasic())](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationProcess/HttpBasic.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/main.md)

[다음 ↪️ - 익명 인증 사용자(anonymous())](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationProcess/Anonymous.md)