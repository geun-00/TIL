# 플로이드-워셜 예제 - 2

### [문제(백준(11403번 - 경로 찾기))](https://www.acmicpc.net/problem/11403)

### 문제 분석
- 플로이드-워셜 알고리즘을 이해하고 있고, 문제의 요구사항에 따라 적절하게 수정할 수 있는지를 묻는 문제다.
- 모든 노드 쌍에 관해 경로가 있는지 여부를 확인하는 방법은 플로이드-워셜 알고리즘을 수행해 결과 리스트를 그대로 출력하면 된다.
- 단, 최단 거리를 구하는 문제가 아니기 때문에 기존 플로이드-워셜 알고리즘에서 최단 거리를 업데이트하는 부분만 수정해야 한다.

### 손으로 풀어보기
1. **입력 데이터를 인접 행렬에 저장한다.**
2. **변경된 플로이드-워셜 알고리즘을 수행한다. S와 E가 모든 중간 경로(K) 중 1개라도 연결돼 있다면 S와 E는 연결 노드로 저장한다.**
3. **알고리즘으로 변경된 인접 행렬을 출력한다.**

### 슈도코드
```text
n(노드 개수)
distance(인접 행렬)

for n 반복:
    인접 행렬 데이터 저장

for k n반복:
    for s n반복:
        for e n반복:
            if distance[s][k]가 1이고, distance[k][e]가 1이면:
                distance[s][e]는 1로 저장
                # k를 거치는 모든 경로 중 1개라도 연결된 경로가 있다면
                # s와 e는 연결 노드로 취급

인접 행렬 출력
```

### 코드 구현 - 파이썬
```python
n = int(input())
distance = [[0 for _ in range(n)] for _ in range(n)]

for i in range(n):
    distance[i] = list(map(int, input().split()))

for k in range(n):
    for s in range(n):
        for e in range(n):
            if distance[s][k] == 1 and distance[k][e] == 1:
                distance[s][e] = 1

for i in range(n):
    for j in range(n):
        print(distance[i][j], end=' ')
    print()
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
        int n = Integer.parseInt(br.readLine());

        int[][] distance = new int[n][n];
        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                distance[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int k = 0; k < n; k++) {
            for (int s = 0; s < n; s++) {
                for (int e = 0; e < n; e++) {
                    if (distance[s][k] == 1 && distance[k][e] == 1) {
                        distance[s][e] = 1;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(distance[i][j] + " ");
            }
            System.out.println();
        }
    }
}
```