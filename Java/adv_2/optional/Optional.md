# Optional

## Optional이 필요한 이유

- **NullPointException 문제**
  - 자바에서 `null`은 **값이 없음**을 표현하는 가장 기본적인 방법이다.
  - 하지만 `null`을 잘못 사용하면 `NullPointException` 예외가 발생하여 프로그램이 예상치 않게 종료될 수 있다.
  - 특히 여러 메서드가 연쇄적으로 호출되어 내부에서 `null` 체크가 누락되면, 추적하기 어렵고 디버깅 비용이 증가한다.
- **가독성 저하**
  - `null`을 반환하거나 사용하게 되면 코드를 작성할 때마다 `if`, `else` 같은 조건문으로 `null` 여부를 계속 확인해야 한다.
  - 이러한 `null` 체크 로직이 누적되면 코드가 복잡해지고 가독성이 떨어진다.
- **불분명한 의도**
  - 메서드 시그니처만 보고서는 이 메서드가 `null`을 반환할 수도 있다는 사실을 명확히 알기 어렵다.
  - 호출하는 입장에서는 반드시 값이 존재할 것이라고 가정했다가, 런타임에 `null`이 나와서 문제가 생길 수 있다.

**이러한 문제를 해결하고자 자바 8부터 `Optional` 클래스를 도입했다.**

- `Optional`은 값이 있을 수도 있고, 없을 수도 있음을 명시적으로 표현해주어 메서드의 호출 의도를 좀 더 분명하게 드러낸다.
- `Optional`을 사용하면 빈 값을 표현할 때 더 이상 `null` 자체를 넘겨주지 않고, `Optional.empty()`처럼 의도를 드러내는
객체를 사용할 수 있다.
- `Optional`을 사용하면 `null` 체크 로직을 간결하게 만들고 특정 경우에 `NullPointException` 예외가 발생할 수 있는 부분을
더 쉽게 파악할 수 있게 해준다.

**null을 직접 반환하는 예제**
```java
import java.util.HashMap;
import java.util.Map;

public class OptionalStartMain {
    private static final Map<Long, String> map = new HashMap<>();

    static {
        map.put(1L, "Kim");
        map.put(2L, "Seo");
    }

    public static void main(String[] args) {
        findAndPrint(1L); // 값이 있는 경우
        findAndPrint(3L); // 값이 없는 경우
    }

    //이름이 있으면 이름을 대문자로 출력, 없으면 "UNKNOWN"을 출력
    static void findAndPrint(Long id) {
        String name = findNameById(id);
        //NPE 발생
        //System.out.println(name.toUpperCase());

        //if문 null 체크 필요
        if (name != null) {
            System.out.println(id + ": " + name.toUpperCase());
        } else {
            System.out.println(id + ": UNKNOWN");
        }
    }

    static String findNameById(Long id) {
        return map.get(id);
    }
}
```

**Optional을 반환하는 예제**
```java
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionalStartMain2 {
    private static final Map<Long, String> map = new HashMap<>();

    static {
        map.put(1L, "Kim");
        map.put(2L, "Seo");
    }

    public static void main(String[] args) {
        findAndPrint(1L); // 값이 있는 경우
        findAndPrint(3L); // 값이 없는 경우
    }

    //이름이 있으면 이름을 대문자로 출력, 없으면 "UNKNOWN"을 출력
    static void findAndPrint(Long id) {
        Optional<String> optName = findNameById(id);
        String name = optName.orElse("UNKNOWN");
        System.out.println(id + ": " + name.toUpperCase());
    }

    static Optional<String> findNameById(Long id) {
        String findName = map.get(id);
        return Optional.ofNullable(findName);
    }
}
```

`Optional`을 사용하면 값이 없을 수도 있다는 점을 호출하는 측에 명확히 전달하여 놓치기 쉬운 `null` 체크를 강제하고
코드의 안전성을 높일 수 있다.

---

## Optional 생성과 값 획득

**Optional을 생성하는 메서드**
1. `Optional.of(T value)` : 내부 값이 확실히 `null`이 아닐 때 사용. `null`을 전달하면 `NullPointException` 발생
2. `Optional.ofNullable(T value)` : 값이 `null`일 수도 있고 아닐 수도 있을 때 사용. `null`이면 `Optional.empty()`를 반환
3. `Optional.empty()` : 명시적으로 값이 없음을 표현할 때 사용

