# 람다 vs 익명 클래스

자바에서 익명 클래스와 람다 표현식은 모두 간단하게 기능을 구현하거나 일회성으로 사용할 객체를 만들 때 유용하다. 하지만
그 사용 방식과 의도에는 차이가 있다.

### 1. 문법 차이

- **익명 클래스**
  - 익명 클래스는 클래스를 선언하고 즉시 인스턴스를 생성하는 방식이다.
  - 반드시 `new 인터페이스명() { ... }` 형태로 작성해야 하며, 메서드를 오버라이드해서 구현한다.
  - 익명 클래스도 하나의 클래스이다.
- **람다 표현식**
  - 람다 표현식은 `->` 연산자를 사용하여 매개변수와 실행할 함수를 간결하게 표현할 수 있는 방식이다.
  - 함수형 인터페이스를 간단히 구현할 때 주로 사용한다.
  - 람다도 인스턴스가 생성된다.

### 2. 코드의 간결함

- **익명 클래스**는 문법적으로 더 복잡하고 장황하다. `new 인터페이스명()`과 함께 메서드를 오버라이드해야 하므로 코드의 양이 
상대적으로 많다.
- **람다 표현식**은 간결하며 불필요한 코드를 최소화한다. 또한 많은 생략 기능을 지원해서 핵심 코드만 작성할 수 있다.

### 3. 상속 관계

- **익명 클래스**는 일반적은 클래스처럼 다양한 인터페이스와 클래스를 구현하거나 상속할 수 있다. 즉 여러 메서드를 가진 인터페이스를
구현할 때도 사용할 수 있다.
- **람다 표현식**은 메서드를 딱 하나만 가지는 함수형 인터페이스만 구현할 수 있다.
  - 람다 표현식은 클래스를 상속할 수 없다. 오직 함수형 인터페이스만 구현할 수 있으며, 상태(필드, 멤버 변수)나 추가적인
    메서드 오버라이딩은 불가능하다.
  - 람다는 단순히 함수를 정의하는 것으로, 상태나 추가적인 상속 관계를 필요로 하지 않는 상황에서만 사용할 수 있다.

### 4. 호환성

- **익명 클래스**는 자바의 오래된 버전에서도 사용할 수 있다.
- **람다 표현식**은 자바 8부터 도입되었기 때문에 그 이전 버전에서는 사용할 수 없다.

### 5. this 키워드의 의미

- **익명 클래스** 내부에서 `this` 키워드는 익명 클래스 자신을 가리킨다. 외부 클래스와 별도의 컨텍스트를 가진다.
- **람다 표현식**에서 `this` 키워드는 람다를 선언한 클래스의 인스턴스를 가리킨다. 즉 람다 표현식은 별도의 컨텍스트를 가지는 것이 아니라
    람다를 선언한 클래스의 컨텍스트를 유지한다.

**람다 내부의 `this`는 람다가 선언된 외부 클래스의 `this`와 동일하다.**

```java
package lambda.lambda6;

public class OuterMain {
    private String message = "외부 클래스 메시지";

    public void execute() {
        //1. 익명 클래스 예시
        Runnable anonymous = new Runnable() {
            private String message = "익명 클래스 메시지";

            @Override
            public void run() {
                //익명 클래스에서의 this는 익명 클래스의 인스턴스를 가리킴
                System.out.println("[익명 클래스] this: " + this);
                System.out.println("[익명 클래스] this: " + this.getClass());
                System.out.println("[익명 클래스] this: " + this.message);
            }
        };

        //2. 람다 예시
        Runnable lambda = () -> {
            //람다에서의 this는 람다가 선언된 클래스의 인스턴스, 즉 외부 클래스를 가리킴
            System.out.println("[람다] this: " + this);
            System.out.println("[람다] this: " + this.getClass());
            System.out.println("[람다] this: " + this.message);
        };

        anonymous.run();
        System.out.println("-----------------------------------");
        lambda.run();
    }

    public static void main(String[] args) {
        OuterMain outerMain = new OuterMain();
        System.out.println("[외부 클래스] : " + outerMain);
        System.out.println("-----------------------------------");
        outerMain.execute();
    }
}
```
```text
[외부 클래스] : lambda.lambda6.OuterMain@10f87f48
-----------------------------------
[익명 클래스] this: lambda.lambda6.OuterMain$1@34a245ab
[익명 클래스] this: class lambda.lambda6.OuterMain$1
[익명 클래스] this: 익명 클래스 메시지
-----------------------------------
[람다] this: lambda.lambda6.OuterMain@10f87f48
[람다] this: class lambda.lambda6.OuterMain
[람다] this: 외부 클래스 메시지
```

- 람다에서 사용한 `this`와 외부 클래스의 인스턴스 참조값이 서로 같은 것을 확인할 수 있다.
- 익명 클래스는 자신의 클래스(`OuterMain$1`)와 인스턴스가 별도로 존재한다.

### 6. 캡처링

