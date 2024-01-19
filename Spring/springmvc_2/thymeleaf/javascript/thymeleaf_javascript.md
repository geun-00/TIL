# 자바스크립트 인라인
> 자바스크립트에서 타임리프를 편리하게 사용할 수 있는 자바스크립트 인라인 기능을 제공한다.<br>
> ``script th:inline="javascript">``

- 컨트롤러
```java
@GetMapping("/javascript")
    public String javascript(Model model) {
        model.addAttribute("user", new User("userA", 10));
        return "basic/javascript";
    }
```

- HTML
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<!-- 자바스크립트 인라인 사용 전 -->
<script>
    var username = [[${user.username}]];
    var age = [[${user.age}]];
    //자바스크립트 내추럴 템플릿
    var username2 = /*[[${user.username}]]*/ "test username";
    //객체
    var user = [[${user}]];
</script>

<!-- 자바스크립트 인라인 사용 후 -->
<script th:inline="javascript">
    var username = [[${user.username}]];
    var age = [[${user.age}]];
    //자바스크립트 내추럴 템플릿
    var username2 = /*[[${user.username}]]*/ "test username";
    //객체
    var user = [[${user}]];
</script>
</body>
</html>
```

- 자바스크립트 인라인 사용 전 렌더링
```html
<script>
    var username = userA;
    var age = 10;
    //자바스크립트 내추럴 템플릿
    var username2 = /*userA*/ "test username";
    //객체
    var user = BasicController.User(username=userA, age=10);
</script>
```

- 자바스크립트 사용 후 렌더링
```html
<script>
    var username = "userA";
    var age = 10;
    //자바스크립트 내추럴 템플릿
    var username2 = "userA";
    //객체
    var user = {"username":"userA","age":10};
</script>
```