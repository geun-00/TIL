# 로그 추적기

다음 요구사항을 만족하는 로그 추적기를 만들어보자.
- 모든 Public 메서드의 호출과 응답 정보를 로그로 출력한다.
- 애플리케이션의 흐름을 변경하면 안 된다.
  - 로그를 남긴다고 해서 비즈니스 로직의 동작에 영향을 주면 안 된다.
- 메서드 호출에 걸린 시간
- 정상 흐름과 예외 흐름 구분
  - 예외 발생 시 예외 정보가 남아야 한다.
- 메서드 호출의 깊이 표현
- HTTP 요청 구분
  - HTTP 요청 단위로 특정 ID를 남겨서 어떤 HTTP 요청에서 시작된 것인지 명확하게 구분히 가능해야 한다.

### 로그 추적기를 위한 기반 데이터를 가지고 있는 클래스

```java
/**
 * 트랜잭션 ID와 깊이를 갖는 클래스
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)//외부에서 직접 생성할 일이 없다.
public class TraceId {

    private String id;
    private int level;

    //생성 시점에 트랜잭션 ID와 레벨 초기화
    public TraceId() {
        id = createId();
        level = 0;
    }

    private String createId() {
        //앞 8자리만 사용
        return UUID.randomUUID().toString().substring(0, 8);
    }

    //트랜잭션 ID는 그대로, 깊이를 증가한다.
    public TraceId createNextId() {
        return new TraceId(id, level + 1);
    }

    //트랜잭션 ID는 그대로, 깊이를 감소한다.
    public TraceId createPreviousId() {
        return new TraceId(id, level - 1);
    }

    //첫 번째 레벨 여부를 확인한다.
    public boolean isFirstLevel() {
        return level == 0;
    }
}

/**
 * 로그를 시작할 때의 상태 정보를 갖는 클래스
 */
@AllArgsConstructor
@Getter
public class TraceStatus {
    private TraceId traceId;//트랜잭션 ID와 레벨을 갖고 있다.
    private Long startTimeMs;//로그 시작 시간, 이 시간을 기준으로 전체 수행 시간을 구할 수 있다.
    private String message;//시작 시 사용한 메시지, 로그 종료까지 이 메시지를 사용해서 출력해야 한다.
}
```

- [로그 추적기 - V1](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_1/LogTrace_1.md)
- [로그 추적기 - V2(동기화 문제)](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_2/LogTrace_2.md)
- [로그 추적기 - V3(필드 동기화)](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_3/LogTrace_3.md)
- [동시성 문제란?](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/syncProblem/SyncProblem.md)
- [쓰레드 로컬(동시성 문제 해결)](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/threadLocal/ThreadLocal.md)
- [템플릿 메서드 패턴](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/templateMethodPattern/TemplateMethodPattern.md)
- [로그 추적기 - V4(템플릿 메서드 패턴 적용)](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_4/LogTrace_4.md)
- [전략 패턴](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/strategyPattern/StrategyPattern.md)
- [로그 추적기 - V5(템플릿 콜백 패턴 적용)](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_5/LogTrace_5.md)
- [프록시 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/Proxy.md)
  - [프록시 패턴 이론](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/proxy/Proxy.md#%ED%94%84%EB%A1%9D%EC%8B%9C-%ED%8C%A8%ED%84%B4)
  - [데코레이터 패턴 이론](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/proxy/Proxy.md#%EB%8D%B0%EC%BD%94%EB%A0%88%EC%9D%B4%ED%84%B0-%ED%8C%A8%ED%84%B4)
- [JDK 동적 프록시란?](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/jdkDynamicProxy/JDKDynamicProxy.md#jdk-%EB%8F%99%EC%A0%81-%ED%94%84%EB%A1%9D%EC%8B%9C)
  - [JDK 동적 프록시 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/jdkDynamicProxy/JDKDynamicProxy.md#jdk-%EB%8F%99%EC%A0%81-%ED%94%84%EB%A1%9D%EC%8B%9C-%EC%A0%81%EC%9A%A9)
- [CGLIB란?](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/cglib/CGLIB.md#cglib)
  - [프록시 팩토리](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/cglib/CGLIB.md#proxyfactory)
  - [포인트컷, 어드바이스, 어드바이저](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/advisor/Advisor.md)
  - [프록시 팩토리 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/cglib/ApplyProxyFactory.md#%ED%94%84%EB%A1%9D%EC%8B%9C-%ED%8C%A9%ED%86%A0%EB%A6%AC-%EC%A0%81%EC%9A%A9)
- [빈 후처리기란?](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/beanPostProcessor/BeanPostProcessor.md#%EB%B9%88-%ED%9B%84%EC%B2%98%EB%A6%AC%EA%B8%B0)
  - [빈 후처리기 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/beanPostProcessor/BeanPostProcessor.md#%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EB%B9%88-%ED%9B%84%EC%B2%98%EB%A6%AC%EA%B8%B0-%EC%A0%81%EC%9A%A9)
  - [스프링이 제공하는 빈 후처리기](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/beanPostProcessor/SpringBeanPostProcessor.md#%EC%8A%A4%ED%94%84%EB%A7%81%EC%9D%B4-%EC%A0%9C%EA%B3%B5%ED%95%98%EB%8A%94-%EB%B9%88-%ED%9B%84%EC%B2%98%EB%A6%AC%EA%B8%B0)
- [@Aspect AOP](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/aspectAOP/AspectProxy.md#aspect-aop)
- [스프링 AOP 개념](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/idea/SpringAopIdea.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B0%9C%EB%85%90)
- [스프링 AOP 구현](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0)
- [스프링 AOP 포인트컷](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut.md)
- [스프링 AOP 실전 예제](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/example/Example.md)
- [스프링 AOP 주의사항 - 프록시 내부 호출](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/warn/Warn_1.md)
- [스프링 AOP 주의사항 - 프록시 기술과 한계](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/warn/Warn_2.md)

> 전체 내용에 대한 출처 : [인프런 - 김영한 님의 "스프링 핵심 원리 - 고급편"](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EC%9B%90%EB%A6%AC-%EA%B3%A0%EA%B8%89%ED%8E%B8)