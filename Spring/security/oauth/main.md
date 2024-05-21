# 스프링 시큐리티 OAuth2

- OAuth 2.0 용어 이해
  - [OAuth 2.0 소개](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EC%9A%A9%EC%96%B4%EC%9D%B4%ED%95%B4/OAuth.md)
  - [OAuth 2.0 Roles](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EC%9A%A9%EC%96%B4%EC%9D%B4%ED%95%B4/Roles.md)
  - [OAuth 2.0 Client Types](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EC%9A%A9%EC%96%B4%EC%9D%B4%ED%95%B4/ClientTypes.md)
  - [OAuth 2.0 Token Types](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EC%9A%A9%EC%96%B4%EC%9D%B4%ED%95%B4/TokenTypes.md)
- OAuth 2.0 권한부여 유형
  - [OAuth 2.0 Grant Type](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EA%B6%8C%ED%95%9C%EB%B6%80%EC%97%AC/GrantType.md)
  - [권한 부여 코드 승인 방식](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EA%B6%8C%ED%95%9C%EB%B6%80%EC%97%AC/Authorization.md)
  - [암묵적 승인 방식](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EA%B6%8C%ED%95%9C%EB%B6%80%EC%97%AC/Implicit.md)
  - [패스워드 자격증명 승인 방식](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EA%B6%8C%ED%95%9C%EB%B6%80%EC%97%AC/Password.md)
  - [클라이언트 자격증명 승인 방식](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EA%B6%8C%ED%95%9C%EB%B6%80%EC%97%AC/Client.md)
  - [리프레시 토큰 승인 방식](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EA%B6%8C%ED%95%9C%EB%B6%80%EC%97%AC/RefreshToken.md)
  - [PKCE 권한부여 코드 승인 방식](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/%EA%B6%8C%ED%95%9C%EB%B6%80%EC%97%AC/PKCE.md)
- OAuth 2.0 Open ID Connect
  - [Open ID Connect](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OpenID/OpenID.md)
- OAuth 2.0 Client
  - [OAuth 2.0 Client](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuthClient/OAuthClient.md)
  - [클라이언트 앱 시작(`application.yml`, `OAuth2ClientProperties`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuthClient/YmlProperties.md)
  - [ClientRegistration](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuthClient/ClientRegistration.md)
  - [ClientRegistrationRepository](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuthClient/ClientRegistrationRepository.md)
  - [자동 설정에 의한 초기화 과정](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuthClient/AutoConfig.md)
- OAuth 2.0 Client - oauth2Login()
  - [OAuth2LoginConfigurer 초기화](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/OAuth2LoginConfigurer.md)
  - [Authorization Code 요청하기](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/Authorization%20Code.md)
  - [Access Token 교환하기](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/Access%20Token.md)
  - [OAuth 2.0 User 모델](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/OAuthUser.md)
  - [UserInfo 엔드포인트 요청하기](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/UserInfo.md)
  - [OpenID Connect 로그아웃](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/OpenID%20Connect%20%EB%A1%9C%EA%B7%B8%EC%95%84%EC%9B%83.md)
  - [Spring MVC 인증 객체 참조](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/Spring%20MVC%20%EC%9D%B8%EC%A6%9D%20%EA%B0%9D%EC%B2%B4%20%EC%B0%B8%EC%A1%B0.md)
  - [API 커스텀 - `Authorization BaseUrl` & `Redirection BaseUrl`](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/API%EC%BB%A4%EC%8A%A4%ED%85%801.md)
  - [API 커스텀 - `OAuth2AuthorizationRequestResolver`](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/API%EC%BB%A4%EC%8A%A4%ED%85%802.md)
---

> 전체 내용에 대한 출처 : [인프런 - 정수원 님의 "스프링 시큐리티 OAuth2"](https://www.inflearn.com/course/%EC%A0%95%EC%88%98%EC%9B%90-%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0/dashboard)