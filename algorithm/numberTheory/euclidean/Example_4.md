# 확장 유클리드 호제법 예제 - 1

### [문제(백준(21586번 - Ax+By=C))](https://www.acmicpc.net/problem/21568)

### 문제 분석
- 확장 유클리드 호제법을 그대로 구현하면 되는 문제다.

### 손으로 풀어보기
1. **C의 값이 A와 B의 최대 공약수의 배수 형태인지 확인한다. 최대 공약수의 배수 형태라면 C의 값을 최대 공약수로 변경한다. 최대 공약수의 
    배수 형태가 아니라면 -1을 출력하고 프로그램을 종료한다.**

- `3x + 4y = 5`일 때, 3과 4의 최대 공약수는 1고, 5는 1의 배수이므로 C를 1로 변경한다. -> `3x + 4y = 1`

2. **A와 B에 관해 나머지가 0이 나올 때까지 유클리드 호제법을 수행한다.**

| 유클리드 호제법 실행 | 나머지 | 몫 |
|-------------|-----|---|
| `3 % 4 = 3` | 3   | 0 |
| `4 % 3 = 1` | 1   | 1 |
| `3 % 1 = 0` | 0   | 3 |

3. **나머지가 0이 나오면 x = 1, y = 0으로 설정한 후 과정 2에서 구한 몫들을 식(`x = y', y = x' - y' * 몫`)에 대입하면서 역순으로 계산한다.**

| 나머지 | 몫 | `x = y'`, `y = x' - y' * q` 역순 계산           |
|-----|---|---------------------------------------------|
| 3   | 0 | `X = -1`, `Y = 1 - (-1 * 0) = 1` , 계산 순서: 3 |
| 1   | 1 | `X = 1`, `Y = 0 - (1 * 1) = -1` , 계산 순서: 2  |
| 0   | 3 | `X = 0`, `Y = 1 - (0 * 3) = 1` , 계산 순서: 1   |

4. **최종으로 계산된 x, y값에 C를 x와 y의 최대공약수로 나눈 값을 각각 곱해 방정식의 해를 구할 수 있다.**

- 몫 = `5 / gcd(a, b)` = `5 / 1 = 5`, `x = -1 * 5 = -5`, `y = 1 * 5 = 5`
  - x = -5
  - y = 5
### 슈도코드
```text
a b c 입력

gcd(a, b):
    if b가 0:
        a가 최대 공약수
    else:
        gcd(작은 수, 큰 수 % 작은 수)

# 확장 유클리드 호제법 구현
solution(a, b):
    if b가 0:
        재귀 함수를 중단하고 x, y를 각각 1, 0으로 설정하여 return
    quot(a를 b로 나눈 몫)
    solution(b, a % b)  # 재귀 호출
    x = y', y = x' - y' * 몫을 계산하는 로직 구현
    # 재귀에서 빠져나오는 영역에서 실행하면 자연스럽게 역순이 된다.
    계산된 x y return
    
최대 공약수 = gcd(a, b)

if c가 최대 공약수의 배수가 아니면:
    -1 출력
else:
    나머지(b)가 0이 될 때까지 확장 유클리드 호제법 함수 호출
    결괏값에 c/최대 공약수의 값을 곱한 후 해당 값 출력
```

### 코드 구현 - 파이썬
```python
a, b, c = map(int, input().split())


def gcd(a, b):
    if b == 0:
        return a
    else:
        return gcd(b, a % b)


def solution(a, b):
    temp = [0] * 2
    if b == 0:
        temp[0] = 1
        temp[1] = 0
        return temp
    quot = a // b
    v = solution(b, a % b)

    temp[0] = v[1]
    temp[1] = v[0] - v[1] * quot

    return temp


mgcd = gcd(a, b)

if c % mgcd != 0:
    print(-1)
else:
    quot = int(c / mgcd)
    temp = solution(a, b)
    print(temp[0] * quot, end=' ')
    print(temp[1] * quot)
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int a = Integer.parseInt(st.nextToken());
        int b = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());

        int gcd = gcd(a, b);

        if (c % gcd != 0) {
            System.out.println(-1);
        } else {
            int quot = c / gcd;
            int[] result = solution(a, b);
            System.out.println(result[0] * quot + " " + result[1] * quot);
        }
    }

    private static int[] solution(int a, int b) {
        int[] temp = new int[2];
        if (b == 0) { //나머지가 0일 때
            temp[0] = 1; //x는 1
            temp[1] = 0; //y는 0
            return temp;
        }
        int quot = a / b;
        int[] v = solution(b, a % b);
        //역순으로 계산 실행
        temp[0] = v[1]; // x = y'
        temp[1] = v[0] - v[1] * quot;  // y = x' - y' * 몫
        return temp;
    }

    private static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }
}
```