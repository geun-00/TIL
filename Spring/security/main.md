# 스프링 시큐리티

- 초기화 과정 이해
  - [스프링 시큐리티 초기화 과정](https://github.com/genesis12345678/TIL/blob/main/Spring/security/init/Init.md)
  - [SecurityBuilder & SecurityConfigurer](https://github.com/genesis12345678/TIL/blob/main/Spring/security/init/BuilderConfigurer.md)
  - [WebSecurity & HttpSecurity](https://github.com/genesis12345678/TIL/blob/main/Spring/security/init/HttpSecurity.md)
  - [DelegatingFilterProxy & FilterChainProxy](https://github.com/genesis12345678/TIL/blob/main/Spring/security/init/FilterChainProxy.md)
  - [사용자 정의 보안 설정](https://github.com/genesis12345678/TIL/blob/main/Spring/security/init/Custom.md)
- 인증 프로세스
  - [폼 인증(`formLogin()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationProcess/FormLogin.md)
  - [기본 인증(`httpBasic()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationProcess/HttpBasic.md)
  - [기억하기 인증(`rememberMe()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationProcess/RememberMe.md)
  - [익명 사용자(`anonymous()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationProcess/Anonymous.md)
  - [로그아웃(`logout()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationProcess/Logout.md)
  - [요청 캐시(`RequestCache & SavedRequest`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationProcess/RequestCache.md)
- 인증 아키텍처
  - [인증(`Authentication`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationArchitecture/Authentication.md)
  - [인증 컨텍스트(`SecurityContext & SecurityContextHolder`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationArchitecture/SecurityContext.md)
  - [인증 관리자(`AuthenticationManager`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationArchitecture/AuthenticationManager.md)
  - [인증 제공자(`AuthenticationProvider`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationArchitecture/AuthenticationProvider.md)
  - [사용자 상세 서비스(`UserDetailsService`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationArchitecture/UserDetailsService.md)
  - [사용자 상세(`UserDetails`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationArchitecture/UserDetails.md)
- 인증 상태 영속성
  - [`SecurityContextRepository & SecurityContextHolderFilter`](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationPersistence/ContextRepository.md)
  - [스프링 MVC 로그인 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthenticationPersistence/MVCLogin.md)
- 세션 관리
  - [동시 세션 제어(`maximumSessions()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/SessionManagement/MaximumSessions.md)
  - [세션 고정 보호(`sessionFixation()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/SessionManagement/SessionFixation.md)
  - [세션 생성 정책(`sessionCreationPolicy()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/SessionManagement/SessionCreationPolicy.md)
  - [`SessionManagementFilter & ConcurrentSessionFilter`](https://github.com/genesis12345678/TIL/blob/main/Spring/security/SessionManagement/SessionFilter.md)
- 예외 처리
  - [예외 처리(`exceptionHandling()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/exception/ExceptionHandling.md)
  - [예외 필터(`ExceptionTranslationFilter`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/exception/ExceptionTranslationFilter.md)
- 악용 보호
  - [CORS](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Cors_Csrf/Cors.md)
  - [CSRF](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Cors_Csrf/Csrf.md)
  - [CSRF 토큰 유지 및 검증](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Cors_Csrf/CsrfToken.md)
  - [CSRF 통합](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Cors_Csrf/CsrfAggregation.md)
  - [SameSite](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Cors_Csrf/SameSite.md)
- 인가 프로세스
  - [요청 기반 권한 부여(`HttpSecurity.authorizeHttpRequests()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizeProcess/HttpRequests.md)
  - [표현식 및 커스텀 권한 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizeProcess/Expression.md)
  - [요청 기반 권한 부여(`HttpSecurity.securityMatcher()`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizeProcess/SecurityMatcher.md)
  - [메서드 기반 권한 부여(`@PreAuthorize`, `@PostAuthorize`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizeProcess/PreAuthorize.md)
  - [메서드 기반 권한 부여(`@PreFilter`, `@PostFilter`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizeProcess/PreFIlter.md)
  - [메서드 기반 권한 부여(`@Secured`, `JSR-250`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizeProcess/Secured.md)
  - [정적 자원 관리](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizeProcess/StaticResource.md)
  - [계층적 권한(`RoleHirerachy`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizeProcess/RoleHirerachy.md)
- 인가 아키텍처
  - [인가(`Authorization`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizationProcess/Authorization.md)
  - [인가 관리자(`AuthorizationManager`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizationProcess/AuthorizationManager.md)
  - [요청 기반 인가 관리자](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizationProcess/AuthorityAuthorizationManager.md)
  - [`RequestMatcherDelegatingAuthorizationManager` 인가 설정 응용](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizationProcess/RequestMatcherDelegatingAuthorizationManager.md)
  - [메서드 기반 인가 관리자](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizationProcess/PreAuthorizeAuthorizationManager.md)
  - [포인트컷 메서드 보안](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizationProcess/Pointcut.md)
  - [AOP 메서드 보안](https://github.com/genesis12345678/TIL/blob/main/Spring/security/AuthorizationProcess/AOP.md)
- 이벤트 처리
  - [인증 이벤트(`Authentication Events`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Event/AuthenticationEvents.md)
  - [인증 이벤트(`AuthenticationEventPublisher` 활용)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Event/AuthenticationEventPublisher.md)
  - [인가 이벤트(`AuthorizationEvent`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Event/AuthorizationEvent.md)
- 통합하기
  - [Servlet API 통합](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Integration/Servlet.md)
  - [Spring MVC 통합](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Integration/SpringMVC.md)
  - [Spring MVC 비동기 통합](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Integration/SpringMVCAsync.md)
- 고급 설정
  - [다중 보안 설정](https://github.com/genesis12345678/TIL/blob/main/Spring/security/MultiSecurity/MultiSecurity.md)
  - [Custom DSL](https://github.com/genesis12345678/TIL/blob/main/Spring/security/MultiSecurity/CustomDSL.md)
  - [`Redis`를 활용한 이중화 설정](https://github.com/genesis12345678/TIL/blob/main/Spring/security/MultiSecurity/Redis.md)

---

## 실전 예제

<details>
  <summary>회원 인증 시스템</summary>

- [프로젝트 생성 및 기본 구성](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%83%9D%EC%84%B1/Main.md)
- [사용자 정의 보안 설정 및 기본 사용자 구성](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EB%B3%B4%EC%95%88%EC%84%A4%EC%A0%95/Main.md)
- [로그인 페이지 만들기](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EB%A1%9C%EA%B7%B8%EC%9D%B8%ED%8E%98%EC%9D%B4%EC%A7%80/Main.md)
- [회원가입(`PasswordEncoder`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85/Main.md)
- [커스텀 `UserDetailsService`](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/userDetailsService/UserDetailsService.md)
- [커스텀 `AuthenticationProvider`](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/AuthenticationProvider/AuthenticationProvider.md)
- [커스텀 로그아웃](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/Logout/Main.md)
- [커스텀 인증상세 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%9D%B8%EC%A6%9D%EC%83%81%EC%84%B8/Main.md)
- [커스텀 인증성공 핸들러](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%9D%B8%EC%A6%9D%EC%84%B1%EA%B3%B5%ED%95%B8%EB%93%A4%EB%9F%AC/Main.md)
- [커스텀 인증실패 핸들러](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%9D%B8%EC%A6%9D%EC%8B%A4%ED%8C%A8%ED%95%B8%EB%93%A4%EB%9F%AC/Main.md)
- [커스텀 접근제한 하기](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EC%A0%91%EA%B7%BC%EC%A0%9C%ED%95%9C/Main.md)

</details>

<details>
  <summary>비동기 인증</summary>

- [Rest 인증 보안 및 화면 구성](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/Rest%ED%99%94%EB%A9%B4%EA%B5%AC%EC%84%B1/Main.md)
- [Rest 인증 필터 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/%EC%9D%B8%EC%A6%9D%ED%95%84%ED%84%B0/Main.md)
- [`RestAuthenticationProvider` 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/RestAuthenticationProvider/Main.md)
- [Rest 인증 성공 및 실패 핸들러](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/%EC%9D%B8%EC%A6%9D%ED%95%B8%EB%93%A4%EB%9F%AC/Main.md)
- [Rest 인증 상태 영속하기](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/%EC%9D%B8%EC%A6%9D%EC%83%81%ED%83%9C%EC%98%81%EC%86%8D/Main.md)
- [Rest 예외 처리](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/%EC%98%88%EC%99%B8%EC%B2%98%EB%A6%AC/Main.md)
- [Rest 로그아웃 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/%EB%A1%9C%EA%B7%B8%EC%95%84%EC%9B%83/Main.md)
- [Rest CSRF 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/CSRF/Main.md)
- [Rest DSLs 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%EB%B9%84%EB%8F%99%EA%B8%B0_%EC%9D%B8%EC%A6%9D/DSLs/Main.md)

</details>

<details>
  <summary>회원 관리 시스템</summary>

- [기본 구성](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EA%B4%80%EB%A6%AC_%EC%8B%9C%EC%8A%A4%ED%85%9C/%EA%B8%B0%EB%B3%B8%EA%B5%AC%EC%84%B1/Main.md)
- [메모리 기반 프로그래밍 방식 인가 구현]()

</details>

---

> 전체 내용에 대한 출처 : [인프런 - 정수원 님의 "스프링 시큐리티 완전 정복 [6.x 개정판]"](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%EC%99%84%EC%A0%84%EC%A0%95%EB%B3%B5#reviews)