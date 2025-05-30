# 디폴트 메서드

자바는 처음부터 인터페이스와 구현을 명확하게 분리한 언어였다. 즉 자바가 처음 등장했을 때부터 인터페이스는 구현 없이 **메서드의
시그니처만을 정의하는** 용도로 사용되었다.

- **인터페이스 목적** : 코드의 계약을 정의하고, 클래스가 어떤 메서드를 반드시 구현하도록 강제하여 **명세와 구현을 분리**하는 것
- **엄격한 규칙** : 인터페이스에 선언되는 메서드는 기본적으로 모두 추상 메서드였으며, 인터페이스 내에 구현 내용을 포함할 수 없었다.
- **결과** : 이렇게 인터페이스가 엄격함게 구반됨으로써, 클래스는 여러 인터페이스를 구현할 수 있게 되고, 각각의 메서드는
클래스 내부에서 구체적으로 어떻게 동작할지를 자유롭게 정의할 수 있었다. 이를 통해 객체지향적인 설계와 다형성을 극대화할 수 있었다.

자바 8 이전까지는 인터페이스에 새로운 메서드를 추가하면 해당 인터페이스를 구현하는 모든 클래스에서 그 메서드를 구현해야 했다.
`Collection`, `List`와 같이 자바가 기본으로 제공하는 수많은 인터페이스는 이미 많은 개발자들이 구현해서 사용한다. 이런 상황에서
자바가 버전 업을 하면서 해당 인터페이스에 새로운 기능이 추가된다면, 업데이트 하는 순간 모든 개발자들이 컴파일 오류를 겪게 될 것이다.

이런 문제를 방지하기 위해 자바는 하위호환성을 가장 높은 우선순위에 둔다. 결국 인터페이스의 엄격한 규칙 때문에 그 동안 자바
인터페이스에 새로운 기능을 추가하지 못하는 일이 발생하게 되었다.

이런 문제를 해결하기 위해 자바 8에서 **디폴트 메서드**가 도입되었으며, 결과적으로 인터페이스의 엄격함이 유연하게 변경되었다.

**디폴트 메서드의 도입 이유**
1. **하위 호환성 보장**
   - 인터페이스에 새로운 메서드를 추가하더라도, 기존 코드가 깨지지 않도록 하기 위한 목적으로 도입되었다.
   - 인터페이스에 디폴트 구현을 제공하면, 기존에 해당 인터페이스를 구현하던 클래스들은 재정의하지 않아도 정상 동작한다.
2. **라이브러리 확장성**
   - 자바가 제공하는 표준 라이브러리에 정의된 인터페이스에 새 메서드를 추가하면서 사용자들이 일일이 수정하지 않아도 되도록 만들었다.
   - 이를 통해 자바 표준 라이브러리 자체도 적극적으로 개선할 수 있게 되었다.
   - 예) `List` 인터페이스에 `sort()` 메서드
3. **람다와 스트림 API 연계**
   - 자바 8에서 함께 도입된 람다와 스트림 API를 보다 편리하게 활용하기 위해 인터페이스에서 구현 로직을 제공할 필요가 있었다.
   - 예) `Collection` 인터페이스에 `stream()` 메서드
   - 예) `Iterable` 인터페이스에 `forEach()` 메서드
4. **설계 유연성 향상**
   - 디폴트 메서드를 통해 인터페이스에서도 일부 공통 동작 방식을 정의할 수 있게 되었다.
   - 이는 추상 클래스와의 경계를 어느 정도 유연하게 만들지만, 지나치게 복잡한 기능을 인터페이스에 넣는 것은 오히려 설계를
    혼란스럽게 만들 수 있으므로 주의해야 한다.

---

## 디폴트 메서드 사용 예제

알림 기능을 처리하는 `Notifier` 인터페이스와 세 가지 구현체가 있다고 해보자.

```java
public interface Notifier {
    //알림을 보내는 기본 기능
    void notify(String message);
}
```
```java
/*======= 구현체 1 =======*/
public class AppPushNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println("[APP] " + message);
    }
}

/*======= 구현체 2 =======*/
public class EmailNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println("[EMAIL] " + message);
    }
}

/*======= 구현체 3 =======*/
public class SMSNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println("[SMS] " + message);
    }
}
```

```java
import java.util.List;

public class NotifierMainV1 {
    public static void main(String[] args) {
        List<Notifier> notifiers = List.of(new EmailNotifier(), new SMSNotifier(), new AppPushNotifier());
        notifiers.forEach(notifier -> notifier.notify("서비스 가입을 환영합니다!"));
    }
}
```
```text
[EMAIL] 서비스 가입을 환영합니다!
[SMS] 서비스 가입을 환영합니다!
[APP] 서비스 가입을 환영합니다!
```

이 상황에서 요구사항이 추가되어 인터페이스에 새로운 메서드를 추가한다고 해보자.

```java
import java.time.LocalDateTime;

public interface Notifier {
    void notify(String message);

    //신규 기능 추가, 특정 시점에 자동으로 발송하는 스케줄링 기능
    void scheduleNotification(String message, LocalDateTime scheduleTime);
}
```

그리고 세 가지 구현체 중 한 클래스만 새로운 메서드를 재정의하였다.

```java
import java.time.LocalDateTime;

public class EmailNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println("[EMAIL] " + message);
    }

    //신규 기능
    @Override
    public void scheduleNotification(String message, LocalDateTime scheduleTime) {
        System.out.println("[EMAIL 전용 스케줄링] message: " + message + ", time: " + scheduleTime);
    }
}
```

