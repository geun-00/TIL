# 자바 - 리플렉션

---

## 클래스와 메타데이터

클래스가 제공하는 다양한 정보를 동적으로 분석하고 사용하는 기능을 **리플렉션(Reflection)** 이라 한다.
리플렉션을 통해 프로그램 실행 중에 클래스, 메서드, 필드 등에 대한 정보를 얻거나 새로운 객체를
생성하고 메서드를 호출하며 필드의 값을 읽고 쓸 수 있다.

리플렉션을 통해 다음과 같은 정보를 얻을 수 있다.
- **클래스의 메타데이터** : 클래스 이름, 접근 제어자, 부모 클래스, 구현된 인터페이스 등
- **필드 정보** : 필드의 이름, 접근 제어를 확인하고, 해당 필드의 값을 읽거나 수정할 수 있다.
- **메서드 정보** : 메서드 이름, 반환 타입, 매개변수 정보를 확인하고, 실행 중에 동적으로 
    메서드를 호출할 수 있다.
- **생성자 정보** : 생성자의 매개변수 타입과 개수를 확인하고, 동적으로 객체를 생성할 수 있다.

```java
/**
 * 리플렉션 - 예제 클래스
 */
public class BasicData {

    public String publicField;
    private int privateField;

    public BasicData() {
        System.out.println("BasicData.BasicData");
    }

    private BasicData(String data) {
        System.out.println("BasicData.BasicData: " + data);
    }

    public void call() {
        System.out.println("BasicData.call");
    }

    public String hello(String str) {
        System.out.println("BasicData.hello");
        return str + " hello";
    }

    private void privateMethod() {
        System.out.println("BasicData.privateMethod");
    }

    void defaultMethod() {
        System.out.println("BasicData.defaultMethod");
    }

    protected void protectedMethod() {
        System.out.println("BasicData.protectedMethod");
    }
}
```
```java
/**
 * 리플렉션 - 클래스 메타데이터 조회
 */
public class BasicV1 {
    public static void main(String[] args) throws ClassNotFoundException {

        /*===클래스 메타데이터 조회 방법 3가지===*/

        //1. 클래스에서 찾기
        Class<BasicData> basicDataClass1 = BasicData.class;
        System.out.println("basicDataClass1 = " + basicDataClass1);

        //2. 인스턴스에서 찾기
        BasicData basicInstance = new BasicData();
        Class<? extends BasicData> basicDataClass2 = basicInstance.getClass();
        System.out.println("basicDataClass2 = " + basicDataClass2);

        //3. 문자로 찾기
        String className = "reflection.data.BasicData";
        Class<?> basicDataClass3 = Class.forName(className);
        System.out.println("basicDataClass3 = " + basicDataClass3);
    }
}
```
```text
basicDataClass1 = class reflection.data.BasicData
BasicData.BasicData
basicDataClass2 = class reflection.data.BasicData
basicDataClass3 = class reflection.data.BasicData
```

클래스의 메타데이터는 `Class` 라는 클래스로 표현되며, `Class` 클래스를 획득하는 3가지 방법이 있다.
- **클래스에서 찾기**
  - 클래스명에 `.class`를 사용하여 획득할 수 있다.
- **인스턴스에서 찾기**
  - 인스턴스에서 `.getClass()` 메서드를 호출하여 획득할 수 있다.(`Object` 메서드)
  - 반환 타입을 보면 `Class<? extends BasicData>`로 표현되는데, 실제 인스턴스가 `BasicData` 타입일 수도 있지만
    그 자식 타입일 수도 있기 때문이다.
- **문자로 찾기**
  - 단순히 문자로 클래스의 메타데이터를 조회할 수 있다.
  - 예를 들어 콘솔에서 사용자 입력으로 원하는 클래스를 동적으로 찾을 수 있다는 뜻이다.
  - 단 패키지명을 포함한 문자가 필요하다.

**이제 이렇게 찾은 클래스 메타데이터로 어떤 일들을 할 수 있는지 알아보자.**

