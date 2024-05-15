# 비동기 인증 - RestAuthenticationProvider 구현

---

## RestAuthenticationProvider

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final FormAuthenticationProvider formAuthenticationProvider;
    private final RestAuthenticationProvider restAuthenticationProvider;

    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    private final FormAuthenticationSuccessHandler formSuccessHandler;
    private final FormAuthenticationFailureHandler formFailureHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {...}

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(restAuthenticationProvider);
        AuthenticationManager authenticationManager = managerBuilder.build();

        http
                .securityMatcher("/api/login")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.*", "/*/icon-*").permitAll() //정적 자원 관리
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(restAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(authenticationManager)
        ;

        return http.build();
    }

    private RestAuthenticationFilter restAuthenticationFilter(AuthenticationManager authenticationManager) {
        RestAuthenticationFilter restAuthenticationFilter = new RestAuthenticationFilter();
        restAuthenticationFilter.setAuthenticationManager(authenticationManager);

        return restAuthenticationFilter;
    }
}
```

---

[이전 ↩️ - 비동기 인증 - Rest 인증 필터 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/%EC%9D%B8%EC%A6%9D%ED%95%84%ED%84%B0/Main.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/main.md)

[다음 ↪️ - 비동기 인증 - Rest 인증 성공 및 실패 핸들러](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/%EC%9D%B8%EC%A6%9D%ED%95%B8%EB%93%A4%EB%9F%AC/Main.md)