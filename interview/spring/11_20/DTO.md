# DAO, DTO, VO, Entity의 차이를 설명해 주세요.

### DAO (Data Access Object)

- **DAO**는 데이터베이스에 접근하는 역할을 하는 객체이다.
- 실제로 DB에 접근하여 데이터의 CRUD 기능을 수행한다.
- JPA에서는 `JpaRepository`를 상속받는 `Repository` 객체들이 **DAO**라고 볼 수 있다.
- 효율적인 커넥션 관리와 보안을 위해 사용한다.

### DTO (Data Transfer Object)

- **DTO**는 계층 간 데이터를 전달하기 위한 객체다.
- 로직을 가지지 않고, `getter`와 `setter` 메서드만 가진 순수한 객체다.
- 데이터 전달만을 위한 객체라면 `getter`, `setter` 외에 메서드를 가질 이유가 없다.
- `setter`를 사용하면 **가변 객체**, 생성자를 사용하면 **불변 객체**로 활용할 수 있다.

### VO (Value Object)

- **VO**는 값 자체를 표현하는 객체다.
- DTO와 비슷한 개념인데, DTO는 `setter`를 가지고 있어 값이 변할 수 있다.
- VO는 `setter`를 가지지 않아 오직 읽기만 가능하다.(불변, **Read-Only**)
- 또한 VO는 `getter` 외에 다른 로직을 포함할 수 있다.
- **VO는 값 자체 표현 용도로 사용하는 것이 목적이기 때문에 속성값이 모두 같으면 같은 객체로 판단해야 한다.** 때문에 `equals()`, `hashCode()`의 오버라이딩이 필요하다.

### Entity

- **Entity**는 실제 DB 테이블과 매핑이 되는 객체다.
- Entity를 DTO처럼 데이터 전달이나 API 응답 용도로 사용하면 안 된다. 왜냐하면, 불필요한 데이터를 노출할 위험도 있고, Entity는 컬럼이 자주 변경될 수 있는데
    컬림이 변경되면 API 스펙 자체가 바뀐다.
- Entity는 `setter`, 비즈니스 로직을 포함할 수 있다.

### 정리

|       | DTO                             | VO                                   | Entity       |
|-------|---------------------------------|--------------------------------------|--------------|
| 용도    | 계층 간 데이터 전송                     | 값 자체 표현                              | DB 테이블 매핑    |
| 가변/불변 | 가변 혹은 불변                        | 불변                                   | 가변 혹은 불변     |
| 로직    | `getter`, `setter`외 로직을 갖지 않는다. | `getter` 외 로직을 가질 수 있다.(`setter` 제외) | 로직을 가질 수 있다. |


<br>

### 참고
- [참고 블로그](https://today-retrospect.tistory.com/142#%7C%20Entity-1)
- [참고 블로그](https://hstory0208.tistory.com/entry/Spring-DAO-DTO-VO%EB%9E%80-%EA%B0%81%EA%B0%81%EC%9D%98-%EA%B0%9C%EB%85%90%EC%97%90-%EB%8C%80%ED%95%B4-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90)
- [참고 동영상](https://www.youtube.com/watch?v=z5fUkck_RZM)