```java
import java.lang.reflect.Modifier;
import java.util.Arrays;
/**
 * 리플렉션 - 기본 정보 탐색
 */
public class BasicV2 {
    public static void main(String[] args) {

        Class<BasicData> basicData = BasicData.class;

        System.out.println("basicData.getName() = " + basicData.getName());
        System.out.println("basicData.getSimpleName() = " + basicData.getSimpleName());
        System.out.println("basicData.getPackage() = " + basicData.getPackage());

        System.out.println("basicData.getSuperclass() = " + basicData.getSuperclass());
        System.out.println("basicData.getInterfaces() = " + Arrays.toString(basicData.getInterfaces()));

        System.out.println("basicData.isInterface() = " + basicData.isInterface());
        System.out.println("basicData.isEnum() = " + basicData.isEnum());
        System.out.println("basicData.isAnnotation() = " + basicData.isAnnotation());

        int modifiers = basicData.getModifiers();
        System.out.println("basicData.getModifiers() = " + modifiers);
        System.out.println("isPublic = " + Modifier.isPublic(modifiers));
        System.out.println("Modifier.toString() = " + Modifier.toString(modifiers));
    }
}
```
```text
basicData.getName() = reflection.data.BasicData
basicData.getSimpleName() = BasicData
basicData.getPackage() = package reflection.data
basicData.getSuperclass() = class java.lang.Object
basicData.getInterfaces() = []
basicData.isInterface() = false
basicData.isEnum() = false
basicData.isAnnotation() = false
basicData.getModifiers() = 1
isPublic = true
Modifier.toString() = public
```

클래스 이름, 패키지, 부모 클래스, 구현한 인터페이스, 수정자 정보 등 다양한
정보를 획득할 수 있다.

> 수정자는 접근 제어자와 비접근 제어자로 나눌 수 있다.
> - 접근 제어자 : `public`, `protected`, `default`, `private`
> - 비접근 제어자(기타 수정자) : `static`, `final`, `abstract`, `synchronized`, `volatile` 등
> 
> `getModifiers()`를 통해 수정자가 조합된 숫자를 얻고, `Modifiers`를 사용해서
> 실제 수정자 정보를 확인할 수 있다.

---

## 메서드 탐색과 동적 호출

클래스 메타데이터를 통해 클래스가 제공하는 메서드의 정보를 확인할 수 있다.

```java
import java.lang.reflect.Method;
/**
 * 리플렉션 - 메서드 메타데이터
 */
public class MethodV1 {
    public static void main(String[] args) {

        Class<BasicData> helloClass = BasicData.class;

        System.out.println("===== methods() =====");
        Method[] methods = helloClass.getMethods();

        for (Method method : methods) {
            System.out.println("method = " + method);
        }

        System.out.println("\n===== declaredMethods() =====");
        Method[] declaredMethods = helloClass.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.println("method = " + method);
        }
    }
}
```
```text
===== methods() =====
method = public void reflection.data.BasicData.call()
method = public java.lang.String reflection.data.BasicData.hello(java.lang.String)
method = public boolean java.lang.Object.equals(java.lang.Object)
method = public java.lang.String java.lang.Object.toString()
method = public native int java.lang.Object.hashCode()
method = public final native java.lang.Class java.lang.Object.getClass()
method = public final native void java.lang.Object.notify()
method = public final native void java.lang.Object.notifyAll()
method = public final void java.lang.Object.wait(long) throws java.lang.InterruptedException
method = public final void java.lang.Object.wait(long,int) throws java.lang.InterruptedException
method = public final void java.lang.Object.wait() throws java.lang.InterruptedException

===== declaredMethods() =====
method = public void reflection.data.BasicData.call()
method = private void reflection.data.BasicData.privateMethod()
method = protected void reflection.data.BasicData.protectedMethod()
method = void reflection.data.BasicData.defaultMethod()
method = public java.lang.String reflection.data.BasicData.hello(java.lang.String)
```

- `getMethods()` : 해당 클래스와 상위 클래스에서 상속된 **모든 `public` 메서드를 반환**
- `getDeclaredMethods()` : **해당 클래스에서 선언된 모든 메서드를 반환**하며, 접근 제어자에
관계없이 반환한다. (상속된 메서드는 포함하지 않는다.)

