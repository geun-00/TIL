# 스트림(Stream)에 대해서 설명해 주세요.

- 자바에서는 많은 양의 데이터를 저장하기 위해서 배열이나 컬렉션 자료구조를 이용한다.
- 이렇게 저장된 데이터에 접근하기 위해서는 반복문이나 반복자(`iterator`)를 사용하여 매번 새로운 코드를 작성해야 한다.
- 이러한 문제점을 극복하기 위해 Java 8 부터 **스트림 API**를 도입한다.
- 스트림 API는 **데이터를 추상화하여** 다루므로, 다양한 방식으로 저장된 데이터를 읽고 쓰기 위한 공통된 방법을 제공한다.
- 따라서 스트림 API를 사용하면 배열이나 컬렉션뿐만 아니라 파일에 저장된 데이터도 모두 같은 방법으로 다룰 수 있게 된다.

## 스트림 API 특징

- 스트림은 외부 반복을 통해 작업하는 컬렉션과는 달리 **내부 반복**을 통해 작업을 수행한다.
- 스트림은 재사용이 가능한 컬렉션과는 달리 **단 한 번만 사용할 수 있다.**
- 스트림은 **원본 데이터를 변경하지 않는다.**
- 스트림의 연산은 필터-맵(`filter-map`) 기반의 API를 사용하여 지연(`lazy`) 연산을 통해 성능을 최적화한다.
- 스트림은 `parallelStream()` 메서드를 통해 손쉬운 병렬 처리를 지원한다.
- 오토방식 & 언박싱의 비효율을 제거할 수 있는 기본형 스트림을 제공한다.(`IntStream`, `LongStream` 등)

## 스트림 API 동작 흐름

1. **스트림 생성**
   - `Collections.stream()`, `Arrays.stream()`, `Stream.of()`, `Stream.iterate()`, `Stream.generate()` 등으로 생성 가능
2. **스트림 중간 연산(스트림의 변환)**
   - 연산결과가 스트림이기 때문에 **메서드 체인**으로 연결하여 만들 수 있다.(여러 번 사용 가능)
   - 스트림은 중간 연산을 누적하고, **최종 연산이 호출될 때 한꺼번에 처리**되기 때문에(`lazy`) 성능 및 메모리 효율을 향상시킬 수 있다.
   - `filter()`, `distinct()`, `map()`, `limit(),` `skip()`, `sorted()`, `peek()` 등이 있다.
3. **스트림 최종 연산(스트림의 사용)**
   - 중간 연산을 통해 변환된 스트림은 마지막으로 최종 연산을 통해 **각 요소를 소모하여** 결과를 표시한다.(1번만 실행 가능)
   - 즉, 지연(`lazy`) 되었던 모든 중간 연산들이 최종 연산 시에 모두 수행되는 것이다.
   - **이렇게 최종 연산 시에 모든 요소를 소모한 해당 스트림은 더 이상 사용할 수 없게 된다.(일회용)**
   - `forEach()`, `reduce()`, `findFirst()`, `anyMatch()`, `count()`, `max()`, `sum()`, `collect()` 등이 있다.

![img_31.png](img_31.png)


## 스트림 API 장단점

**[장점]**
- **간결한 코드**
  - 반복문을 사용하는 것보다 간결하게 코드를 작성해 가독성도 높일 수 있다.
- **병렬 처리 지원**
  - 내부적으로 병렬 처리를 지원하므로 멀티코어 시스템에서 성능을 향상시킬 수 있다.
- **추상화**
  - 스트림은 다양한 데이터 소스와 동작을 처리할 수 있는 공통 인터페이스를 제공하여 개발자는 세부 사항을 몰라도 같은 방법으로 데이터 처리 작업을 효율적으로 수행할 수 있다.
- **성능 최적화**
  - 스트림은 지연 연산을 통해 필요한 시점에만 데이터 처리를 수행한다.

**[단점]**
- **메모리 부족 & 느린 처리 속도**
  - 대용량의 데이터를 처리할 때 메모리 부족 문제와 처리 속도가 느린 상황이 발생할 수 있다.
  - 성능이 중요한 상황에선 반복문을 사용하는 것이 더 나을 수 있다.
- **어려운 디버깅**
  - 스트림은 메서드 체인을 통해 연산을 하기 때문에 중간 연산 또는 최종 연산에 문제가 생길 경우 전체 메서드 체인을 따라가면서 디버깅 해야 한다.
- **상태 변경 불가**
  - 스트림은 사용 후에는 상태를 변경할 수 없다. 따라서 스트림에서 얻은 결과를 재사용하려면 스트림을 새로 생성해야 한다.

<br>

### 스트림 예시
```java
import java.util.ArrayList;
import java.util.List;

public class StreamMain3 {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();

        students.add(new Student("아이유", "여자", 95));
        students.add(new Student("카리나", "여자", 100));
        students.add(new Student("박보검", "남자", 92));
        students.add(new Student("송중기", "남자", 90));
        students.add(new Student("김태리", "여자", 85));
        students.add(new Student("전정국", "남자", 88));
        students.add(new Student("방탄소년단", "남자", 70));
        students.add(new Student("이지은", "여자", 63));
        students.add(new Student("윤아", "여자", 68));
        students.add(new Student("하정우", "남자", 75));
        students.add(new Student("공유", "남자", 80));


        //90점 이상 사람의 이름 출력하기
        students.stream()
                .filter(student -> student.getScore()>=90)
                .map(Student::getName)
                .forEach(System.out::println);

        //중위값 구하기
        long size = students.stream().count();
        Integer medium = students.stream()
                                 .map(Student::getScore)
                                 .sorted()
                                 .skip(size / 2)
                                 .findFirst()
                                 .orElse(0);

        System.out.println("medium = " + medium);

    }
}
```

<br>

### 참고
- [참고 사이트](https://www.tcpschool.com/java/java_stream_concept)
- [참고 동영상](https://www.youtube.com/watch?v=7Kyf4mMjbTQ)
- [참고 동영상](https://www.youtube.com/watch?v=4ZtKiSvZNu4)
- [참고 블로그](https://velog.io/@kakdark/Stream)