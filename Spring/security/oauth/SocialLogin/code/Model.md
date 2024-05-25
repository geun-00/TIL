# Social Login - 코드 구현 - Model

- 서비스 제공자 별로(`Google`, `Naver`, `Keycloak`) 사용자 정보를 담는 클래스를 만들어야 한다.
- 서비스 제공자 마다 사용자의 속성을 가져오는 방식에 차이가 있다.

---

### ProviderUser

```java
public interface ProviderUser {

    String getId();
    String getUsername();
    String getPassword();
    String getEmail();
    String getProvider();
    List<? extends GrantedAuthority> getAuthorities();
    Map<String, Object> getAttributes();
}
```
> 공통적인 속성을 가져오는 인터페이스

---

### OAuth2ProviderUser

```java
public abstract class OAuth2ProviderUser implements ProviderUser{

    protected Map<String, Object> attributes;
    protected OAuth2User oAuth2User;
    protected ClientRegistration clientRegistration;

    public OAuth2ProviderUser(Map<String, Object> attributes, OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        this.attributes = attributes;
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public String getPassword() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    @Override
    public String getProvider() {
        return clientRegistration.getRegistrationId();
    }

    @Override
    public String getEmail() {
        return (String) getAttributes().get("email");
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
```

> - 서비스 제공자가 사용자의 정보를 전달할 때 공통적으로 사용하는 동일한 속성과 약간씩 차이나는 속성들이 있다.
> - 공통적인 부분은 하나의 추상 클래스로 묶어주고, 각각 차이나는 속성은 개별 클래스에서 정의하도록 한다.
> - 이 부모 클래스의 속성들은 자식 클래스의 생성자에서 넘어온 정보들이다.

---

### GoogleUser

```java
public class GoogleUser extends OAuth2ProviderUser {

    public GoogleUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("sub");
    }
}
```
> - 생성자에서 부모 클래스에게 [OAuth2User](https://github.com/genesis12345678/TIL/blob/main/Spring/security/oauth/OAuth2Login/OAuthUser.md)에 담긴 `Attributes`(사용자 정보)를 넘겨주었기 때문에 자식 클래스에서 자신에게 맞는 `Attributes`를 참조할 수 있다.

---

### NaverUser

```java
public class NaverUser extends OAuth2ProviderUser{

    public NaverUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super((Map<String, Object>) oAuth2User.getAttributes().get("response"), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("id");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("email");
    }
}
```

> [네이버 로그인 개발가이드 - 프로필 정보 조회](https://developers.naver.com/docs/login/devguide/devguide.md#3-4-5-%EC%A0%91%EA%B7%BC-%ED%86%A0%ED%81%B0%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%ED%94%84%EB%A1%9C%ED%95%84-api-%ED%98%B8%EC%B6%9C%ED%95%98%EA%B8%B0)
> - 네이버 같은 경우 사용자의 정보가 `response`로 한 번 감싸져 응답이 오기 때문에 부모 클래스에게 `attributes`를 넘길 때 `response`를 풀어주어야 한다.
> - 그래야 원하는 속성에 접근할 수 있다.

---

### KeycloakUser

```java
public class KeycloakUser extends OAuth2ProviderUser{

    public KeycloakUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return (String) getAttributes().get("sub");
    }

    @Override
    public String getUsername() {
        return (String) getAttributes().get("preferred_username");
    }
}
```

---

### User

```java
@Data
@Builder
public class User {

    private String registrationId;
    private String id;
    private String username;
    private String password;
    private String provider;
    private String email;
    private List<? extends GrantedAuthority> authorities;
}
```
> 데이터베이스에 저장될 User 객체