```java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 리플렉션 - 동적 메서드 호출
 */
public class MethodV2 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //정적 메서드 호출 - 일반적인 메서드 호출
        BasicData helloInstance = new BasicData();
        helloInstance.call(); //코드를 변경하지 않는 이상 정적이다.

        //동적 메서드 호출 - 리플렉션 사용
        Class<? extends BasicData> helloClass = helloInstance.getClass();

        //메서드 이름을 변수로 변경할 수 있다.
        String methodName = "hello";
        Method method = helloClass.getDeclaredMethod(methodName, String.class);
        Object returnValue = method.invoke(helloInstance, "hi");
        System.out.println("returnValue = " + returnValue);
    }
}
```
```text
BasicData.BasicData
BasicData.call
BasicData.hello
returnValue = hi hello

```

- 리플렉션을 사용하면 다양한 체크 예외가 발생한다.
- **일반적인 메서드 호출 - 정적**
  - 인스턴스의 참조를 통해 메서드를 호출하는 방식
  - 이 방식은 코드를 변경하지 않는 이상 다른 메서드로 변경하는 것이 불가능하다.
  - 호출하는 메서드가 이미 코드로 작성되어서 정적으로 변경할 수 없는 상태이다.
- **동적 메서드 호출 - 리플렉션 사용**
  - 리플렉션을 사용하면 동적으로 메서드를 호출할 수 있다.
  - 클래스 메타데이터가 제공하는 `getMethod()`에 메서드 이름, 사용하는 매개변수의 타입을 전달하면
    원하는 메서드를 찾을 수 있다.
  - `method.invoke()` 메서드에 실행할 인스턴스와 인자를 전달하면 해당 인스턴스에 있는
    메서드를 실행할 수 있다.
  - 메서드를 찾을 때 `getMethod()`에 `String` 변수를 넘겨주었다. 즉 예를 들어 사용자 콘솔 입력을 통해서
    얼마든지 호출할 메서드 이름을 변경할 수 있다.
  - 따라서 여기서 호출할 메서드 대상은 정적으로 딱 코드에 정해진 것이 아니라, 언제든지
    동적으로 변경할 수 있다.

<details>
    <summary>동적 메서드 호출 예시</summary>

```java
/**
 * 리플렉션 - 동적 메서드 호출 - 예시
 */
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }
}
```
```java
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
/**
 * 리플렉션 - 동적 메서드 호출 - 예시
 */
public class MethodV3 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("호출 메서드: ");

        String methodName = scanner.nextLine();

        System.out.print("숫자1: ");
        int num1 = scanner.nextInt();

        System.out.print("숫자2: ");
        int num2 = scanner.nextInt();

        Calculator calculator = new Calculator();
        //호출할 메서드를 변수 이름으로 동적으로 선택

        Class<? extends Calculator> calculatorClass = calculator.getClass();
        Method method = calculatorClass.getMethod(methodName, int.class, int.class);

        Object returnValue = method.invoke(calculator, num1, num2);
        System.out.println("returnValue = " + returnValue);
    }
}
```
```text
호출 메서드: add
숫자1: 1
숫자2: 3
returnValue = 4
```
```text
호출 메서드: sub
숫자1: 1
숫자2: 3
returnValue = -2
```

</details>

---

## 필드 탐색과 값 변경

리플렉션을 활용해서 필드를 탐색하고 필드의 값을 변경할 수 있다.

```java
import java.lang.reflect.Field;
/**
 * 리플렉션 - 필드 탐색
 */
public class FieldV1 {
    public static void main(String[] args) {

        Class<BasicData> helloClass = BasicData.class;

        System.out.println("===== fields() =====");
        Field[] fields = helloClass.getFields();
        for (Field field : fields) {
            System.out.println("field = " + field);
        }

        System.out.println("\n===== declaredFields() =====");
        Field[] declaredFields = helloClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println("declaredField = " + declaredField);
        }
    }
}
```
```text
===== fields() =====
field = public java.lang.String reflection.data.BasicData.publicField

===== declaredFields() =====
declaredField = public java.lang.String reflection.data.BasicData.publicField
declaredField = private int reflection.data.BasicData.privateField
```

