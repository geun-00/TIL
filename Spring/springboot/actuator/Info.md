# 애플리케이션 정보

`info` 엔드포인트는 애플리케이션의 기본 정보를 노출한다.

**기본으로 제공하는 기능**
- `java` : 자바 런타임 정보
- `os` : OS 정보
- `env` : `Environment`에서 `info.`으로 시작하는 정보
- `build` : 빌드 정보, `META-INF/build-info.properties` 파일이 필요하다.
- `git` : `git`정보, `git.properties` 파일이 필요하다.

`java`, `os`, `env`는 기본으로 비활성화 되어 있다.

`localhost:8080/actuator/info`를 처음 실행하면 정보들이 보이지 않는다. 기능을 활성화 해야 한다.

**java, os**
```yaml
management:
  info:
    java:
      enabled: true
    os:
      enabled: true
```
- `java`와 `os`를 활성화 했다.
- **management 바로 밑에 info다.**
- 실행해 보면 자바와 OS 관련 정보를 확인할 수 있다.

**env**

`Environment`에서 `info.`으로 시작하는 정보를 출력한다.
```yaml
management:
  info:
    env:
      enabled: true

info:
  app:
    name: hello-actuator
    company: jgy
```
- 실행해 보면 `application.yml`에서 `info`로 시작하는 부분의 정보가 노출이 된다.
```json
{
  "app": {
    "name": "hello-actuator",
    "company": "jgy"
  }
  ...
}
```

**build**

빌드 정보를 노출하려면 빌드 시점에 `META-INF/build-info.properties`파일을 만들어야 한다.<br>
`gradle`을 사용하면 다음 내용을 추가하면 된다.

```groovy
springBoot {
    buildInfo()
}
```
- 이렇게 하고 빌드를 해보면 `build` 폴더 안에 `resources/main/META-INF/build-info.properties` 파일이 만들어진다.
- 실행해 보면 애플리케이션의 기본 정보와 버전, 빌드된 시간을 확인할 수 있다.

**git**

`build`와 유사하게 빌드 시점에 사용한 `git`정보도 노출할 수 있다.<br>
`git`정보를 노출하려면 `git.properties`파일이 필요하다.

`gradle`에 다음 git 정보를 추가하면 된다.
```groovy
plugins {
    ...
    id "com.gorylenko.gradle-git-properties" version "2.4.1" //git info
}
```
- 이렇게 하고 빌드를 해보면 `build` 폴더 안에 `resources/main/git.properties` 파일이 만들어진다.
- `git`은 기본으로 활성화 되어 있기 때문에 이 파일만 있으면 바로 확인할 수 있다.
- 애플리케이션을 배포할 때 기대와 다르게 동작할 때가 있는데 확인해보면 다른 커밋이나 다른 브랜치의 내용이 배포가 됐을 수 있다. 이럴 때 도움이 된다.

`git`에 대한 더 자세한 정보를 보고 싶으면 다음 옵션을 적용하면 된다.
```yaml
management:
  info:
    git:
      mode: "full"
```

[`info`의 사용자 정의 기능 추가 공식 매뉴얼](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.info.writing-custom-info-contributors)