# 조합 예제 - 8

### [문제(백준(1947번 - 선물 전달))](https://www.acmicpc.net/problem/1947)

### 문제 분석
- 완전 순열이라는 개념의 문제이다.
- 완전 순열의 개념은 n개의 원소의 집합에서 원소들을 재배열할 때 이전과 같은 위치에 배치되는 원소가 1개도 없을 때를 말한다.
- 이 문제에서 필요한 것은 완전 순열의 개념이 아니라 문제에 주어진 조건에 따라 적절한 점화식을 도출해내는 역량이다.

### 손으로 풀어보기
1. **dp[N] 배열의 의미를 `N`명일 때 선물을 교환할 수 있는 모든 경우의 수로 정한다. `N`명이 존재한다고 가정했을 때, A가 B에게 선물을 줬다고 가정해본다.
    이때 교환 방식은 다음 2가지 방식만이 존재한다.**
   - **선물을 교환하는 2가지 방식**
     1. B도 A에게 선물을 줬을 때(양방향)
        - `N`명 중 2명이 교환을 완료했으므로 남은 경우의 수는 `dp[N-2]`
     2. B는 A가 아닌 다른 사람에게 선물했을 때(단방향)
        - `N`명 중 B만 받은 선물이 정해진 상태이므로 남은 학생은 `N - 1`이며, 경우의 수는 `dp[N - 1]`

   - 이것은 A가 B라는 학생에게 선물을 준 것으로 가정하고 경우의 수를 생각한 것이지만 실제로는 A는 자기 자신을 제외한 N - 1명에게 선물을 전달할 수 있다.
   - 이 내용을 이용해 도출해낸 완전 순열의 경우의 수를 구하는 점화식은 다음과 같다.
   - **완전 순열 점화식**
     - `dp[N]` = `(N - 1) * (dp[N - 2] + dp[N - 1])`
   - 도출해낸 점화식으로 이 문제를 해결할 수 있다.

2. **dp 배열을 초기화한다.**
   - `dp[1] = 0` : 혼자서는 선물을 교환할 수 없다.
   - `dp[2] = 1` : 두 명인 경우 서로 교환하는 경우만 존재한다.

3. **완전 순열 점화식으로 정답을 구한다.**
   - `dp[3]` = `(3 - 1) * (dp[1] + dp[2])` = 2 * 1 = 2
   - `dp[4]` = `(4 - 1) * (dp[2] + dp[3])` = 3 * 3 = 9
   - `dp[5]` = `(5 - 1) * (dp[3] + dp[4])` = 4 * 11 = `44`

### 슈도코드
```text
n(학생 수)
dp(n명일 때 선물을 교환할 수 있는 모든 경우의 수 배열)
dp[1] = 0
dp[2] = 1

for i 3~n:
    dp[i] = (i - 1) * (dp[i-1] + dp[i-2]) % 1_000_000_000       # 항상 mod 연산한 값을 저장한다.

dp[n] 출력
```

### 코드 구현 - 파이썬
```python
n = int(input())
dp = [0] * 1_000_001
dp[1] = 0
dp[2] = 1

for i in range(3, n + 1):
    dp[i] = (i - 1) * (dp[i - 1] + dp[i - 2]) % 1_000_000_000

print(dp[n])
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        long[] dp = new long[1_000_001];
        dp[1] = 0;
        dp[2] = 1;

        for (int i = 3; i <= n; i++) {
            dp[i] = (i - 1) * (dp[i - 1] + dp[i - 2]) % 1_000_000_000;
        }
        System.out.println(dp[n]);
    }
}
```