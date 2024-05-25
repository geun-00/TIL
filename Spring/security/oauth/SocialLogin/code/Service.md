# Social Login - 코드 구현 - Service

### AbstractOAuth2UserService

```java
@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public abstract class AbstractOAuth2UserService {

    private final UserService userService;
    private final UserRepository userRepository;

    public ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        String registrationId = clientRegistration.getRegistrationId();

        switch (registrationId) {
            case "keycloak" -> {
                return new KeycloakUser(oAuth2User, clientRegistration);
            }
            case "google" -> {
                return new GoogleUser(oAuth2User, clientRegistration);
            }
            case "naver" -> {
                return new NaverUser(oAuth2User, clientRegistration);
            }
        }

        return null;
    }

    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {

        User user = userRepository.findByUsername(providerUser.getUsername());

        if (user == null) {
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            userService.register(registrationId, providerUser);
        } else {
            log.info("user = {}", user);
        }
    }
}
```
> **OAuth2User**와 **OidcUser**를 공통적으로 처리하기 위한 추상 클래스

---

### CustomOAuth2UserService

```java
@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService
                                    implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    public CustomOAuth2UserService(UserService userService, UserRepository userRepository) {
        super(userService, userRepository);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest); //인가 서버와 통신 후 반환되는 사용자 정보

        ProviderUser providerUser = super.providerUser(clientRegistration, oAuth2User);

        //회원가입
        super.register(providerUser, userRequest);

        return oAuth2User;
    }
}
```
> **OAuth2User** 처리를 위한 클래스

---

### CustomOidcUserService

```java
@Service
public class CustomOidcUserService extends AbstractOAuth2UserService
                                implements OAuth2UserService<OidcUserRequest, OidcUser> {

    public CustomOidcUserService(UserService userService, UserRepository userRepository) {
        super(userService, userRepository);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();

        OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);

        ProviderUser providerUser = super.providerUser(clientRegistration, oidcUser);

        //회원가입
        super.register(providerUser, userRequest);

        return oidcUser;
    }
}
```
> **OidcUser** 처리를 위한 클래스

--- 

### UserService

```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(String registrationId, ProviderUser providerUser) {

        User user = User.builder()
                .registrationId(registrationId)
                .id(providerUser.getId())
                .username(providerUser.getUsername())
                .provider(providerUser.getPassword())
                .email(providerUser.getEmail())
                .authorities(providerUser.getAuthorities())
                .build();

        userRepository.register(user);
    }
}
```