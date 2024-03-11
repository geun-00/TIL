# 그래프의 표현 예제 - 2

### [문제(백준(1325번 - 효율적인 해킹))](https://www.acmicpc.net/problem/1325)

### 문제 분석
- `N`과 `M`의 크기가 작은 편이므로 시간 복잡도와 관련된 제약은 크지 않다.
- 문제에서 잘 확인해야 할 부분은 신뢰 관계가 A, B라고 했을 때 A가 B를 신뢰한다는 것이다.
- 또한 가장 많은 컴퓨터를 해킹할 수 있는 컴퓨터는 신뢰를 가장 많이 받는 컴퓨터이다.
- 그래프의 노드와 에지를 기준으로 이해하면 A라는 노드에서 탐색 알고리즘으로 방문하는 노드가 B, C라고 하면 B, C는 A에게 신뢰받는 노드가 된다.

### 손으로 풀어보기
1. **인접 리스트로 컴퓨터와 신뢰 관계 데이터의 그래프를 표현한다.(유방향)**
2. **모든 노드로 각각 BFS 탐색 알고리즘을 적용해 탐색을 수행하면서 탐색되는 노드들의 신뢰도를 증가시켜준다.**
3. **탐색 종료 후 신뢰도 리스트를 탐색해 신뢰도의 최댓값을 Max값으로 지정하고, 신뢰도 리스트를 다시 탐색하면서 Max값을 지니고 있는 노드를 오름차순 출력한다.**

### 슈도코드
```text
n(노드 개수) m(에지 개수)
A(인접 리스트)
ans(정답 리스트)

BFS:
    큐에 시작 노드 삽입
    visit 방문 기록
    while 큐가 비어 있을 때까지:
        큐에서 노드 데이터 가져오기
        if 현내 노드의 연결 노드 중 미 방문 노드:
            visit 방문 기록
            신규 노드 index의 정답 리스트값 증가
            큐에 노드 삽입

for m 반복:
    A 인접 리스트 데이터 저장

for i 1~n 반복:
    visit 방문 기록 리스트 초기화
    BFS(i) 실행

for i 1~n 반복:
    ans 리스트에서 가장 큰 값 찾기 (maxVal)

for i 1~n 반복:
    ans 리스트에서 maxVal과 같은 값을 가진 index 출력
```

### 코드 구현 - 파이썬
```python
import sys
from collections import deque

input = sys.stdin.readline

n, m = map(int, input().split())
A = [[] for _ in range(n + 1)]
ans = [0] * (n + 1)


def BFS(node):
    queue = deque()
    queue.append(node)

    visit = [False] * (n + 1)
    visit[node] = True

    while queue:
        now_node = queue.popleft()
        for i in A[now_node]:
            if not visit[i]:
                visit[i] = True
                ans[i] += 1
                queue.append(i)


for i in range(m):
    a, b = map(int, input().split())
    A[a].append(b)

for i in range(1, n + 1):
    BFS(i)

maxVal = max(ans)

result = []
for i in range(1, n + 1):
    if ans[i] == maxVal:
        result.append(str(i))

print(" ".join(result))
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static ArrayList<Integer>[] A;
    static int[] ans;
    static int n;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        A = new ArrayList[n + 1];
        ans = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            A[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            A[a].add(b);
        }

        for (int i = 1; i <= n; i++) {
            BFS(i);
        }

        int maxVal = Arrays.stream(ans).max().orElse(0);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < ans.length; i++) {
            if (ans[i] == maxVal) {
                sb.append(i).append(" ");
            }
        }
        System.out.println(sb);
    }

    private static void BFS(int node) {
        Queue<Integer> qu = new LinkedList<>();
        qu.add(node);

        boolean[] visit = new boolean[n + 1];
        visit[node] = true;

        while (!qu.isEmpty()) {
            int now_node = qu.poll();
            for (int next : A[now_node]) {
                if (!visit[next]) {
                    visit[next] = true;
                    ans[next]++;
                    qu.add(next);
                }
            }
        }
    }
}
```