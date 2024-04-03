# Exception과 RuntimeException의 차이는 무엇인가요?

- [자바에서의 예외](https://github.com/genesis12345678/TIL/blob/main/Spring/database_1/javaException/javaException.md#%EC%9E%90%EB%B0%94-%EC%98%88%EC%99%B8)는 두 가지로 나눌 수 있다.
- **체크 예외(Checked Exception)**
  - `Exception`을 상속받은 예외
  - 컴파일 시에 검출되며, 개발자가 예외처리 코드를 구현하지 않으면 컴파일 에러가 발생한다.
  - **체크 예외는 반드시 잡아서 처리하거나 밖으로 던지도록 선언해야 한다.** 그렇지 않으면 컴파일 오류가 발생한다.
  - `SQLException`, `IOException` 등이 있다.
- **언체크 예외(Unchecked Exception)**
  - `RuntimeException`을 상속받은 예외
  - 컴파일 시에는 검출되지 않지만, 프로그램이 실행되는 과정에서 발생하는 예외다.
  - 예외를 던지는 `throws`를 생략할 수 있으며, 이 경우 자동으로 예외를 던지게 된다.
  - `NullPointException`, `IllegalArgumentException` 등이 있다.