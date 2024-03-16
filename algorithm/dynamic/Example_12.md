# 동적 계획법 예제 - 12

### [문제(백준(2098번 - 외판원 순회))](https://www.acmicpc.net/problem/2098)

### 문제 분석
- `N`의 범위가 작기 때문에 모든 순서를 완전 탐색하면 정답을 구할 수 있다.
- **점화식 정의**
  - `dp[c][v]` = 현재 도시가 `c`, 현재까지 방문한 모든 도시 리스트가 `v`일 때, 앞으로 남은 모든 도시를 경유하는 데 필요한 최소 비용
- 예를 들어 `dp[2][1, 2]`는 현재 도시가 2이고, 1, 2 도시를 방문한 상태에서 나머지 모든 도시를 경유하는 데 필요한 비용이다.
- 완전 탐색의 경우에는 DFS나 BFS를 이용하면 된다.
- 문제는 `dp[c][v]`에서 `v`가 현재까지 방문한 모든 도시 리스트 라는 것이다. 리스트 데이터를 `j` 변수 1개에 어떻게 저장해야 할까?
- `bit`, 즉 이진수로 표현할 수 있다.
- 예를 들어 총 도시가 4개일 때 방문 도시를 이진수의 각 자릿수로 표현하고 방문 시 1, 미방문 시 0의 값으로 저장한다.
  - 4, 1번 방문 => 이진수 표현(`1001`) => `dp[i][9]`
  - 3, 2번 방문 => 이진수 표현(`110`) => `dp[i][6]`
  - 4, 3, 2, 1번 방문 => 이진수 표현(`1111`) => `dp[i][15]`
- 이런 방식으로 방문 리스트를 1개의 변수로 표현할 수 있다.

### 손으로 풀어보기
1. **먼저 점화식을 구해본다. `c`번 도시에서 `v` 리스트 도시를 방문한 후 남은 모든 도시를 순회하기 위한 최소 비용은 현재 방문하지 않은 모든 도시에 대해 반복하고, 방문하지 않은 도시를 
    `i`라고 했을 때 `W[c][i]`는 도시 `c`에서 도시 `i`로 가기 위한 비용을 나타낸다.**
   - `dp[c][v]` = `min(dp[c][v] , dp[i][v | (1 << i)] + W[c][i])`

2. **W 리스트를 저장한다.**
3. **점화식으로 정답을 구하고, 최솟값을 정답으로 출력한다.**

### 슈도코드
```text
N(도시의 수)
dp[i][j] (i 도시에서 j 도시로 가는 데 드는 비용 저장 리스트)

for i N:
    W 리스트 데이터 저장

dp (현재 도시 c, 방문한 도시 리스트 v일 때 남은 모든 도시를 경유하는 데 필요한 최소 비용 테이블)

solution(c, v):
    if 모든 도시를 방문:
        시작 도시로 돌아갈 수 있을 때 -> W[c][시작 도시] 리턴
        시작 도시로 돌아갈 수 없을 때 -> 무한대 리턴
    if 이미 계산한 적이 있으면:
        dp[c][v]    # 메모이제이션
    
    for i N:
        if 미방문 도시 and 갈 수 있는 도시:
            minValue = min(minValue, solution(i, (v | ( 1 << i))) + W[c][i])
    
    dp[c][v] = minValue
    return dp[c][v]

solution(0, 1)
정답 출력
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline
N = int(input())
W = [[0 for _ in range(N)] for _ in range(N)]

for i in range(N):
    W[i] = list(map(int, input().split()))

dp = [[0 for _ in range(1 << 16)] for _ in range(16)]  # 1 << 16 = 2^16

def solution(c, v):
    if v == (1 << N) - 1:
        if W[c][0] == 0:
            return sys.maxsize
        else:
            return W[c][0]

    if dp[c][v] != 0:
        return dp[c][v]

    minValue = sys.maxsize

    for i in range(N):
        if (v & (1 << i)) == 0 and W[c][i] != 0:
            minValue = min(minValue, solution(i, (v | (1 << i))) + W[c][i])

    dp[c][v] = minValue
    return dp[c][v]


print(solution(0, 1))
```
- 모든 도시 순회 판단 연산식
  - ` if v == (1 << N) - 1`
  - 예) `N = 4`(도시의 개수가 4) : `(1 << 4) - 1` = `16 - 1` = 15
  - 15를 이진수로 표현하면 `1111`이 되어 각 자리가 모두 1이기 때문에 모든 도시를 방문한 상태라고 볼 수 있다.
- 방문 도시 확인 연산식
  - `if (v & (1 << i)) == 0`
  - 예) `i = 3`(4번째 도시 확인 여부 확인) : `1 << 3 = 8 = 1000(이진수)`
  - `v & 1000` 연산(`AND`비트 연산)을 수행했을 때 결과가 0이면 4번째 도시를 방문하지 않았다고 판단할 수 있다.
- 방문 도시 저장 연산식
  - `v | (1 << i)`
  - 예) `i = 2`(3번째 도시 저장) : `1 << 2 = 4 = 100(이진수)`
  - `v | 100` 연산(`OR`비트 연산)을 수행하면 `v`의 이진수 표현시 3번째 자리를 1로 저장하게 되어 3번째 도시를 방문했다는 사실을 저장하게 된다.


### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static long[][] dp;
    static int[][] W;
    static int N;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        W = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                W[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dp = new long[16][1 << 16];

        System.out.println(solution(0, 1));
    }

    private static long solution(int c, int v) {
        if (v == (1 << N) - 1) {
            if (W[c][0] == 0) {
                return Integer.MAX_VALUE;
            } else {
                return W[c][0];
            }
        }
        if (dp[c][v] != 0) {
            return dp[c][v];
        }

        int minValue = Integer.MAX_VALUE;
        for (int i = 0; i < N; i++) {
            if ((v & (1 << i)) == 0 && W[c][i] != 0) {
                minValue = (int) Math.min(minValue, solution(i, (v | (1 << i))) + W[c][i]);
            }
        }
        return dp[c][v] = minValue;
    }
}
```