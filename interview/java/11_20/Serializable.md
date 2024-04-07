# 자바에서 직렬화에 대해서 설명해 주세요.

- 자바는 메모리에 생성된 객체를 **파일 또는 네트워크**로 출력할 수 있다.
- 객체를 출력하려면 필드값을 일렬로 늘어선 바이트로 변경해야 하는데, 이것을 **직렬화(`Serialization`)** 라고 한다.
- 반대로 직렬화된 바이트를 객체의 필드값으로 복원하는 것을 **역직렬화(`Deserialization`)** 라고 한다.
- 자바에서 직렬화를 사용하면 객체의 상태를 다른 프로세스나 JVM 인스턴스, 네트워크 등에서 재사용할 수 있다.

![img_10.png](image/img_10.png)

직렬화를 구현하려면 객체에 `Serializable` 인터페이스를 구현하고, `ObjectOutputStream`으로 직렬화, `ObjectInputStream`으로 역직렬화를 할 수 있다.

### Serializable 인터페이스

- 자바는 `Serializable` 인터페이스를 구현한 클래스만 직렬화할 수 있도록 제한한다.
- 이 인터페이스는 아무런 기능이 없고, 객체를 직렬화할 수 있다고 표시하는 역할을 한다.
- 객체가 직렬화될 때 인스턴스 필드값은 직렬화 대상이지만 정적 필드값(`static 필드`)과 `transient`로 선언된 필드값은 직렬화에서 제외된다.

### 직렬화 주의점

- 클래스 변경을 개발자가 예측할 수 없을 때는 직렬화 사용을 지양한다.
- 개발자가 직접 컨트롤 할 수 없는 클래스(라이브러리 등)는 직렬화 사용을 지양한다.
- 자주 변경되는 클래스는 직렬화 사용을 지양한다.
- 역직렬화에 실패하는 상황에 대한 예외처리는 필수로 구현해야 한다.
- 직렬화 데이터는 타입, 클래스 메타정보를 포험하므로 사이즈가 크다. 때문에 트래픽에 따라 비용 증가 문제가 발생할 수 있기 때문에 `JSON` 포맷으로 변경하는 것이 좋다.


<br>

### 참고
- [참고 동영상](https://www.youtube.com/watch?v=XpPZ7duqNj4)
- [참고 블로그](https://gyoogle.dev/blog/computer-language/Java/Serialization.html)
- [참고 블로그](https://inpa.tistory.com/entry/JAVA-%E2%98%95-%EC%A7%81%EB%A0%AC%ED%99%94Serializable-%EC%99%84%EB%B2%BD-%EB%A7%88%EC%8A%A4%ED%84%B0%ED%95%98%EA%B8%B0#%EC%9E%90%EB%B0%94_%EC%A7%81%EB%A0%AC%ED%99%94_%EB%AC%B8%EC%A0%9C%EC%A0%90)