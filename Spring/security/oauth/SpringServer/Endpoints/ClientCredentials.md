# OAuth 2.0 Token Endpoint

# Client Credentials 흐름

![img_42.png](image/img_42.png)

---

# 코드 흐름

## 1. 토큰 요청

![img_43.png](image/img_43.png)

## 2. OAuth2TokenEndpointFilter -> OAuth2ClientCredentialsAuthenticationConverter

![img_44.png](image/img_44.png)

- 필요한 정보를 추출해서 `OAuth2ClientCredentialsAuthenticationToken`을 반환한다.

## 3. ProviderManager -> OAuth2ClientCredentialsAuthenticationProvider

![img_45.png](image/img_45.png)

- 스코프 정보를 확인한다.

![img_46.png](image/img_46.png)

- 액세스 토큰만 발급받고 `OAuth2AccessTokenAuthenticationToken`을 반환한다.

## 4. OAuth2TokenEndpointFilter -> sendAccessTokenResponse()

![img_47.png](image/img_47.png)

![img_48.png](image/img_48.png)