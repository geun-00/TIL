# 회원 관리 시스템 - 필터에 의한 DB 연동

- 프로그래밍 방식의 인가 처리를 위해서 `CustomDynamicAuthorizationManager`를 만들었다.
- 이 클래스를 호출하는 클래스는 스프링 시큐리티의 `RequestMatcherDelegatingAuthorizationManager`이다.
- 직접 만든 `CustomDynamicAuthorizationManager` 클래스가 인가 처리를 하기 위해서 **SecurityConfig** 설정 중 `access(authorizationManager)`를 설정했다.
- 이렇게 되면 초기화 과정 속에서 `RequestMatcherDelegatingAuthorizationManager`의 **mapping** 속성에 `CustomDynamicAuthorizationManager`가 저장된다.
- 최종 구조는 사용자의 요청은 우선 `RequestMatcherDelegatingAuthorizationManager`가 받고, `CustomDynamicAuthorizationManager`의 **check()** 메서드를 호출한다.
- 사용자의 요청을 처음부터 `CustomDynamicAuthorizationManager`에서 받으면 좀 더 괜찮은 구조가 될 수 있다.

`access(authorizationManager)` 설정으로는 순서를 변경할 수 없고, 필터를 사용해야 한다.

---

## AuthorizationFilter

![img.png](img.png)

