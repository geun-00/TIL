# 회원 인증 시스템 - 사용자 정의 보안 설정 및 기본 사용자 구성

---

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll() //루트 경로(/) 전체 허용
                        .anyRequest().authenticated() //나머지 경로 
                )
                .formLogin(Customizer.withDefaults()); //폼 로그인 방식 스프링 시큐리티 기본 구성 사용

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}1111").roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }
}
```

> `password`에 접두사 `{noop}`를 붙여주면 암호화 되지 않은 평문을 사용할 수 있다고 한다.

---

[이전 ↩️ - 회원 인증 시스템 - 프로젝트 생성 및 기본 구성]()

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/main.md)

[다음 ↪️ -회원 인증 시스템 - 로그인 페이지 만들기]()