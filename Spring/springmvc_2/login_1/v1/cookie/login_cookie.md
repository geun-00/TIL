# 쿠키를 사용하여 로그인 처리하기

- 도메인 Member
```java
@Data
public class Member {

    private Long id;

    @NotEmpty
    private String loginId; //로그인 ID
    @NotEmpty
    private String name; //사용자 이름
    @NotEmpty
    private String password;
}
```

- 저장소 Repository
```java
@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L; //static 사용

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save: member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public Optional<Member> findByLoginId(String loginId) {
        return findAll().stream()
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }
}
```
- 컨트롤러
```java
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {
        return "members/addMemberForm";
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/addMemberForm";
        }

        memberRepository.save(member);
        return "redirect:/";
    }
}
```

- HTML
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <link th:href="@{/css/bootstrap.min.css}"
          href="../css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container {
            max-width: 560px;
        }
        .field-error {
            border-color: #dc3545;
            color: #dc3545;
        }
    </style>
</head>
<body>

<div class="container">

    <div class="py-5 text-center">
        <h2>회원 가입</h2>
    </div>

    <h4 class="mb-3">회원 정보 입력</h4>

    <form action="" th:action th:object="${member}" method="post">

        <div th:if="${#fields.hasGlobalErrors()}">
            <p class="field-error" th:each="err : ${#fields.globalErrors()}" th:text="${err}">전체 오류 메시지</p>
        </div>

        <div>
            <label for="loginId" th:text="#{label.save.loginId}"></label>
            <input type="text" id="loginId" th:field="*{loginId}" class="form-control"
                   th:errorclass="field-error">
            <div class="field-error" th:errors="*{loginId}" />
        </div>
        <div>
            <label for="password" th:text="#{label.save.password}"></label>
            <input type="password" id="password" th:field="*{password}" class="form-control"
                   th:errorclass="field-error">
            <div class="field-error" th:errors="*{password}" />
        </div>
        <div>
            <label for="name" th:text="#{label.save.name}"></label>
            <input type="text" id="name" th:field="*{name}" class="form-control"
                   th:errorclass="field-error">
            <div class="field-error" th:errors="*{name}" />
        </div>

        <hr class="my-4">

        <div class="row">
            <div class="col">
                <button class="w-100 btn btn-primary btn-lg" type="submit" th:text="#{button.member.save}"></button>
            </div>
            <div class="col">
                <button class="w-100 btn btn-secondary btn-lg" onclick="location.href='items.html'"
                        th:onclick="|location.href='@{/}'|"
                        type="button" th:text="#{button.cancel}"></button>
            </div>
        </div>

    </form>

</div> <!-- /container -->
</body>
</html>
```

## 로그인 기능
> [쿠키](https://github.com/genesis12345678/TIL/blob/main/Http/httpHeader_1/header_1.md#%EC%BF%A0%ED%82%A4)<br>
> 서버에서 로그인에 성공하면 HTTP 응답에 쿠키를 담아서 브라우저애 전달한다. 그러면 브라우저는 앞으로 해당 쿠키를 지속해서 보내준다.

- **영속 쿠키** : 만료 날짜를 입력하면 해당 날짜까지 유지된다.
- **세션 쿠키** : 만료 날짜를 생략하면 브라우저 종료시 까지만 유지된다.


- 로그인 서비스
```java
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return null 로그인 실패
     */
    public Member login(String loginId, String password) {
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
```
- 로그인 DTO
```java
@Data
public class LoginForm {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}
```
- 로그인 컨트롤러
```java
 @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        //쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        return "redirect:/";
    }
```
로그인에 성공하면 쿠키 이름=`memberId`, 값=회원의 `id`를 담아서 `HttpServletResponse`에 담는다. 웹 브라우저는 종료 전까지 회원의 `id`를 계속 보내준다.

- 홈 컨트롤러
```java
@GetMapping("/")
public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        if (memberId == null) {
            return "home";
        }

        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
}
```
`@CookieValue`를 사용하여 편리하게 쿠키를 조회할 수 있다. 로그인하지 않은 사용자도 홈에 접근 가능하게 하기 위해 `required=false`를 사용한다.

## 로그아웃
> 세션 쿠키이므로 웹 브라우저 종료 시 자동으로 로그아웃 되기도 하고, 서버에서 해당 쿠키의 종료 날짜를 0으로 지정할 수도 있다.

- 컨트롤러
```java
@PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
}

private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
}
```

## 쿠키 보안 문제

- 쿠키의 값을 임의로 변경할 수 있다.
  - 클라이언트가 쿠키를 강제로 변경하면 다른 사용자가 될 수 있다.
  - 웹 브라우저에 개발자 모드에서 실제로 변경이 가능하다.
- 쿠키에 보관된 정보는 훔쳐갈 수 있다.
  - 쿠키 정보는 웹 브라우저에도 보관되고, 네트워크 요청마다 서버로 전달된다.
  - 쿠키에 중요한 개인정보가 담겨 있다면 해커에게 털릴 시 매우 위험하다.

대안으로 여러 방법이 있다.
- 쿠키에 중요한 값을 노출하지 않고 사용자 별로 예측 불가능한 임의의 토큰(랜덤 값)을 노출하고, 서버에서 토큰과 사용자 ID를 매핑해서 인식한다. 토큰은 서버에서 관리한다.
- 토큰은 해커가 임의의 값을 넣어도 찾을 수 없도록 예측 불가능 해야 한다.
- 해커가 토큰을 털어가도 일정 시간 지나면 사용할 수 없도록 서버에서 해당 토큰의 만료 시간을 짧게 유지한다.(30분, 1시간...)
  - 또는 해킹이 의심되는 경우 서버에서 해당 토큰을 강제로 제거한다.