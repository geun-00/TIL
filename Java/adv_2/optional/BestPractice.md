# Optional 베스트 프랙티스

- `Optional`은 좋아보여도 무분별하게 사용하면 오히려 코드 가독성과 유지보수에 도움이 되지 않을 수 있다.
- `Optional`은 주로 **메서드의 반환값에 대해 값이 없을 수도 있음**을 표현하기 위해 도입되었다는 것을 명심해야 한다.
- 핵심은 **메서드의 반환값**에 `Optional`을 사용하라는 것이다.

## 1. 반환 타입으로만 사용하고, 필드에는 가급적 쓰지 않기

- `Optional`은 주로 **메서드의 반환값**에 대해 값이 없을 수도 있음을 표현하기 위해 도입되었다.
- 클래스의 필드(멤버 변수)에 `Optional`을 직접 두는 것은 권장되지 않는다.

**안티 패턴**
```java
import java.util.Optional;

public class Product {
    private Optional<String> name;
    
    //...
}
```

이렇게 되면 3가지 상황이 발생한다.
1. `name = null` 가능
2. `name = Optional.empty()` 가능
3. `name = Optional.of(value)` 가능
- `Optional` 자체도 참조 타입이기 때문에 개발자의 부주의로 `Optional` 필드에 `null`을 할당하면, 그 자체로 `NullPointException`
    예외를 발생시킬 여지를 남긴다.
- 값이 없음을 명시하기 위해 사용하는 것이 `Optional`인데, 정작 필드 자체가 `null`일 수 있는 것이다.

**권장 예시**
```java
import java.util.Optional;

public class Product {
    private String name; //필드는 원시(또는 참조) 타입 그대로 둔다.

    //...

    //필드가 null일 수도 있음을 고려해야 한다면 메서드에서 Optional로 변환해서 반환할 수 있다.
    public Optional<String> getNameAsOptional() {
        return Optional.ofNullable(name);
    }
}
```

만약 `Optional`로 필드 값을 받고 싶다면 필드는 `Optional`을 사용하지 않고, **반환하는 시점**에 `Optional`로 감싸주는 것이
일반적으로 더 나은 방법이다.

---

## 2. 메서드 매개변수로 Optional을 사용하지 않기

- 자바 공식 문서에도 `Optional`은 메서드의 반환값으로 사용하기를 권장하며, 매개변수로 사용하지 말라고 명시되어 있다.
- 호출하는 측에서는 단순히 `null` 전달 대신 `Optional.empty()`를 전달해야 하는 부담이 생기며, 결국 `null`이든 `Optional.empty()`든 큰 차이가 없어 가독성만 떨어진다.

**안티 패턴**
```java
public void processOrder(Optional<Long> orderId) {
    if (orderId.isPresent()) {
        System.out.println("Order ID: " + orderId.get());
    } 
    else {
        System.out.println("Order ID is empty!");
    }
}
```

**권장 예시**
```java
// 오버로드
public void processOrder(long orderId) {
    // 이 메서드는 orderId가 항상 있어야 하는 경우
    System.out.println("Order ID: " + orderId);
}

// 오버로드
public void processOrder() {
    // 이 메서드는 orderId가 없을 때 호출할 경우
    System.out.println("Order ID is empty!");
}
```
```java
// 방어적 코드(여기서는 null 허용, 내부에서 처리)
public void processOrder(Long orderId) {
    if (orderId == null) {
        System.out.println("Order ID is empty!");
        return;
    }
    
    System.out.println("Order ID: " + orderId);
}
```

- **오버로드**된 메서드를 만들거나, **명시적으로 `null` 허용 여부**를 문서화하는 방식이 있다.
- 어떤 방식이든 `Optional`을 매개변수로 받는 것은 **지양**하고, 오히려 **반환 타입을 `Optional`로** 두는 것이 더 자연스러운 활용 방법이다.

---

## 3. 컬렉션이나 배열 타입을 Optional로 감싸지 않기

- `List`, `Set` 등 컬렉션은 컬렉션 자체로 비어있는 상태를 표현할 수 있다.
- 따라서 `Optional<List<T>>`와 같은 형태는 `Optional.empty()`와 `Collections.emptyList()`가 이중 표현이 되고, 혼란을 야기한다.

**안티 패턴**
```java
public Optional<List<String>> getUserRoles(String userId) {
    List<String> userRolesList = getRoles(userId);
    if (foundUser) {
        return Optional.of(userRolesList);
    } 
    else {
        return Optional.empty();
    }
}

//반환 받은 쪽에서는 다음과 같이 사용해야 한다.
Optional<List<String>> optList = getUserRoles("someUser");
if (optList.isPresent()) {
    // ...
}
```
정작 내부의 리스트가 비어있을 수 있으므로 한번 더 체크해야 하는 모호함이 생긴다. `Optional`이 비어있는지 체크해야 하고,
또 리스트가 비어있는지 추가로 체크해야 한다.

**권장 예시**
```java
public List<String> getUserRoles(String userId) {
    // ...
    if (!foundUser) {
        // 권장: 빈 리스트 반환
        return Collections.emptyList();
    }
    return userRolesList;
}
```

이렇게 빈 컬렉션을 반환하면, 호출 측에서는 단순히 `list.isEmpty()`로 처리하면 된다.

---

## 4. isPresent() + get() 직접 사용하지 않기

- `Optional`의 `get()` 메서드는 가급적 사용하지 않아야 한다.
- `isPresent()`에서 존재할 시 `get()`으로 얻는 것은 사실상 `null` 체크와 다를 바가 없으며, `NoSuchElementException` 같은 예외가 발생할 위험이 있다.
- 대신 `orElseXXX()`, `ifPresentOrElse()`, `map()`, `filter()` 등의 메서드를 사용하여 간결하고 안전하게 처리할 수 있다.

