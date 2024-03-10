# 오일러 피 함수 예제 - 1

### [문제(백준(11689번 - GCD(n, k) = 1))](https://www.acmicpc.net/problem/11689)

### 문제 분석
- `GCD(n , k) = 1`란 `n`과 `k`의 최대 공약수가 1인 수, 즉 서로소를 뜻한다.
- 1 ~ `n` 사이에서 서로소인 자연수의 개수를 묻는, 즉 오일러 피 함수를 잘 구현할 수 있는지 묻는 문제이다.

### 손으로 풀어보기
1. **서로소의 개수를 표현하는 변수 result와 현재 소인수 구성을 표시하는 변수 n을 선언한다. 입력이 45면 `n = 45`, `result = 45`**
2. **오일러 피 핵심 이론을 참고해 2 ~ N의 제곱근까지 탐색하면서 소인수일 때 `result = result - (result / 소인수)`연산으로 result값을 업데이트한다. 이때 n에서
    이 소인수는 나누기 연산으로 삭제한다.**

- `P(현재 수) = 2 => n(45) % P(2) != 0` : 소인수가 아님
- `P(현재 수) = 3 => n(45) % P(3) == 0` : 소인수이므로 값 업데이트
  - `result = 45 - 45 / 3 = 30`
  - `n = 45 / 3^2 = 5` => `n`을 3으로 나누어 떨어질 때까지 나누기 연산 실행(45 -> 15 -> 5)
- `P(현재 수) = 4 => 현재 n(5)의 제곱근보다 4가 크므로 반복문 종료`

3. **반복문 종료 후 현재 `n`이 1보다 크면 `n`이 마지막 소인수라는 뜻이다. `result = result - (result / n)`연산으로 result값을 마지막으로 업데이트한 값이 정답이 된다.**

- `result(30) = 30 - (30 / 5) = 24`


### 슈도코드
```text
n(소인수 표현) result(결과)

for 2 ~ n의 제곱근:
    if 현재 값이 소인수:
        result = result - (result / 현재 값)
        n에서 현재 소인수 내역을 모두 제거(2^7 * 11이라면 11만 남도록)

if n > 1:
    result = result - result / n

result 출력
```

### 코드 구현 - 파이썬
```python
import math

n = int(input())
result = n

for i in range(2, int(math.sqrt(n)) + 1):
    if n % i == 0:
        result -= result / i

        while n % i == 0:
            n /= i

if n > 1:
    result -= result / n

print(int(result))
```
- `while n % i == 0`
  - 현재 소인수로 계속해서 나눌 수 있을 떄까지 나누어준다.
  - 예) `2^7 * 11 * 13`일 때, `2^7`을 제거하는 과정
- `if n > 1`
  - 어떤 수 `n`을 소인수분해 했을 때 `n`의 소인수들 중 `n`의 제곱근 값보다 큰 수를 가진 소인수는 1개 또는 0개다.(1개 이상 있을 수 없음)
  - 반복문에서 효율을 위해 제곱근까지만 탐색했으므로 `n`의 제곱근 값보다 큰 수를 가진 소인수 1개가 누락된 케이스를 처리하는 과정이다.

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        long n = Long.parseLong(br.readLine());
        long result = n;

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                result -= result / i;
            }
            while (n % i == 0) {
                n /= i;
            }
        }

        if (n > 1) {
            result -= result / n;
        }
        System.out.println(result);
    }
}
```