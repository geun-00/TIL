# 스프링 부트 스타터

## 라이브러리 직접 관리

```groovy
plugins {
    id 'org.springframework.boot' version '3.0.2'
    id 'java'
}

group = 'hello'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = '17'

configurations {
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}


dependencies {
    //스프링 웹 MVC
    implementation 'org.springframework:spring-webmvc:6.0.4'
    //내장 톰캣
    implementation 'org.apache.tomcat.embed:tomcat-embed-core:10.1.5'
    //JSON 처리
    implementation 'com.fasterxml.jackson.core:jackson-databind:2.14.1'
    //스프링 부트 관련
    implementation 'org.springframework.boot:spring-boot:3.0.2'
    implementation 'org.springframework.boot:spring-boot-autoconfigure:3.0.2'
    //LOG 관련
    implementation 'ch.qos.logback:logback-classic:1.4.5'
    implementation 'org.apache.logging.log4j:log4j-to-slf4j:2.19.0'
    implementation 'org.slf4j:jul-to-slf4j:2.0.6'
    //YML 관련
    implementation 'org.yaml:snakeyaml:1.33'
}

tasks.named('test') {
    useJUnitPlatform()
}
```
- **라이브러리를 직접 선택하였다.** 여러가지 문제가 생긴다.
- 웹 프로젝트를 하나 설정하기 위해 수 많은 라이브러리들을 알아야 하고, 라이브러리 버전까지 선택해야 한다.
- 각 라이브러리들 간 서로 호환이 잘 되는 버전도 있지만 호환이 잘 안되는 버전도 있다.
- 개발자가 라이브러리의 버전을 선택할 때 이런 부분까지 고려하는 것은 어렵다.

## 라이브러리 버전 관리

스프링 부트는 부트 버전에 맞춘 최적화된 라이브러리 버전을 선택해준다.

```groovy
plugins {
    id 'org.springframework.boot' version '3.0.2'
    id 'io.spring.dependency-management' version '1.1.0' //추가
    id 'java'
}
```
- 버전관리 기능을 사용하려면 플러그인을 추가해야 한다.

```groovy
dependencies{
    //스프링 웹, MVC
    implementation 'org.springframework:spring-webmvc'
    //내장 톰캣
    implementation 'org.apache.tomcat.embed:tomcat-embed-core'
    //JSON 처리
    implementation 'com.fasterxml.jackson.core:jackson-databind'
    //스프링 부트 관련
    implementation 'org.springframework.boot:spring-boot'
    implementation 'org.springframework.boot:spring-boot-autoconfigure'
    //LOG 관련
    implementation 'ch.qos.logback:logback-classic'
    implementation 'org.apache.logging.log4j:log4j-to-slf4j'
    implementation 'org.slf4j:jul-to-slf4j'
    //YML 관련
    implementation 'org.yaml:snakeyaml'
}
```
- 버전 정보가 모두 제거되었다.


- `io.spring.dependency-management` 플러그인을 사용하면 [bom](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-dependencies/build.gradle)을 참고한다.
- 각각의 라이브러리에 대한 버전이 명시되어 있다.
- 현재 프로젝트에서 지정한 스프링 부트 버전을 참고한다.
- 스프링 부트의 버전을 변경하면 라이브러리들의 버전도 변경한다.
- 참고 : [스프링 부트가 관리하는 외부 라이브러리 버전을 확인하는 방법](https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html#appendix.dependency-versions.coordinates)

## 스프링 부트 스타터

위 예제를 보면 웹 프로젝트를 하나 실행하기 위해 수 많은 라이브러리가 필요했다.<br>
스프링 부트는 프로젝트를 시작하는 데 필요한 관련 라이브러리를 모아둔 스프링 부트 스타터를 제공한다.

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
}
```
- 이 라이브러리 하나로 지금까지 직접 넣어주었던 모든 라이브러리가 포함된다.
- 스타터도 스타터를 가질 수 있다.
- 이름 패턴이 있다. `spring-boot-starter-*`
- [스프링 부트 스타터 전체 목록 공식 매뉴얼](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.build-systems.starters)

일부 외부 라이브러리의 버전을 변경하고 싶을 때 이런 형식을 `build.gradle`에 넣어주면 편리하게 변경할 수 있다.
- `ext['tomcat.version']='10.1.4'`
- [스프링 부트가 관리하는 외부 라이브러리 버전 변경에 필요한 속성 값](https://docs.spring.io/spring-boot/docs/current/reference/html/dependency-versions.html#appendix.dependency-versions.properties)
- 직접 버전을 변경하는 일은 거의 없지만 아주 가끔 문제가 발생하기도 한다.