- `fields()` : 해당 클래스와 상위 클래스에서 상속된 **모든 `public` 필드를 반환**
- `declaredFields()` : **해당 클래스에서 선언된 모든 필드를 반환**하며, 접근 제어자에 관계없이 반환
  (상속된 필드는 포함하지 않는다.)

```java
/**
 * 리플렉션 - 필드 값 변경 (예제 클래스)
 */
public class User {

    private String id;
    private String name;
    private Integer age;

    public User(String id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /*getter and setter*/
    /*toString*/
}
```
```java
import java.lang.reflect.Field;
/**
 * 리플렉션 - 필드 값 변경
 */
public class FieldV2 {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        User user = new User("Id1", "userA", 20);
        System.out.println("기존 이름 = " + user.getName());

        Class<? extends User> userClass = user.getClass();
        Field nameField = userClass.getDeclaredField("name");

        //private 필드에 접근 허용, private 메서드도 이렇게 호출 가능
        nameField.setAccessible(true);
        nameField.set(user, "userB");
        System.out.println("변경된 이름 = " + user.getName());
    }
}
```
```text
기존 이름 = userA
변경된 이름 = userB
```

- 리플렉션은 `private` 필드에 접근할 수 있는 특별한 기능을 제공한다.
- `setAccessible(true)` 기능은 `Method`도 제공하기 때문에 `private` 메서드도 호출할 수 있다.

> **리플렉션 주의사항**
> 
> 리플렉션을 활용하면 `private` 접근 제어자에도 직접 접근해서 값을 변경할 수 있다.
> 하지만 이는 **객체 지향 프로그래밍의 원칙을 위반하는 행위로 간주될 수 있다.** `private` 접근 제어자는
> 클래스 내부에서만 데이터를 보호하고, 외부에서의 직접적인 접근을 방지하기 위해 사용된다.
> 리플렉션을 통해 이러한 접근 제한을 무시하는 것은 캡슐화 및 유지보수성에 악영향을 미칠 수 있다.
> 예를 들어 클래스의 내부 구조나 구현 세부 사항이 변경될 경우 리플렉션을 사용한 코드는 쉽게 깨질 수 있으며
> 예상치 못한 버그를 초래한다.
> 
> 가능한 경우 `getter`, `setter` 같은 접근 메서드를 사용하는 것이 바람직하다.
> 리플렉션은 주로 테스트나 라이브러리 개발 같은 특별한 상황에서 유용하지만, 일반적인
> 애플리케이션 코드에서는 권장되지 않는다. 이를 무분별하게 사용하면 코드의 가독성과 안전성을
> 크게 저하시킬 수 있다.

---

## 리플렉션 활용 예제

데이터를 저장할 때 반드시 `null`을 사용하면 안 된다고 가정해보자.

```java
/**
 * 리플렉션 - 활용 예제
 */
public class User {

    private String id;
    private String name;
    private Integer age;

    public User(String id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    /*getter and setter*/
    /*toString*/
}
```
```java
/**
 * 리플렉션 - 활용 예제
 */
public class Team {

    private String id;
    private String name;

    public Team(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /*getter and setter*/
    /*toString*/
}
```
```java
/**
 * 리플렉션 - 활용 예제
 */
public class FieldV3 {
    public static void main(String[] args) {

        User user = new User("id1", null, null);
        Team team = new Team("team1", null);
        System.out.println("===== before =====");
        System.out.println("user = " + user);
        System.out.println("team = " + team);

        if (user.getId() == null) user.setId("");
        if (user.getName() == null) user.setName("");
        if (user.getAge() == null) user.setAge(0);

        if (team.getId() == null) team.setId("");
        if (team.getName() == null) team.setName("");

        System.out.println("\n===== after =====");
        System.out.println("user = " + user);
        System.out.println("team = " + team);
    }
}
```
```text
===== before =====
user = User{id='id1', name='null', age=null}
team = Team{id='team1', name='null'}

===== after =====
user = User{id='id1', name='', age=0}
team = Team{id='team1', name=''}
```

각각의 객체에 들어있는 데이터를 직접 다 찾아서 값을 입력했다. 다른 클래스에도
이와 같은 기능을 적용해야 한다면 매우 많은 번거로운 코드를 작성해야 한다.

리플렉션을 사용해서 이 문제를 해결해보자.

