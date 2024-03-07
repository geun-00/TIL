# 너비 우선 탐색  예제 - 2

### [문제(백준(2178번 - 미로 탐색))](https://www.acmicpc.net/problem/2178)

### 문제 분석
- `N`, `M`의 최대 크기가 100으로 매우 작기 때문에 시간 제한은 별도로 생각하지 않아도 된다.
- 문제의 요구사항은 지나야 하는 칸 수의 최솟값을 찾는 것이다. 이는 완전 탐색을 진행하며 몇 번째 깊이에서 원하는 값을 찾을 수 있는지 구하는 것과 동일하다.
- BFS를 사용해 최초로 도달했을 때 깊이를 출력하면 문제를 해결할 수 있다.
- DFS보다 BFS가 적합한 이유는 BFS는 해당 깊이에서 갈 수 있는 노드 탐색을 마친 후 다음 깊이로 넘어가기 때문이다.

### 손으로 풀어보기
- 먼저 2차원 리스트에 데이터를 저장한 다음 `(1, 1)`에서 BFS를 실행한다.
- 상, 하, 좌, 우 네 방향을 보며 인접한 칸을 본다.
- 인접한 칸의 숫자가 1이면서 아직 방문하지 않았다면 큐에 삽입한다.
- 종료 지점 `(N, M)`에서 BFS를 종료하며 깊이를 출력한다.

### 슈도코드
```text
dx, dy(상하좌우를 탐색하기 위한 define값 정의 변수)
n(row), m(column)
A(데이터 저장 2차원 행렬)
visit(방문 기록 리스트)

for n 반복:
    for m 반복:
        A 리스트에 데이터 저장
        
BFS:
    큐에 시작 노드 삽입
    visit 방문 처리
    while 큐가 비어 있을 때까지:
        큐에서 노드 데이터 가져오기
        for 상하좌우 탐색:
            if 유효한 좌표:
                if 이동할 수 있는 칸이면서 방문하지 않은 노드:
                    visit 방문 처리
                    A 리스트에 현재 depth를 현재 노드의 depth + 1로 업데이트
                    큐에 데이터 삽입
                    
BFS(0, 0)실행
A[n-1][m-1] 출력
```

### 코드 구현 - 파이썬
```python
from collections import deque

dx = [-1, 1, 0, 0]
dy = [0, 0, -1, 1]

n, m = map(int, input().split())
A = [[0] * m for _ in range(n)]
visit = [[False] * m for _ in range(n)]

for i in range(n):
    numbers = list(input())
    for j in range(m):
        A[i][j] = int(numbers[j])


def BFS(x, y):
    queue = deque()
    queue.append((x, y))
    visit[x][y] = True

    while queue:
        now = queue.popleft()
        for i in range(4):
            nx = now[0] + dx[i]
            ny = now[1] + dy[i]

            if nx >= 0 and ny >= 0 and nx < n and ny < m:
                if A[nx][ny] == 1 and not visit[nx][ny]:
                    visit[nx][ny] = True
                    A[nx][ny] = A[now[0]][now[1]] + 1
                    queue.append((nx, ny))


BFS(0, 0)
print(A[n-1][m-1])
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static int[][] A;
    static boolean[][] visit;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static int n, m;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        A = new int[n][m];
        visit = new boolean[n][m];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            String str = st.nextToken();
            for (int j = 0; j < m; j++) {
                A[i][j] = Integer.parseInt(str.substring(j, j + 1));
            }
        }

        BFS(0, 0);
        System.out.println(A[n-1][m-1]);

    }

    private static void BFS(int x, int y) {
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{x, y});
        visit[x][y] = true;

        while (!queue.isEmpty()) {
            int[] now = queue.poll();

            for (int i = 0; i < 4; i++) {
                int nx = now[0] + dx[i];
                int ny = now[1] + dy[i];

                if (nx >= 0 && ny >= 0 && nx < n && ny < m) {
                    if (A[nx][ny] == 1 && !visit[nx][ny]) {
                        visit[nx][ny] = true;
                        A[nx][ny] = A[now[0]][now[1]] + 1;
                        queue.add(new int[]{nx, ny});
                    }
                }
            }
        }
    }
}
```