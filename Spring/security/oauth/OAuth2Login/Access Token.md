# oauth2Login() - Access Token 교환하기

## 주요 클래스

### OAuth2LoginAuthenticationFilter

![img_9.png](image/img_9.png)

- 인가 서버로부터 리다이렉트 되면서 전달된 `code`를 인가 서버의 `Access Token`으로 교환하고 `Access Token`이 저장된 `OAuth2LoginAuthenticationToken`을 **AuthenticationManager**에 위임하여
    `UserInfo` 정보를 요청해서 최종 사용자에 로그인한다.
- `OAuth2AuthorizedClientRepository`를 사용하여 **OAuth2AuthorizedClient**를 저장한다.
- 인증에 성공하면 `OAuth2AuthenticationToken`이 생성되고 **SecurityContext**에 저장되어 인증 처리를 완료한다.

**요청 매핑 URL**
- `RequestMatcher` : `/login/oauth2/code/*`

### OAuth2LoginAuthenticationProvider

- 인가 서버로부터 리다이렉트 된 이후 프로세스를 처리하며 `Access Token`으로 교환하고 이 토큰을 사용하여 `UserInfo` 처리를 담당한다.
- **Scope**에 `openid`가 포함되어 있으면 `OidcAuthorizationCodeAuthenticationProvider`를 호출하고, 아니면 `OAuth2AuthorizationCodeAuthenticationProvider`를 호출하도록 제어한다.

![img_10.png](image/img_10.png)

### OAuth2AuthorizationCodeAuthenticationProvider

- 권한 코드 부여 흐름을 처리하는 `AuthenticationProvider`
- 인가 서버에 `Authorization Code`와 `Access Token`의 교환을 담당하는 클래스

![img_11.png](image/img_11.png)

### OidcAuthorizationCodeAuthenticationProvider

![img_12.png](image/img_12.png)

- **OpenID Connect Core 1.0** 권한 코드 부여 흐름을 처리하는 `AuthenticationProvider` 이며 **요청 Scope**에 `openid`가 존재할 경우 실행된다.

### DefaultAuthorizationCodeTokenResponseClient

![img_13.png](image/img_13.png)

- 인가 서버의 `token` 엔드 포인트로 통신을 담당하며 `Access Token`을 받은 후 **OAuth2AccessTokenResponse**에 저장하고 반환한다.

---

## 구조

![img_14.png](image/img_14.png)

--- 

## 흐름

![img_15.png](image/img_15.png)

![img_16.png](image/img_16.png)

---

[이전 ↩️ - OAuth 2.0 Client(oauth2Login) - Authorization Code 요청하기](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/Authorization%20Code.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/main.md)

[다음 ↪️ - OAuth 2.0 Client(oauth2Login) - OAuth 2.0 User 모델](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/OAuthUser.md)
