# call by value와 call by reference 의 차이 예시를 코드를 통해 보여주세요.

```java
public class Main {
    public static void main(String[] args) throws InterruptedException {
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