**안티 패턴**
```java
public static void main(String[] args) {
    Optional<String> optStr = Optional.ofNullable("Hello");
    if (optStr.isPresent()) {
        System.out.println(optStr.get());
    } else {
        System.out.println("Nothing");
    }
}
```

**권장 예시**
```java
public static void main(String[] args) {
    Optional<String> optStr = Optional.ofNullable("Hello");
    
    // 1) orElse
    System.out.println(optStr.orElse("Nothing"));
 
    // 2) ifPresentOrElse
    optStr.ifPresentOrElse(
        System.out::println,
        () -> System.out.println("Nothing")
    );
    
    // 3) map
    int length = optStr.map(String::length).orElse(0);
    System.out.println("Length: " + length);
}
```

- 권장 메서드를 잘 조합하면 `get()` 없이도 대부분의 로직을 처리할 수 있다.
- `get()` 메서드는 가급적 사용하지 말고 간단한 예제나 테스트에서만 사용하는 것을 권장한다.
- 반드시 `get()`을 사용해야 한다면, 이럴 때는 반드시 `isPresent()`와 함께 사용하는 것을 권장한다.

---

## 5. orElse()와 orElseGet() 차이를 분명히 이해하기

- `orElse(T other)`는 항상 `other`를 **즉시** 생성하거나 계산한다. **(즉시 평가)**
  - `Optional`의 값이 존재해도 불필요한 연산 또는 객체 생성이 일어날 수 있다.
  - 간단한 상수나 변수 등의 비용이 크지 않은 대체값이라면 `orElse()`를 사용해도 좋다.
- `orElseGet(Supplier supplier)`는 **필요할 때만**(빈 `Optional`일때만) `supplier`의 람다를 호출한다. **(지연 평가)**
  - 값이 이미 존재하는 경우에는 람다가 실행되지 않으므로, 비용이 큰 연산을 뒤로 미룰 수 있다. 
  - **복잡하고 비용이 큰 객체 생성이 필요한 경우** 또는 **Optional 값이 이미 존재할 가능성이 높다면** `orElseGet()`을 사용하자.

---

## 6. 항상 Optional이 좋은 것은 아니다.

`Optional`은 편의성과 안전성을 높여주지만, 모든 곳에서 무조건 사용하는 것은 오히려 코드 복잡성을 증가시킬 수 있다.
특히 다음과 같은 경우 `Optional` 사용이 오히려 불필요할 수 있다.
1. **항상 값이 있는 상황**
   - 비즈니스 로직상 `null`이 될 수 없는 경우에는 그냥 일반 타입을 사용하거나, 방어적 코드로 예외를 던지는 편이 낫다.
2. **값이 없으면 예외를 던지는 것이 더 자연스러운 상황**
   - 예를 들어 ID 기반으로 무조건 존재하는 DB 엔티티를 찾아야 하는 경우, `Optional` 대신 예외를 던지는 게 API 설계상 명확할 수 있다.
3. **흔히 비는 경우가 아니라 흔히 채워져 있는 경우**
   - `Optional`을 쓰면 매번 `get()`, `orElse()`, `orElseThrow()` 등의 처리가 강제되므로 오히려 코드가 장황해질 수 있다.
4. **성능이 극도로 중요한 로우레벨 코드**
   - `Optional`은 래퍼 객체를 생성하므로, 수많은 객체가 단기간에 생겨나는 영역(루프 내부 등)에서는 성능 영향을 줄 수 있다.

```java
// 1. 항상 값이 있는 경우: 차라리 Optional 사용 X
public String findConfigValue() {
    // 이 로직은 무조건 "NotNull" 반환
    // null이 나오면 프로그래밍적 오류
    return "ConfigValue";
}

// 2. 값이 없으면 예외가 맞는 경우
public String findRequiredEntity(Long id) {
    // DB나 Repository에서 무조건 존재해야 하는 엔티티
    Entity entity = repository.find(id);
    if (entity == null) {
        throw new IllegalStateException("Required Entity not found!");
    }
    return entity.getName();
}

// 3. null이 날 가능성이 희박하고, 주요 흐름에서 필수로 존재해야 하는 경우
public String getValue(Data data) {
    // 비즈니스상 data.getValue()가 null이면 안 되는 상황이라면?
    // Optional보다 null 체크 후 예외가 더 직관적일 수 있음
    if (data.getValue() == null) {
        throw new IllegalArgumentException("Value is missing, cannot proceed!");
    }
    return data.getValue();
}
```

---

### 기타 - Optional 기본형 타입 지원

`OptionalInt`, `OptionalLong`, `OptionalDouble`과 같은 기본형 타입의 `Optional`도 있는데, 보통 다음과 같은 이유로 잘 사용되지 않는다.

1. 일반 `Optional`과 달리 `map()`, `flatMap()` 등의 다양한 연산 메서드를 제공하지 않는다. 그래서 범용적으로 활용하기보다는
특정 메서드만 사용하게 되어 일반 `Optional` 처럼 메서드 체인을 이어 가며 코드를 간결하게 작성하기 어렵다.
2. 기존에 이미 `Optional`을 주로 사용하고 있는 코드에서 특정 상황만을 위해 기본형 `Optional`을 섞어 쓰면 오히려 가독성을 떨어뜨린다.

일반적인 상황에서는 일반 `Optional` 하나로 통일하는 편이 가독성과 유지보수 면에서 유리하고, 충분히 빠른 성능을 제공한다.