- **익명 클래스**는 외부 변수에 접근할 수 있지만, 지역 변수는 반드시 `final` 혹은 **사실상 final**인 변수만 캡처할 수 있다.
- **람다 표현식**도 익명 클래스와 같이 캡처링을 지원한다. 지역 변수는 반드시 `final` 혹은 **사실상 final**인 변수만 캡처할 수 있다.

> **🤔 사실상 final (effectively final)**
> 
> **사실상 final** 지역 변수는 지역 변수에 `final` 키워들 사용하지는 않았지만, 값을 변경하지 않는 지역 변수를 의미한다. 즉
> `final` 키워드를 넣지 않았을 뿐이지 실제로는 `final` 키워드를 넣은 것처럼 중간에 값을 변경하지 않는 지역 변수다. 따라서
> **사실상 final** 지역 변수는 `final` 키워드를 넣어도 동일하게 작동해야 한다.
> 
> [참고](https://github.com/geun-00/TIL/blob/main/Java/mid_1/Nested/Local.md#%EC%A7%80%EC%97%AD-%ED%81%B4%EB%9E%98%EC%8A%A4---%EC%A7%80%EC%97%AD-%EB%B3%80%EC%88%98-%EC%BA%A1%EC%B2%98)

```java
public class CaptureMain {
    public static void main(String[] args) {

        final int final1 = 10; //명시적으로 final

        int final2 = 20; // 사실상(final): 재할당(값 변경) 없음
        int changedVar = 30; // 값이 변경되는 변수

        // 1. 익명 클래스에서의 캡처
        Runnable anonymous = new Runnable() {
            @Override
            public void run() {
                System.out.println("익명 클래스 - final1: " + final1);
                System.out.println("익명 클래스 - final2: " + final2);
                // 컴파일 오류
//                System.out.println("익명 클래스 - changedVar: " + changedVar);
            }
        };

        // 2. 람다 표현식에서의 캡처
        Runnable lambda = () -> {
            System.out.println("람다 - final1: " + final1);
            System.out.println("람다 - final2: " + final2);
            // 컴파일 오류
//            System.out.println("람다 - changedVar: " + changedVar);
        };

        // changedVar 값을 변경해서  "사실상 final"이 아님
        changedVar++;

        anonymous.run();
        lambda.run();
    }
}
```

익명 클래스나 람다 안에서 외부의 지역 변수를 캡처해서 사용할 수 있다. 단 이때 **final** 또는 **사실상 final** 지역 변수만 접근할 수 있다.

### 7. 생성 방식

- **익명 클래스**
  - 익명 클래스는 새로운 클래스를 정의하여 객체를 생성하는 방식이다. 즉 컴파일 시 새로운 내부 클래스로 변환된다.
  - 이 방식은 클래스가 메모리 상에서 별도로 관리된다. 따라서 메모리 상에 약간의 추가 오버헤드가 발생한다.
- **람다 표현식**
  - 람다는 내부적으로 `invokeDynamic` 이라는 메커니즘을 사용하여 컴파일 타임에 실제 클래스 파일을 생성하지 않고, 런타임
    시점에 동적으로 필요한 코드를 처리한다.
  - 따라서 람다는 익명 클래스보다 메모리 관리가 더 효율적이며 생성된 클래스 파일이 없으므로 클래스 파일 관리의 복잡성도 줄어든다.

즉 이론적으로는 람다가 별도의 클래스 파일도 만들지 않고 더 가볍기 때문에 약간의 메모리와 성능상 이점이 있지만 아주 미미한 차이이므로
익명 클래스와 람다의 성능 차이는 거의 없다고 보면 된다.

### 8. 상태 관리

- **익명 클래스**
  - 익명 클래스는 인스턴스 내부에 **상태(필드, 멤버 변수)를** 가질 수 있다. 예를 들어 익명 클래스 내부에 멤버 변수를 선언하고
  해당 변수의 값을 변경하거나 상태를 관리할 수 있다.
  - 따라서 상태를 필요로 하는 경우, 익명 클래스가 유리하다.
- **람다 표현식**
  - 클래스는 그 내부에 **상태(필드, 멤버 변수)** 와 **기능(메서드)** 을 가진다. 반면에 함수는 그 내부에 상태를 가지지 않고 기능만 제공한다.
  - 함수인 람다는 기본적으로 필드(멤버 변수)가 없으므로 **스스로 상태를 유지하지는 않는다.**

### 9. 용도 구분

- **익명 클래스**
  - **상태를 유지**하거나 다중 메서드를 구현할 필요가 있는 경우
  - 기존 클래스 또는 인터페이스를 상속하거나 구현할 때
  - 복잡한 인터페이스 구현이 필요할 때
- **람다 표현식**
  - **상태를 유지할 필요가 없고,** 간결함이 중요한 경우
  - 단일 메서드만 필요한 간단한 함수형 인터페이스 구현 시
  - 더 나은 성능(큰 차이는 없음)과 간결한 코드가 필요한 경우