```java
import java.util.Optional;

public class OptionalCreationMain {
    public static void main(String[] args) {
        //of(): 값이 null이 아님이 확실할 때 사용, null이면 NPE 발생
        Optional<String> opt1 = Optional.of("Hello Optional!");
        System.out.println("opt1 = " + opt1); // Optional[Hello Optional!]

        //ofNullable() : 값이 null일 수도 아닐 수도 있을 때
        Optional<String> opt2 = Optional.ofNullable("Hello!");
        Optional<String> opt3 = Optional.ofNullable(null);
        System.out.println("opt2 = " + opt2); // Optional[Hello!]
        System.out.println("opt3 = " + opt3); // Optional.empty

        //empty() : 비어있는 Optional을 명시적으로 생성
        Optional<Object> opt4 = Optional.empty();
        System.out.println("opt4 = " + opt4); // Optional.empty
    }
}
```

**Optional의 값을 확인 또는 획득하는 메서드**
1. `isPresent()`, `isEmpty()`
   - 간단 확인용
   - `isEmpty()` : 자바 11 이상
2. `get()`
   - 값이 없으면 `NoSuchElementException` 발생
   - 직접 사용 시 주의해야 하며, 가급적 `orElse()`, `orElseXXX()` 계열의 메서드를 사용하는 것이 안전
3. `orElse(T other)`
4. `orElseGet(Supplier supplier)`
5. `orElseThrow(Supplier supplier)`
6. `or(Supplier supplier)`
   - 값이 있으면 해당 값의 `Optional`을 그대로 반환
   - 값이 없으면 `Supplier`가 제공하는 다른 `Optional` 반환
   - 값 대신 `Optional`을 반환하는 것이 특징

```java
import java.util.Optional;

public class OptionalRetrievalMain {
    public static void main(String[] args) {
        //문자열 "Hello"가 있는 Optional과 비어있는 Optional 준비
        Optional<String> optValue = Optional.of("Hello");
        Optional<String> optEmpty = Optional.empty();

        //isPresent() / isEmpty()
        System.out.println("optValue.isPresent() = " + optValue.isPresent());
        System.out.println("optEmpty.isPresent() = " + optEmpty.isPresent());
        System.out.println("optEmpty.isEmpty() = " + optEmpty.isEmpty());

        //get() : 직접 내부 값을 꺼냄, 값이 없으면 예외 발생
        System.out.println("optValue.get() = " + optValue.get());
        //System.out.println("optEmpty.get() = " + optEmpty.get()); //NoSuchElementException 발생

        //orElse() : 값이 있으면 그 값, 없으면 지정된 기본값 사용
        System.out.println("optValue.orElse(\"default\") = " + optValue.orElse("default"));
        System.out.println("optEmpty.orElse(\"default\") = " + optEmpty.orElse("default"));

        //값이 없을 때만 Supplier가 실행되어 기본값 생성
        String value2 = optValue.orElseGet(() -> {
            System.out.println("람다 호출 - optValue"); //호출 안됨
            return "New Value";
        });

        String empty2 = optEmpty.orElseGet(() -> {
            System.out.println("람다 호출 - optEmpty");
            return "New Value";
        });
        System.out.println("value2 = " + value2);
        System.out.println("empty2 = " + empty2);

        //값이 있으면 반환, 없으면 예외 발생
        String value3 = optValue.orElseThrow(() -> new RuntimeException("값이 없습니다!"));
        try {
            String empty3 = optEmpty.orElseThrow(() -> new RuntimeException("값이 없습니다!"));
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        //Optional을 반환
        Optional<String> or1 = optValue.or(() -> Optional.of("Fallback"));
        Optional<String> or2 = optEmpty.or(() -> Optional.of("Fallback"));
        System.out.println("or1 = " + or1);
        System.out.println("or2 = " + or2);
    }
}
```
```text
optValue.isPresent() = true
optEmpty.isPresent() = false
optEmpty.isEmpty() = true
optValue.get() = Hello
optValue.orElse("default") = Hello
optEmpty.orElse("default") = default
람다 호출 - optEmpty
value2 = Hello
empty2 = New Value
예외 발생: 값이 없습니다!
or1 = Optional[Hello]
or2 = Optional[Fallback]
```

