## Resources

### ResourcesService
```java
@Service
@Transactional
@RequiredArgsConstructor
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository resourcesRepository;

    /*
     개별 리소스 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Resources getResources(long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    /*
     전체 리소스 조회
     생성된 쿼리
     select
        r1_0.resource_id,
        r1_0.http_method,
        r1_0.order_num,
        r1_0.resource_name,
        r1_0.resource_type 
    from
        resources r1_0 
    order by
        r1_0.order_num
     */
    @Override
    @Transactional(readOnly = true)
    public List<Resources> getResources() {
        return resourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    /*
     리소스 등록
     */
    @Override
    public void createResources(Resources resources){
        resourcesRepository.save(resources);
    }

    /*
     리소스 삭제
     */
    @Override
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
    }
}
```
### ResourcesRepository
```java
public interface ResourcesRepository extends JpaRepository<Resources, Long> { }
```

---

## Role

### RoleService
```java
@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /*
     개별 권한 조회
     */
    @Override
    @Transactional(readOnly = true)
    public Role getRole(long id) {
        return roleRepository.findById(id).orElse(new Role());
    }

    /*
     전체 권한 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    /*
     개별 회원 조회 화면에서 권한 목록을 표현식이 아닌 권한만 나타내기
     */
    @Override
    @Transactional(readOnly = true)
    public List<Role> getRolesWithoutExpression() {
        return roleRepository.findAllRolesWithoutExpression();
    }

    /*
     권한 등록
     */
    @Override
    public void createRole(Role role){
        roleRepository.save(role);
    }

    /*
     권한 삭제
     */
    @Override
    public void deleteRole(long id) {
        roleRepository.deleteById(id);
    }
}
```

### RoleRepository
```java
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRoleName(String name);

    void delete(Role role);

    @Query("select r from Role r where r.isExpression = 'N'")
    List<Role> findAllRolesWithoutExpression();
}
```

---

## User

### UserManagementService
```java
@Service("userManagementService")
@Transactional
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserManagementRepository userManagementRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /*
     사용자 정보 수정
     */
    @Override
    public void modifyUser(AccountDto accountDto){
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDto, Account.class);

        if(accountDto.getRoles() != null){
            Set<Role> roles = new HashSet<>();

            accountDto.getRoles().forEach(role -> {
                Role r = roleRepository.findByRoleName(role);
                roles.add(r);
            });
            account.setUserRoles(roles);
        }
        account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        userManagementRepository.save(account);
    }

    /*
     개별 사용자 조회
     */
    @Override
    @Transactional(readOnly = true)
    public AccountDto getUser(Long id) {
        Account account = userManagementRepository.findById(id).orElse(new Account());
        ModelMapper modelMapper = new ModelMapper();
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);

        List<String> roles = account.getUserRoles()
                                    .stream()
                                    .map(Role::getRoleName)
                                    .toList();

        accountDto.setRoles(roles);
        return accountDto;
    }

    /*
     전체 사용자 조회
     */
    @Override
    @Transactional(readOnly = true)
    public List<Account> getUsers() {
        return userManagementRepository.findAll();
    }

    /*
     사용자 삭제
     */
    @Override
    public void deleteUser(Long id) {
        userManagementRepository.deleteById(id);
    }
}
```

### UserManagementRepository
```java
public interface UserManagementRepository extends JpaRepository<Account, Long> { }
```

### UserService
```java
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void createUser(Account account) {
        Role role = roleRepository.findByRoleName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        account.setUserRoles(roles);
        userRepository.save(account);
    }
}
```
> [기존 회원가입](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/%ED%9A%8C%EC%9B%90%EA%B0%80%EC%9E%85/Main.md) 에서 권한 정보를 단순 문자열로 저장하던 로직에서 `Role`이라는 엔티티로 저장하는 로직으로 변경
