# oauth2Client() - OAuth2ClientConfigurer 초기화

![img.png](image/img.png)

> - `oauth2Client()`는 **AuthorizationCodeGrantConfigurer**를 호출하면서 위 클래스들을 생성한다.
> - `OAuth2AuthorizationCodeGrantFilter`를 제외한 나머지 클래스들은 [권한 부여 코드 승인 방식]()에서 인가 서버로 **권한 부여 코드**를 요청할 때 사용하는 클래스다.
> - `OAuth2AuthorizationCodeGrantFilter`는 **액세스 토큰**을 발급받기 위해 사용하는 클래스다.

---

```java
@Configuration
@EnableWebSecurity
public class OAuth2ClientConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .anyRequest().authenticated()
                )
//                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
        ;
        return http.build();
    }
}
```

> - `oauth2Login()` : 인가 서버로부터 클라이언트가 인가를 받고, 최종 사용자가 인증 처리를 하는 데까지 기능들을 포함
> - `oauth2Client()` : 인가 서버로부터 클라이언트가 인가를 받는 과정만 처리

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/main.md)

[다음 ↪️ - OAuth 2.0 Client(oauth2Client) - OAuth2AuthorizedClient]()