---

## Optional 값 처리

`Optional`에는 값이 존재할 때와 존재하지 않을 때를 처리하기 위한 다양한 메서드들을 제공한다. 이를 활용하면 `null` 체크 로직 
없이 안전하고 간결하게 값을 다룰 수 있다.

**Optional 값 처리 메서드**
1. `ifPresent(Consumer action)`
2. `ifPresentOrElse(Consumer action, Runnable emptyAction)`
3. `map(Function mapper)`
4. `flatMap(Function mapper)`
5. `filter(Predicate predicate)`
6. `stream()`

```java
import java.util.Optional;

public class OptionalProcessingMain {
    public static void main(String[] args) {
        Optional<String> value = Optional.of("Hello");
        Optional<String> empty = Optional.empty();

        System.out.println("==== 1. ifPresent() ====");
        //값이 존재하면 Consumer, 없으면 아무 일도 하지 않음
        value.ifPresent(v -> System.out.println("v = " + v));
        empty.ifPresent(v -> System.out.println("v = " + v)); //실행 X

        System.out.println();

        System.out.println("==== 2. ifPresentOrElse() ====");
        //값이 존재하면 Consumer, 없으면 Runnable
        value.ifPresentOrElse(
                v -> System.out.println("v = " + v),    // Consumer
                () -> System.out.println("empty")               // Runnable
        );
        empty.ifPresentOrElse(
                v -> System.out.println("v = " + v),    // Consumer
                () -> System.out.println("empty")               // Runnable
        );

        System.out.println();

        System.out.println("==== 3. map() ====");
        /*==== map ====*/
        //값이 있으면 Function 적용 후 Optional 반환, 없으면 Optional.empty()
        Optional<Integer> valueMap = value.map(String::length);
        Optional<Integer> emptyMap = empty.map(String::length);
        System.out.println("valueMap = " + valueMap);
        System.out.println("emptyMap = " + emptyMap);

        System.out.println();

        System.out.println("==== 4. flatMap() ====");
        //flatMap() : map()과 유사, 이미 Optional을 반환하는 경우 중첩을 제거
        Optional<Optional<Integer>> nested = value.map(s -> Optional.of(s.length()));
        Optional<Integer> flat = value.flatMap(s -> Optional.of(s.length()));
        System.out.println("nested = " + nested);
        System.out.println("flat = " + flat);

        System.out.println();

        System.out.println("==== 5. filter() ====");
        //filter() : 값이 있고 조건을 만족하면 그 값을 그대로, 불만족시 Optional.empty()
        Optional<String> filter1 = value.filter(s -> s.startsWith("H"));
        Optional<String> filter2 = value.filter(s -> s.startsWith("X")); //empty
        System.out.println("filter1 = " + filter1);
        System.out.println("filter2 = " + filter2);

        System.out.println();

        System.out.println("==== 6. stream() ====");
        //stream() : 값이 있으면 단일 요소 스트림, 없으면 빈 스트림
        value.stream().forEach(System.out::println);
        empty.stream().forEach(System.out::println); //실행 X
    }
}
```
```text
==== 1. ifPresent() ====
v = Hello

==== 2. ifPresentOrElse() ====
v = Hello
empty

==== 3. map() ====
valueMap = Optional[5]
emptyMap = Optional.empty

==== 4. flatMap() ====
nested = Optional[Optional[5]]
flat = Optional[5]

==== 5. filter() ====
filter1 = Optional[Hello]
filter2 = Optional.empty

==== 6. stream() ====
Hello
```

---

## 즉시 평가와 지연 평가

- **즉시 평가** : 값(또는 객체)을 바로 생성하거나 계산해 버리는 것
- **지연 평가** : 값이 실제로 필요할 때, 즉 사용될 때까지 계산을 미루는 것

**즉시 평가**와 **지연 평가**를 이해하면 `orElse()`와 `orElseGet()`의 차이를 이해할 수 있다.

