# 조합 예제 - 7

### [문제(백준(1256번 - 사전))](https://www.acmicpc.net/problem/1256)

### 문제 분석
- 사전에서 다루는 문자열이 `a`와 `z`밖에 없다는 점에 착안해 접근해본다.
- **핵심 아이디어**
  - `a`와 `z`의 개수가 각각 N, M개 일때 이 문자들로 만들 수 있는 모든 경우의 수는 N + M개에서 N를 뽑는 경우의 수 또는 N + M개에서 M개를 뽑는 경우의 수와 동일하다.

### 손으로 풀어보기
1. **조합의 경우의 수를 나타내는 dp배열을 초기화하고, 점화식으로 값을 계산해 저장한다.**
   - **조합 점화식**
     - `D[i][j]` = `D[i - 1][j]` + `D[i - 1][j - 1]`

2. **몇 번째 문자열을 표현해야 하는지를 나타내는 변수를 K라고 하고, 현재 자릿수에서 `a`를 선택했을 때 남아있는 문자들로 만들 수 있는 모든 경우의 수를 T라고 했을 떄,
    `T`와 `K`를 비교해 문자를 선택한다.**
   - **문자 선택 기준**
     - `T >= K` : 현재 자리 문자를 `a`로 선택
     - `T < K` : 현재 자리 문자를 `z`로 선택하고, `K = K - T`로 업데이트
   - 예제 입력 1 기준
     - `a = 2`, `z = 2`이고 `a`를 선택했을 때 나머지 문자열로 만들 수 있는 경우의 수는 `D[3][2]`다.(3은 남은 문자 총 개수, 2는 남은 `z`의 개수)
     - `D[3][2]` = 3 >= `K(2)`이므로 `a` 선택, `z`는 2개 남음
     - `D[2][2]` = 1 < `K(2)`이므로 `z` 선택, `z`는 1개 남음, `K = K - T = 1`로 업데이트
     - `D[1][1]` = 1 >= `K(1)`이므로 `a` 선택, `z`는 1개 남음
     - `D[0][1]` = 0 < `K(1)`이므로 `z` 선택

3. **과정 2를 `a`와 `z`의 문자들의 수를 합친 만큼 반복해 정답 문자열을 출력한다.**


### 슈도코드
```text
n(a 문자 개수) m(z 문자 개수) k(순번)
dp(조합 경우의 수 배열)

for i 200번:     # n + m 
    for j 0~i:
        dp[i][j] = dp[i - 1][j] + dp[i - 1][j - 1]
        if dp[i][j]의 값이 k의 범위를 벗어나면:
            dp[i][j]를 k범위의 최댓값으로 저장

if 불가능한 k:
    -1 출력
else:
    while 모든 문자를 사용할 때까지:
        if a 문자를 선택했을 때 남은 문자들로 만들 수 있는 모든 경우의 수 >= k:
            a 출력
            n 1 감소 (a 문자 개수 감소)
        else:
            z 출력
            k의 값을 계산된 모든 경우의 수를 뺀 값으로 저장
            m 1 감소 (z 문자 개수 감소)
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline

n, m, k = map(int, input().split())
dp = [[0 for _ in range(201)] for _ in range(201)]  # n + m개 중에서 조합을 골라야 하므로 n, m의 최댓값은 100, 그래서 200크기로 생성

for i in range(201):
    for j in range(0, i + 1):
        # 0개를 뽑는 경우의 수와 i개 중에 i개를 뽑는 경우의 수는 1이다.
        if j == 0 or j == i:
            dp[i][j] = 1
        else:
            dp[i][j] = dp[i - 1][j] + dp[i - 1][j - 1]
            if dp[i][j] > 1_000_000_000:
                dp[i][j] = 1_000_000_001  # k 최대 범위를 벗어나면 범위의 최댓값 저장, 굳이 처리를 하지 않아도 통과는 된다.

if dp[n + m][m] < k:
    print(-1)
else:
    while not (n == 0 and m == 0):
        if dp[n - 1 + m][m] >= k:  # a를 선택해도 남는 경우의 수가 k보다 큰 경우
            print("a", end='')
            n -= 1
        else:
            print("z", end='')
            k -= dp[n - 1 + m][m]  # n - 1 + m: n(a의 개수)에서 1개를 선택하고 남은 m(z의 개수)를 더한 것, 즉 a를 선택하고 남은 문자 총 개수
            m -= 1
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

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        long[][] dp = new long[201][201];

        for (int i = 0; i <= 200; i++) {
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    dp[i][j] = 1;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i - 1][j - 1];
                    if (dp[i][j] > 1_000_000_000) {
                        dp[i][j] = 1_000_000_001;  //자바에서는 이 처리를 해주지 않으면 ArrayIndexOutOfBounds 에러가 발생한다.
                    }
                }
            }
        }

        if (dp[n + m][m] < k) {
            System.out.println(-1);
        } else {
            while (!(n == 0 && m == 0)) {
                if (dp[n - 1 + m][m] >= k) {
                    System.out.print("a");
                    n--;
                } else {
                    System.out.print("z");
                    k -= (int) dp[n - 1 + m][m];
                    m--;
                }
            }
        }
    }
}
```