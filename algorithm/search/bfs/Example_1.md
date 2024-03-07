# 너비 우선 탐색 예제 - 1

### [문제(백준(1260번 - DFS와 BFS))](https://www.acmicpc.net/problem/1260)

### 문제 분석
- `DFS`와 `BFS`를 구현할 수 있는지 물어보는 기본 문제다.
- 노드 번호가 작은 것을 먼저 방문해야 하는 것에 주의해야 한다.

### 손으로 풀어보기
1. **인접 리스트에 그래프 저장**
2. **DFS를 실행하면서 방문 리스트 체크와 탐색 노드 기록을 수행한다. 문제 조건에서 작은 번호의 노드부터 탐색한다고 했으므로 인접 노드를 오름차순으로 정렬한 후 재귀 함수를 호출한다.**
3. **BFS도 같은 방식으로 노드를 오름차순으로 정렬하여 큐에 삽입하고 진행한다.**
4. **DFS와 BFS를 탐색하며 기록한 데이터를 출력한다.**

### 슈도코드
```text
n(노드 개수) m(에지 개수) v(시작점)
A(그래프 데이터 저장 인접 리스트)

for m 반복:
    A 인접 리스트에 그래프 데이터 저장
    
for n+1 반복:
    각 노드와 관련된 에지 정렬
    
dfs:
    현재 노드 출력
    visit 방문 처리
    현재 노드의 연결 노드 중 방문하지 않은 노드로 dfs 실행

visit 리스트 초기화
dfs(v) 실행

bfs:
    큐 자료구조에 시작 노드 삽입
    visit 방문 처리
    while 큐가 비어 있을 때까지:
        큐에서 노드 데이터 가져오기
        가져온 노드 출력
        현재 노드의 연결 노드 중 방문하지 않은 노드로 큐에 삽입하고 방문 처리

visit 리스트 초기화
bfs(v) 실행
```

### 코드 구현 - 파이썬
```python
from collections import deque

n, m, start = map(int, input().split())
A = [[] for _ in range(n + 1)]

for _ in range(m):
    u, v = map(int, input().split())
    A[u].append(v)
    A[v].append(u)

for i in range(n + 1):
    A[i].sort()


def dfs(node):
    print(node, end=' ')
    visit[node] = True
    for i in A[node]:
        if not visit[i]:
            dfs(i)


visit = [False] * (n + 1)
dfs(start)


def bfs(node):
    queue = deque()
    queue.append(node)
    visit[node] = True

    while queue:
        now_node = queue.popleft()
        print(now_node, end=' ')

        for i in A[now_node]:
            if not visit[i]:
                visit[i] = True
                queue.append(i)


print()
visit = [False] * (n + 1)
bfs(start)
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static ArrayList<Integer>[] A;
    static boolean[] visit;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int start = Integer.parseInt(st.nextToken());

        A = new ArrayList[n + 1];

        for (int i = 1; i <= n; i++) {
            A[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            A[a].add(b);
            A[b].add(a);
        }

        for (int i = 1; i <= n; i++) {
            Collections.sort(A[i]);
        }

        visit = new boolean[n + 1];
        dfs(start);
        sb.append("\n");

        visit = new boolean[n + 1];
        bfs(start);

        System.out.println(sb);
    }

    private static void bfs(int node) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(node);
        visit[node] = true;

        while (!queue.isEmpty()) {
            int now_node = queue.poll();
            sb.append(now_node).append(" ");

            for (int next : A[now_node]) {
                if (!visit[next]) {
                    visit[next] = true;
                    queue.add(next);
                }
            }
        }
    }

    private static void dfs(int node) {
        visit[node] = true;
        sb.append(node).append(" ");

        for (int next : A[node]) {
            if (!visit[next]) {
                dfs(next);
            }
        }
    }
}
```