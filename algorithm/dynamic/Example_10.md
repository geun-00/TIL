# 동적 계획법 예제 - 10

### [문제(백준(2342번 - DDR))](https://www.acmicpc.net/problem/2342)

### 문제 분석
- 주어진 내용에 충실하게 점화식을 구할 수 있는지를 알아보는 문제이다.
- 아이디어는 수열의 최대 길이가 100,000이므로 모든 경우의 수를 점화식으로 표현해 구해보는 것이다.
- **점화식 정의**
  - `dp[N][L][R]` = `N`개의 수열을 수행한 후 왼발의 위치가 `L`, 오른발의 위치가 `R`일 때 최소 누적 힘
- 이렇게 정의한다면 직전 수열까지 구한 최솟값을 이용해 해당 값을 구할 수 있다.
- 예를 들어 직전에 오른 다리가 2의 자리에 있었다가 현재 `R` 자리로 이동했다면 `dp[N][L][R]`의 최솟값 후보 중 하나로 `dp[N - 1][L][2] + (2 => R로 이동한 힘)`
    이 될 수 있다.
- 왼발을 움직일 때는 예를 들어 `dp[N - 1][L'][R] + (L' => L로 이동한 힘)` 역시 `dp[N][L][R]`의 최솟값 후보가 될 수 있다.
- 즉, 한 발만 움직여 `dp[N][L][R]`의 위치를 만들 수 있는 모든 경우의 수를 비교해 최솟값을 이 위치에 저장하는 작접을 수행하면 문제를 해결할 수 있다.

### 손으로 풀어보기
1. **점화식 `dp[N][L][R]`을 구한다.**
   - `mp[i][j]`를 `i`에서 `j`로 이동하는 데 드는 힘이라고 하면 직전에 오른발을 움직일 때 점화식은 다음과 같다.
     - `dp[N][L][R]` = `min(dp[N - 1][L][i] + mp[i][R])`
   - 직전에 왼발을 움직일 때는 다음과 같다.
     - `dp[N][L][R]` = `min(dp[N - 1][i][R] + mp[i][L])`
   - **이 점화식을 왼발과 오른발을 구분해 두 발로 만들 수 있는 모든 경우를 고려해 반복해야 한다.**

2. **충분히 큰 수로 dp 테이블을 초기화하고, 점화식을 이용해 값을 채운다.**
3. **수열을 모두 수행한 후 최솟값을 출력한다.**

### 슈도코드
```text
dp[N][L][R](N번 수열까지 수행했을 때, 왼쪽 다리가 L, 오른쪽 다리가 R에 있을 때 힘의 최솟값)
mp(한 발을 이동할 때 드는 힘 저장 리스트)  # 예) mp[1][2] => 1에서 2로 이동할 때 드는 힘

dp를 충분히 큰 수로 초기화
dp[0][0][0] = 0     # 처음 상태

while 모든 수열을 수행할 때까지:
    for i 4번:   # 오른쪽 다리를 이동해 현재 다리 위치로 만들 수 있는 경우의 수
        오른발을 옮겨 현재 모습이 됐을 때 최소 힘 저장
    
    for j 4번:   # 왼쪽 다리를 이동해 현재 다리 위치로 만들 수 있는 경우의 수
        왼발을 옮겨 현재 모습이 됐을 때 최소 힘 저장

for i 4번:
    for j 4번:
        min(min, dp[s][i][j])   # s개의 수열을 수행했을 때 최솟값

min 출력 
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline
dp = [[[sys.maxsize for _ in range(5)] for _ in range(5)] for _ in range(100_001)]

mp = [
    [0, 2, 2, 2, 2],  # 0에 있던 발이 0, 1, 2, 3, 4번으로 이동할 때 드는 힘
    [2, 1, 3, 4, 3],  # 1에 있던 발이 0, 1, 2, 3, 4번으로 이동할 때 드는 힘
    [2, 3, 1, 3, 4],  # 2에 있던 발이 0, 1, 2, 3, 4번으로 이동할 때 드는 힘
    [2, 4, 3, 1, 3],  # 3에 있던 발이 0, 1, 2, 3, 4번으로 이동할 때 드는 힘
    [2, 3, 4, 3, 1]]  # 4에 있던 발이 0, 1, 2, 3, 4번으로 이동할 때 드는 힘

dp[0][0][0] = 0
inputList = list(map(int, input().split()))

s = 1
index = 0

while inputList[index] != 0:
    n = inputList[index]

    for i in range(5):
        if n == i:  # 두 발이 같은 자리에 있을 수 없다.
            continue

        for j in range(5):  # 오른발을 옮겨 현재 모습이 됐을 때 최소 힘 저장
            dp[s][i][n] = min(dp[s][i][n], dp[s - 1][i][j] + mp[j][n])

    for j in range(5):
        if n == j:
            continue
        for i in range(5):
            dp[s][n][j] = min(dp[s][n][j], dp[s - 1][i][j] + mp[i][n])

    s += 1
    index += 1

s -= 1
result = sys.maxsize

for i in range(5):
    for j in range(5):
        result = min(result, dp[s][i][j])

print(result)
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {


    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long[][][] dp = new long[100_001][5][5];
        
        for (long[][] longs : dp) {
            for (long[] aLong : longs) {
                Arrays.fill(aLong, Integer.MAX_VALUE);
            }
        }
        dp[0][0][0] = 0;

        int[][] mp = {
                {0, 2, 2, 2, 2},
                {2, 1, 3, 4, 3},
                {2, 3, 1, 3, 4},
                {2, 4, 3, 1, 3,},
                {2, 3, 4, 3, 1}
        };

        int[] input = Arrays.stream(br.readLine().split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray();
        int s = 1;
        int index = 0;

        while (input[index] != 0) {
            int n = input[index];

            for (int i = 0; i < 5; i++) {
                if (n == i) {
                    continue;
                }
                for (int j = 0; j < 5; j++) {
                    dp[s][i][n] = Math.min(dp[s][i][n], dp[s - 1][i][j] + mp[j][n]);
                }
            }

            for (int i = 0; i < 5; i++) {
                if (n == i) {
                    continue;
                }
                for (int j = 0; j < 5; j++) {
                    dp[s][n][i] = Math.min(dp[s][n][i], dp[s - 1][i][j] + mp[j][n]);
                }
            }

            s++;
            index++;
        }

        s--;
        long result = Integer.MAX_VALUE;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                result = Math.min(result, dp[s][i][j]);
            }
        }
        System.out.println(result);
    }
}
```