# Redis를 활용한 이중화 설정

- **이중화**는 시스템의 부하를 분산하고, 단일 실패 지점(Single Point of Failure, `SPOF`) 없이 서비스를 지속적으로 제공하는 아키텍처를 구현하는 것을 목표로 하며
    스프링 시큐리티는 이러한 이중화 환경에서 인증, 권한 부여, 세션 관리 등의 보안 기능을 제공한다.
- 스프링 시큐리티는 사용자 세션을 안전하게 관리하며 이중화된 환경에서 세션 정보를 공유할 수 있는 메커니즘을 제공하며 대표적으로 **레디스** 같은 분산 캐시를 사용하여
    세션 정보를 여러 서버 간에 공유할 수 있다.

---

- 라이브러리 추가

```text
implementation 'org.springframework.boot:spring-boot-starter-data-redis'
implementation 'org.springframework.session:spring-session-data-redis'
```

- 레디스 설정 클래스

```java
@Configuration
@EnableRedisHttpSession
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

}
```
```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
```

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user").hasAuthority("ROLE_USER")
                        .requestMatchers("/db").hasAuthority("ROLE_DB")
                        .requestMatchers("/admin").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
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

- 레디스를 사용한 이중화 설정으로 서버 두 개중 하나가 꺼져도 동일한 세션 쿠키만 가지고 인증 상태를 유지할 수 있다.
- 레디스 설정을 하지 않으면 스프링은 각 서버의 WAS 별로 세션을 생성하고, 레디스 설정을 하면 두 개의 서버가 같은 레디스 저장소를 보기 때문에 세션 쿠키의 공유가 가능하다.

---

[이전 ↩️ - Custom DSL](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/MultiSecurity/CustomDSL.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/main.md)