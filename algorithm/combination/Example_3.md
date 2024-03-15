# 조합 예제 - 3

### [문제(백준(2775번 - 부녀회장이 될테야))](https://www.acmicpc.net/problem/2775)

### 문제 분석
- "a층의 b호에 살려면 자신의 아래층(a - 1층)의 1호부터 b호까지 사람들의 수의 합만큼 사람들을 데려와 살아야 한다."라는 내용을 식으로 하면 다음처럼 된다.
- `D[a][b]` = `D[a-1][0]` + ... + `D[a-1][b-1]` + `D[a-1][b]`
- 위 식을 응용하면 점화식을 변경할 수 있다.
- a층 b호는 a층 b-1호의 값에서 자기 아래층(a - 1층 b호)의 사람 수만 더하면 된다는 것을 알 수 있다.
- 이 내용을 적용해 일반화된 점화식을 다음과 같이 도출할 수 있다.
- `D[a][b]` = `D[a][b-1]` + `D[a-1][b]`
- 층의 수가 매우 적은 편이므로 모든 층수에 관해 구해 놓고 테스트 케이스를 구할 수 있다.

### 손으로 풀어보기
1. **DP 배열을 초기화한다.**
   - **DP 테이블 초기화**
     - `DP[i][1]` = 1 : 1호에는 항상 1명
     - `DP[0][i]` = i : 0층 i호에는 i명이 산다.

2. **DP 배열을 점화식을 활용해 채운다.**
   - **점화식**
     - `D[i][j]` = `D[i][j - 1]` + `D[i - 1][j]`

3. **질의와 관련된 D[k][n]을 출력한다.**

### 슈도코드
```text
dp 리스트

for i 14만큼:
    dp[i][1] = 1  # i층의 1호는 항상 1의 값을 지닐 수 있다.
    dp[0][i] = i  # 0층의 i호는 i의 값을 지닐 수 있다.
    
for i 1~14:
    for j 2~14:
        dp[i][j] = dp[i][j-1] + dp[i-1][j]

t(테스트 케이스)

for t 반복:
    k(층수)
    n(호수)
    print(dp[k][n])
```

### 코드 구현 - 파이썬
```python
dp = [[0 for _ in range(15)] for _ in range(15)]

for i in range(1, 15):
    dp[i][1] = 1
    dp[0][i] = i

for i in range(1, 15):
    for j in range(2, 15):
        dp[i][j] = dp[i][j - 1] + dp[i - 1][j]

t = int(input())

for _ in range(t):
    k = int(input())
    n = int(input())
    
    print(dp[k][n])
```

### 코드 구현 - 자바
```java
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int[][] dp = new int[15][15];
        for (int i = 1; i < 15; i++) {
            dp[i][1] = 1;
            dp[0][i] = i;
        }

        for (int i = 1; i < 15; i++) {
            for (int j = 2; j < 15; j++) {
                dp[i][j] = dp[i][j - 1] + dp[i - 1][j];
            }
        }

        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            int k = Integer.parseInt(br.readLine());
            int n = Integer.parseInt(br.readLine());
            System.out.println(dp[k][n]);
        }
    }
}
```