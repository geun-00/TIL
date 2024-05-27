# OAuth 2.0 Resource Server

## OAuth 2.0 Resource Server

- OAuth 2.0 인가 프레임워크의 역할 중 **클라이언트 및 인가 서버와의 통신을 담당하는 리소스 서버의 기능을 필터 기반으로 구현한 모듈**
- 간단한 설정만으로 **클라이언트의 리소스 접근 제한, 토큰 검증을 위한 인가 서버와의 통신 등의 구현이 가능하다.**
- 애플리케이션의 권한 관리를 별도 인가 서버에 위임하는 경우에 사용할 수 있으며 리소스 서버는 요청을 인가할 때 이 인가 서버에 물어볼 수 있다.

### OAuth2ResourceServer

- 클라이언트의 접근을 제한하는 인가 정책을 설정하는 것이 주목적
- 인거 서버에서 발급한 `Access Token`의 유효성을 검증하고 접근 범위(스코프)에 따라 적절한 자원을 전달하도록 설정한다.

### JWT

- `JWT`로 전달되는 토큰을 검증하기 위한 `JwtDecoder`, `BarerTokenAuthenticationFilter`, `JwtAuthenticationProvider` 등의 클래스 모델들을 제공한다.
- 자체 검증 프로세스를 지원한다.

### Opaque

- **Opaque** : 불투명한
- 인가 서버의 `introspection` 엔드 포인트로 검증할 수 있는 `Opaque` 토큰을 지원한다.
- 실시간으로 토큰의 활성화 여부를 확인할 수 있다.

---

### 의존성

```text
implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
```

- 리소스 서버를 지원하는 코드는 대부분 `spring-security-oauth2-resource-server`에 들어있다.
- **JWT**를 디코딩하고 검증하는 로직은 `spring-security-oauth2-jose`에 있다.
- 따라서 리소스 서버가 사용할 Bearer 토큰을 **JWT**로 인코딩 한다면 두 모듈이 모두 필요하다.
- 위의 의존성은 두 모듈이 모두 포함되어 있다.

---

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/main.md)

[다음 ↪️ - OAuth 2.0 Resource Server - Resource Server 시작(`application.yml`,`OAuth2ResourceServerProperties`)](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/ResourceServer/Properties.md)