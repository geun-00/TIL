# OAuth 2.0 Resource Server MAC & RSA 토큰 검증 - JwtDecoder 에 의한 검증(MAC)

### JwtDecoderConfig

```java
@Configuration
public class JwtDecoderConfig {

    @Bean
    @ConditionalOnProperty(prefix = "spring.security.oauth2.resourceserver.jwt", name = "jws-algorithms", havingValue = "HS256", matchIfMissing = false)
    public JwtDecoder jwtDecoderBySecretKeyValue(OctetSequenceKey octetSequenceKey, OAuth2ResourceServerProperties properties) {
        
        return NimbusJwtDecoder.withSecretKey(octetSequenceKey.toSecretKey())
                .macAlgorithm(MacAlgorithm.from(properties.getJwt().getJwsAlgorithms().get(0)))
                .build();
    }
}
```
> - `SecretKey` 기반 **JwtDecoder** 생성
> - 대칭키 방식으로 생성된 토큰을 검증하기 위해 `JWK`를 상속한 `OctetSequenceKey`로 SecretKey 기반 `JwtDecoder`를 생성한다.

### application.yml

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          jws-algorithms: HS256
```

### SecurityConfig

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MacSecuritySigner macSecuritySigner;
    private final OctetSequenceKey octetSequenceKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService());
        AuthenticationManager authenticationManager = builder.build();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated())
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthenticationFilter(macSecuritySigner, octetSequenceKey, authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .oauth2ResourceServer(config -> config.jwt(Customizer.withDefaults()))
        ;

        return http.build();
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter(MacSecuritySigner macSecuritySigner, OctetSequenceKey octetSequenceKey, 
                                                           AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(macSecuritySigner, octetSequenceKey);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        return jwtAuthenticationFilter;
    }
    
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails user = User.withUsername("user")
                .password("1234")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
```

> 기존에 필터를 제거하고 `JwtDecoder`에 의한 검증이 되도록 API 설정
 
---

[이전 ↩️ - OAuth 2.0 Resource Server MAC & RSA 토큰 검증 - JwtAuthorizationMacFilter(MAC)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/MAC_RSA/JwtAuthorizationMacFilter.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/main.md)

[다음 ↪️ - OAuth 2.0 Resource Server MAC & RSA 토큰 검증 - JwtAuthorizationRsaFilter(RSA)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/MAC_RSA/JwtAuthorizationRsaFilter.md)