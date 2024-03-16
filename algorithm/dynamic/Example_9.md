# 동적 계획법 예제 - 9

### [문제(백준(1328번 - 고층 빌딩))](https://www.acmicpc.net/problem/1328)

### 문제 분석
- 먼저 점화식의 의미를 정의해본다.
  - `dp[N][L][R]` = `N`개의 빌딩이 있고 왼쪽에서 `L`개, 오른쪽에서 `R`개가 보일 때 가능할 경우의 수
- **적절하게 점화식을 정의하고 난 후에는 이 문제를 어떻게 하면 단순화할 수 있을지 생각해 봐야 한다.**
- 먼저 `N - 1`개 빌딩과 관련된 모든 경우의 수를 알고 있다고 가정해 본다.
- 그러면 이후 1개의 빌딩을 어느 곳에 배치할 것인지 결정하는 것이 관건인데, 이때 배치하는 빌딩이 가장 크다면 가장 왼쪽이나 오른쪽에 배치할 때 보이는 빌딩의 수는
  1개가 될 것이다.
- 하지만 중간에 배치하면 어떤 수가 나올지 예상하기 어렵다.
- **1가지 관점을 다르게 생각해보자.**
- **높이가 애매한 빌딩을 마지막에 배치하는 것이 아니라 일정한 규칙에 따라 배치해 단순화 해볼 수 있을 것이다.**
- 가장 큰 빌딩을 마지막에 배치하면 중간에 배치했을 때의 경우가 복잡하므로 이와 반대로 가장 작은 빌딩을 `N`번째로 배치한다고 가정해보면, 명확하게 3가지 경우의 수가 생긴다.
  - 왼쪽에 배치한 경우
    - 왼쪽에서 보는 빌딩의 수 1 증가
  - 오른쪽에 배치한 경우
    - 오른쪽에서 보는 빌딩의 수 1 증가
  - 가운데 배치한 경우
    - 양쪽에서 보는 빌딩의 수가 증가하지 않음
    - 단, 가운데 배치할 수 있는 경우의 수는 빌딩의 수가 `N`개 일때, `N-2`개의 위치에 배열 가능하다.

### 손으로 풀어보기
1. **상황에 따른 점화식을 구한다.**
   - **`N`개의 빌딩이 왼쪽에 `L`개, 오른쪽에 `R`개가 보인다고 가정**
     - `N - 1`개의 빌딩에서 왼쪽에 빌딩을 추가할 때 왼쪽 빌딩이 1개 증가하므로 이전 경우의 수는 다음과 같다.
       - `dp[N - 1][L - 1][R]`
     - `N - 1`개의 빌딩에서 오른쪽에 빌딩을 추가할 때 오른쪽 빌딩이 1개 증가하므로 이전 경우의 수는 다음과 같다.
       - `dp[N - 1][L][R - 1]`
     - `N - 1`개의 빌딩에서 가운데 빌딩을 추가할 때는 증가 수가 없지만, `N - 2`개의 위치에 배치할 수 있으므로 `N - 2`를 곱한다.
       - `dp[N - 1][L][R] * (N - 2)`
     - **3가지 경우의 수를 모두 더하면 다음 점화식이 나온다.**
       - `dp[N][L][R]` = `dp[N - 1][L - 1][R] + dp[N - 1][L][R - 1] + dp[N - 1][L][R] * (N - 2)`

2. **dp 테이블을 초기화한다.**
   - 건물이 1개면 경우의 수는 1개다.
     - `dp[1][1][1] = 1`

3. **점화식을 이용해 답을 구한다.**

### 슈도코드
```text
dp[N][L][R] # 빌딩 N개를 왼쪽에서 L개, 오른쪽에서 R개가 보이도록 배치할 수 있는 모든 경우의 수)
dp[1][1][1] = 1 # 건물이 1개일 때 배치될 경우의 수는 1개

for i 2~N:
    for j 1~L:
        for k 1~R:
            dp[i][j][k] = 
                    dp[i -1][j - 1][k] +    # 가장 작은 빌딩을 왼쪽에 놓는 경우
                    dp[i - 1][j][k - 1] +   # 가장 작은 빌딩을 오른쪽에 놓는 경우
                    dp[i - 1][j][k] * (i - 2]   # 가장 작은 빌딩을 가운데에 놓는 경우
                    결과를 1_000_000_007 나머지 연산 수행
dp[N][L][R] 출력
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline
N, L, R = map(int, input().split())
dp = [[[0 for _ in range(101)] for _ in range(101)] for _ in range(101)]

dp[1][1][1] = 1

for i in range(2, N + 1):
    for j in range(1, L + 1):
        for k in range(1, R + 1):
            dp[i][j][k] = (dp[i - 1][j - 1][k] + dp[i - 1][j][k - 1] + dp[i - 1][j][k] * (i - 2)) % 1_000_000_007

print(dp[N][L][R])
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
        int N = Integer.parseInt(st.nextToken());
        int L = Integer.parseInt(st.nextToken());
        int R = Integer.parseInt(st.nextToken());

        long[][][] dp = new long[101][101][101];

        dp[1][1][1] = 1;

        for (int i = 2; i <= N; i++) {
            for (int j = 1; j <= L; j++) {
                for (int k = 1; k <= R; k++) {
                    dp[i][j][k] = (dp[i - 1][j - 1][k] +
                                   dp[i - 1][j][k - 1] +
                                   dp[i - 1][j][k] * (i - 2)) % 1_000_000_007;
                }
            }
        }

        System.out.println(dp[N][L][R]);
    }
}
```