### AdminController

```java
@Controller
public class AdminController {
    @GetMapping("/admin/home")
    public String home() {
        return "admin/home";
    }
}
```
> `ROLE_ADMIN` 권한을 가진 사용자만이 위 경로와 밑에 컨트롤러 경로들까지 접근 가능하다.

---

### ResourcesController

```java
@Controller
@RequiredArgsConstructor
public class ResourcesController {

    private final ResourcesService resourcesService;
    private final RoleRepository roleRepository;
    private final RoleService roleService;

    /*
     리소스 목록 뷰
     */
    @GetMapping("/admin/resources")
    public String getResources(Model model) {
        List<Resources> resources = resourcesService.getResources();
        model.addAttribute("resources", resources);

        return "admin/resources";
    }

    /*
     리소스 등록 뷰에서 "등록"을 눌렀을 때 처리
     */
    @PostMapping("/admin/resources")
    public String createResources(ResourcesDto resourcesDto) {
        ModelMapper modelMapper = new ModelMapper();

        Role role = roleRepository.findByRoleName(resourcesDto.getRoleName());
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        Resources resources = modelMapper.map(resourcesDto, Resources.class);
        resources.setRoleSet(roles);

        resourcesService.createResources(resources);

        return "redirect:/admin/resources";
    }

    /*
     리소스 등록 뷰
     */
    @GetMapping("/admin/resources/register")
    public String resourcesRegister(Model model) {

        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        List<String> myRoles = new ArrayList<>();
        model.addAttribute("myRoles", myRoles);

        ResourcesDto resources = new ResourcesDto();
        Set<Role> roleSet = new HashSet<>();

        roleSet.add(new Role());
        resources.setRoleSet(roleSet);
        model.addAttribute("resources", resources);

        return "admin/resourcesdetails";
    }

    /*
     리소스 목록 뷰에서 개별 리소스를 조회했을 때
     */
    @GetMapping("/admin/resources/{id}")
    public String resourceDetails(@PathVariable("id") String id, Model model) {

        List<Role> roleList = roleService.getRoles();
        model.addAttribute("roleList", roleList);

        Resources resources = resourcesService.getResources(Long.parseLong(id));
        List<String> myRoles = resources.getRoleSet()
                                        .stream()
                                        .map(Role::getRoleName)
                                        .toList();
        model.addAttribute("myRoles", myRoles);

        ModelMapper modelMapper = new ModelMapper();
        ResourcesDto resourcesDto = modelMapper.map(resources, ResourcesDto.class);
        model.addAttribute("resources", resourcesDto);

        return "admin/resourcesdetails";
    }

    /*
     개별 리소스 조회 화면에서 "삭제" 를 눌렀을 때
     */
    @GetMapping("/admin/resources/delete/{id}")
    public String removeResources(@PathVariable("id") String id) {

        resourcesService.deleteResources(Long.parseLong(id));

        return "redirect:/admin/resources";
    }
}
```

---

### RoleController

```java
@Controller
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /*
     권한 목록 뷰
     */
    @GetMapping("/admin/roles")
    public String getRoles(Model model) {

        model.addAttribute("roles", roleService.getRoles());

        return "admin/roles";
    }

    /*
     권한 등록 뷰
     */
    @GetMapping("/admin/roles/register")
    public String rolesRegister(Model model) {

        model.addAttribute("roles", new RoleDto());

        return "admin/rolesdetails";
    }

    /*
     권한 등록 뷰에서 "등록"을 눌렀을 때 처리
     */
    @PostMapping("/admin/roles")
    public String createRole(RoleDto roleDto) {
        ModelMapper modelMapper = new ModelMapper();
        Role role = modelMapper.map(roleDto, Role.class);
        roleService.createRole(role);

        return "redirect:/admin/roles";
    }

    /*
    권한 목록 뷰에서 개별 권한을 조회했을 때
    */
    @GetMapping("/admin/roles/{id}")
    public String getRole(@PathVariable("id") String id, Model model) {
        Role role = roleService.getRole(Long.parseLong(id));

        ModelMapper modelMapper = new ModelMapper();
        RoleDto roleDto = modelMapper.map(role, RoleDto.class);
        model.addAttribute("roles", roleDto);

        return "admin/rolesdetails";
    }

    /*
     개별 권한 조회 화면에서 "삭제" 를 눌렀을 때
     */
    @GetMapping("/admin/roles/delete/{id}")
    public String removeRoles(@PathVariable("id") String id) {

        roleService.deleteRole(Long.parseLong(id));

        return "redirect:/admin/roles";
    }
}
```

---

### UserManagementController

```java
@Controller
@RequiredArgsConstructor
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final RoleService roleService;

    /*
     회원 목록 뷰
     */
    @GetMapping("/admin/users")
    public String getUsers(Model model) {

        List<Account> users = userManagementService.getUsers();
        model.addAttribute("users", users);

        return "admin/users";
    }

    /*
     개별 회원 조회 화면에서 회원 정보를 수정하고 "등록"을 눌렀을 때
     */
    @PostMapping("/admin/users")
    public String modifyUser(AccountDto accountDto) {

        userManagementService.modifyUser(accountDto);

        return "redirect:/admin/users";
    }

    /*
     회원 목록 뷰에서 개별 회원을 조회했을 때
     */
    @GetMapping("/admin/users/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {

        AccountDto accountDto = userManagementService.getUser(id);
        List<Role> roleList = roleService.getRolesWithoutExpression();

        model.addAttribute("user", accountDto);
        model.addAttribute("roleList", roleList);

        return "admin/userdetails";
    }

    /*
     개별 회원 조회 화면에서 "삭제"를 눌렀을 때
     */
    @GetMapping("/admin/users/delete/{id}")
    public String removeUser(@PathVariable("id") Long id) {

        userManagementService.deleteUser(id);

        return "redirect:admin/users";
    }
}
```