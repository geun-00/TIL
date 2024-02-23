# 헬스 정보

헬스 정보를 사용하면 애플리케이션에 문제가 발생했을 때 문제를 빠르게 인지할 수 있다.

헬스 정보는 단순히 애플리케이션이 요청에 응답을 할 수 있는지 판단하는 것을 넘어서 애플리케이션이 사용하는 DB가 응답하는지, 디스크 사용량에는 문제가 없는지
같은 다양한 정보들을 포함해서 만들어진다.

헬스 정보를 더 자세히 보려면 다음 옵션을 지정하면 된다.
```yaml
management: 
  endpoint:
    health:
      show-details: always
```
- 이렇게 하면 `db`, `diskSpace`, `ping`에 대한 정보가 아주 자세하게 노출된다.
- 자세하게 노출하는 것을 원하지 않는다면 `show-details`옵션 대신 다음 옵션을 사용하면 된다.

```yaml
management: 
  endpoint:
    health:
      show-components: always
```
- 이렇게 하면 각 헬스 컴포넌트의 상태 정보만 간략하게 노출한다.

헬스 정보 상태는 `UP`, `DOWN`으로 나타내는데 헬스 컴포넌트 중에 하나라도 문제가 있으면 전체 상태는 `DOWN`이 된다.
```json
{
  "status": "DOWN",
  "components": {
    "db": {
      "status": "DOWN"
    },
    "diskSpace": {
      "status": "UP"
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

[자세한 헬스 기본 지원 기능 공식 매뉴얼](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.health)