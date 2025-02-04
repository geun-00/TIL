# 자바 - 자원 정리 이해

다음 예제 코드를 바탕으로 자바의 자원 정리를 이해해보자.

```java
public class CallException extends Exception{
    public CallException(String message) {
        super(message);
    }
}

public class CloseException extends Exception{
    public CloseException(String message) {
        super(message);
    }
}
```
```java
public class ResourceV1 {

    private String name;

    public ResourceV1(String name) {
        this.name = name;
    }

    //정상 로직 호출
    public void call() {
        System.out.println(name + " call");
    }

    /**
     * 비정상 로직 호출
     * @throws CallException
     */
    public void callEx() throws CallException {
        System.out.println(name + " callEx");
        throw new CallException(name + " ex");
    }

    //정상 종료
    public void close() {
        System.out.println(name + " close");
    }

    /**
     * 비정상 종료
     * @throws CloseException
     */
    public void closeEx() throws CloseException {
        System.out.println(name + " closeEx");
        throw new CloseException(name + " ex");
    }
}
```

---

## V1

```java
/**
 * 자원 정리 이해 - V1
 */
public class ResourceCloseMainV1 {

    public static void main(String[] args) {
        try {
            logic();
        } catch (CallException e) {
            System.out.println("CallException 예외 처리");
            throw new RuntimeException(e);
        } catch (CloseException e) {
            System.out.println("CloseException 예외 처리");
            throw new RuntimeException(e);
        }
    }

    private static void logic() throws CallException, CloseException {
        ResourceV1 resource1 = new ResourceV1("resource1");
        ResourceV1 resource2 = new ResourceV1("resource2");

        resource1.call();
        resource2.callEx(); //throw CallException

        System.out.println("자원 정리"); //호출 안됨
        resource2.closeEx();
        resource1.closeEx();
    }
}
```
```text
resource1 call
resource2 callEx
CallException 예외 처리
Exception in thread "main" java.lang.RuntimeException: network.tcp.autoclosable.CallException: resource2 ex
...
```

- 서로 관련된 자원은 나중에 생성한 자원을 먼저 정리해야 한다.
- 예를 들어 위 코드의 경우 `resource2`를 먼저 닫고, `resource1`을 닫아야 한다.
왜냐하면 `resource2`의 입장에서 `resource1`의 정보를 아직 참고하고 있을 수 있기 때문이다.

여기서 문제는 `callEx()`에서 발생하는 예외 때문에 자원 정리 코드가 호출되지 않는다.

---

## V2

이번에는 예외가 발생해도 자원을 정리하도록 해보자.

```java
/**
 * 자원 정리 이해 - V2
 */
public class ResourceCloseMainV2 {

    public static void main(String[] args) {
        try {
            logic();
        } catch (CallException e) {
            System.out.println("CallException 예외 처리");
            throw new RuntimeException(e);
        } catch (CloseException e) {
            System.out.println("CloseException 예외 처리");
            throw new RuntimeException(e);
        }
    }

    private static void logic() throws CallException, CloseException {

        ResourceV1 resource1 = null;
        ResourceV1 resource2 = null;

        try {
            resource1 = new ResourceV1("resource1");
            resource2 = new ResourceV1("resource2");

            resource1.call();
            resource2.callEx(); //throw CallException

        } catch (CallException e) {
            System.out.println("ex: " + e);
            throw e;

        } finally {
            if (resource2 != null) resource2.closeEx(); //throw CloseException
            if (resource1 != null) resource1.closeEx(); //호출 안됨
        }
    }
}
```
```text
resource1 call
resource2 callEx
ex: network.tcp.autoclosable.CallException: resource2 ex
resource2 closeEx
CloseException 예외 처리
Exception in thread "main" java.lang.RuntimeException: network.tcp.autoclosable.CloseException: resource2 ex
...
```

`finally` 코드 블록을 사용해서 자원을 닫는 코드가 항상 호출되도록 했다.
자원 객체를 생성하기 전에 예외가 발생하면 자원은 `null`이 되므로 `null` 체크를 해야 한다.

