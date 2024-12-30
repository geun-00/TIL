# 함수형 인터페이스 API - Function

- 역할 : 매핑(타입 변환)
- 실행 메서드 : `apply()`, `applyAsXxx()`

## Function<T, R>

- 객체 `T`를 객체 `R`로 매핑

![img.png](image/img.png)

## BiFunction<T, U, R>

- 객체 `T`와 `U`를 객체 `R`로 매핑

![img_1.png](image/img_1.png)

## IntFunction<R>

- `int`를 객체 `R`로 매핑

![img_2.png](image/img_2.png)

## DoubleFunction<R>

- `double`을 객체 `R`로 매핑

![img_3.png](image/img_3.png)

## LongFunction<R>

- `long`을 객체 `R`로 매핑

![img_4.png](image/img_4.png)

## ToIntFunction<T>

- 객체 `T`를 `int`로 매핑

![img_5.png](image/img_5.png)

## ToDoubleFunction<T>

- 객체 `T`를 `double`로 매핑

![img_6.png](image/img_6.png)

## ToLongFunction<T>

- 객체 `T`를 `long`으로 매핑

![img_7.png](image/img_7.png)

## IntToDoubleFunction

- `int`를 `double`로 매핑

![img_8.png](image/img_8.png)

## IntToLongFunction

- `int`를 `long`으로 매핑

![img_9.png](image/img_9.png)

## DoubleToIntFunction

- `double`을 `int`로 매핑

![img_10.png](image/img_10.png)

## DoubleToLongFunction

- `double`을 `long`으로 매핑

![img_11.png](image/img_11.png)

## LongToIntFunction

- `long`을 `int`로 매핑

![img_12.png](image/img_12.png)

## LongToDoubleFunction

- `long`을 `double`로 매핑

![img_13.png](image/img_13.png)

## ToIntBiFunction<T, U>

- 객체 `T`와 `U`를 `int`로 매핑

![img_14.png](image/img_14.png)

## ToDoubleBiFunction<T, U>

- 객체 `T`와 `U`를 `double`로 매핑

![img_15.png](image/img_15.png)

## ToLongBiFunction<T, U>

- 객체 `T`와 `U`를 `long`으로 매핑

![img_16.png](image/img_16.png)

---

# Function 디폴트 메서드 - andThen()

- `Function`과 `BiFunction` 에는 디폴트 메서드 `andThen()`이 있다.
- 이 디폴트 메서드로 두 개 이상의 `apply()` 메서드를 실행시킬 수 있다.
- 메서드 체인으로 연결이 가능하다.

![img_17.png](image/img_17.png)

![img_18.png](image/img_18.png)

![img_20.png](image/img_20.png)

문자열을 먼저 대문자로 변환하고, 문자열의 길이를 구한다.

# Function 디폴트 메서드 - compose()

- `Function`에는 디폴트 메서드 `compose()`이 있다.
- 이 디폴트 메서드로 두 개 이상의 `apply()` 메서드를 실행시킬 수 있다.
- 메서드 체인으로 연결이 가능하다.
- `andThen`과 차이점은 함수 실행 순서이다.

![img_19.png](image/img_19.png)

![img_21.png](image/img_21.png)

문자열 길이를 구하기 전에 문자열을 대문자로 변환한다.

# Function 정적 메서드 - identity()

- `Function`에는 정적 메서드 `identity()`가 있다.
- 주로 `stream`에서 `map`으로 구조 변경 시 자기 자신을 인자로 넘겨주는 경우에 사용되는데, `i -> i`와 비교했을 때 가독성이나
명확한 의도 표현에 유리하다는 장점이 있다.

![img_22.png](image/img_22.png)