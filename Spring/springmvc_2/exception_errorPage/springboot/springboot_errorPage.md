# 스프링 부트 - 오류 페이지
예외 처리 페이지를 만들기 위해서 복잡한 과정을 거쳤다.
- `WebServerCustomizer` 생성
- 예외 종류에 따라서 `ErrorPage` 추가
- 예외 처리용 컨트롤러 생성

**스프링 부트는 이런 과정을 모두 기본으로 제공한다.**

개발자가 따로 `ErrorPage`를 만들지 않으면 모든 오류는 `/error`를 호출하고 매핑하는 컨트롤러인 `BasicErrorController`라는 컨트롤러가 이미 구현되어 있다.

덕분에 개발자는 **오류 페이지만 등록하면 된다.**

- View 선택 우선수위
1. 뷰 템플릿
   - `resources/templates/error/500.html`
   - `resources/templates/error/5xx.html`
2. 정적 리소스(`static`,`public`)
   - `resources/static/error/400.html`
   - `resources/static/error/404.html`
   - `resources/static/error/4xx.html`
3. 적용 대상이 없을 때
   - `resources/templates/error.html`

해당 경로 위치에 HTTP 상태 코드 이름의 뷰 파일을 넣어두면 된다. 

뷰 템플릿이 정적 리소스보다 우선순위가 높고 404, 500 처럼 구체적인 것이 4xx, 5xx처럼 덜 구체적인 것보다 우선순위가 높다.

- `resources/templates/error/4xx.html`
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="container" style="max-width: 600px">
    <div class="py-5 text-center">
        <h2>4xx 오류 화면 스프링 부트 제공</h2>
    </div>
    <div>
        <p>오류 화면 입니다.</p>
    </div>
    <hr class="my-4">
</div> <!-- /container -->
</body>
</html>
```

- `resources/templates/error/404.html`
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="utf-8">
</head>
<body>
<div class="container" style="max-width: 600px">
  <div class="py-5 text-center">
    <h2>404 오류 화면 스프링 부트 제공</h2>
  </div>
  <div>
    <p>오류 화면 입니다.</p>
  </div>
  <hr class="my-4">
</div> <!-- /container -->
</body>
</html>
```

- `resources/templates/error/500.html`
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="utf-8">
</head>
<body>
<div class="container" style="max-width: 600px">
  <div class="py-5 text-center">
    <h2>500 오류 화면 스프링 부트 제공</h2>
  </div>
  <div>
    <p>오류 화면 입니다.</p>
  </div>
  <hr class="my-4">
</div> <!-- /container -->
</body>
</html>
```

<br>

# 스프링 부트 - 오류 페이지2
> `BasicErrorController`는 여러 가지 정보를 model에 담아서 뷰에 전달한다. 뷰 템플릿은 이 값을 활용해서 출력할 수 있다.

- 컨트롤러
```java
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="container" style="max-width: 600px">
    <div class="py-5 text-center">
        <h2>500 오류 화면 스프링 부트 제공</h2>
    </div>
    <div>
        <p>오류 화면 입니다.</p>
    </div>

    <ul>
        <li>오류 정보</li>
        <ul>
            <li th:text="|timestamp: ${timestamp}|"></li>
            <li th:text="|path: ${path}|"></li>
            <li th:text="|status: ${status}|"></li>
            <li th:text="|message: ${message}|"></li>
            <li th:text="|error: ${error}|"></li>
            <li th:text="|exception: ${exception}|"></li>
            <li th:text="|errors: ${errors}|"></li>
            <li th:text="|trace: ${trace}|"></li>
        </ul>
        </li>
    </ul>

    <hr class="my-4">
</div> <!-- /container -->
</body>
</html>
```

오류 관련 내부 정보들을 노출하는 것이 좋지 않다. 그래서 `BasicErrorController`에서 다음 오류 정보를 model에 포함할지 여부를 선택할 수 있다.
```properties
server.error.include-exception=true
server.error.include-message=always
server.error.include-stacktrace=on_param
server.error.include-binding-errors=on_param
```
`never`, `always`,`on_param` 3가지 옵션이 있다.
- `never` : 사용하지 않음
- `always` : 항상 사용
- `on_param` : 파라미터가 있을 때 사용
  - 예시: `http://localhost:8080/error-ex?message=&errors=&trace=`

**사용자에게는 사용자가 이해할 수 있는 간단한 오류 메시지를 보여주고 오류는 서버에 로그로 남겨서 로그로 확인해야 한다.**

스프링 부트 오류 관련 옵션
- `server.error.whitelael.enabled=true` : 오류 처리 화면을 못 찾을 시, 스프링 whitelabel 오류 페이지 적용
- `server.error.path=/error` : 오류 페이지 경로, 스프링이 자동 등록하는 서블릿 글로벌 오류 페이지 경로와 `BaiscErrorController` 경로에 함께 사용된다.



