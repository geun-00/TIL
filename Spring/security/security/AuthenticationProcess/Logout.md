# 로그아웃

- 스프링 시큐리티는 기본적으로 `DefaultLogoutPageGeneratingFilter`를 통해 로그아웃 페이지를 제공하며 **`GET` /logout** URL로 접근이 가능하다.
- 로그아웃 실행은 기본적으로 **`POST` /logout** 으로만 가능하다 CSRF 기능을 비활성화 할 경우 또는 `RequestMatcher`를 사용할 경우 `GET`, `PUT`, `DELETE` 모두 가능하다.
- 로그아웃 필터를 거치지 않고 스프링 MVC 에서 커스텀하게 구현할 수 있으며 로그인 페이지가 커스텀하게 생성될 경우 로그아웃 기능도 커스텀하게 구현해야 한다.

---

## logout()

![img_14.png](image/img_14.png)

---

## LogoutFilter

![img_15.png](image/img_15.png)

---

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/logoutSuccess").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                        .logoutSuccessUrl("/logoutSuccess")
                        .logoutSuccessHandler((request, response, authentication) ->
                                response.sendRedirect("/logoutSuccess"))

                        .deleteCookies("JSESSIONID", "remember-me")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .addLogoutHandler((request, response, authentication) -> {
                            HttpSession session = request.getSession();
                            session.invalidate();
                            SecurityContextHolder.getContextHolderStrategy().getContext().setAuthentication(null);
                            SecurityContextHolder.getContextHolderStrategy().clearContext();
                        })
                        .permitAll()
                );


        return http.build();
    }
}
```

---

# 로그아웃 과정 디버깅

## 1. LogoutFilter

- 요청이 로그아웃에 해당하는 요청이면 로그아웃 로직을 수행하고, 다음 필터로 넘어가지 않는다.
- 여기서 `LogoutHandler`는 설정 클래스에서 직접 설정한 핸들러 외에 스프링이 내부적으로 사용하는 핸들러가 들어 있다.

![img_26.png](image_1/img_26.png)

## 2. CompositeLogoutHandler

- 등록된 `LogoutHandler`를 모두 반복하면서 로그아웃 과정을 수행한다.

![img_27.png](image_1/img_27.png)

### 2-1. CookieClearingLogoutHandler

- 설정 클래스 `deleteCookies()`에서 설정한 쿠키 이름들에 대해 모두 삭제용 쿠키를 생성한다.

![img_28.png](image_1/img_28.png)

![img_29.png](image_1/img_29.png)

### 2-2. CustomLogoutHandler

- 설정 클래스에서 설정한 핸들러

![img_30.png](image_1/img_30.png)

### 2-3. CsrfLogoutHandler

- CSRF 토큰을 삭제한다.

![img_31.png](image_1/img_31.png)

### 2-4. SecurityContextLogoutHandler

- `SecurityContext`를 비우고, 인증 객체를 제거한다.

![img_32.png](image_1/img_32.png)

### 2-5. LogoutSuccessEventPublishingLogoutHandler

- `LogoutSuccessEvent`를 발행

![img_33.png](image_1/img_33.png)

---

[이전 ↩️ - 익명 인증 사용자(anonymous())](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/AuthenticationProcess/Anonymous.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/main.md)

[다음 ↪️ - 요청 캐시(RequestCache & SavedRequest)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/AuthenticationProcess/RequestCache.md)