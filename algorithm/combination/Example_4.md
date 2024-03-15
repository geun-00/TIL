# 조합 예제 - 4

### [문제(백준(1010번 - 다리 놓기))](https://www.acmicpc.net/problem/1010)

### 문제 분석
- 이 문제의 핵심은 **문제의 내용을 읽고 조합 문제로 생각할 수 있는가**이다.
- 특히 다리끼리는 서로 겹쳐질 수 없다는 조건이 이 문제를 쉽게 만들고 있다.
- 이 조건 때문에 이 문제를 M개의 사이트에서 N개를 선택하는 문제로 변경할 수 있다.
- 겹치지 않게 하려면 동쪽에서 N개를 선택한 후 서쪽과 동쪽의 가장 위쪽 사이트에서부터 차례대로 연결할 수밖에 없기 때문이다.
- 결국 이 문제는 M개에서 N개를 뽑는 경우의 수를 구하는 조합 문제로 변형해 풀 수 있다.

### 손으로 풀어보기
1. **dp[31][31]로 dp 배열을 선언하고, 값을 초기화한다.**
   - **DP 배열 초기화**
       - `D[i][j]`일 떄, `i` = 총 숫자 개수, `j` = 선택 수 개수 (`i`개 중 `j`개를 뽑는 경우의 수)
           - `D[i][1]` = `i` => `i`개 중 1개를 뽑는 경우의 수는 `i`개
           - `D[i][0]` = 1 => `i`개 중 1개도 선택하지 않는 경우의 수는 1개
           - `D[i][i]` = 1 => `i`개 중 `i`개를 선택하는 경우의 수는 1개

2. **점화식을 이용해 dp 배열을 채운다. N과 M의 최댓값이 30보다 작으므로 미리 dp 배열의 값을 30까지 구한다.**
   - **조합 점화식**
       - `D[i][j]` = `D[i - 1][j]` + `D[i - 1][j - 1]`

3. **테스트 케이스를 실행해 D[M][N]을 출력한다.**


### 슈도코드
```text
dp 배열

for 30만큼 반복:
    dp[i][1] = i
    dp[i][0] = 1
    dp[i][i] = 1

for i n 반복:
    for k i만큼 반복:   # 고르는 수의 개수가 전체 개수를 넘을 수 없음
        DP[i][j] = DP[i - 1][j] + DP[i - 1][j - 1]

t(테스트 케이스)

for t 반복:
    m, n 입력
    print(dp[m][n])
```

### 코드 구현 - 파이썬
```python
dp = [[0 for _ in range(31)] for _ in range(31)]

for i in range(31):
    dp[i][1] = i
    dp[i][0] = 1
    dp[i][i] = 1

for i in range(2, 31):
    for j in range(1, i):
        dp[i][j] = dp[i - 1][j] + dp[i - 1][j - 1]

t = int(input())

for _ in range(t):
    n, m = map(int, input().split())

    print(dp[m][n])
```

### 코드 구현 - 자바
```java
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int[][] dp = new int[31][31];
        for (int i = 0; i < 31; i++) {
            dp[i][1] = i;
            dp[i][i] = i;
            dp[i][0] = 1;
        }

        for (int i = 2; i < 31; i++) {
            for (int j = 1; j < 31; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i - 1][j - 1];
            }
        }

        int t = Integer.parseInt(br.readLine());
        for (int i = 0; i < t; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());
            System.out.println(dp[m][n]);
        }
    }
}
```