# 서블릿 API 오류 페이지
- WebServerCustomizer
```java
@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class,"/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
```
- ApiExceptionController
```java
@RestController
@Slf4j
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {
        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        return new MemberDto(id, "hello " + id);
    }
    
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
```
정상 호출을 하면 API로 JSON 데이터가 정상 반환이 되고 오류가 발생하면 미리 만들어준 오류 페이지 HTML이 반환된다. 웹 브라우저가 아닌 이상
HTML을 직접 받아서 할 수 있는게 없기 때문에 클라이언트는 JSON이 반환되기를 기대한다.

오류 페이지 컨트롤러도 JSON 응답을 할 수 있도록 해야한다.

- ErrorPageController
```java
@RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {

        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>();
        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));
}
```
`produces = MediaType.APPLICATION_JSON_VALUE` 은 클라이언트가 요청하는 HTTP Header의 `Accept` 값이 `application/json`일 때 해당 메서드가
호출된다는 것이다. 클라이언트가 받고 싶은 미디어 타입이 `json`이면 이 컨트롤러의 메서드가 호출된다.

HTTP Header에 `Accept`가 `application/json`이 아니면 기존 오류 응답인 HTML 응답이 출력된다.

Jackson 라이브러리는 `Map`을 JSON 구조로 변환할 수 있고 `ResponseEntity`를 사용해서 응답하기 때문에 메시지 컨버터가 동작하면서 클라이언트에 JSON이 반환된다.

<br>

# API 예외 처리 - 스프링 부트 기본 오류 처리
스프링 부트는 위의 과정을 이미 구현해 놓았다. 
- 스프링 부트가 제공하는 `BasicErrorController`
```java
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
    HttpStatus status = getStatus(request);
    Map<String, Object> model = Collections
        .unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
    response.setStatus(status.value());
    ModelAndView modelAndView = resolveErrorView(request, response, status, model);
    return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
}

@RequestMapping
public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    HttpStatus status = getStatus(request);
    if (status == HttpStatus.NO_CONTENT) {
        return new ResponseEntity<>(status);
    }
    Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
    return new ResponseEntity<>(body, status);
}
```
`/error` 동일한 경로를 처리하는 두 메서드가 있다.

클라이언트 요청 `Accept`헤더 값이 `text/html`인 경우 `errorHtml()`을 호출해서 View를 반환하고<br>
그 외 경우네는 `error()`를 호출해 `ResponseEntity`로 HTTP Body에 JSON 데이터를 반환한다.