> 1. `AuthorizationFilter`는 **SecurityContextHolder**에서 `Authentication`을 가져오는 `Supplier`를 구성한다.
> 2. `Supplice<Authentication>`과 **HttpServletRequest**를 `AuthorizationManager`에 전달한다. `AuthorizationManager`는 `authorizeHttpRequests`의 패턴과 요청을 매칭하고 해당 규칙을 실행한다.
>    - **인가 거부 됐을 때**
>      - `AuthorizationDeniedEvent`가 발행되고, **AccessDeniedException**이 발생한다. 이 경우 [ExceptionTranslationFilter](https://github.com/genesis12345678/TIL/blob/main/Spring/security/exception/ExceptionTranslationFilter.md)가 **AccessDeniedException**을 처리한다.
>    - **인가 허용 됐을 때**
>      - `AuthorizationGrantedEvent`가 발행되고, **AuthorizationFilter**가 `FilterChain`을 계속하여 처리한다.

- 스프링 시큐리티의 `AuthorizationFilter`는 초기화 과정에서 `AuthorizationManager` 속성을 `RequestMatcherDelegatingAuthorizationManager`로 초기화한다.
- 즉, 클라이언트의 요청을 `AuthorizationFilter`가 받고, `RequestMatcherDelegatingAuthorizationManager`가 인가 처리를 하게 된다.([참고](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationArchitecture/Authentication.md))
- 그렇다면 여기서 얻을 수 있는 힌트는 커스텀한 `AuthorizationFilter`를 만들어 `AuthorizationManager`를 `CustomDynamicAuthorizationManager`로 초기화 하면 클라이언트의 요청을 직접 만든 `AuthorizationManager`가 받을 수 있을 것이다.

---

###

```java
@Getter
@Setter
public class CustomAuthorizationFilter extends GenericFilterBean {
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final AuthorizationManager<HttpServletRequest> authorizationManager;

    private AuthorizationEventPublisher eventPublisher = CustomAuthorizationFilter::noPublish;

    private boolean observeOncePerRequest = false;

    private boolean filterErrorDispatch = true;

    private boolean filterAsyncDispatch = true;

    public CustomAuthorizationFilter(AuthorizationManager<HttpServletRequest> authorizationManager) {
        Assert.notNull(authorizationManager, "authorizationManager cannot be null");
        this.authorizationManager = authorizationManager;
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (this.observeOncePerRequest && isApplied(request)) {
            chain.doFilter(request, response);
            return;
        }

        if (skipDispatch(request)) {
            chain.doFilter(request, response);
            return;
        }

        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);

        try {
            AuthorizationDecision decision = this.authorizationManager.check(this::getAuthentication, request);
            this.eventPublisher.publishAuthorizationEvent(this::getAuthentication, request, decision);
            if (decision != null && !decision.isGranted()) {
                throw new AccessDeniedException("Access Denied");
            }
            chain.doFilter(request, response);
        }
        finally {
            request.removeAttribute(alreadyFilteredAttributeName);
        }
    }

    private boolean skipDispatch(HttpServletRequest request) {
        if (DispatcherType.ERROR.equals(request.getDispatcherType()) && !this.filterErrorDispatch) {
            return true;
        }
        if (DispatcherType.ASYNC.equals(request.getDispatcherType()) && !this.filterAsyncDispatch) {
            return true;
        }
        return false;
    }

    private boolean isApplied(HttpServletRequest request) {
        return request.getAttribute(getAlreadyFilteredAttributeName()) != null;
    }

    private String getAlreadyFilteredAttributeName() {
        String name = getFilterName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + ".APPLIED";
    }

    public void setSecurityContextHolderStrategy(SecurityContextHolderStrategy securityContextHolderStrategy) {
        Assert.notNull(securityContextHolderStrategy, "securityContextHolderStrategy cannot be null");
        this.securityContextHolderStrategy = securityContextHolderStrategy;
    }

    private Authentication getAuthentication() {
        Authentication authentication = this.securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationCredentialsNotFoundException(
                    "An Authentication object was not found in the SecurityContext");
        }
        return authentication;
    }

    public void setAuthorizationEventPublisher(AuthorizationEventPublisher eventPublisher) {
        Assert.notNull(eventPublisher, "eventPublisher cannot be null");
        this.eventPublisher = eventPublisher;
    }


    private static <T> void noPublish(Supplier<Authentication> authentication, T object,
                                      AuthorizationDecision decision) {

    }
}
```

> - 스프링 시큐리티의 `AuthorizationFilter` 로직을 그대로 가져왔다.
> - 생성자로 받는 `AuthorizationManager`를 직접 만든 클래스로 사용하면 `CustomDynamicAuthorizationManager`가 인가 처리를 바로 할 수 있게 된다.

### RequestMatcherDynamicAuthorizationManager

```java
@Component
@RequiredArgsConstructor
public class RequestMatcherDynamicAuthorizationManager implements AuthorizationManager<HttpServletRequest> {
    
    List<RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>>> mappings;
    private static final AuthorizationDecision DENY = new AuthorizationDecision(false);
    private static final AuthorizationDecision ACCESS = new AuthorizationDecision(true);
    
    private final HandlerMappingIntrospector handlerMappingIntrospector;
    private final ResourcesRepository resourcesRepository;

    @PostConstruct
    public void mapping() {

        DynamicAuthorizationService dynamicAuthorizationService =
                new DynamicAuthorizationService(new PersistentUrlRoleMapper(resourcesRepository));

        mappings = dynamicAuthorizationService.getUrlRoleMappings()
                .entrySet().stream()
                .map(entry -> new RequestMatcherEntry<>(
                        new MvcRequestMatcher(handlerMappingIntrospector, entry.getKey()),
                        customAuthorizationManager(entry.getValue())))
                .collect(Collectors.toList());
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest request) {

        for (RequestMatcherEntry<AuthorizationManager<RequestAuthorizationContext>> mapping : this.mappings) {

            RequestMatcher matcher = mapping.getRequestMatcher();
            RequestMatcher.MatchResult matchResult = matcher.matcher(request);

            if (matchResult.isMatch()) {
                AuthorizationManager<RequestAuthorizationContext> manager = mapping.getEntry();
                return manager.check(authentication,
                        new RequestAuthorizationContext(request, matchResult.getVariables()));
            }
        }
        return ACCESS;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, HttpServletRequest request) {
        AuthorizationManager.super.verify(authentication, request);
    }

    private AuthorizationManager<RequestAuthorizationContext> customAuthorizationManager(String role) {
        if (role.startsWith("ROLE")) {
            return AuthorityAuthorizationManager.hasAuthority(role);
        }else{
            return new WebExpressionAuthorizationManager(role);
        }
    }
}
```

> 기존 `CustomDynamicAuthorizationManager`와 로직은 똑같고, 구현체만 다르다.
> `CustomAuthorizationFilter` 생성자에 맞게 `AuthorizationManager`의 제네릭 타입만 변경해 주었다.

### 

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
    private final RestAuthenticationSuccessHandler restSuccessHandler;
    private final RestAuthenticationFailureHandler restFailureHandler;
//    private final AuthorizationManager<RequestAuthorizationContext> authorizationManager;
    private final AuthorizationManager<HttpServletRequest> authorizationManager;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().access(authorizationManager))
                        .anyRequest().permitAll())
                .formLogin(form -> form
                        .loginPage("/login").permitAll() //커스텀 로그인 페이지
                        .authenticationDetailsSource(authenticationDetailsSource)
                        .successHandler(formSuccessHandler)
                        .failureHandler(formFailureHandler)
                )
                .authenticationProvider(formAuthenticationProvider)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new FormAccessDeniedHandler("/denied"))
                )
                .addFilterAfter(customAuthorizationFilter(), ExceptionTranslationFilter.class) //필터 추가
        ;

        return http.build();
    }

    private CustomAuthorizationFilter customAuthorizationFilter() {
        return new CustomAuthorizationFilter(authorizationManager);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
        ...
    }
}
```

> - 이제 `SecurityFilterChain`의 인가 API는 별 의미가 없으니 `permitAll`로 열어 놓는다.
> - 이렇게만 설정하면 스프링 시큐리티의 `AuthorizationFilter`와 직접 만든 `CustomAuthorizationFilter`의 기능이 겹치게 된다. 이 문제는 밑에 클래스에서 해결한다.

###

```java
@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FilterChainProxy filterChainProxy;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        disableAuthorizationFilter();//추가
        setupData();
        alreadySetup = true;
    }

    //추가
    private void disableAuthorizationFilter() {
        filterChainProxy.getFilterChains()
                .forEach(sfc -> sfc.getFilters().remove(sfc.getFilters().size() - 1));
    }

    private void setupData() {
        ...
    }

    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        ...
    }

    public void createUserIfNotFound(final String userName, final String email, final String password, Set<Role> roleSet) {
        ...
    }
}
```
> - 스프링이 시작될 때 ADMIN 사용자를 생성하는 로직에 필터 체인을 수정하는 코드가 추가되었다.
> - 스프링 시큐리티의 `AuthorizationFilter`는 필터들 중 항상 마지막에 위치해 있다. 
> - 위 코드처럼 마지막 필터를 지우고 **SecurityConfig**에서 `addFilterAfter()`로 직접 만든 `CustomAuthorizationFilter`를 추가한 것이다.

---

> 이 방식처럼 스프링 시큐리티가 이미 제공하는 있는 기능을 제거해 버리고 직접 구현한 코드로 대체하는 것은 위험 부담도 있고 스프링 시큐리티의 버전 변경에 따른 동기화가 되지 않지만 이것도 하나의 방법이라는 것이다.

---

[이전 ↩️ - 회원 관리 시스템 - 계층적 권한 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EA%B4%80%EB%A6%AC_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EA%B3%84%EC%B8%B5%EC%A0%81%EA%B6%8C%ED%95%9C/Main.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/main.md)