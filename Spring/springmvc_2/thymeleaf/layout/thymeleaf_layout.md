# 템플릿 레이아웃 1
> 템플릿에서 일부 코드 조각을 가지고 와서 사용했다면 코드 조각을 레이아웃에 넘겨서 사용하는 방법도 있다.<br>
> 예를 들면 ``<head>``에 공통으로 사용하는 ``css``, ``javascript``같은 공통 정보들을 한 곳에 모아두고 공통으로 사용하지만 각 페이지마다
> 필요한 정보를 더 추가해서 사용할 수 있다.

- 컨트롤러
```java
@GetMapping("/layout")
    public String layout() {
        return "template/layout/layoutMain";
    }
```

- layout_base.html
```html
<html xmlns:th="http://www.thymeleaf.org">
<head th:fragment="common_header(title,links)">
    <title th:replace="${title}">레이아웃 타이틀</title>
    <!-- 공통 -->
    <link rel="stylesheet" type="text/css" media="all" th:href="@{/css/ awesomeapp.css}">
    <link rel="shortcut icon" th:href="@{/images/favicon.ico}">
    <script type="text/javascript" th:src="@{/sh/scripts/codebase.js}"></script>
    <!-- 추가 -->
    <th:block th:replace="${links}" />
</head>
```

- layoutMain.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="template/layout/base :: common_header(~{::title},~{::link})">
    <title>메인 타이틀</title>
    <link rel="stylesheet" th:href="@{/css/bootstrap.min.css}">
    <link rel="stylesheet" th:href="@{/themes/smoothness/jquery-ui.css}">
</head>
<body>
메인 컨텐츠
</body>
</html>
```

- 렌더링 된 HTML
```html
<!DOCTYPE html>
<html>
<head>
    <title>메인 타이틀</title>
    <!-- 공통 -->
    <link rel="stylesheet" type="text/css" media="all" href="/css/ awesomeapp.css">
    <link rel="shortcut icon" href="/images/favicon.ico">
    <script type="text/javascript" src="/sh/scripts/codebase.js"></script>
    
    <!-- 추가 -->
    <link rel="stylesheet" href="/css/bootstrap.min.css">
    <link rel="stylesheet" href="/themes/smoothness/jquery-ui.css">
</head>
<body>
메인 컨텐츠
</body>
</html>
```

- ``common_header(~{::title},~{::link})``
  - 현재 페이지의 title 태그와 link 태그들을 전달한다.

<br>

# 템플릿 레이아웃 2
> ``<head>``뿐만 아니라 HTML 전체에 적용할 수 있다.

- 컨트롤러
```java
 @GetMapping("/layoutExtend")
    public String layoutExtend() {
        return "template/layoutExtend/layoutExtendMain";
    }
```

- layoutFile.html
```html
<!DOCTYPE html>
<html th:fragment="layout (title, content)" xmlns:th="http://www.thymeleaf.org">
<head>
    <title th:replace="${title}">레이아웃 타이틀</title>
</head>
<body>
<h1>레이아웃 H1</h1>
<div th:replace="${content}">
    <p>레이아웃 컨텐츠</p>
</div>
<footer>
    레이아웃 푸터
</footer>
</body>
</html>
```

- layoutExtendMain.html
```html
<!DOCTYPE html>
<html th:replace="~{template/layoutExtend/layoutFile :: layout(~{::title}, ~{::section})}"
      xmlns:th="http://www.thymeleaf.org">
<head>
    <title>메인 페이지 타이틀</title>
</head>
<body>
<section>
    <p>메인 페이지 컨텐츠</p>
    <div>메인 페이지 포함 내용</div>
</section>
</body>
</html>
```

- 렌더링 된 HTML
```html
<!DOCTYPE html>
<html>
<head>
    <title>메인 페이지 타이틀</title>
</head>
<body>
<h1>레이아웃 H1</h1>
<section>
    <p>메인 페이지 컨텐츠</p>
    <div>메인 페이지 포함 내용</div>
</section>
<footer>
    레이아웃 푸터
</footer>
</body>
</html>
```