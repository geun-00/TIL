# 로거

`loggers` 엔드포인트를 사용하면 로깅과 관련된 정보를 확인하고 실시간으로 변경할 수도 있다.

```java
@Slf4j
@RestController
public class LogController {

    @GetMapping("/log")
    public String log() {
        log.trace("trace log");
        log.debug("debug log");
        log.info("info log");
        log.warn("warn log");
        log.error("error log");
        return "OK";
    }
}
```
- 여러 레벨의 로그를 남기는 컨트롤러

```yaml
logging:
  level:
    hello.controller: debug
```
- 이제 위 컨트롤러는 `debug` 레벨까지 출력된다.
  - `trace`를 제외하고 모두 출력

**loggers 엔드포인트 호출**
- `localhost:8080/actuator/loggers`

```json
{
  "levels": [
    "OFF",
    "ERROR",
    "WARN",
    "INFO",
    "DEBUG",
    "TRACE"
  ],
  "loggers": {
    "ROOT": {
      "configuredLevel": "INFO",
      "effectiveLevel": "INFO"
    },
    "_org": {
      "effectiveLevel": "INFO"
    },
    "_org.springframework": {
      "effectiveLevel": "INFO"
    },
    "_org.springframework.web": {
      "effectiveLevel": "INFO"
    },
    "_org.springframework.web.servlet": {
      "effectiveLevel": "INFO"
    },
    .
    .
    "hello.controller": {
      "configuredLevel": "DEBUG",
      "effectiveLevel": "DEBUG"
    },
    "hello.controller.LogController": {
      "effectiveLevel": "DEBUG"
    },
    .
    .
}
```
- 로그를 별도로 설정하지 않으면 스프링 부트는 기본으로 `INFO`를 사용한다.
- `ROOT`의 `configuredLevel`이 `INFO`이므로 그 하위 모두 `IFNO`레벨이 적용된다.
- `LogController`는 `DEBUG`로 설정했다. 그래서 해당 부분에 `configuredLevel`이 `DEBUG`로 설정되고, 그 하위도 `DEBUG` 레벨이 적용된다.

**더 자세히 조회하기**
- `localhost:8080/actuator/loggers/{로거이름}` 패턴으로 특정 로거 이름을 기준으로 조회할 수 있다.

`localhost:8080/actuator/loggers/hello.controller`
```json
{
  "configuredLevel": "DEBUG",
  "effectiveLevel": "DEBUG"
}
```

**실시간 로그 레벨 변경**<br>
개발 서버는 보통 `DEBUG` 로그를 사용하고, 운영 서버는 요청이 많아 로그가 아주 많이 남기 때문에 `INFO` 로그 레벨을 사용한다.<br>
그런데 서비스 운영 중에 급하게 로그 레벨을 변경하고 싶으면 어떻게 해야 할까? 일반적으로는 로깅 설정을 변경하고 서버를 재시작 해야한다.

`loggers` 엔드포인트를 사용하면 애플리케이션 재시작 없이 실시간으로 로그 레벨을 변경할 수 있다.

**`POST`로 `localhost:8080/actuator/loggers/{로거이름}`** 에 JSON 데이터를 요청하면 된다.
```http request
POST localhost:8080/actuator/loggers/hello.controller
Content-Type: application/json

{
  "configuredLevel": "TRACE"
}
```
- 이렇게 하면 서버 재시작 없이 실시간으로 로그 레벨을 변경할 수 있다.
- 물론 서버를 재시작하면 다시 `DEBUG` 레벨로 돌아간다.