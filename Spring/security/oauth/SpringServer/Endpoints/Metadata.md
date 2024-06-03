# Spring Authorization Server - 엔드포인트 프로토콜

# OAuth 2.0 Authorization Server Metadata Endpoint

## OAuth2AuthorizationServerMetadataEndpointConfigurer

- OAuth2 Authorization Server Metadata 엔드포인트를 사용자 정의 하는 기능을 제공한다.
- `OAuth2AuthorizationServerMetadataEndpointFilter`를 구성하고 이를 OAuth2 인증 서버 `SecurityFilterChain` 빈에 등록한다.

## OAuth2AuthorizationServerMetadataEndpointFilter

- OAuth2 Authorization Server Metadata 요청을 처리하고 응답을 반환한다.

![img_97.png](image/img_97.png)

## RequestMatcher

- **토큰 검사 요청 패턴**
  - `/.well-known/oauth-authorization-server, GET`

--- 
 
# 동작 확인

## 1. 메타데이터 엔드포인트 요청

![img_109.png](image/img_109.png)

## 2. OAuth2AuthorizationServerMetadataEndpointFilter

![img_110.png](image/img_110.png)

## 3. 메타데이터 응답

![img_111.png](image/img_111.png)

---

## OAuth2AuthorizationServerConfigurer

- JWK Set 엔드포인트에 대한 지원을 제공한다.
- `NimbusJwkSetEndpointFilter`를 구성하고 이를 OAuth2 인증 서버 `SecurityFilterChain` 빈에 등록한다.
- `NimbusJwkSetEndpointFilter`은 JWK Set을 반환하는 필터다.
- JWK Set 엔드포인트는 `JWKSource<SecurityContext>` 빈이 등록된 경우에만 구성된다.

## RequestMatcher

- **토큰 검사 요청 패턴**
  - `/oauth2/jwks, GET`

![img_98.png](image/img_98.png)

![img_99.png](image/img_99.png)

---

## 리소스 서버 설정

![img_100.png](image/img_100.png)

![img_101.png](image/img_101.png)

## 인가 서버 설정

![img_102.png](image/img_102.png)

---

# 동작 확인

## 1. JwkSetEndpoint 요청

![img_103.png](image/img_103.png)

## 2. NimbusJwkSetEndpointFilter

- 이 필터는 `jwkSet`을 반환하기만 하는데, 반환하기 전에 한 과정을 거친다.

![img_104.png](image/img_104.png)

![img_107.png](image/img_107.png)

- 처음 `JWKSet` 정보는 위와 같은데 아래의 과정을 거쳐 `PublicKey`만 필터링하여 응답하게 된다.

![img_105.png](image/img_105.png)

![img_106.png](image/img_106.png)

## 3. JWKSet 응답

![img_108.png](image/img_108.png)

---

# 리소스 서버 연동

## 리소스 서버 - 컨트롤러

![img_112.png](image/img_112.png)

- 이 요청은 인증이 되어야 정상적인 응답이 가능하다.
- 토큰으로 인증을 시도하면 리소스 서버는 인가 서버로 `jwkSet` 엔드포인트로 요청을 보내 공개키를 얻으려고 할 것이다.

## 1. 리소스 서버로 요청

![img_113.png](image/img_113.png)

## 2. 리소스 서버 - BearerTokenAuthenticationFilter -> JwtAuthenticationProvider

- `jwtDecoder.decode()`의 내부 과정으로 인가 서버의 엔드 포인트로 요청한다.

![img_114.png](image/img_114.png)

## 3. 인가 서버 - NimbusJwkSetEndpointFilter

- `PublicKey`만 필터링하여 응답하게 된다.

![img_115.png](image/img_115.png)

## 4. 리소스 서버 - MVC

- 인증 과정을 모두 거쳤으므로 리소스 서버는 정상적으로 응답할 수 있다.

![img_116.png](image/img_116.png)

---

[이전 ↩️ - Spring Authorization Server(엔드포인트 프로토콜) - ]()

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/main.md)

[다음 ↪️ - Spring Authorization Server(엔드포인트 프로토콜) - ]()
