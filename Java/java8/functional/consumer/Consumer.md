# 함수형 인터페이스 API - Consumer

- 역할 : 매개값을 받아서 처리 (리턴값 X)
- 실행 메서드 : `accept()`

## Consumer<T>

> 객체 `T`를 받아 소비

![img.png](image/img.png)

![img_1.png](image/img_1.png)

## BiConsumer<T, U>

> 객체 `T`와 `U`를 받아 소비

![img_2.png](image/img_2.png)

![img_3.png](image/img_3.png)

## DoubleConsumer

> `double` 값을 받아 소비

![img_4.png](image/img_4.png)

![img_5.png](image/img_5.png)

## IntConsumer

> `int` 값을 받아 소비

![img_6.png](image/img_6.png)

![img_7.png](image/img_7.png)

## LongConsumer

> `long` 값을 받아 소비

![img_8.png](image/img_8.png)

![img_9.png](image/img_9.png)

## ObjIntConsumer<T>

> 객체 `T`와 `int` 값을 받아 소비

![img_10.png](image/img_10.png)

![img_11.png](image/img_11.png)

## ObjDoubleConsumer<T>

> 객체 `T`와 `double` 값을 받아 소비

![img_12.png](image/img_12.png)

![img_13.png](image/img_13.png)

## ObjLongConsumer<T>

> 객체 `T`와 `long` 값을 받아 소비

![img_14.png](image/img_14.png)

![img_15.png](image/img_15.png)

---

# Consumer 디폴트 메서드 - andThen()

- `Consumer`, `IntConsumer`, `DoubleConsumer`, `LongConsumer`, `BiConsumer`에는 디폴트 메서드 `andThen()`이 있다.
- 이 디폴트 메서드로 두 개 이상의 `accept()` 메서드를 실행시킬 수 있다.
- 메서드 체인으로 연결이 가능하다.

![img_16.png](image/img_16.png)

![img_17.png](image/img_17.png)
