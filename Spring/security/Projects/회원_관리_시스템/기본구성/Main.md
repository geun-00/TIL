# 회원 관리 시스템 기본 구성

---

![img.png](image/img.png)

> 설정 클래스에서 모두 하드 코딩 되어있는 권한 규칙 코드를 모두 프로그래밍에 의한 동적 권한으로 적용될 수 있도록 한다.

> - **회원 관리**
>   - 회원 리스트, 회원 상세정보, 권한 부여
> - **권한 관리**
>   - 권한 리스트, 권한 생성, 수정, 삭제
> - **자원 관리**
>   - 자원 리스트, 자원 생성, 수정, 삭제, 권한 매핑

---

## 엔티티 관계도

![img_1.png](image/img_1.png)

> - 한 명의 사용자는 여러 권한을 가질 수 있고, 하나의 권한은 여러 사용자에게 매핑될 수 있다.
> - 하나의 권한은 여러 자원을 가질 수 있고, 하나의 자원은 여러 권한에 매핑될 수 있다.

---

## 테이블 관계도

![img_2.png](image/img_2.png)

---

## 패키지 구성

![img_3.png](image/img_3.png)

---

## 코드

### [컨트롤러]()
### [서비스 & 레포지토리]()
### [엔티티 & DTO]()
### SetUp
```java
@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
        setupData();
        alreadySetup = true;
    }

    private void setupData() {
        HashSet<Role> roles = new HashSet<>();
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", "관리자");
        roles.add(adminRole);
        createUserIfNotFound("admin", "pass", roles);
    }

    public Role createRoleIfNotFound(String roleName, String roleDesc) {
        Role role = roleRepository.findByRoleName(roleName);

        if (role == null) {
            role = Role.builder()
                    .roleName(roleName)
                    .roleDesc(roleDesc)
                    .isExpression("N")
                    .build();
        }
        return roleRepository.save(role);
    }

    public void createUserIfNotFound(final String userName, final String password, Set<Role> roleSet) {
        Account account = userRepository.findByUsername(userName);

        if (account == null) {
            account = Account.builder()
                    .username(userName)
                    .password(passwordEncoder.encode(password))
                    .userRoles(roleSet)
                    .build();
        }
        userRepository.save(account);
    }
}
```
> 스프링은 `ApplicationContext`를 초기화 하거나 새로 고칠 때 **ContextRefreshedEvent**를 발생시킨다.<br>
> 즉, 위 코드는 애플리케이션이 실행될 때 (`admin`, `pass`)의 계정 정보와 `ADMIN` 권한을 가진 사용자 하나를 저장한다.
> 
> [참고](https://eblo.tistory.com/165)