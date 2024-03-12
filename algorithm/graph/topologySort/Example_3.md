# 위상 정렬 예제 - 3

### [문제(백준(1948번 - 임계경로))](https://www.acmicpc.net/problem/1948)

### 문제 분석
- 출발 도시와 도착 도시가 주어지기 때문에 일반적인 위상 정렬이 아닌 시작점을 출발 도시로 지정하고 위상 정렬을 수행하면 출발 도시에서 도착 도시까지
    거치는 모든 도시와 관련된 임계 경로값을 구할 수 있다.
- **단, 이 문제의 핵심은 1분도 쉬지 않고 달려야 하는 도로의 수를 구하는 것인데, 이를 해결하려면 에지 뒤집기라는 아이디어가 필요하다.**
- 에지 뒤집기 아이디어는 그래프 문제에서 종종 나오는 개념이다.

### 손으로 풀어보기
1. **인접 리스트에 노드 데이터를 저장하고, 진입 차수 리스트 값을 업데이트한다. 이때 에지의 방향이 반대인 역방향 인접 리스트도 함께 생성하고 저장한다.**
2. **시작 도시에서 위장 정렬을 수행해 각 도시와 관련된 임계 정보를 저장한다.**
3. **도착 도로에서 역방향으로 위상 정렬을 수행한다. 이때 `이 도시의 임계 경로값 + 도로 시간(에지) == 이전 도시의 임계 경로값`일 경우에는 이 도로를 1분도
    쉬지 않고 달려야 하는 도로로 카운팅하고, 이 도시를 큐에 삽입하는 로직으로 구현해야 한다.**
4. **도착 도시의 임계 경로값과 1분도 쉬지 않고 달려야 하는 도로의 수를 출력한다.**

> **노드를 큐에 삽입할 때 주의할 점**
> 
> 1분도 쉬지 않고 달려야 하는 도로로 이어진 노드와 연결된 다른 도로만이 1분도 쉬지 않고 달려야 하는 도로의 후보가 될 수 있으므로 이 메커니즘을 바탕으로
> 노드를 큐에 삽입해야 한다.<br>
> 또한 중복으로 도로를 카운트하지 않기 위해 이미 방문한 적이 있는 노드는 큐에 넣어 주지 않는다. 기존의 위상 정렬 방식을 완벽히 이해하고, 요구사항에 따라
> 적절하게 로직을 수정할 수 있어야만 문제를 해결할 수 있기 때문에 많이 고민해야 한다.

### 슈도코드
```text
n(도시 수) m(도로 수)
A(인접 리스트) reverseA(역방향 인접 리스트)
inDegree(진입 차수 리스트)


for m 반복:
    인접, 역방향 인접 리스트 데이터 저장
    진입 차수 리스트 저장
    
시작 도시, 도착 도시 데이터 입력

큐 생성
출발 도시 큐 삽입
result(결과)

while 큐가 빌 때까지:
    큐에서 현재 노드 가져오기
    for 현재 노드에서 갈 수 있는 노드:
        다음 노드 진입 차수 1 감소
        result[다음 노드] = max(다음 노드 경로, 현재 노드 경로 + 도로 시간 값)
        if 다음 노드 진입 차수가 0이면:
            큐에 다음 노드 추가

count(1분도 쉬지 않고 달려야 하는 도로의 수)
visit(방문 처리 리스트)
도착 도시 큐 삽입
도착 도시 방문 처리

while 큐가 빌 때까지:  # 역방향 인접 리스트
    큐에서 현재 노드 가져오기
    for 현재 노드에서 갈 수 있는 노드:
        if result[다음 노드] + 도로를 걸리는 데 지나는 시간(에지) == result[now]:
            count 증가
            if 미방문 도시:  
                visit 방문 처리
                큐에 다음 노드 추가

만나는 시간(result[도착 도시]) 출력
count 출력
```

### 코드 구현 - 파이썬
```python
import sys
from collections import deque

input = sys.stdin.readline


class Node:

    def __init__(self, next_node, dist):
        self.next_node = next_node
        self.dist = dist


n = int(input())
m = int(input())

A = [[] for _ in range(n + 1)]
reverseA = [[] for _ in range(n + 1)]
inDegree = [0] * (n + 1)

for i in range(m):
    S, E, V = map(int, input().split())
    A[S].append(Node(E, V))
    reverseA[E].append(Node(S, V))
    inDegree[E] += 1

start, end = map(int, input().split())

qu = deque()
qu.append(start)
result = [0] * (n + 1)

while qu:
    now = qu.popleft()
    for next in A[now]:
        inDegree[next.next_node] -= 1

        result[next.next_node] = max(result[next.next_node], result[now] + next.dist)

        if inDegree[next.next_node] == 0:
            qu.append(next.next_node)


count = 0
visit = [False] * (n + 1)
qu.clear()
qu.append(end)
visit[end] = True

while qu:
    now = qu.popleft()
    for next in reverseA[now]:
        if result[next.next_node] + next.dist == result[now]:
            count += 1

            if not visit[next.next_node]:
                visit[next.next_node] = True
                qu.append(next.next_node)

print(result[end])
print(count)
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static class Node{
        int node, dist;

        public Node(int node, int dist) {
            this.node = node;
            this.dist = dist;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());

        ArrayList<Node>[] A = new ArrayList[n + 1];
        ArrayList<Node>[] reverseA = new ArrayList[n + 1];
        int[] inDegree = new int[n + 1];

        for (int i = 1; i < n + 1; i++) {
            A[i] = new ArrayList<>();
            reverseA[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            String[] input = br.readLine().split(" ");
            int S = Integer.parseInt(input[0]);
            int E = Integer.parseInt(input[1]);
            int V = Integer.parseInt(input[2]);

            A[S].add(new Node(E, V));
            reverseA[E].add(new Node(S, V));

            inDegree[E]++;
        }

        StringTokenizer st = new StringTokenizer(br.readLine());
        int start = Integer.parseInt(st.nextToken());
        int end = Integer.parseInt(st.nextToken());

        Queue<Integer> qu = new LinkedList<>();
        qu.offer(start);
        int[] result = new int[n + 1]; //임계 경로 값

        while (!qu.isEmpty()) {
            int now = qu.poll();
            for (Node next : A[now]) {
                inDegree[next.node]--;
                result[next.node] = Math.max(result[next.node], next.dist + result[now]);
                if (inDegree[next.node] == 0) {
                    qu.offer(next.node);
                }
            }
        }

        boolean[] visit = new boolean[n + 1];
        qu.clear();
        qu.offer(end);
        visit[end] = true;
        int count = 0;

        while (!qu.isEmpty()) {
            int now = qu.poll();
            for (Node next : reverseA[now]) {
                if (result[now] == result[next.node] + next.dist) {
                    count++;

                    if (!visit[next.node]) {
                        visit[next.node] = true;
                        qu.offer(next.node);
                    }
                }
            }
        }

        System.out.println(result[end]);
        System.out.println(count);

    }
}
```