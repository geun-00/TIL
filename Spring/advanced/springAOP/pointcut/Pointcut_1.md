# 포인트컷 지시자

## execution

공식 문서 문법
```text
execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern)  throws-pattern?)

execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)
```
- 메서드 실행 조인 포인트를 매칭한다.
- `?`는 생략할 수 있다.
- `*`같은 패턴을 지정할 수 있다.

### 가장 정확한 포인트컷
```java
@Test
void exactMatch() {
    //public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```
- `AspectJExpressionPointcut`의 `pointcut.setExpression()`을 통해서 포인트컷 표현식을 지정할 수 있다.
- `pointcut.matches(메서드, 대상 클래스)`를 실행하면 지정한 포인트컷 표현식의 매칭 여부를 `true`, `false`로 반환한다.
- 접근제어자? : `public`
- 반환타입 : `String`
- 선언타입? : `hello.aop.member.MemberServiceImpl`
- 메서드이름 : `hello`
- 파라미터 : `(String)`
- 예외? : 생략

### 가장 많이 생략한 포인트컷
```java
@Test
void allMatch() {
    pointcut.setExpression("execution(* *(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```
- 접근제어자? : 생략
- 반환타입 : `*`
- 선언타입? : 생략
- 메서드이름 : `*`
- 파라미터 : `(..)`
- 예외? : 생략
- `*`은 아무 값이 들어와도 된다는 뜻이다.
- 파라미터에서 `..`은 파라미터의 타입과 파라미터 수가 상관없다는 뜻이다.(`0..*`)

### 메서드 이름 매칭 관련 포인트컷
```java
@Test
void nameMatch() {
    pointcut.setExpression("execution(* hello(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

@Test
void nameMatchStar1() {
    pointcut.setExpression("execution(* hel*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

@Test
void nameMatchStar2() {
    pointcut.setExpression("execution(* *el*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

//메서드 명이 존재하지 않아 false 반환
@Test
void nameMatchFalse() {
    pointcut.setExpression("execution(* fail(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
}
```
- 메서드 이름 앞뒤에 `*`을 사용해서 매칭할 수 있다.

### 패키지 매칭 관련 포인트컷
```java
@Test
void PackageExactMatch() {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

@Test
void PackageExactMatch2() {
    pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

//.이 한개
@Test
void PackageExactFalse() {
    pointcut.setExpression("execution(* hello.aop.*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
}

@Test
void PackageMatchSubPackage1() {
    pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

//.이 두개
@Test
void PackageMatchSubPackage2() {
    pointcut.setExpression("execution(* hello.aop..*.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```
- `hello.aop.member.*(1).*(2)`
  - `(1)` : 타입(클래스 또는 인터페이스)
  - `(2)` : 메서드 이름
- 패키지에서 `.`과 `..`은 차이가 있다.
  - `.` : 정확하게 해당 위치의 패키지
  - `..` : 해당 위치의 패키지와 그 하위 패키지도 포함

### 타입 매칭 - 부모 타입 허용
```java
Test
void typeExactMatch1() {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

//인터페이스(부모 타입)로 조회, 매칭에 성공한다.
    @Test
    void typeMatchSuperType() {
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```
- `typeExactMatch1()` : 타입 정보가 정확하게 일치하기 때문에 매칭된다.
- `typeMatchSuperType()` : `execution`에서 부모 타입을 선언해도 그 자식 타입은 매칭된다.
  - `MemberServiceImpl`은 `MemberService` 인터페이스의 구현체다.

### 타입 매칭 - 부모 타입에 있는 메서드만 허용
```java
@Test
void typeMatchInternal1() throws NoSuchMethodException {
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");

    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();
}

//포인트컷으로 지정한 MemberService 인터페이스에는 internal이라는 메서드가 없다.
@Test
void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);
    assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
}
```
- 부모 타입을 표현식에 선언한 경우 부모 타입에서 선언한 메서드가 자식 타입에 있어야 매칭에 성공한다.

### 파라미터 매칭
```java
/**
 * String 타입의 파라미터 허용
 * (String)
 */
@Test
void argsMatch() {
    pointcut.setExpression("execution(* *(String))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

/**
 * 파라미터가 없어야 함
 * ()
 */
@Test
void argsMatchNoArgs() {
    pointcut.setExpression("execution(* *())");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
}

/**
 * 정확히 하나의 파라미터 허용, 모든 타입 허용
 * (Xxx)
 */
@Test
void argsMatchStar() {
    pointcut.setExpression("execution(* *(*))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

/**
 * 숫자와 무관하게 모든 파라미터, 모든 타입 허용
 * 파라미터가 없어도 됨
 * (), (Xxx), (Xxx, Xxx)
 */
@Test
void argsMatchAll() {
    pointcut.setExpression("execution(* *(..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}

/**
 * String 타입으로 시작하고 숫자와 무관하게 모든 파라미터, 모든 타입 허용
 * (String), (String, Xxx), (String, Xxx, Xxx) 허용
 */
@Test
void argsMatchComplex() {
    pointcut.setExpression("execution(* *(String, ..))");
    assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
}
```
- `execution` 파라미터 매칭 규칙
  - `(String)` : 정확하게 String 타입 파라미터
  - `()` : 파라미터가 없어야 한다.
  - `(*)` : 정확히 하나의 파라미터, 모든 타입 허용
  - `(*, *)` : 정확히 두개의 파라미터, 모든 타입 허용
  - `(..)` : 숫자와 무관하게 모든 파라미터, 모든 타입 허용. 파라미터가 없어도 된다.(`0..*`)
  - `(String, ..)` : String 타입으로 시작해야 하며 숫자와 무관하게 모든 파라미터, 모든 타입 허용