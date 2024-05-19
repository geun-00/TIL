# OAuth 2.0 Client

- OAuth 2.0 인가 프레임워크의 역할 중 인가 서버 및 리소스 서버와의 통신을 담당하는 클라이언트의 기능을 필터 기반으로 구현한 모듈
- 간단한 설정만으로 OAuth 2.0 인증 및 리소스 접근 권한, 인가 서버 엔드 포인트 통신 등의 구현이 가능하며 커스터마이징의 확장이 용이하다.

### OAuth 2.0 Login
- 애플리케이션의 사용자를 외부 OAuth 2.0 Provider 나 OpenID Connect 1.0 Provider 계정으로 로그안할 수 있는 기능을 제공한다.
- 글로벌 서비스 프로바이더인 "구글 계정으로 로그인", "깃허브 계정으로 로그인" 기능 등을 OAuth 2.0 로그인을 구현해 사용할 수 있도록 지원한다.
- OAuth 2.0 인가 프레임워크의 권한 부여 유형 중 **Authorization Code** 방식을 사용한다.

### OAuth 2.0 Client
- OAuth 2.0 인가 프레임워크에 정의된 클라이언트 역할을 지원한다.
- 인가 서버의 권한 부여 유형에 따른 엔드 포인트와 직접 통신할 수 있는 API를 제공한다.
  - Client Credentials
  - Resource Owner Password Credentials
  - Refresh Token
- 리소스 서버의 보호자원 접근에 대한 연동 모듈을 구현할 수 있다.

---

### 의존성 추가

```text
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
```

---

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/main.md)

[다음 ↪️ - OAuth 2.0 Client - `application.yml`, `OAuth2ClientProperties`](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuthClient/YmlProperties.md)