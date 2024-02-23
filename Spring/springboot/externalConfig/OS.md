# 외부 설정 - OS 환경 변수

OS 환경 변수(OS environment variables)는 해당 OS를 사용하는 모든 프로그램에서 읽을 수 있는 설정값이다. 다른 외부 설정과 비교해서 사용 범위가 가장 넓다.

애플리케이션에서 OS 환경 변수의 값을 읽어보자.
```java
@Slf4j
public class OsEnv {

    public static void main(String[] args) {
        Map<String, String> envMap = System.getenv();
        for (String key : envMap.keySet()) {
            log.info("env {}={}", key, System.getenv(key));
        }
    }
}
```
- `System.getenv()`로 전체 OS 환경 변수를 `Map`으로 조회할 수 있다.

OS 환경 변수를 설정하고 필요한 곳에서 `System.getenv`를 사용하면 외부 설정을 사용할 수 있다.<br>
DB 접근 URL과 같은 정보를 OS 환경 변수에 설정해두고 읽어들일 수 있다.

OS 환경 변수는 이 프로그램 뿐만 아니라 다른 프로그램에서도 사용할 수 있다.(전역 변수 같은 효과) <br>
해당 애플리케이션을 사용하는 자바 프로그램 안에서만 사용되는 외부 설정값을 사용하고 싶다면 이 방법을 사용하면 안 된다.