`finally` 코드 블록은 항상 호출되기 때문에 자원이 잘 정리될 것 같지만, 자원을
정리하는 중에 `finally` 코드 블록 안에서 예외가 발생하면 그 밑에 있는 자원 정리 코드는
호출되지 않는다.

이 코드에서 발생한 핵심 예외는 `CallException`이다. 이 예외 때문에 문제가 된 것이다.
그런데 `finally` 코드 블록에서 자원을 정리하면서 `CloseException` 예외가 추가로 발생했다.
즉 예외 때문에 자원을 정리하고 있는데 자원 정리 중에 또 다른 예외가 발생한 것이다.
이 경우 로직을 호출한 쪽에서는 핵심 예외인 `CallException`이 아니라 `finally` 블록 안에서
새로 생성된 `CloseException`을 받게 된다. **핵심 예외가 사라졌다!**

개발자 입장에서는 핵심 예외를 확인해야 제대로 된 문제를 찾을 수 있을 것이다. 자원을 
닫는 중에 발생한 예외는 부가 예외일 뿐이다.

---

## V3

이번에는 자원 정리 코드에서 또 `try-catch`를 사용해서 자원 정리 중에
발생하는 예외를 잡아서 처리해보자.

```java
/**
 * 자원 정리 이해 - V3
 */
public class ResourceCloseMainV3 {

    public static void main(String[] args) {
        try {
            logic();
        } catch (CallException e) {
            System.out.println("CallException 예외 처리");
            throw new RuntimeException(e);
        } catch (CloseException e) {
            System.out.println("CloseException 예외 처리");
            throw new RuntimeException(e);
        }
    }

    private static void logic() throws CallException, CloseException {

        ResourceV1 resource1 = null;
        ResourceV1 resource2 = null;

        try {
            resource1 = new ResourceV1("resource1");
            resource2 = new ResourceV1("resource2");

            resource1.call();
            resource2.callEx();

        } catch (CallException e) {
            System.out.println("ex: " + e);
            throw e;

        } finally {

            if (resource2 != null) {
                try {
                    resource2.closeEx(); //CloseException 발생
                } catch (CloseException e) {
                    //close()에서 발생한 예외는 버린다. 필요하면 로깅 정도
                    System.out.println("close ex: " + e);
                }
            }

            if (resource1 != null) {
                try {
                    resource1.closeEx(); //CloseException 발생
                } catch (CloseException e) {
                    System.out.println("close ex: " + e);
                }
            }
        }
    }
}
```
```text
resource1 call
resource2 callEx
ex: network.tcp.autoclosable.CallException: resource2 ex
resource2 closeEx
close ex: network.tcp.autoclosable.CloseException: resource2 ex
resource1 closeEx
close ex: network.tcp.autoclosable.CloseException: resource1 ex
CallException 예외 처리
Exception in thread "main" java.lang.RuntimeException: network.tcp.autoclosable.CallException: resource2 ex
...
```

- `finally` 블록에서 각각의 자원을 닫을 때도 예외가 발생하면 예외를 잡아서 처리했다.
- 이렇게 하면 자원 정리 시점에 예외가 발생해도 다음 자원을 닫을 수 있다.
- 자원 정리 시점에 발생한 예외를 잡아서 처리했기 때문에 자원 정리 시점에 발생한 부가 예외가
핵심 예외를 가리지 않는다.
- 자원 정리 시점에 발생한 예외는 당장 더 처리할 수 있는 부분이 없다. 따라서 로그를 남겨서
개발자가 인지할 수 있게 하는 정도면 충분하다.

핵심적인 문제들은 해결되었지만 아쉬운 부분이 많다.

- `resource` 변수를 선언하면서 동시에 할당할 수 없다. (`try`와 `finally`의 스코프가 다르다.)
- `catch` 이후에 `finally`를 호출하여 자원 정리가 조금 늦어진다.
- 개발자가 실수로 `close()`를 호출하지 않을 수도 있다.
- 개발자가 `close()` 호출 순서를 실수할 수도 있다. (자원을 생성한 순서와 반대로 닫아야 한다.)

