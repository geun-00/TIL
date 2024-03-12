# 다익스트라 예제 - 3

### [문제(백준(1854번 - K번째 최단경로 찾기))](https://www.acmicpc.net/problem/1854)

### 문제 분석
- 시작점과 도착점이 주어지고 이 목적지까지 가는 K번째 최단 경로를 구하는 문제다.
- 노드 개수 1,000개, 에지 개수 2,000,000개 이면서 시간 제한이 2초이므로 다익스트라 알고리즘으로 접근해볼 수 있다.
- 이 문제에서 가장 고민되는 부분은 최단 경로가 아니라 K번째 최단 경로라는 것이다.
- **K번째 최단 경로 해결 아이디어**
  - 최단 경로를 표현하는 리스트를 K개의 row를 갖는 2차원 리스트의 형태로 사용하면 최단 경로뿐 아니라 최단 경로 ~ K번째 최단 경로까지 표현할 수 있을 것 같다.
  - 기존 다익스트라 로직에서 사용한 노드를 방문 체크하고 다음 도착 시 해당 노드를 다시 사용하지 않도록 설정하는 부분은 필요없을 것 같다. 왜냐하면 K번째 경로를 찾기
    위해서는 노드를 여러 번 쓰는 경우가 생기기 때문이다.

### 손으로 풀어보기
1. **입력을 기반으로 그래프를 구현한다. 도시는 노드로, 도로는 에지로 나타낸다.**
2. **변수를 선언하고, 그래프 데이터를 받는 부분은 일반적인 다익스트라 알고리즘 준비 과정과 같다.**
3. **유일하게 다른 점은 최단 거리 리스트를 1차원이 아닌 K개의 row를 갖는 2차원 리스트로 선언한다는 것이다. 최단 거리 리스트는 최초 시작 노드의 첫 번째 경로는 0,
    그 외의 경로는 모두 큰 값으로 초기화한다. 그 후 다음 규칙을 토대로 거리 리스트를 채운다.**

- **최단 거리 리스트 채우기 규칙**
  1. 우선순위 큐에서 연결된 노드와 가중치 데이터를 가져온다.
  2. 연결 노드의 K번째 경로와 신규 경로를 비교해 신규 경로가 더 작을 때 업데이트한다. 이때 경로가 업데이트 되는 경우 거리 배열을 오름차순으로 정렬하고 
    우선순위 큐에 연결 노드를 추가한다.
  3. 과정 1~2를 우선순위 큐가 비어질 때까지 반복한다. K번째 경로를 찾기 위해 노드를 여러 번 방문하는 경우가 있으므로 기존 다익스트라의 방문 노드를 체크하여 재사용하지 않는
    로직은 구현하지 않는다.
  4. 최단 거리 리스트를 탐색하면서 K번째 경로가 바뀌지 않았으면(큰 값이 그대로면) -1을 출력하고, 그 외에는 해당 경로값을 출력한다.

### 슈도코드
```text
n(노드 개수) m(에지 개수) k(k번째 최단 경로)
A(인접 리스트)
distance(거리 저장 리스트)

for m 반복:
    인접 리스트 데이터 저장
 
우선순위 큐 시작 노드 저장
시작 도시 0 저장

while 큐가 빌 때까지:
    우선순위 큐에서 데이터 가져오기(거리, 노드)
    for 현재 노드에서 연결된 에지 탐색:
        새로운 총 거리 = 현재 노드의 거리 + 에지 가중치
        if 새로운 노드의 K번째 최단 거리 > 새로운 총 거리:
            새로운 노드의 K번째 최단 거리를 총 거리로 변경하고 거리 순으로 정렬
            우선순위 큐에 새로운 데이터 추가(거리, 노드)

for n 반복:
    if 각 노드의 거리 리스트에 K번째 값이 최초 설정값:
        -1 출력
    else:
        K번째 값 출력
```

