# 자바 동시성 프로그래밍 - 비동기 프로그래밍

## thenCompose[Async] ()

![img_78.png](image/img_78.png)

- **개념**
  - 인스턴스 메서드로서 (비)동기적으로 하나의 `CompletableFutuer` 가 완료되면 그 결과를 다음 작업으로 전달하고 이어서 다음 작업을 수행할 수 있도록 해준다.
  - 이를 통해 여러 비동기 작업을 연속적으로 실행하고 조합할 수 있다.
- **인수 값**
  - `Function<T, CompletionStage<U>>` 함수를 인수로 받고 작업 결과를 반환한다.
- **반환 값**
  - 새로운 `CompletableFutuer<T>` 객체를 반환하며 `CompletableFutuer`에 작업 결과를 저장한다.
- **실행 객체**
  - `UniCompose`

![img_79.png](image/img_79.png)

![img_80.png](image/img_80.png)

### thenCompose[Async] () 흐름도

![img_81.png](image/img_81.png)

### thenCompose[Async] () 예제 코드

![img_85.png](image/img_85.png)

![img_86.png](image/img_86.png)

---

## thenCombine[Async] ()

![img_82.png](image/img_82.png)

- **개념**
  - 두 개의 `CompletableFuture`가 모두 완료되었을 때 특정 함수를 실행하고 그 결과를 새로운 `CompletableFuture`에 저장하고 반환한다.
- **인수 값**
  - `CompletionStage<U>`, `BiFunction<T, U, V>` 함수를 인수로 받고 최종 작업 결과를 반환한다.
- **반환 값**
  - 새로운 `CompletableFutuer<T>` 객체를 반환하며 `CompletableFutuer`에 작업 결과를 저장한다.

![img_83.png](image/img_83.png)

![img_84.png](image/img_84.png)

### thenCombine[Async] () 예제 코드

![img_87.png](image/img_87.png)

![img_88.png](image/img_88.png)

---

[이전 ↩️ - 비동기 프로그래밍 - 비동기 결과 조작(`thenAccept()` & `thenRun()`)](https://github.com/genesis12345678/TIL/blob/main/Java/reactive/AsyncProgramming/thenRun.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/reactive/Main.md)

[다음 ↪️ - 비동기 프로그래밍 - 비동기 작업 조합(`allOf()` & `anyOf()`)](https://github.com/genesis12345678/TIL/blob/main/Java/reactive/AsyncProgramming/anyOf.md)