---

## V4

`try-with-resources`를 사용하여 위 문제들을 해결할 수 있다.

```java
public class ResourceV2 implements AutoCloseable {

    private String name;

    public ResourceV2(String name) {
        this.name = name;
    }

    public void call() {
        System.out.println(name + " call");
    }

    public void callEx() throws CallException {
        System.out.println(name + " callEx");
        throw new CallException(name + " ex");
    }

    @Override
    public void close() throws CloseException {
        System.out.println(name + " close");
        throw new CloseException(name + " ex");
    }
}
```

`try-with-resources`를 사용하기 위해 자원에 `AutoCloseable`을 구현했다.
그리고 `close()`는 항상 `CloseException`을 던진다.

```java
/**
 * 자원 정리 이해 - V4
 */
public class ResourceCloseMainV4 {

    public static void main(String[] args) {
        try {
            logic();
        } catch (CallException e) {
            System.out.println("CallException 예외 처리");

            Throwable[] suppressed = e.getSuppressed();
            for (Throwable throwable : suppressed) {
                System.out.println("throwable = " + throwable);
            }

            throw new RuntimeException(e);
        } catch (CloseException e) {
            System.out.println("CloseException 예외 처리");
            throw new RuntimeException(e);
        }
    }

    private static void logic() throws CallException, CloseException {

        try (ResourceV2 resource1 = new ResourceV2("resource1");
             ResourceV2 resource2 = new ResourceV2("resource2")) {

            resource1.call();
            resource2.callEx(); //throw CallException

        } catch (CallException e) {
            System.out.println("ex: " + e);
            throw e;
        }
    }
}
```
```text
resource1 call
resource2 callEx
//자원 정리 순서 집중!
resource2 close
resource1 close
ex: network.tcp.autoclosable.CallException: resource2 ex
CallException 예외 처리
throwable = network.tcp.autoclosable.CloseException: resource2 ex
throwable = network.tcp.autoclosable.CloseException: resource1 ex
Exception in thread "main" java.lang.RuntimeException: network.tcp.autoclosable.CallException: resource2 ex
...
```

`try-with-resources`는 `close()`를 자동 호출해주는 기능 외에 여러 
기능들을 더 제공한다.

- **리소스 누수 방지** : 모든 리소스가 제대로 닫히도록 보장한다. 실수로 `finally` 블록을 적지 않거나
`finally` 블록 안에서 자원 해제 코드를 누락하는 문제들을 예방할 수 있다.
- **코드 간결성 및 가독성 향상** : 명시적인 `close()` 호출이 필요 없다.
- **스코프 범위 한정** : 리소스로 사용되는 변수의 스코프가 `try` 블록 안으로 한정된다. 따라서
코드 유지보수가 더 쉬워진다.
- **조금 더 빠른 자원 해제** : 기존에는 `try → catch → finally`로 `catch` 이후에 자원을 반납했다.
반면 `try-with-resources`는 `try` 블록이 끝나면 즉시 `close()`를 호출한다.
- **자원 정리 순서** : 먼저 선언한 자원을 나중에 정리한다.
- **부가 예외 포함**
  - `try-with-resources`를 사용하는 중에 핵심 로직 예외와 자원을 정리하는 중에 발생하는 
    부가 예외가 모두 발생하면 어떻게 될까?
  - `try-with-resources`는 핵심 예외만을 반환한다. 부가 예외는 핵심 예외안에
    `Suppressed`로 담아서 반환한다.
  - 개발자는 자원 정리 중에 발생한 부가 예외를 `ex.getSuppressed()`를 통해 활용할 수 있다.
  - 즉 `try-with-resources`를 사용하면 핵심 예외를 반환하면서 동시에 부가 예외도 필요하면 확인할 수 있다.

추가로 자바 예외에는 `ex.addSuppressed(ex)`라는 메서드로 예외 안에 참고할
예외를 담아둘 수 있다.



---

[이전 ↩️ - 네트워크 프로그램 예제]()

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/Main.md)

[다음 ↪️ - 네트워크 프로그램 자원 정리]()