### 코드 구현 - 파이썬(`heapq` 사용)
```python
import heapq
import sys

input = sys.stdin.readline

n, m, k = map(int, input().split())
A = [[] for _ in range(n + 1)]
distance = [[sys.maxsize] * k for _ in range(n + 1)]

for i in range(m):
    a, b, c = map(int, input().split())
    A[a].append((b, c))

pq = [(0, 1)]  # (가중치, 노드)   가중치 우선
distance[1][0] = 0

while pq:
    cost, node = heapq.heappop(pq)
    for next_node, next_cost in A[node]:
        new_cost = cost + next_cost

        if distance[next_node][k - 1] > new_cost:
            distance[next_node][k - 1] = new_cost

            distance[next_node].sort()  # 정렬로 인해 거리 순으로 변경됨
            heapq.heappush(pq, [new_cost, next_node])


result = []
for i in range(1, n + 1):
    if distance[i][k-1] == sys.maxsize:
        result.append(str(-1))
    else:
        result.append(str(distance[i][k-1]))

print("\n".join(result))
```
- `heapq.heappush(heap, item)` : `item`을 `heap`에 추가
- `heapq.heappop(heap)` : `heap`에서 가장 작은 원소를 pop 하고 리턴
- `heapq` 모듈은 리스트를 최소 힙처럼 다룰 수 있도록 하기 때문에 리스트를 생성한 후 `heapq`의 함수를 호출할 때마다 리스트를 인자에 넘겨야 한다.

### 코드 구현 - 파이썬(`PriorityQueue` 사용)
```python
import sys
from queue import PriorityQueue

input = sys.stdin.readline

n, m, k = map(int, input().split())
A = [[] for _ in range(n + 1)]
distance = [[sys.maxsize] * k for _ in range(n + 1)]

for i in range(m):
    a, b, c = map(int, input().split())
    A[a].append((b, c))

pq = PriorityQueue()
pq.put((0, 1))
distance[1][0] = 0

while pq.qsize() > 0:
    cost, node = pq.get()

    for next_node, next_cost in A[node]:
        new_cost = cost + next_cost

        if new_cost < distance[next_node][k - 1]:
            distance[next_node][k - 1] = new_cost
            distance[next_node].sort()
            pq.put((new_cost, next_node))


result = [str(distance[i][k - 1]) 
          if distance[i][k - 1] != sys.maxsize 
          else str(-1) 
          for i in range(1, n + 1)]

print("\n".join(result))
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    // 인접 리스트, 우선순위 큐에 저장할 노드
    static class Node implements Comparable<Node>{
        int node, cost;

        public Node(int node, int cost) {
            this.node = node;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node o) {
            return this.cost - o.cost;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int[][] distance = new int[n + 1][k];
        for (int i = 0; i < n + 1; i++) {
            Arrays.fill(distance[i], Integer.MAX_VALUE);
        }

        ArrayList<Node>[] A = new ArrayList[n + 1];
        for (int i = 1; i < n + 1; i++) {
            A[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());

            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());

            A[a].add(new Node(b, c));
        }

        Queue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(1, 0));
        distance[1][0] = 0;

        while (!pq.isEmpty()) {
            Node now = pq.poll();
            int node = now.node;
            int cost = now.cost;

            for (Node next : A[node]) {
                int nextNode = next.node;
                int nextCost = next.cost;

                int newCost = cost + nextCost;

                if (distance[nextNode][k - 1] > newCost) {
                    distance[nextNode][k - 1] = newCost;

                    Arrays.sort(distance[nextNode]); // 중요!
                    pq.add(new Node(nextNode, newCost));
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < n + 1; i++) {
            if (distance[i][k - 1] == Integer.MAX_VALUE) {
                sb.append(-1).append("\n");
            } else {
                sb.append(distance[i][k - 1]).append("\n");
            }
        }
        System.out.println(sb);
    }
}
```