```java
import java.lang.reflect.Field;
/**
 * 리플렉션 - 리플렉션 활용 필드 기본 값 도입
 * 어떤 객체든 받아서 기본 값을 적용하는 유틸리티 클래스
 */
public class FieldUtil {

    public static void nullFieldToDefault(Object target) throws IllegalAccessException {
        Class<?> targetClass = target.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();

        for (Field field : declaredFields) {
            field.setAccessible(true);

            if (field.get(target) != null) {
                continue;
            }

            if (field.getType() == String.class) {
                field.set(target, "");
            }
            else if (field.getType() == Integer.class) {
                field.set(target, 0);
            }
        }
    }
}
```
```java
/**
 * 리플렉션 - 활용 예제
 */
public class FieldV4 {
    public static void main(String[] args) throws IllegalAccessException {

        User user = new User("id1", null, null);
        Team team = new Team("team1", null);

        System.out.println("===== before =====");
        System.out.println("user = " + user);
        System.out.println("team = " + team);

        FieldUtil.nullFieldToDefault(user);
        FieldUtil.nullFieldToDefault(team);

        System.out.println("\n===== after =====");
        System.out.println("user = " + user);
        System.out.println("team = " + team);
    }
}
```
```text
===== before =====
user = User{id='id1', name='null', age=null}
team = Team{id='team1', name='null'}

===== after =====
user = User{id='id1', name='', age=0}
team = Team{id='team1', name=''}
```

리플렉션을 사용하여 다른 객체에도 편리하게 기본 값을 적용할 수 있게 되었다.
이처럼 리플렉션을 활용하면 기존 코드로 해결하기 어려운 공통 문제를 손쉽게 처리할 수도 있다.

---

## 생성자 탐색과 객체 생성

리플렉션을 활용하면 생성자를 탐색하고 탐색한 생성자를 사용해서 객체를 생성할 수 있다.

```java
import java.lang.reflect.Constructor;
/**
 * 리플렉션 - 생성자 탐색
 */
public class ConstructV1 {
    public static void main(String[] args) throws ClassNotFoundException {

        Class<?> aClass = Class.forName("reflection.data.BasicData"); //패키지명 주의

        System.out.println("===== constructors() =====");
        Constructor<?>[] constructors = aClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("constructor = " + constructor);
        }

        System.out.println("\n===== declaredConstructors() =====");
        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            System.out.println("constructor = " + constructor);
        }
    }
}
```
```text
===== constructors() =====
constructor = public reflection.data.BasicData()

===== declaredConstructors() =====
constructor = public reflection.data.BasicData()
constructor = private reflection.data.BasicData(java.lang.String)
```

```java
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 리플렉션 - 생성자 활용
 */
public class ConstructV2 {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<?> aClass = Class.forName("reflection.data.BasicData");

        Constructor<?> constructor = aClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object instance = constructor.newInstance("hello"); //필요시 캐스팅해서 사용
        System.out.println("instance = " + instance);

        Method method = aClass.getDeclaredMethod("call");
        method.invoke(instance);
    }
}
```
```text
BasicData.BasicData: hello
instance = reflection.data.BasicData@7291c18f
BasicData.call
```

`Class.forName()`으로 클래스 정보를 동적으로 조회하고, `getDeclaredConstructor(String.class)`로
매개변수로 `String`을 사용하는 생성자를 조회한다.

찾은 생성자를 사용해서 인자를 넘겨 객체를 생성한다. 그리고 이렇게 생성한 인스턴스에
메서드 이름을 동적으로 찾아서 호출한다.

위 코드를 보면 클래스를 동적으로 찾아서 인스턴스를 생성하고, 메서드도 동적으로 호출한다.
코드 어디에도 실제 타입(`BasicData`)이나 인스턴스의 메서드를 직접 호출하는 부분을 직접 코딩하지 않았다.
클래스를 찾고 생성하는 방법과 생성한 클래스의 메서드를 호출하는 방법 모두 동적으로 처리한 것이다.

---

[이전 ↩️ - HTTP 서버 개발](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/was/server1.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/Main.md)

[다음 ↪️ - HTTP 서버 개발(with 리플렉션)]()