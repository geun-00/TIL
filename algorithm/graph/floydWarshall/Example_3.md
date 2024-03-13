# 플로이드-워셜 예제 - 3

### [문제(백준(1389번 - 케빈 베이컨의 6단계 법칙))](https://www.acmicpc.net/problem/1389)

### 문제 분석
- BFS 탐색 알고리즘을 이용해도 해결할 수 있는 문제이다.
- 유저의 최대 수가 100으로 매우 작기 때문에 플로이드-워셜 알고리즘으로도 해결할 수 있다.
- 이를 위해서는 몇 가지 아이디어가 필요하다.
  - 사람들이 직접적인 친구 관계를 맺은 상태를 비용 1로 계산한다. 가중치를 1로 정한 후 인접 행렬에 저장한다.
  - 플로이드-워셜은 모든 쌍과 관련된 최단 경로이므로 한 row값은 애 row의 index값에서 다른 모든 노드와 관련된 최단 경로를 나타낸다고 볼 수 있다.
    즉, i번째 row의 합이 i번째 사람의 케빈 베이컨의 수가 된다는 뜻이다.

### 손으로 풀어보기
1. **인접 행렬을 생성한 후, 자기 자신이면(`i==j`)0, 아니면 큰 수로 인접 행렬의 값을 초기화한다. 그리고 주어진 친구 관계 정보를 인접 행렬에 저장한다.
    i와 j가 친구라면 `distance[i][j]`와 `distance[j][i]`를 1로 저장한다.**
2. **플로이드-워셜 알고리즘을 수행해 3중 for 문으로 모든 중간 경로를 탐색한다.**
- `Math.min(D[S][E], D[S][K] + D[K][E])`

3. **케빈 베이컨의 수(각 행의 합)를 비교해 가장 작은 수가 나온 행 번호를 정답으로 출력한다. 같은 수가 있을 때는 더 작은 행 번호를 출력한다.**

### 슈도코드
```text
n(노드 수) m(에지 수)
distance(인접 행렬) # 큰 수로 초기화

for n 반복:
    시작과 출발이 같으면 0
    
for m 반복:
    인접 행렬 데이터 저장
    양방향으로 가중치 1로 저장

for k n반복:
    for s n반복:
        for e n반복:
            distance[s][e]보다 distance[s][k] + distance[k][e]가 작으면 업데이트

min
ans

for i n반복:
    for j n반복:
        리스트의 행 합
    if min > 리스트의 행 합:
        min = 리스트의 행 합
        ans = i

ans 출력
```

### 코드 구현 - 파이썬
```python
import sys

n, m = map(int, input().split())
distance = [[sys.maxsize for _ in range(n + 1)] for _ in range(n + 1)]

for i in range(1, n + 1):
    distance[i][i] = 0

for i in range(m):
    s, e = map(int, input().split())
    distance[s][e] = 1
    distance[e][s] = 1

for k in range(1, n + 1):
    for s in range(1, n + 1):
        for e in range(1, n + 1):
            if distance[s][e] > distance[s][k] + distance[k][e]:
                distance[s][e] = distance[s][k] + distance[k][e]

min = sys.maxsize
ans = 0

for i in range(1, n + 1):
    temp = 0
    for j in range(1, n + 1):
        temp += distance[i][j]

    if min > temp:
        min = temp
        ans = i

print(ans)
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

        long[][] distance = new long[n + 1][n + 1]; //주의! long 사용
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                if (i != j) {
                    distance[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            distance[s][e] = 1;
            distance[e][s] = 1;
        }

        for (int k = 1; k < n + 1; k++) {
            for (int s = 1; s < n + 1; s++) {
                for (int e = 1; e < n + 1; e++) {
                    if (distance[s][e] > distance[s][k] + distance[k][e]) {
                        distance[s][e] = distance[s][k] + distance[k][e];
                    }
                }
            }
        }

        int min = Integer.MAX_VALUE;
        int ans = 0;

        for (int i = 1; i < n + 1; i++) {
            int temp = 0;
            for (int j = 1; j < n + 1; j++) {
                temp += distance[i][j];
            }
            if (temp < min) {
                min = temp;
                ans = i;
            }
        }
        System.out.println(ans);

    }
}
```