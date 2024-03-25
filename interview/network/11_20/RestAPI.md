# REST API란 무엇인지 설명해주세요.

![img_7.png](image/img_7.png)

- **REST API는 HTTP 프로토콜을 기반으로 하는 API 기술이다.**

## REST 란?

- **REST(Representational State Transfer)**, **자원을 이름으로 구분하여** 해당 자원의 상태를 주고받는 모든 것을 의미한다.
- HTTP URI를 통해 자원을 명시한다.
- HTTP Method를 통해 해당 자원에 대한 CRUD 연산을 적용하는 것을 의미한다.

### REST 구성 요소
- 자원(Resource) : HTTP URI
- 자원에 대한 행위(Verb) : HTTP Method
- 자원에 대한 행위의 내용(Representations) : HTTP Message Payload

### REST 특징
- 서버-클라이언트 구조(Server-Client)
- 무상태(Stateless)
- 캐시 처리 가능(Cacheable)
- 계층화(Layered System)
- 인터페이스 일관성(Uniform Interface)

### REST 장단점
**장점**
- HTTP 프로토콜의 인프라를 그대로 사용하므로 REST API 사용을 위한 별도의 인프라를 구축할 필요가 없다.
- HTTP 프로토콜의 표준을 최대한 활용하여 여러 추가적인 장점을 함께 가져갈 수 있게 해준다.
- HTTP 표준 프로토콜에 따르는 모든 플랫폼에서 사용이 가능하다.
- REST API 메시지가 의도하는 바를 명확하게 나타내므로 의도하는 바를 쉽게 파악할 수 있다.
- 서버와 클라이언트의 역할을 명확하게 분리한다.

**단점**
- 표준이 존재하지 않는다.
- HTTP Method 가 제한적이다.
- 브라우저를 통해 테스트할 일이 많은 서비스라면 쉽게 고칠 수 있는 URL보다 헤더 정보의 값을 처리해야 하므로 전문성이 요구된다.
- 구형 브라우저에서 호환이 되지 않아 지원해주지 못하는 동작이 많다.

## REST API란?

- REST API란 REST의 원리를 따르는 API를 의미한다.
- REST API를 올바르게 설계하기 위해서는 지켜야 하는 몇 가지 규칙이 있다.

### REST API 설계

1. URI는 **동사보다는 명사를, 대문자보다는 소문자**를 사용한다.
2. **마지막에 슬래시(`/`)를 포함하지 않는다.**
3. 언더바(`_`)대신 **하이폰(`-`)을 사용**한다.
4. **파일 확장자**는 URI에 포함하지 않는다.
5. **행위를 포함하지 않는다.**