하지만 나머지 구현체들은 이 기능을 재정의하지 않았기 때문에 컴파일 오류가 발생한다.
즉 기존에 존재하던 나머지 구현체들도 **강제로** 이 메서드를 구현하도록 요구된다.

**디폴트 메서드로 문제를 해결할 수 있다.** `default` 키워드와 함께 인터페이스에 메서드를 새로 추가하면서, 기본 구현을 제공할 수 있다.
따라서 구현 클래스들은 이 메서드를 굳이 재정의하지 않아도 된다. 물론 재정의해서 특별한 로직을 쓰고 싶다면 재정의할 수 있다.

```java
import java.time.LocalDateTime;

public interface Notifier {
    void notify(String message);

    //void scheduleNotification(String message, LocalDateTime scheduleTime);

    //신규 기능 추가
    default void scheduleNotification(String message, LocalDateTime scheduleTime) {
        System.out.println("[기본 스케줄링] message: " + message + ", time: " + scheduleTime);
    }
}
```
```java
import java.time.LocalDateTime;

public class EmailNotifier implements Notifier {
    @Override
    public void notify(String message) {
        System.out.println("[EMAIL] " + message);
    }

    //신규 기능
    @Override
    public void scheduleNotification(String message, LocalDateTime scheduleTime) {
        System.out.println("[EMAIL 전용 스케줄링] message: " + message + ", time: " + scheduleTime);
    }
}
```
```java
import java.time.LocalDateTime;
import java.util.List;

public class NotifierMainV2 {
    public static void main(String[] args) {
        List<Notifier> notifiers = List.of(new EmailNotifier(), new SMSNotifier(), new AppPushNotifier());
        notifiers.forEach(notifier -> notifier.notify("서비스 가입을 환영합니다!"));

        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        notifiers.forEach(notifier -> notifier.scheduleNotification("hello", tomorrow));
    }
}
```
```text
[EMAIL] 서비스 가입을 환영합니다!
[SMS] 서비스 가입을 환영합니다!
[APP] 서비스 가입을 환영합니다!
[EMAIL 전용 스케줄링] message: hello, time: 2025-05-31T12:58:56.232696300
[기본 스케줄링] message: hello, time: 2025-05-31T12:58:56.232696300
[기본 스케줄링] message: hello, time: 2025-05-31T12:58:56.232696300
```

디폴트 메서드를 재정의한 구현체(`EmailNotifier`)만 재정의된 메서드가 실행되고, 재정의하지 않은 구현체들은 인터페이스에
디폴트 메서드가 호출된 것을 확인할 수 있다.

결과적으로 **새 메서드가 추가되었음에도 불구하고** 해당 인터페이스를 구현하는 기존 클래스들이 **큰 수정 없이도** 동작을 계속 유지할 수 있게 되었다.

---

## 디폴트 메서드의 올바른 사용법

디폴트 메서드는 강력한 기능이지만, 잘못 사용하면 오히려 코드가 복잡해지고 유지보수하기 어려워질 수 있다.

**디폴트 메서드를 사용할 때 고려해야 할 주요 사항**
1. **하위 호환성을 위해 최소한으로 사용**
   - 디폴트 메서드는 주로 **이미 배포된 인터페이스에** 새로운 메서드를 추가하면서 기존 구현체 코드를 깨뜨리지 않기 위한 목적으로 만들어졌다.
   - 새 메서드가 필요한 상황이고, 기존 구현 클래스가 많은 상황이 아니라면, 원칙적으로는 각각 구현하거나 또는 추상 메서드를 추가하는 것을 고려해야 한다.
   - 불필요한 디폴트 메서드의 남용은 코드 복잡도를 높일 수 있다.
2. **인터페이스는 여전히 추상화의 역할**
   - 디폴트 메서드를 통해 인터페이스에 로직을 넣을 수 있다 하더라도, 가능한 한 로직은 구현 클래스나 별도 클래스에 두고 인터페이스는
    **계약의 역할**에 충실한 것이 좋다.
   - 디폴트 메서드는 어디까지나 **하위 호환을 위한 기능**이나 **공통으로 쓰기 쉬운 간단한 로직을 제공하는 정도**가 이상적이다.
3. **다중 상속(충돌) 문제**
   - 하나의 클래스가 여러 인터페이스를 동시에 구현하는 상황에서 **서로 다른 인터페이스에 동일한 시그니처의 디폴트 메서드**가 존재하면
    충돌이 발생한다.
   - 이 경우 **구현 클래스에서** 반드시 메서드를 재정의해야 한다. 그리고 직접 구현 로직을 작성하거나 또는 어떤 인터페이스의
    디폴트 메서드를 쓸 것인지 명시해 주어야 한다.

```java
interface A {
    default void hello() {
        System.out.println("Hello from A");
    }
}

interface B {
    default void hello() {
        System.out.println("Hello from B");
    }
}
    
public class MyClass implements A, B {
    @Override
    public void hello() {
        // 반드시 충돌을 해결해야 함
        // 1. 직접 구현
        // 2. A.super.hello();
        // 3. B.super.hello();
    }
}
```

4. **디폴트 메서드에 상태를 두지 않기**
   - 인터페이스는 일반적으로 **상태 없이** 동작만 정의하는 **추상화 계층**이다.
   - 인터페이스에 정의하는 디폴트 메서드도 **구현을 일부 제공**할 뿐 인스턴스 변수를 활용하거나 여러 차례 호출 시 상태에 따라
    동작이 달라지는 등의 동작은 **지양**해야 한다.
   - 이런 로직이 필요하다면 클래스(추상 클래스)로 옮기는 것이 더 적절하다.
