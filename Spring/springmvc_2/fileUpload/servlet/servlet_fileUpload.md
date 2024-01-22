# 서블릿 파일 업로드

## V1
- 컨트롤러
```java
@Controller
@Slf4j
@RequestMapping("/servlet/v1")
public class ServletUploadControllerV1 {

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request={}", request);
        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);
        return "upload-form";
    }
}
```
- upload-form.html
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="container">
    <div class="py-5 text-center">
        <h2>상품 등록 폼</h2>
    </div>
    <h4 class="mb-3">상품 입력</h4>
    <form th:action method="post" enctype="multipart/form-data">
        <ul>
            <li>상품명<input type="text" name="itemName"></li>
            <li>파일<input type="file" name="file" ></li>
        </ul>
        <input type="submit"/>
    </form>
</div> <!-- /container -->
</body>
</html>
```
- application.properties
```properties
logging.level.org.apache.coyote.http11=debug
```
이 옵션은 HTTP 요청 메시지를 확인할 수 있다.

실행해보면 로그에 `multipart/form-data`방식으로 전송이 됐다.

- 멀티파트 사용 옵션
```properties
spring.servlet.multipart.max-file-size=1MB 
spring.servlet.multipart.max-request-size=10MB
```
용량이 큰 파일이 무제한 업로드 되는 것을 제한할 수 있다. 사이즈를 넘으면 예외(`SizeLimitExceedException`)가 발생한다.
- `max-file-size` : 파일 하나의 최대 사이즈, 기본 1MB
- `max-request-size` : 요청 하나에 여러 파일들의 전체 합, 기본 10MB

또 `spring.servlet.multipart.enabled=false` 가 있다.<br>
멀티파트는 일반적인 폼 요청(`application/x-www-form-urlencoded`)보다 훨씬 복잡하다. 이 옵션을 끄면 서블릿 컨테이너는 멀티파트와 관련된 처리를 하지 않는다.
(default: `true`)

<br>

## V2

이제 파일을 받아야 하는데 파일이 저장되는 경로가 필요하다.
- application.properties
```properties
file.dir=C:/Users/User/file/
```
- 컨트롤러
```java
@Controller
@Slf4j
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {

        log.info("request={}", request);
        String itemName = request.getParameter("itemName");
        log.info("itemName={}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name={}", part.getName());
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName));
            }

            log.info("submittedFileName={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize());

            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body={}", body);

            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath={}", fullPath);
                part.write(fullPath);
            }

        }

        return "upload-form";
    }
}
```
멀티파트 형식은 전송 데이터를 하나하나 각각 부분(`Part`)으로 나누어 전송하는데 `parts`에는 이렇게 나누어진 데이터가 각각 담긴다. 서블릿이 제공하는 `Part`는
멀티파트 형식을 편리하게 읽을 수 있는 다양한 메서드를 제공한다.
- `part.getSubmittedFileName()` : 클라이언트가 전달한 파일명
- `part.getInputStream()` : Part의 전송 데이터를 읽을 수 있다.
- `part.write()` : Part를 통해 전송된 데이터를 저장할 수 있다.