```java
public class Logger {
    private boolean isDebug = false;

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    //DEBUG로 설정한 경우만 출력 - 데이터를 받음
    public void debug(Object message) {
        if (isDebug) {
            System.out.println("[DEBUG] " + message);
        }
    }
}
```

이 로거 클래스의 사용 목적은 일반적인 상황에서는 로그를 남기지 않다가, 디버깅이 필요한 경우에만 디버깅용 로그를 
추가로 출력하는 것이다.

**V1**
```java
public class LogMain1 {
    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.setDebug(true);
        logger.debug(10 + 20);

        System.out.println("디버그 모드 해제");

        logger.setDebug(false);
        logger.debug(100 + 200);
    }
}
```
```text
[DEBUG] 30
디버그 모드 해제
```

### 자바 언어의 연산 순서와 즉시 평가

자바는 연산식을 보면 기본적으로 **즉시 평가**한다. 예를 들어 `logger.debug(10 + 20)` 연산에서 `10 + 20`이라는 연산을 처리할
순서가 되면 그때 바로 **즉시 평가(계산)** 한다.

너무 자연스럽지만, 이런 방식이 때로는 문제가 되는 경우가 있다. 예를 들어 `logger.setDebug(false)`로 디버그 모드를 해제하고 나서는
`logger.debug(100 + 200)`의 `100 + 200` 연산은 어디에도 사용되지 않는다. 하지만 이 연산은 **즉시 평가**가 되기 때문에 계산된 후에
버려진다. 즉 미래에 전혀 사용하지 않을 값을 계산해서 비용이 낭비된 셈이다.

또 다른 예제로 정말 사용하지도 않을 연산을 처리한 것이지 확인해보자.

```java
public class LogMain2 {
    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.setDebug(true);
        logger.debug(value100() + value200());

        System.out.println("디버그 모드 해제");

        logger.setDebug(false);
        logger.debug(value100() + value200());
    }

    static int value100() {
        System.out.println("value100 호출");
        return 100;
    }

    static int value200() {
        System.out.println("value200 호출");
        return 200;
    }
}
```
```text
value100 호출
value200 호출
[DEBUG] 300
디버그 모드 해제
value100 호출
value200 호출
```

출력 결과를 보면 정말로 메서드를 호출하기 전에 괄호 안의 내용이 먼저 평가(계산)되는 것을 확인할 수 있다.

그렇다면 원하는 대로 디버그 모드일 때만 해당 연산을 처리하려면 어떻게 해야할까? 가장 간단한 방법은 매번 `if`문을 사용해서
체크하는 방법이 있다.

```java
//디버그 모드 체크
if (logger.isDebug()) {
    logger.debug(value100() + value200());
}
```

이렇게 하면 코드는 깔끔하지 않지만, 필요없는 연산을 계산하지 않아도 된다. 코드도 깔끔해지고 사용하지 않을 연산이 미리
수행되지 않도록 하는 방법은 없을까?

이렇게 하려면 **연산을 정의하는 시점**과 **연산을 실행하는 시점**을 분리해야 한다. 
다시 말해 **연산의 실행을 최대한 지연해서 평가(계산)** 해야 한다.

자바 언어에서 이를 구현할 수 있는 방법은 대표적으로 **익명 클래스** 또는 **람다**를 사용해 문제를 해결할 수 있다.

**V2 - Supplier 추가**
```java
import java.util.function.Supplier;

public class Logger {
    private boolean isDebug = false;

    //...

    //추가
    public void debug(Supplier<?> supplier) {
        if (isDebug) {
            System.out.println("[DEBUG] " + supplier.get());
        }
    }
}
```
`Supplier`를 통해서 람다를 받도록 했다. 해당 람다는 **`get()`을 실행할 때** 해당 람다를 평가(연산)한다.

```java
public class LogMain3 {
    public static void main(String[] args) {
        Logger logger = new Logger();
        logger.setDebug(true);
        logger.debug(() -> value100() + value200());

        System.out.println("디버그 모드 해제");

        logger.setDebug(false);
        logger.debug(() -> value100() + value200());
    }

    static int value100() {
        System.out.println("value100 호출");
        return 100;
    }

    static int value200() {
        System.out.println("value200 호출");
        return 200;
    }
}
```
```text
value100 호출
value200 호출
[DEBUG] 300
디버그 모드 해제
```

