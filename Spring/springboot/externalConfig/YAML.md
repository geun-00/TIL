# YAML

스프링은 설정 데이터를 사용할 때 `application.properties`뿐만 아니라 `application.yml`이라는 형식도 지원한다.

`YAML(YAML Ain't Markup Language)`은 사람이 읽기 좋은 데이터 구조를 목표로 한다. 확장자는 `yaml`, `yml`이다.(주로 `yml`을 사용)

- `application.properties`
```properties
my.datasource.url=local.db.com
my.datasource.username=username
my.datasource.password=password
my.datasource.etc.max-connection=1
my.datasource.etc.timeout=3500ms
my.datasource.etc.options=CACHE, ADMIN
```

- `application.yml`
```yaml
my:
  datasource:
    url: local.db.com
    username: local_user
    password: local_pw
    etc:
      max-connection: 1
      timeout: 60s
      options: LOCAL, CACHE
```
- `YAML`의 가장 큰 특징은 사람이 읽기 좋게 계층 구조를 이룬다는 점이다.
- `YAML`은 공백으로 계층 구조를 만든다.(보통 2칸 사용)
- 구분 기호로 `:`를 사용한다.
  - `:` 이후에 공백을 하나 넣고 값을 넣어주면 된다.

스프링은 `YAML`의 계층 구조를 `properties`처럼 평평하게 만들어서 읽어들인다.

> **참고**<br>
> `application.properties`와 `application.yml`을 같이 사용하면 `application.properties`가 우선권을 가진다.<br>
> 이 둘을 함께 사용하는 것은 일관성이 없으므로 권장되진 않는다.(실무에서는 설정 정보가 많아서 보기 편한 `yml`을 선호한다.)

**YML에도 프로필을 적용할 수 있다.**
```yaml
my:
  datasource:
    url: local.db.com
    username: local_user
    password: local_pw
    etc:
      max-connection: 1
      timeout: 60s
      options: LOCAL, CACHE
---
spring:
  config:
    activate:
      on-profile: dev
my:
  datasource:
    url: dev.db.com
    username: dev_user
    password: dev_pw
    etc:
      max-connection: 10
      timeout: 60s
      options: DEV, CACHE
---
spring:
  config:
    activate:
      on-profile: prod
my:
  datasource:
    url: prod.db.com
    username: prod_user
    password: prod_pw
    etc:
      max-connection: 50
      timeout: 10s
      options: PROD, CACHE
```
- `yml`은 `---`로 논리 파일을 구분한다.