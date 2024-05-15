# 비동기 인증 - Rest 로그아웃 구현

---

![img.png](img.png)

> 폼 방식과 비동기 방식의 로그아웃 요청은 다를 수 있겠지만, 서버에서 로그아웃 처리는 폼 방식과 다를 것이 없다.
> 
> [참고](https://github.com/genesis12345678/TIL/blob/main/Spring/security/Projects/%ED%9A%8C%EC%9B%90_%EC%9D%B8%EC%A6%9D_%EC%8B%9C%EC%8A%A4%ED%85%9C/Logout/Main.md)

---

### Controller

```java
@RestController
@RequestMapping("/api")
public class RestApiController {
    /**
     * ...
     */

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "logout";
    }
}
```

---

[이전 ↩️ - 비동기 인증 - Rest 로그아웃 구현]()

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Spring/security/main.md)

[다음 ↪️ - 비동기 인증 - Rest CSRF 구현]()