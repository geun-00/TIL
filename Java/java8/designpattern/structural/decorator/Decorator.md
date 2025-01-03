# 데코레이터 패턴

## 데코레이터 패턴 정의

객체들을 새로운 행동을 포함한 특수 래퍼 객체 내에 넣어서 위 행동들을 해당 객체들에 연결시키는 구조적 디자인 패턴

데코레이터 패턴으로 객체를 추가 요소를 동적으로 더할 수 있으며, 서브클래스를 만들 때보다 훨씬 유연하게 기능을 확장할 수 있다.

## 데코레이터 패턴 구조

![img.png](image/img.png)

## 데코레이터 패턴 예제 코드 - 1

**데코레이터 패턴으로 커피 메뉴에 포함될 수 있는 재료들의 조합을 생성한다.** 
일반적인 방법으로는 모든 조합을 미리 클래스로 정의해야 하며, 새로운 재료가 늘어날 때마다 
클래스는 배수로 늘어날 것이다.

![img_1.png](image/img_1.png)

![img_2.png](image/img_2.png)

![img_3.png](image/img_3.png)

![img_4.png](image/img_4.png)

![img_5.png](image/img_5.png)

![img_6.png](image/img_6.png)

![img_7.png](image/img_7.png)

![img_8.png](image/img_8.png)

> 데코레이터 순서는 원본 대상 객체 생성자를 장식자 생성자가 래핑하는 형태로 보면 된다.
> 
> ex) `new 장식자(new 원본())`

## 데코레이터 패턴 예제 코드 - 2

**html 태그를 사용해 다양한 글씨체를 출력하듯이, 각각의 html 태그를 데코레이터로 생각해
텍스트를 여러 태그로 감싸보자.**

![img_9.png](image/img_9.png)

![img_10.png](image/img_10.png)

![img_12.png](image/img_12.png)

![img_11.png](image/img_11.png)

![img_13.png](image/img_13.png)

![img_14.png](image/img_14.png)

![img_15.png](image/img_15.png)

![img_16.png](image/img_16.png)

![img_17.png](image/img_17.png)

## 데코레이터 패턴 예제 코드 - 3

**데이터를 가져올 때도 동기화 처리, 시간 측정 등과 같은 부가 처리 기능 요구 사항을 처리해야 할 수 있다.**
매번 새로운 클래스를 만드는 대신 데코레이터 패턴으로 기능 확장에 유연하게 대처해보자.

![img_25.png](image/img_25.png)

![img_18.png](image/img_18.png)

![img_19.png](image/img_19.png)

![img_20.png](image/img_20.png)

![img_21.png](image/img_21.png)

![img_22.png](image/img_22.png)

![img_23.png](image/img_23.png)

![img_24.png](image/img_24.png)

실행 결과를 보면 알 수 있듯이, 어떤 걸 먼저 장식하느냐에 따라 효과가 완전히 달라진다.
따라서 장식자를 감쌀 때 예상한 결과가 나오는지 충분한 검토를 수행해야 한다.

## 데코레이터 패턴 장단점

### 데코레이터 장점

- 서브클래스를 만들 때보다 훨씬 유연하게 기능을 확장할 수 있다.
- 객체를 여러 데코레이터로 래핑하여 여러 동작을 결합할 수 있다.
- 런타임에 동적으로 기능을 변경할 수 있다.
- 각 장식자 클래스마다 고유의 책임을 가져 **SRP** 원칙을 준수한다.
- 클라이언트 코드 수정없이 기능 확장이 필요하면 새로운 장식자 클래스를 추가하면 되므로 **OCP** 원칙을 준수한다.

### 데코레이터 단점

- 장식자 일부를 제거하고 싶을 때, 래퍼 스택에서 특정 래퍼를 제거하기 어렵다.
- 데코레이터를 조합하는 초기 생성코드가 복잡해질 수 있다.
  - `new A(new B(new C(new D(...))))`
- 데코레이터의 행동이 데코레이터 스택 내의 순서에 의존하지 않는 방식으로 데코레이터를 구현하기가 어렵다.
  - 어느 장식자를 먼저 데코레이팅 하느냐에 따라 데코레이터 스택 순서가 결졍되는데, 순서에 의존하지 않는
    방식으로 데코레이터를 구현하기는 어렵다.

## 실전에서 사용되는 데코레이터 패턴

- 자바의 I/O 메서드
  - `InputStream`, `OutputStream`, `Reader`, `Writer`의 생성자를 활용한 래퍼
- `java.util.Collections`의 메서드들
  - `checkedXxx()`, `synchronizedXxx()`, `unmodifiableXxx()`
- `javax.servlet.http.HttpServletRequestWrapper`, `HttpServletResponseWrapper` 

---

### 참고

- [참고 블로그](https://inpa.tistory.com/entry/GOF-%F0%9F%92%A0-%EB%8D%B0%EC%BD%94%EB%A0%88%EC%9D%B4%ED%84%B0Decorator-%ED%8C%A8%ED%84%B4-%EC%A0%9C%EB%8C%80%EB%A1%9C-%EB%B0%B0%EC%9B%8C%EB%B3%B4%EC%9E%90)
- [참고 사이트](https://refactoring.guru/ko/design-patterns/decorator)
- [참고 강의](https://www.inflearn.com/course/%EA%B0%9D%EC%B2%B4%EC%A7%80%ED%96%A5-%EB%94%94%EC%9E%90%EC%9D%B8-%ED%8C%A8%ED%84%B4-%EC%96%84%EC%BD%94/dashboard)
- [참고 책](https://www.yes24.com/Product/Goods/108192370)