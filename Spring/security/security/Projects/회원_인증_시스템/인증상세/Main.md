# 회원 인증 시스템 - 커스텀 인증상세 구현

---

## WebAuthenticationDetails

- HTTP 요청과 관련된 인증 세부 정보를 포함하는 클래스로서 기본적으로 사용자의 IP 주소와 세션 ID와 같은 정보를 가지고 있다.
- 특정 인증 메커니즘에서 요청의 추가적인 정보를 인증 객체에 추가할 때 사용할 수 있으며 `Authentication` 객체와 함께 사용된다.

---

## AuthenticationDetailsService

- 인증 과정 중에 `Authentication` 객체에 세부 정보를 제공하는 소스 역할을 한다.
- **WebAuthenticationDetails** 객체를 생성하는 데 사용되며 인증 필터에서 참조한다.

![img.png](img.png)

---

### login.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
...
    <div th:replace="~{layout/header::header}"></div>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-2 sidebar">
                <div th:replace="~{layout/sidebar::sidebar}"></div>
            </div>
            <div class="col-md-10 content">
                <div class="login-form">
                    <h2>Login</h2>
                    <div th:if="${param.error}" class="form-group">
                        <span th:text="${exception}?: '잘못된 아이디나 암호입니다'" class="alert alert-danger"></span>
                    </div>
                    <form th:action="@{/login}" method="post">
                        <input type="hidden" th:value="secret" th:name="secret_key"/> //추가
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" class="form-control" id="username" name="username" required>
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" class="form-control" id="password" name="password" required>
                        </div>
                        <button type="submit" class="btn btn-primary">Login</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="footer" th:replace="~{layout/footer::footer}"></div>
    </body>
</html>
```

> 로그인 폼을 보내면서 `secret`이라는 값을 같이 보내고 이것을 활용하도록 할 수 있다.

### FormAuthenticationDetails

```java
@Getter
public class FormAuthenticationDetails extends WebAuthenticationDetails {

    private final String secretKey;

    public FormAuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.secretKey = request.getParameter("secret_key");
    }
}
```

### FormWebAuthenticationDetailsSource
```java
@Component
public class FormWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new FormAuthenticationDetails(request);
    }
}
```

### SecurityConfig
```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.*", "/*/icon-*").permitAll() //정적 자원 관리
                        .requestMatchers("/", "/signup").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login").permitAll() //커스텀 로그인 페이지
                        .authenticationDetailsSource(authenticationDetailsSource)
                )
                .authenticationProvider(authenticationProvider)
        ;

        return http.build();
    }
}
```

### FormAuthenticationProvider
```java
@Component("authenticationProvider")
@RequiredArgsConstructor
public class FormAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String loginId = authentication.getName();
        String password = (String) authentication.getCredentials();

        AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(loginId);

        if (!passwordEncoder.matches(password, accountContext.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        //추가
        String secretKey = ((FormAuthenticationDetails) authentication.getDetails()).getSecretKey();
        if (secretKey == null || !secretKey.equals("secret")) {
            throw new SecretException("Invalid secret");
        }

        return new UsernamePasswordAuthenticationToken(
                accountContext.getAccountDto(), null, accountContext.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
```
```java
import org.springframework.security.core.AuthenticationException;

public class SecretException extends AuthenticationException {

    public SecretException(String message) {
        super(message);
    }
}
```

> 기존에 `username`과 `password`만 검증하던 과정에 추가적인 정보까지 검증하는 과정을 추가했다.

---

[이전 ↩️ - 회원 인증 시스템 - 커스텀 로그아웃](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/Logout/Main.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/main.md)

[다음 ↪️ - 회원 인증 시스템 - 커스텀 인증성공 핸들러](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%9D%B8%EC%A6%9D%EC%84%B1%EA%B3%B5%ED%95%B8%EB%93%A4%EB%9F%AC/Main.md)
