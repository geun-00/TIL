# 비동기 인증 - Rest 인증 보안 및 화면 구성

> [다중 보안 설정](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/MultiSecurity/MultiSecurity.md)으로 비동기 인증과 폼 인증을 각각 처리하도록 한다.

### SecurityConfig

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;
    private final AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 폼 인증 설정
     */ 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.*", "/*/icon-*").permitAll() //정적 자원 관리
                .requestMatchers("/", "/signup", "/login*").permitAll()
                .requestMatchers("/user").hasRole("USER")
                .requestMatchers("/manager").hasRole("MANAGER")
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").permitAll()
                .authenticationDetailsSource(authenticationDetailsSource)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
            )
            .authenticationProvider(authenticationProvider)
            .exceptionHandling(exception -> exception
                .accessDeniedHandler(new FormAccessDeniedHandler("/denied"))
            )
        ;

        return http.build();
    }

    /**
     * 비동기 인증 설정
     */
    @Bean
    @Order(1) //Rest 방식의 요청을 먼저 처리하도록 순서를 지정
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/login")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.*", "/*/icon-*").permitAll() //정적 자원 관리
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable) 
                //Rest 방식의 비동기 설정은 클라이언트에서 직접 CSRF 값을 직접 전달해 주어야 한다. 일단 비활성화
        ;

        return http.build();
    }
}
```

### Controller
```java
@Controller
public class LoginController {
    
    @GetMapping("/api/login")
    public String restLogin() {
        return "rest/login";
    }
}
```

### rest/login.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
...
    <script>
        function login() {
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-Requested-With': 'XMLHttpRequest' //Ajax 통신임을 서버에 전달하기 위한 헤더 설정
                },
                body: JSON.stringify({ username, password }),
            })
                    .then(response => {
                        if(response.ok){
                            window.location.replace("/");
                            console.log(data);
                        }
                    })
                    .catch(error => {
                        console.error('Error during login:', error);
                    });
        }
    </script>
</head>
<body>
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
                    <span th:text="${exception} ?: '잘못된 아이디나 암호입니다'" class="alert alert-danger"></span>
                </div>
                <div class="form-group">
                    <label for="username">Username</label>
                    <input type="text" class="form-control" id="username" required>
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" required>
                </div>
                <button onclick="login()" class="btn btn-primary">Login</button>
            </div>
        </div>
    </div>
</div>
<div class="footer" th:replace="~{layout/footer::footer}"></div>
</body>
</html>
```

---

[이전 ↩️ - 회원 인증 시스템 - 커스텀 접근 제한하기](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%A0%91%EA%B7%BC%EC%A0%9C%ED%95%9C/Main.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/main.md)

[다음 ↪️ - 비동기 인증 - Rest 인증 필터 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/%EC%9D%B8%EC%A6%9D%ED%95%84%ED%84%B0/Main.md)
