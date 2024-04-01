# 쓰레드 세이프 하다는 것은 무엇을 의미하나요?

- **쓰레드 안전(`Thread-Safety`)** 하다는 것은 [멀티 쓰레드 프로그래밍](https://github.com/genesis12345678/TIL/blob/main/interview/os/11_20/Multi.md)에서 일반적으로 어떤 함수나 변수, 혹은 객체가 **여러 쓰레드로부터** 동시에 접근이 이루어져도
  프로그램의 실행에 문제가 없는 것을 말한다.
- 하나의 함수가 한 쓰레드로부터 호출되어 실행 중일 때, 다른 쓰레드가 그 함수를 호출하여 동시에 함께 실행되더라도 각 쓰레드에서의 함수 실행 결과가 올바르게 나오는 것을 말한다.

**쓰레드 안전 여부 판단 방법**
- 전역 변수나 힙, 파일과 같이 여러 쓰레드가 동시에 접근할 수 있는 자원을 사용하는가?
- 핸들과 포인터를 통한 데이터의 간접 접근이 가능한가?
- 부수 효과(`Side Effect`)를 가져오는 코드가 있는가?

# 쓰레드 안전을 지키기 위한 4가지 방법

- Mutual Exclusion (상호 배제)
- Atomic Operation (원자 연산)
- Thread-Local Storage (쓰레드 로컬 저장소)
- Re-Entrancy(재진입성)

### Mutual Exclusion (상호 배제)

- 공유 자원에 하나의 `Thread`만 접근할 수 있도록 [세마포어 / 뮤텍스](https://github.com/genesis12345678/TIL/blob/main/interview/os/11_20/MutexSemaphore.md)로 락을 통제하는 방법이다.
- 일반적으로 많이 사용하는 방법이다.

### Atomic Operation (원자 연산)

- 공유 자원에 접근할 때는 **원자 연산**을 이용하거나 원자적으로 정의된 접근 방법을 사용함으로써 상호 배제를 구현할 수 있다.
- **Atomic**
  - 공유 자원 변경에 필요한 연산을 원자적으로 분리한 뒤에 실제로 데이터의 변경이 이루어지는 시점에 `Lock`을 걸고, 데이터를 변경하는 시간 동안
    다른 쓰레드의 접근이 불가능하도록 하는 방법이다.

### [Thread-Local Storage](https://github.com/genesis12345678/TIL/blob/main/Spring/advanced/threadLocal/ThreadLocal.md#%EC%93%B0%EB%A0%88%EB%93%9C-%EB%A1%9C%EC%BB%AC) (쓰레드 로컬 저장소)

- 공유 자원의 사용을 최대한 줄이고 각각의 쓰레드에서만 접근 가능한 저장소를 사용함으로써 동시 접근을 막는 방법이다.
- 일반적으로 공유 상태를 피할 수 없을 때 사용하는 방식이며, 전역 변수 사용을 자제하라는 뜻으로 생각하면 된다.

### Re-entrancy (재진입성)

- 쓰레드 호출과 상관없이 프로그램에 문제가 없도록 작성하는 방법이다.
- 어떤 함수가 한 쓰레드에 의해 호출되어 실행 중이라면 다른 쓰레드가 그 함수를 호출하더라도 그 결과가 각각에게 올바르게 주어져야 한다.
- 쓰레드끼리 독립적으로 동작할 수 있도록 코드를 작성하는 것이다.

<br>

# Java에서 Thread-Safe 하게 설계하는 방법

- `java.util.concurrent` 패키지 하위의 클래스들을 사용한다.
- 인스턴스 변수를 두지 않는다.
- 싱글톤 패턴을 사용한다.
- 동기화(`Syncronized`) 블럭에서 연산을 수행한다.

### 멀티쓰레드 환경에서 안전한 싱글톤 인스턴스 만들기

- **게으른 초기화 (`Synchronized` 블록 사용)**
  - 속도가 너무 느려서 권장되지 않는다.
- **Double-Check Locking**
  - `if`문으로 존재 여부 체크하고, `Synchronized` 블록 사용하는 방법
  - 어느 정도 문제 해결은 가능하지만 완벽하지 않다.
- **Holder에 의한 초기화**
  - 클래스 안에서 클래스(`Holder`)를 두어 **JVM Class Loader** 메커니즘을 이용한 방법
  - 개발자가 직접 동기화 문제를 해결하기 보다는 JVM의 원자적인 특성을 이용해 초기화의 책임을 JVM으로 이동하는 방법
  - 일반적으로 싱글톤을 이용하는 방법

> Spring 에서는 이런 싱글톤 패턴을 통해 `Bean` 에 대한 관리가 너무 복잡해지는 것을 방지하기 위해, Singleton Registry인 **Application Context**를 가지고 있다.


# java.util.concurrent 패키지란?

- Java 5 에서 추가된 패키지로, 멀티쓰레딩 프로그래밍을 위한 다양한 유틸리티 클래스들을 제공하는 패키지이다.
  - **Locks** : 상호 배제를 사용할 수 있는 클래스를 제공한다.
  - **Atomic** : 동기화가 되어있는 변수를 제공한다.
  - **Executors** : 쓰레드 풀 생성, 쓰레드 생명주기 관리, Task 등록과 실행 등을 간편하게 처리할 수 있다.
  - **Queue** : 쓰레드 안전한 큐를 제공한다.
  - **Synchronizers** : 특수한 목적의 동기화를 처리하는 5개의 클래스를 제공한다.
    - `Semaphore`, `CountDownLatch`, `CyclicBarrier`, `Phaser`, `Exchanger`

<br>

### 참고
- [참고 블로그](https://zion830.tistory.com/57)
- [참고 블로그](https://developer-ellen.tistory.com/205)