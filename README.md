# TIL

## INDEX

### Spring

<details>
    <summary>스프링 핵심 원리</summary>

<details>
    <summary>기본</summary>

- [메인](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/spring_basic.md#%EC%8A%A4%ED%94%84%EB%A7%81-%ED%95%B5%EC%8B%AC-%EA%B8%B0%EB%B3%B8-%EC%9B%90%EB%A6%AC)
- [순수 자바 설계(1)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/pureJava/java_1.md#%EC%88%9C%EC%88%98%ED%95%9C-%EC%9E%90%EB%B0%94%EB%A7%8C%EC%9C%BC%EB%A1%9C-%EC%84%A4%EA%B3%84---1) - 스프링 기술 없이 자바만으로 설계할 때 문제점
- [순수 자바 설계(2)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/pureJava/java_2.md#%EC%88%9C%EC%88%98%ED%95%9C-%EC%9E%90%EB%B0%94%EB%A7%8C%EC%9C%BC%EB%A1%9C-%EC%84%A4%EA%B3%84---2) - 자바만으로 설계했을 때 문제점을 해결해보고 스프링 기술 접목해보기
- [스프링 컨테이너(1)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/springContainer/container_1.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88---1) - 스프링 컨테이너 생성 과정과 컨테이너에 등록된 빈 조회하는 법
- [스프링 컨테이너(2)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/springContainer/container_2.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88---2) - 스프링 빈을 생성하는 여러가지 방법(`BeanFactory`와 `ApplicationContext`)
- [싱글톤 컨테이너(1)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/singleton/singleton_1.md#%EC%8B%B1%EA%B8%80%ED%86%A4-%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88---1) - 싱글톤 패턴과 싱글톤 방식의 주의점(공유필드)
- [싱글톤 컨테이너(2)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/singleton/singleton_2.md#%EC%8B%B1%EA%B8%80%ED%86%A4-%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88---2) - `@Configuration`에 대해
- [컴포넌트 스캔](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/componentScan/componentScan.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EC%BB%B4%ED%8F%AC%EB%84%8C%ED%8A%B8-%EC%8A%A4%EC%BA%94) - 자동 빈 등록
- [의존관계 자동 주입(1)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/DI/DI_1.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84-%EC%9E%90%EB%8F%99-%EC%A3%BC%EC%9E%85---1) - 다양한 의존관계 주입 방법
- [의존관계 자동 주입(2)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/DI/DI_2.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EC%9D%98%EC%A1%B4%EA%B4%80%EA%B3%84-%EC%9E%90%EB%8F%99-%EC%A3%BC%EC%9E%85---2) - 조회한 빈이 2개 이상일 때 구분하는 방법
- [빈 생명주기](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/beanLifeCycle/beanLifeCycle.md#%EB%B9%88-%EC%83%9D%EB%AA%85%EC%A3%BC%EA%B8%B0-%EC%BD%9C%EB%B0%B1) - 스프링 빈의 생명주기 관리
- [빈 스코프(1)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/beanScope/beanScope_1.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B9%88-%EC%8A%A4%EC%BD%94%ED%94%84---1) - 스프링의 다양한 빈 스코프, 프로토타입 스코프
- [빈 스코프(2)](https://github.com/genesis12345678/TIL/blob/main/Spring/basic/beanScope/beanScope_2.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B9%88-%EC%8A%A4%EC%BD%94%ED%94%84---2) - 스프링이 다양한 빈 스코프, 웹 스코프

</details>

<details>
    <summary>고급</summary>

- [메인](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/LogTrace.md#%EB%A1%9C%EA%B7%B8-%EC%B6%94%EC%A0%81%EA%B8%B0)
- 로그 추적기 개발
  - [V1](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_1/LogTrace_1.md#%EB%A1%9C%EA%B7%B8-%EC%B6%94%EC%A0%81%EA%B8%B0---v1) - 가장 원초적인 방법
  - [V2](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_2/LogTrace_2.md#%EB%A1%9C%EA%B7%B8-%EC%B6%94%EC%A0%81%EA%B8%B0---v2) - 동기화 문제 발생
  - [V3](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_3/LogTrace_3.md#%EB%A1%9C%EA%B7%B8-%EC%B6%94%EC%A0%81%EA%B8%B0---v3) - 필드 동기화 사용, 동시성 문제 발생
  - [V4](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_4/LogTrace_4.md#%EB%A1%9C%EA%B7%B8-%EC%B6%94%EC%A0%81%EA%B8%B0---v4) - 템플릿 메서드 패턴 적용
  - [V5](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/logTrace_5/LogTrace_5.md#%EB%A1%9C%EA%B7%B8-%EC%B6%94%EC%A0%81%EA%B8%B0---v5) - 템플릿 콜백 패턴 적용
- [동시성 문제](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/syncProblem/SyncProblem.md#%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C---%EC%98%88%EC%A0%9C) - 동시성 문제에 대해
- [쓰레드 로컬](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/threadLocal/ThreadLocal.md#%EC%93%B0%EB%A0%88%EB%93%9C-%EB%A1%9C%EC%BB%AC) - 동시성 문제를 해결하는 쓰레드 로컬에 대해
- [템플릿 메서드 패턴](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/templateMethodPattern/TemplateMethodPattern.md#%ED%85%9C%ED%94%8C%EB%A6%BF-%EB%A9%94%EC%84%9C%EB%93%9C-%ED%8C%A8%ED%84%B4) - 템플릿 메서드 패턴에 대해
- [전략 패턴](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/strategyPattern/StrategyPattern.md#%EC%A0%84%EB%9E%B5-%ED%8C%A8%ED%84%B4) - 전략 패턴에 대해
- [프록시 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/Proxy.md) - 인터페이스 기반 프록시와 구체 클래스 기반 프록시
  - [프록시 패턴](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/proxy/Proxy.md#%ED%94%84%EB%A1%9D%EC%8B%9C-%ED%8C%A8%ED%84%B4) - 프록시 패턴에 대해
  - [데코레이터 패턴](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/proxyAndDecorator/proxy/Proxy.md#%EB%8D%B0%EC%BD%94%EB%A0%88%EC%9D%B4%ED%84%B0-%ED%8C%A8%ED%84%B4) - 데코레이터 패턴에 대해
- [JDK 동적 프록시](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/jdkDynamicProxy/JDKDynamicProxy.md#jdk-%EB%8F%99%EC%A0%81-%ED%94%84%EB%A1%9D%EC%8B%9C) - JDK 동적 프록시에 대해
  - [JDK 동적 프록시 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/jdkDynamicProxy/JDKDynamicProxy.md#jdk-%EB%8F%99%EC%A0%81-%ED%94%84%EB%A1%9D%EC%8B%9C-%EC%A0%81%EC%9A%A9)
- [CGLIB](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/cglib/CGLIB.md#cglib) - `CGLIB`과 프록시 팩토리에 대해
  - [프록시 팩토리 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/cglib/ApplyProxyFactory.md#%ED%94%84%EB%A1%9D%EC%8B%9C-%ED%8C%A9%ED%86%A0%EB%A6%AC-%EC%A0%81%EC%9A%A9)
- [포인트컷, 어드바이스, 어드바이저](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/advisor/Advisor.md#%ED%8F%AC%EC%9D%B8%ED%8A%B8%EC%BB%B7-%EC%96%B4%EB%93%9C%EB%B0%94%EC%9D%B4%EC%8A%A4-%EC%96%B4%EB%93%9C%EB%B0%94%EC%9D%B4%EC%A0%80) - 포인트컷, 어드바이스, 어드바이저에 대해
- [빈 후처리기](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/beanPostProcessor/BeanPostProcessor.md#%EB%B9%88-%ED%9B%84%EC%B2%98%EB%A6%AC%EA%B8%B0) - 프록시 팩토리를 적용했을 때 문제점을 해결하는 빈 후처리기에 대해
  - [빈 후처리기 적용](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/beanPostProcessor/BeanPostProcessor.md#%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EB%B9%88-%ED%9B%84%EC%B2%98%EB%A6%AC%EA%B8%B0-%EC%A0%81%EC%9A%A9)
  - [스프링이 제공하는 빈 후처리기](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/beanPostProcessor/SpringBeanPostProcessor.md#%EC%8A%A4%ED%94%84%EB%A7%81%EC%9D%B4-%EC%A0%9C%EA%B3%B5%ED%95%98%EB%8A%94-%EB%B9%88-%ED%9B%84%EC%B2%98%EB%A6%AC%EA%B8%B0) - 스프링 AOP 기술 접목
- [@Aspect AOP](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/aspectAOP/AspectProxy.md#aspect-aop) - `@Aspect`에 대해
- 스프링 AOP
  - [개념](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/idea/SpringAopIdea.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B0%9C%EB%85%90) - 스프링 AOP에 대해 개념적으로 이해하기(적용 방식, 용어 등)
  - [구현](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0) - 스프링 AOP 구현
    - [V1](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_1_3.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v1) - `@Aspect` 사용
    - [V2](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_1_3.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v2) - 포인트컷 분리해보기
    - [V3](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_1_3.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v3) - 어드바이스 여러 개 적용
    - [V4](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_4_6.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v4) - 포인트컷을 외부 클래스로 분리
    - [V5](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_4_6.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v5) - 어드바이스 순서 지정
    - [V6](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/implement/SpringAopImplement_4_6.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EA%B5%AC%ED%98%84---v6) - 어드바이스 종류에 대해
  - [포인트컷](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop---%ED%8F%AC%EC%9D%B8%ED%8A%B8%EC%BB%B7) - 여러 포인트컷 지시자에 대해
    - [execution](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut_1.md#%ED%8F%AC%EC%9D%B8%ED%8A%B8%EC%BB%B7-%EC%A7%80%EC%8B%9C%EC%9E%90) - `execution` 문법
    - [within](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut_2.md#within) - `within` 문법
    - [args](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut_2.md#args) - `args` 문법
    - [@target, @within](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut_2.md#target-within) - `@target`, `@within`에 대해
    - [@annotation](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut_3.md#annotation) - `@annotation`에 대해
    - [bean](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut_3.md#bean) - `bean`에 대해
    - [매개변수 전달](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut_3.md#%EB%A7%A4%EA%B0%9C%EB%B3%80%EC%88%98-%EC%A0%84%EB%8B%AC) - 포인트컷 표현식을 사용하여 어드바이스에 매개변수 전달
    - [this, target](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/pointcut/Pointcut_3.md#this%EC%99%80-target) - `this`와 `target`에 대해
  - [예제]() - 스프링 AOP를 활용하여 로그 출력과 재시도를 하는 AOP 구현해보기
  - 주의사항 - 스프링 AOP 주의사항
    - [프록시 내부 호출](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/warn/Warn_1.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EC%A3%BC%EC%9D%98%EC%82%AC%ED%95%AD) - 프록시 내부 호출 문제와 여러가지 대안
    - [프록시 기술과 한계](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/springAOP/warn/Warn_2.md#%EC%8A%A4%ED%94%84%EB%A7%81-aop-%EC%A3%BC%EC%9D%98%EC%82%AC%ED%95%AD) - 프록시 기술의 한계(타입 캐스팅, 의존관계 주입, CGLIB)와 스프링의 해결책
</details>
</details>

<details>
    <summary>스프링 MVC</summary>
<details>
    <summary>1편</summary>

- [메인](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_1/springmvc_1.md#spring-mvc---1)
- [웹 애플리케이션](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_1/web_application/web_application.md#web-application) - 웹 애플리케이션에 대한 전반적인 이해
- [서블릿](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_1/servlet/servlet.md#%EC%84%9C%EB%B8%94%EB%A6%BF) - 서블릿 컨테이너 동작 방식과 `HttpServletRequest`, `HttpServletResponse`에 대해
- [JSP, MVC 패턴](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_1/jsp_mvc/jsp_mvc.md#jsp%EC%99%80-mvc-%ED%8C%A8%ED%84%B4) - 서블릿과 JSP, MVC 패턴에 대해
- [MVC 프레임워크 만들기](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_1/make_mvc/mvc.md#%ED%94%84%EB%A1%A0%ED%8A%B8-%EC%BB%A8%ED%8A%B8%EB%A1%A4%EB%9F%AC-%ED%8C%A8%ED%84%B4) - 프론트 컨트롤러 패턴으로 직접 MVC 프레임워크를 만들어보면서 컨트롤러에 대해 이해하기
- [스프링 MVC 구조](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_1/spring_mvc/spring_mvc.md#%EC%8A%A4%ED%94%84%EB%A7%81-mvc-%EA%B5%AC%EC%A1%B0) - MVC 프레임워크를 직접 만들어 본 것을 기반으로 스프링 MVC 구조 이해하기
- [스프링 MVC 기본 기능](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_1/springmvc_feature/feature.md#%EC%8A%A4%ED%94%84%EB%A7%81-mvc---%EA%B8%B0%EB%B3%B8-%EA%B8%B0%EB%8A%A5) - 스프링 MVC가 지원하는 요청과 응답의 여러가지 기능
</details>

<details>
    <summary>2편</summary>

- [메인](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/springmvc_2.md#spring-mvc---2)
- [타임리프 기본 기능](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/thymeleaf/thymeleaf.md#%ED%83%80%EC%9E%84%EB%A6%AC%ED%94%84) - 타임리프와 타임리프 기본 문법
- [타임리프와 스프링](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/thymeleaf_spring/thymeleaf_spring.md#%ED%83%80%EC%9E%84%EB%A6%AC%ED%94%84---%EC%8A%A4%ED%94%84%EB%A7%81-%ED%86%B5%ED%95%A9) - 타임리프와 스프링 통합
- [메시지, 국제화](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/message/message.md#%EB%A9%94%EC%8B%9C%EC%A7%80-%EA%B5%AD%EC%A0%9C%ED%99%94) - 설정 파일(`.properties`)을 활용한 메시지 관리와 국제화 서비스
- [검증 Validation](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/validation/validation.md#%EA%B2%80%EC%A6%9D) - 요청에 대한 검증을 순수 코드부터 애노테이션 적용까지 점진적으로 알아보기
- [로그인 처리(1)](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/login_1/login_1.md#%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC---%EC%BF%A0%ED%82%A4-%EC%84%B8%EC%85%98) - 쿠키와 세션으로 로그인 처리를 구현하면서 쿠키와 세션 알아보기
- [로그인 처리(2)](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/login_2/login_2.md#%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC---%ED%95%84%ED%84%B0-%EC%9D%B8%ED%84%B0%EC%85%89%ED%84%B0) - 서블릿 필터와 스프링 인터셉터로 공통 관심사 해결
- [예외 처리와 오류 페이지](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/exception_errorPage/exception_errorPage.md#%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC%EC%99%80-%EC%98%A4%EB%A5%98-%ED%8E%98%EC%9D%B4%EC%A7%80) - 애플리케이션에서 예외가 발생했을 때 과정과 오류 페이지 관리
- [API 예외 처리](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/api_exception/api_exception.md#api-%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC) - API 예외 처리를 순수 코드부터 애노테이션 적용까지 점진적으로 알아보기
- [스프링 타입 컨터버](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/typeConverter/typeConverter.md#%EC%8A%A4%ED%94%84%EB%A7%81-%ED%83%80%EC%9E%85-%EC%BB%A8%EB%B2%84%ED%84%B0) - 컨터버와 포맷터에 대해
- [파일 업로드](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_2/fileUpload/fileUpload.md#%ED%8C%8C%EC%9D%BC-%EC%97%85%EB%A1%9C%EB%93%9C) - 서블릿과 스프링으로 파일 업로드 해보기

</details>

</details>

<details>
    <summary>스프링 DB</summary>
<details>
    <summary>1편</summary>

- [메인](https://github.com/genesis12345678/TIL/blob/main/Spring/database_1/database_1.md#%EC%8A%A4%ED%94%84%EB%A7%81-db-1%ED%8E%B8)
- [JDBC](https://github.com/genesis12345678/TIL/blob/main/Spring/database_1/database_1.md#jdbc) - JDBC에 대해
- [커넥션 풀과 데이터 소스]() - 커넥션 풀과 데이터 소스(`DataSource`)에 대해
- [트랜잭션](https://github.com/genesis12345678/TIL/blob/main/Spring/database_1/transaction/transaction.md#%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98) - 트랜잭션 개념과 트랜잭션 적용 해보기
- [스프링 트랜잭션](https://github.com/genesis12345678/TIL/blob/main/Spring/database_1/spring_transaction/spring_transaction.md#%EC%8A%A4%ED%94%84%EB%A7%81%EA%B3%BC-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0---%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98) - 트랜잭션을 적용했을 때 문제점을 스프링으로 해결해보기
- [자바 예외](https://github.com/genesis12345678/TIL/blob/main/Spring/database_1/javaException/javaException.md#%EC%9E%90%EB%B0%94-%EC%98%88%EC%99%B8) - 자바의 예외에 대해(체크, 언체크 예외)
- [스프링 예외 처리](https://github.com/genesis12345678/TIL/blob/main/Spring/database_1/springException/springException.md#%EC%8A%A4%ED%94%84%EB%A7%81%EA%B3%BC-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0---%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC-%EB%B0%98%EB%B3%B5) - 스프링에서 예외 추상화를 하는 방법

</details>

<details>
    <summary>2편</summary>

- [메인](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/db2.md#db2---%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%A0%91%EA%B7%BC-%EA%B8%B0%EC%88%A0)
- [JdbcTemplate](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/jdbcTemplate/jdbcTemplate.md#jdbctemplate) - `JdbcTemplate` 구현하면서 알아보기
- [MyBatis](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/myBaits/myBatis.md#mybatis) - `MyBatis` 구현하면서 알아보기
- [JPA](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/jpa/jpa.md#jpa) - `JPA` 구현하면서 알아보기
- [스프링 데이터 JPA](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/springJpa/springJpa.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-jpa) - `스프링 데이터 JPA` 구현하면서 알아보기
- [Querydsl](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/querydsl/querydsl.md#querydsl) - `Querydsl` 구현하면서 알아보기
- [테스트](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/test/dbTest.md#db-%EC%A0%91%EA%B7%BC---%ED%85%8C%EC%8A%A4%ED%8A%B8) - 테스트 코드에서 DB 접근에 대해
- [활용 방안](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/tradeOff/tradeOff.md#%ED%99%9C%EC%9A%A9-%EB%B0%A9%EC%95%88) - `스프링 데이터 JPA`와 `Querydsl`을 같이 사용할 때 트레이드 오프
- [스프링 트랜잭션](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/transaction/transaction.md#%EC%8A%A4%ED%94%84%EB%A7%81-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EC%9D%B4%ED%95%B4) - 스프링의 트랜잭션에 대해 더 알아보기
- [트랜잭션 전파(1)](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/propagation/tx_propagation_1/tx_propagation.md#%EC%8A%A4%ED%94%84%EB%A7%81-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EC%A0%84%ED%8C%8C-1) - 트랜잭션 전파에 대해
- [트랜잭션 전파(2)](https://github.com/genesis12345678/TIL/blob/main/Spring/database_2/propagation/tx_propagation_2/tx_propagation.md#%EC%8A%A4%ED%94%84%EB%A7%81-%ED%8A%B8%EB%9E%9C%EC%9E%AD%EC%85%98-%EC%A0%84%ED%8C%8C-2) - 트랜잭션 전파 활용
</details>
</details>

<details>
    <summary>스프링 부트</summary>

- [메인](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/SpringBoot.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8)
- [웹 서버와 서블릿 컨테이너](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/servletContainer/ServletContainer.md#%EC%9B%B9-%EC%84%9C%EB%B2%84%EC%99%80-%EC%84%9C%EB%B8%94%EB%A6%BF-%EC%BB%A8%ED%85%8C%EC%9D%B4%EB%84%88) - 스프링 부트가 없는 과거 버전으로 개발해보기
- [내장 톰캣](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/embedTomcat/EmbedTomcat.md#%EB%82%B4%EC%9E%A5-%ED%86%B0%EC%BA%A3) - 내장 톰캣을 사용하여 여러 문제점들을 해결하고 스프링 부트 접목해보기
- [스프링 부트 스타터](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/starterLibrary/Library.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8-%EC%8A%A4%ED%83%80%ED%84%B0) - 스프링 부트가 라이브러리 버전 관리를 하는 방법
- [스프링 부트의 자동 구성](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/autoConfig/AutoConfig.md#%EC%9E%90%EB%8F%99-%EA%B5%AC%EC%84%B1) - 스프링 부트의 자동 구성에 대해
  - [자동 구성(1)](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/autoConfig/AutoConfig.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8%EC%9D%98-%EC%9E%90%EB%8F%99-%EA%B5%AC%EC%84%B1) - 자동 구성을 직접 만들어보고 자동 구성 이해하기
  - [자동 구성(2)](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/autoConfig/SpringBootAutoConfig_2.md#%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8%EC%9D%98-%EC%9E%90%EB%8F%99-%EA%B5%AC%EC%84%B1) - 라이브러리를 직접 만들어보고 자동 구성 이해하기
- [외부 설정과 프로필](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/ExternalConfig.md#%EC%99%B8%EB%B6%80-%EC%84%A4%EC%A0%95) - 외부 설정으로 데이터를 관리하는 방법
  - [OS 환경 변수](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/OS.md#%EC%99%B8%EB%B6%80-%EC%84%A4%EC%A0%95---os-%ED%99%98%EA%B2%BD-%EB%B3%80%EC%88%98) - OS 환경 변수 사용법
  - [자바 시스템 속성](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/JavaSystem.md#%EC%99%B8%EB%B6%80-%EC%84%A4%EC%A0%95---%EC%9E%90%EB%B0%94-%EC%8B%9C%EC%8A%A4%ED%85%9C-%EC%86%8D%EC%84%B1) - 자바 시스템 속성 사용법
  - [커맨드 라인 인수](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/CommandLine.md#%EC%99%B8%EB%B6%80-%EC%84%A4%EC%A0%95---%EC%BB%A4%EB%A7%A8%EB%93%9C-%EB%9D%BC%EC%9D%B8-%EC%9D%B8%EC%88%98) - 커맨드 라인 인수 사용법
  - [스프링 통합](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/SpringCombine.md#%EC%8A%A4%ED%94%84%EB%A7%81-%ED%86%B5%ED%95%A9) - 여러 외부 설정 값에 따라서 스프링이 데이터를 읽는 방법
  - [설정 데이터](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/ExternalFile.md#%EC%84%A4%EC%A0%95-%EB%8D%B0%EC%9D%B4%ED%84%B0---%EC%99%B8%EB%B6%80-%ED%8C%8C%EC%9D%BC) - 설정 데이터(`.properties`) 외부 파일과 내부 파일, 우선순위에 대해
  - [외부 설정 사용](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/ExternalRead.md#%EC%99%B8%EB%B6%80-%EC%84%A4%EC%A0%95-%EC%82%AC%EC%9A%A9) - `@Value`와 `@ConfigurationProperties`에 대해
  - [YAML](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/YAML.md#yaml) - `.yml`에 대해
  - [@Profile](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/%40Profile.md#profile) - `@Profile`에 대해
- [액츄에이터](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/actuator/Actuator.md#%EC%95%A1%EC%B8%84%EC%97%90%EC%9D%B4%ED%84%B0) - 스프링 부트 액츄에이터에 대해
  - [시작](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/actuator/Basic.md#%EC%95%A1%EC%B8%84%EC%97%90%EC%9D%B4%ED%84%B0-%EC%8B%9C%EC%9E%91) - 액츄에이터 기본 사용법과 엔드포인트
  - [헬스 정보](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/actuator/Health.md#%ED%97%AC%EC%8A%A4-%EC%A0%95%EB%B3%B4) - 액츄에이터 헬스 정보에 대해
  - [애플리케이션 정보](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/actuator/Info.md#%EC%95%A0%ED%94%8C%EB%A6%AC%EC%BC%80%EC%9D%B4%EC%85%98-%EC%A0%95%EB%B3%B4) - 액츄에이터 애플리케이션 정보에 대해
  - [로거](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/actuator/Logger.md#%EB%A1%9C%EA%B1%B0) - 액츄에이션 로거 정보에 대해
  - [HTTP 요청 응답 기록](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/actuator/HttpExchange.md#http-%EC%9A%94%EC%B2%AD-%EC%9D%91%EB%8B%B5-%EA%B8%B0%EB%A1%9D) - 액츄에이터 HTTP 요청 응답 기록에 대해
  - [보안](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/actuator/Security.md#%EC%95%A1%EC%B8%84%EC%97%90%EC%9D%B4%ED%84%B0-%EB%B3%B4%EC%95%88) - 액츄에이터 보안 유의점
- [마이크로미터](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/micrometer.md#%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%EB%AF%B8%ED%84%B0) - 마이크로미터와 메트릭에 대해
  - [프로메테우스](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Prometheus.md#%ED%94%84%EB%A1%9C%EB%A9%94%ED%85%8C%EC%9A%B0%EC%8A%A4) - 프로메테우스 기본 사용법과 기본 기능
  - [그라파나](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/SpringBoot.md#%EA%B7%B8%EB%9D%BC%ED%8C%8C%EB%82%98) - 그라파나 기본 사용법
- [모니터링 메트릭 활용](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/MetricsUse.md#%EB%AA%A8%EB%8B%88%ED%84%B0%EB%A7%81-%EB%A9%94%ED%8A%B8%EB%A6%AD-%ED%99%9C%EC%9A%A9) - 비즈니스 메트릭 등록해보기, 실무 모니터링 팁
  - [카운터](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Counter.md#%EB%A9%94%ED%8A%B8%EB%A6%AD-%EB%93%B1%EB%A1%9D---%EC%B9%B4%EC%9A%B4%ED%84%B0) - 카운터 메트릭 등록
    - [V1](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Counter.md#%EC%B9%B4%EC%9A%B4%ED%84%B0---v1) - 코드로 만들기
    - [V2](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Counter.md#%EC%B9%B4%EC%9A%B4%ED%84%B0---v2) - 애노테이션 적용
  - [타이머](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Timer.md#%EB%A9%94%ED%8A%B8%EB%A6%AD-%EB%93%B1%EB%A1%9D---%ED%83%80%EC%9D%B4%EB%A8%B8) - 타이머 메트릭 등록
    - [V1](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Timer.md#%ED%83%80%EC%9D%B4%EB%A8%B8---v1) - 코드로 만들기
    - [V2](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Timer.md#%ED%83%80%EC%9D%B4%EB%A8%B8---v2) - 애노테이션 적용
  - [게이지](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Gauge.md#%EB%A9%94%ED%8A%B8%EB%A6%AD-%EB%93%B1%EB%A1%9D---%EA%B2%8C%EC%9D%B4%EC%A7%80) - 게이지 메트릭 등록
    - [V1](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Gauge.md#%EA%B2%8C%EC%9D%B4%EC%A7%80---v1) - 코드로 만들기
    - [V2](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Gauge.md#%EA%B2%8C%EC%9D%B4%EC%A7%80---v2) - 간단한 버전
</details>