람다를 사용해서 **연산을 정의하는 시점**과 **실행(평가)하는 시점**을 분리하여 값이 실제로 필요할 때까지 계산을 미룰 수 있었다.

---

## orElse() vs orElseGet()

`orElse()`는 보통 데이터를 받아서 인자가 **즉시 평가**되고, `orElseGet()`은 람다를 받아서 인자가 **지연 평가**된다.

```java
import java.util.Optional;
import java.util.Random;

public class OrElseGetMain {
    public static void main(String[] args) {
        Optional<Integer> optValue = Optional.of(100);
        Optional<Integer> optEmpty = Optional.empty();

        System.out.println("단순 계산");
        Integer i1 = optValue.orElse(10 + 20); //10 + 20 계산 후 버림
        Integer i2 = optEmpty.orElse(10 + 20); //10 + 20 계산 후 사용
        System.out.println("i1 = " + i1);
        System.out.println("i2 = " + i2);

        // 값이 있으면 그 값, 없으면 지정된 기본값 사용
        System.out.println("\n=== orElse ===");
        System.out.println("값이 있는 경우");
        Integer value1 = optValue.orElse(createData()); // 즉시 평가
        System.out.println("value1 = " + value1);

        System.out.println("값이 없는 경우");
        Integer empty1 = optEmpty.orElse(createData()); // 즉시 평가
        System.out.println("empty1 = " + empty1);

        // 값이 있으면 그 값, 없으면 지정된 람다 사용
        System.out.println("\n=== orElseGet ===");
        System.out.println("값이 있는 경우");
        Integer value2 = optValue.orElseGet(() -> createData()); // 지연 평가
        System.out.println("value2 = " + value2);

        System.out.println("값이 없는 경우");
        Integer empty2 = optEmpty.orElseGet(() -> createData()); // 지연 평가
        System.out.println("empty2 = " + empty2);
    }

    static int createData() {
        System.out.println("[데이터 생성 중...]");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("3초 후 - [데이터 생성 완료]");
        return new Random().nextInt(100);
    }
}
```
```text
단순 계산
i1 = 100
i2 = 30

=== orElse ===
값이 있는 경우
[데이터 생성 중...]
3초 후 - [데이터 생성 완료]
value1 = 100
값이 없는 경우
[데이터 생성 중...]
3초 후 - [데이터 생성 완료]
empty1 = 9

=== orElseGet ===
값이 있는 경우
value2 = 100
값이 없는 경우
[데이터 생성 중...]
3초 후 - [데이터 생성 완료]
empty2 = 66
```

**두 메서드의 차이**
- `orElse(T other)`는 빈 값이면 `other`를 반환하는데, `other`를 항상 **미리 계산**한다. 
`other`를 생성하는 비용이 큰 경우, 실제로 값이 있을 때도 쓸데없이 생성 로직이 실행될 수 있기 때문에 주의해야 한다.
- `orElseGet(Supplier supplier)`은 빈 값이면 `supplier`를 통해 값을 생성하기 때문에, 값이 있을 때는 `supplier`가 호출되지 않는다.
- 정리하면 `orElseGet()`에 넘기는 표현식은 필요할 때만 평가해 **지연 평가**가 적용되므로, 생성 비용이 높은 객체를 다룰 때는
`orElseGet()`이 더 효율적이다.

**사용 용도**
- `orElse(T other)`
  - **값이 존재하지 않을 가능성(생성 연산이 꼭 필요한 경우)이 높거나**, 또는 `orElse()`에 넘기는 **객체(또는 메서드)의 생성 비용이 크지 않은 경우**
  - 연산이 없는 상수나 변수의 경우
- `orElseGet(Supplier supplier)`
  - 주로 **`orElse()`의 넘길 값의 생성 비용이 큰 경우**, 또는 **값이 들어있을 가능성이 높아 굳이 매번 대체 값을 계산할 필요가 없는 경우**

---

## Optional 활용 예제

### 예제 1

**시나리오**
1. `User`라는 클래스 안에 `Address`라는 주소 정보가 **있을 수 있다.**
2. 주소가 없을 수도 있으므로, `address` 필드는 **`null`일 수도 있다고 가정한다.**

