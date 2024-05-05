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
  - [인증(`Authentication`)]()
  - [인증 컨텍스트(`SecurityContext & SecurityContextHolder`)]()
  - [인증 관리자(`AuthenticationManager`)]()
  - [인증 제공자(`AuthenticationProvider`)]()
  - [사용자 상세 서비스(`UserDetailsService`)]()
  - [사용자 상세(`UserDetails`)]()
- 인증 상태 영속성
  - [`SecurityContextRepository & SecurityContextHolderFilter`]()
  - [스프링 MVC 로그인 구현]()


> 전체 내용에 대한 출처 : [인프런 - 정수원 님의 "스프링 시큐리티 완전 정복 [6.x 개정판]"](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%EC%99%84%EC%A0%84%EC%A0%95%EB%B3%B5#reviews)