# 자바는 call by value와 call by reference 중 무엇인가요?

- 자바는 **call by value**이다.
- 자바는 매개변수를 전달할 때 기본값이든 참조값이든 값을 항상 복사해서 전달한다.
- 새로운 스택 프레임이 생성되면 해당 스택 프레임에는 복사해서 넘긴 참조값을 힙 영역에서 새롭게 추가해 참조한다.
- 때문에 기존 객체의 참조값을 변경할 수 없고, 원래의 값에는 영향을 미치지 않는다.
- **객체의 내용은 변경할 수 있지만, 객체의 참조값 자체는 변경할 수 없다.**




# call by value와 call by reference 의 차이 예시를 코드를 통해 보여주세요.

```java
public class Main {
    public static void main(String[] args) {
        int value = 5;
        callByValue(value);
        System.out.println("value = " + value); //value = 5

        int[] ref = {5};
        callByRef(ref);
        System.out.println("ref[0] = " + ref[0]); //ref[0] = 6

    }
    private static void callByValue(int value) {
        value++;
    }

    private static void callByRef(int[] ref) {
        ref[0]++;
    }
}
```

- `call by value` 는 인자로 받은 값을 **복사하여** 처리하기 때문에 기존값 `value`에는 아무 영향이 없다.
- `call by reference` 는 인자로 받은 값의 **주소를 참조**하기 때문에 기존 참조 `ref`에도 영향이 있다.

```java
public class Main {
    public static void main(String[] args) {
        Person p1 = new Person(10);
        //스택: p1  =>  힙: x001, age=10
        System.out.println("before : " + p1.age); //10

        addAge(p1);
        //스택: p1  =>  힙: x001, age=11
        System.out.println("after1 : " + p1.age); //11

        changePerson(p1);
        //스택: p1  =>  힙: x001, age=11
        System.out.println("after2 : " + p1.age); //11

    }

    private static void changePerson(Person p1) {
        p1 = new Person(30);
        //스택: p1  =>  힙: x002, age = 30
        //메서드가 끝나고 스택 프레임에서 제거되면 p1이 참조하는 x002는 GC 대상
        //참조값을 복사해서 넘겼기 때문에 main의 x001은 그대로
    }

    private static void addAge(Person p1) {
        p1.age++;
    }

    static class Person{
        int age;

        public Person(int age) {
            this.age = age;
        }
    }
}
```

<br>

### 참고
- [참고 블로그](https://www.blog.ecsimsw.com/entry/%EC%9E%90%EB%B0%94%EB%8A%94-Call-by-Value-%EC%9D%B4%EB%8B%A4)