```java
public class User {
    private String name;
    private Address address;

    public User(String name, Address address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }
}
```
```java
public class Address {
    private String street;

    public Address(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }
}
```

**V1 - null 체크 방식으로 구현**
```java
public class AddressMain1 {
    public static void main(String[] args) {
        User user1 = new User("user1", null);
        User user2 = new User("user1", new Address("hello street"));

        printStreet(user1);
        printStreet(user2);
    }

    //주소가 없으면 "UNKNOWN"
    private static void printStreet(User user) {
        String street = getUserStreet(user);
        if (street != null) {
            System.out.println(street);
        } else {
            System.out.println("UNKNOWN");
        }
    }

    static String getUserStreet(User user) {
        if (user == null) {
            return null;
        }

        Address address = user.getAddress();
        if (address == null) {
            return null;
        }
        return address.getStreet();
    }
}
```

**V2 - Optional로 개선**
```java
import java.util.Optional;

public class AddressMain2 {
    public static void main(String[] args) {
        User user1 = new User("user1", null);
        User user2 = new User("user1", new Address("hello street"));

        printStreet(user1);
        printStreet(user2);
    }

    private static void printStreet(User user) {
        Optional<String> street = getUserStreet(user);
        street.ifPresentOrElse(
                System.out::println,    //값이 있을 때
                () -> System.out.println("UNKNOWN") //값이 없을 때
        );
    }

    //Optional을 반환
    static Optional<String> getUserStreet(User user) {
        return Optional.ofNullable(user)
                       .map(User::getAddress)
                       .map(Address::getStreet);
        //map 체이닝 중간에 null이면 Optional.empty() 반환
    }
}
```

### 예제 2

**시나리오**
1. `Order` 클래스 안에 `Delivery` 정보가 **있을 수 있다.**
2. 각 주문의 배송 상태를 출력한다.
3. 배송 정보가 없거나, 배송이 취소된 경우 "배송X" 라고 표시해야 한다.

```java
public class Order {
    private Long id;
    private Delivery delivery;

    public Order(Long id, Delivery delivery) {
        this.id = id;
        this.delivery = delivery;
    }

    public Long getId() {
        return id;
    }

    public Delivery getDelivery() {
        return delivery;
    }
}
```
```java
public class Delivery {
    private String status;
    private boolean canceled;

    public Delivery(String status, boolean canceled) {
        this.status = status;
        this.canceled = canceled;
    }

    public String getStatus() {
        return status;
    }

    public boolean isCanceled() {
        return canceled;
    }
}
```

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DeliveryMain {
    static Map<Long, Order> orderRepository = new HashMap<>();

    static {
        orderRepository.put(1L, new Order(1L, new Delivery("배송 완료", false)));
        orderRepository.put(2L, new Order(2L, new Delivery("배송 중", false)));
        orderRepository.put(3L, new Order(3L, new Delivery("배송 중", true)));
        orderRepository.put(4L, new Order(4L, null));
    }

    public static void main(String[] args) {
        System.out.println("1 = " + getDeliveryStatus(1L)); //배송 완료
        System.out.println("2 = " + getDeliveryStatus(2L)); //배송 중
        System.out.println("3 = " + getDeliveryStatus(3L)); //배송 X
        System.out.println("4 = " + getDeliveryStatus(4L)); //배송 X
    }

    //Optional 사용
    private static String getDeliveryStatus(Long orderId) {
        return findOrder(orderId).map(Order::getDelivery)
                                 .filter(delivery -> !delivery.isCanceled())
                                 .map(Delivery::getStatus)
                                 .orElse("배송 X");
    }

    //if - null 체크 사용
    private static String getDeliveryStatusWithNull(Long orderId) {
        Order order = orderRepository.get(orderId);
        if (order == null) {
            return null;
        }

        Delivery delivery = order.getDelivery();
        if (delivery == null || delivery.isCanceled()) {
            return "배송 X";
        }

        return delivery.getStatus();
    }

    static Optional<Order> findOrder(Long orderId) {
        return Optional.ofNullable(orderRepository.get(orderId));
    }
}
```