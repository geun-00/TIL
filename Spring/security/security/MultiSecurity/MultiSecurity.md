# 다중 보안 설정

- 스프링 시큐리티는 여러 `SecurityFilerChain` 빈을 등록해서 다중 보안 기능을 구성할 수 있다.

![img.png](image/img.png)

---

## 다중 보안 설정 초기화 구성

![img_1.png](image/img_1.png)

---

## 다중 보안 설정 요청 흐름도

![img_2.png](image/img_2.png)

- `HttpSecurity` 인스턴스 별로 보안 기능이 작동한다.
- 요청에 따라 `RequestMatcher`와 매칭되는 필터가 작동된다.

---

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @Order
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults())
        ;

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {

        http
                .securityMatchers(matchers -> matchers
                        .requestMatchers("/api/**"))

                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll())
        ;

        return http.build();
    }

   @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{noop}1111")
                .roles("USER")
                .build();

        UserDetails manager = User.withUsername("db")
                .password("{noop}1111")
                .roles("DB")
                .build();

       UserDetails admin = User.withUsername("admin")
               .password("{noop}1111")
               .roles("ADMIN", "SECURE")
               .build();

        return new InMemoryUserDetailsManager(user, manager, admin);
    }
}
```

![img_3.png](image/img_3.png)

- 일반적으로 `SecurityFilerChain`은 한 개지만, 다중으로 설정했기 때문에 두 개가 생성되었다.
- `RequestMatcher`마다 처리해야 하는 방식이 다르기 때문에 필터의 개수에 차이가 있다.
- `FilterChainProxy`는 각 요청마다 알맞은 `SecurityFilterChain`을 가져와 해당 필터들로 처리한다.
- 만약 `@Order`를 통해 넓은 범위의 URL을 먼저 설정했을 경우 의도하지 않게 동작할 수 있다.

---

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/main.md)

[다음 ↪️ - Custom DSL](https://github.com/genesis12345678/TIL/blob/main/Spring/security/MultiSecurity/CustomDSL.md)