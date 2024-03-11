# 그래프의 표현 예제 - 1

### [문제(백준(18352번 - 특정 거리의 도시 찾기))](https://www.acmicpc.net/problem/18352)

### 문제 분석
- 모든 도로의 거리가 1이므로 가중치가 없는 인접 리스트로 이 그래프를 표현할 수 있다.
- 도시와 개수가 300,000, 도로의 최대 크기가 1,000,000이므로 BFS 탐색을 수행하면 시간 복잡도 안에서 해결할 수 있다.

### 손으로 풀어보기
1. **인접 리스트로 도시와 도로 데이터의 그래프를 구현한다.**
2. **BFS 탐색 알고리즘으로 탐색을 수행하면서 각 도시로 가는 최단 거릿값을 방문 리스트에 저장한다. 최초에 방문하는 도시(`X`)는 이동하지 않으므로 방문 리스트에 0을 저장하고, 
   이후 방문하는 도시는 이전 도시의 방문 리스트값 +1을 방문 리스트에 저장하는 방식으로 이동 거리를 저장한다.**
3. **탐색 종료 후 방문 리스트에서 값이 `K`와 같은 도시의 번호를 모두 출력한다.**

### 슈도코드
```text
n(노드 개수) m(에지 개수) k(목표 거리) x(시작 노드)
A(인접 리스트)
ans(정답 리스트)
visit(방문 기록 리스트)  # -1로 초기화

BFS:
    큐에 시작 노드 삽입
    visit 현재 노드 방문 기록  # 거리 저장 형태로 1 증가
    while 큐가 비어 있을 때까지:
        큐에서 노드 데이터 가져오기
        if 현재 노드의 연결 노드 중 미 방문 노드:
            visit 리스트값 1 증가
            큐에 노드 삽입

for m 반복:
    A 인접 리스트에 데이터 저장
    
BFS(x) 실행

for n 반복:
    방문 거리가 k인 노드의 숫자를 정답 리스트에 더하기

정답 리스트 오름차순 정렬 후 순차 출력
```

### 코드 구현 - 파이썬
```python
import sys
from collections import deque

input = sys.stdin.readline

n, m, k, x = map(int, input().split())
A = [[] for _ in range(n + 1)]
ans = []
visit = [-1] * (n + 1)


def BFS(node):
    queue = deque()
    queue.append(node)

    visit[node] += 1

    while queue:
        now_node = queue.popleft()
        for i in A[now_node]:
            if visit[i] == -1:
                visit[i] = visit[now_node] + 1
                queue.append(i)


for _ in range(m):
    start, end = map(int, input().split())
    A[start].append(end)

BFS(x)

for i in range(1, n + 1):
    if visit[i] == k:
        ans.append(i)

result = []

if not ans:
    print(-1)
else:
    ans.sort()
    for i in ans:
        result.append(str(i))

print("\n".join(result))
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static ArrayList<Integer>[] A;
    static int[] visit;
    static List<Integer> ans = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int x = Integer.parseInt(st.nextToken());

        A = new ArrayList[n + 1];
        visit = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            A[i] = new ArrayList<>();
        }

        Arrays.fill(visit, -1);

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            A[start].add(end);
        }

        BFS(x);

        for (int i = 1; i <= n; i++) {
            if (visit[i] == k) {
                ans.add(i);
            }
        }

        if (ans.isEmpty()) {
            System.out.println(-1);
        } else {
            StringBuilder sb = new StringBuilder();
            Collections.sort(ans);

            for (int num : ans) {
                sb.append(num).append("\n");
            }

            System.out.println(sb);
        }
    }

    private static void BFS(int node) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(node);
        visit[node] += 1;

        while (!queue.isEmpty()) {
            int now_node = queue.poll();

            for (int next : A[now_node]) {
                if (visit[next] == -1) {
                    visit[next] = visit[now_node] + 1;
                    queue.add(next);
                }
            }
        }
    }
}
```