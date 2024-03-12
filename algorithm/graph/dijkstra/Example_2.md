# 다익스트라 예제 - 2

### [문제(백준(1916번 - 최소비용 구하기))](https://www.acmicpc.net/problem/1916)

### 문제 분석
- 시작점과 도착점이 주어지고, 이 목적지까지 가는 최소 비용(최단 거리)을 구하는 문제이다.
- 버스 비용의 범위가 음수가 아니기 때문이 이 문제는 다익스트라 알고리즘을 이용해 해결할 수 있다.

### 손으로 풀어보기
1. **데이터를 기반으로 그래프를 구현한다. 도시는 노드로, 도시 간 버스 비용은 에지로 나타낸다.**
2. **도시 개수만큼 인접 리스트의 크기를 설정한다. 이때 버스의 비용(가중치)이 있기 때문에 데이터는 `(목표 노드, 가중치)` 형태로 저장한다.
    그리고 버스의 개수 만큼 반복문을 돌면서 그래프를 인접 리스트에 저장한다.**
3. **다익스트라 알고리즘 수행 후 최단 거리 리스트가 완성되면 정답을 출력한다.**

### 슈도코드
```text
n(도시 개수) m(버스 개수)
A(인접 리스트)
distance(거리 저장 리스트)
visit(방문 저장 리스트)

for m 반복:
    인접 리스트 데이터 저장

start, end 입력

다익스트라(start, end):
    start를 우선순위 큐에 삽입
    
    while 큐가 빌 때까지:
        현재 선택된 노드 방문 확인
        현재 노드 방문 처리
        for 현재 노드의 다음 노드:
            if 현재 선택 노드 최단 거리 + 비용 < 다음 노드의 최단 거리:
                다음 노드 최단 거리 업데이트
                우선순위 큐 다음 노드 추가
    
    end의 최종 거리 반환

다익스트라 결괏값 출력
```

### 코드 구현 - 파이썬
```python
import sys
from queue import PriorityQueue

input = sys.stdin.readline


class Node:
    def __init__(self, node, dist):
        self.node = node
        self.dist = dist


n = int(input())
m = int(input())

A = [[] for _ in range(n + 1)]
distance = [sys.maxsize] * (n + 1)
visit = [False] * (n + 1)

for i in range(m):
    S, E, V = map(int, input().split())
    A[S].append(Node(E, V))

start, end = map(int, input().split())


def dijkstra(start, end):
    pq = PriorityQueue()
    pq.put((0, start))
    distance[start] = 0

    while pq.qsize() > 0:
        now = pq.get()
        now_node = now[1]
        
        if not visit[now_node]:
            visit[now_node] = True
            
            for next in A[now_node]:
                if not visit[next.node] and distance[next.node] > distance[now_node] + next.dist:
                    distance[next.node] = distance[now_node] + next.dist
                    pq.put((distance[next.node], next.node))

    return distance[end]


print(dijkstra(start, end))
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    // 인접 리스트에 저장할 노드
    static class Node{
        int node, dist;

        public Node(int node, int dist) {
            this.node = node;
            this.dist = dist;
        }
    }

    // 우선순위 큐에 value값 기준 오름차순 정렬할 정보
    static class Info implements Comparable<Info>{
        int value, node;

        public Info(int value, int node) {
            this.value = value;
            this.node = node;
        }

        @Override
        public int compareTo(Info o) {
            return this.value - o.value;
        }
    }

    static boolean[] visit;
    static int[] distance;
    static ArrayList<Node>[] A;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int V = Integer.parseInt(br.readLine());
        int E = Integer.parseInt(br.readLine());

        visit = new boolean[V + 1];
        distance = new int[V + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        A = new ArrayList[V + 1];

        for (int i = 1; i < V + 1; i++) {
            A[i] = new ArrayList<>();
        }

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());

            A[u].add(new Node(v, w));
        }

        st = new StringTokenizer(br.readLine());
        int start = Integer.parseInt(st.nextToken());
        int end = Integer.parseInt(st.nextToken());

        System.out.println(dijkstra(start, end));
    }

    private static int dijkstra(int start, int end) {
        Queue<Info> pq = new PriorityQueue<>();
        pq.add(new Info(0, start));

        distance[start] = 0;

        while (!pq.isEmpty()) {
            Info now = pq.poll();
            int cur = now.node;
            if (!visit[cur]) {
                visit[cur] = true;

                for (Node next : A[cur]) {
                    int next_node = next.node;
                    int dist = next.dist;

                    if (!visit[next_node] && distance[next_node] > distance[cur] + dist) {
                        distance[next_node] = distance[cur] + dist;
                        pq.add(new Info(distance[next_node], next_node));
                    }
                }
            }
        }

        return distance[end];
    }
}
```