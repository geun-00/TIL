# 최소 신장 트리 예제 - 2

### [문제(백준(17472번 - 다리 만들기 2))](https://www.acmicpc.net/problem/17472)

### 문제 분석
- 문제 조건에서 데이터의 크기는 매우 작은 편이라 시간 복잡도의 제약은 크지 않다.
- 몇 개의 단계로 나눠서 생각해야 한다.
  - 먼저 주어진 지도에서 섬으로 표현된 값을 각각의 섬마다 다르게 표현해야 한다.
  - 그 이후 각 섬의 모든 위치에서 다른 섬으로 연결할 수 있는 에지가 있는지 확인해 에지 리스트를 만든다.
  - 이후에는 최소 신장 트리를 적용하면 문제를 해결할 수 있다.

### 손으로 풀어보기
1. **지도의 정보를 2차원 리스트에 저장하고 섬으로 표시된 모든 점에서 BFS를 실행해 섬을 구분한다.**
2. **모든 섬에서 상하좌우로 다리를 지어 다른 섬으로 연결할 수 있는지 확인한다. 연결할 곳이 현재 섬이면 탐색 중단, 바다라면 탐색을 계속 수행한다. 다른 섬을 만났을 때
    다리의 길이가 2 이상이면 이 다리를 에지 리스트에 추가한다.**
3. **전 단계에서 수집한 모든 에지를 오름차순 정렬해 최소 신장 트리 알고리즘을 수행한다. 알고리즘이 끝나면 사용한 에지의 합을 출력한다.**

### 슈도코드
```text
n, m(행렬 크기)
dx, dy(네 방향 탐색 상수)
map(맵 정보 리스트)
visit(BFS 방문 리스트)

for n 반복:
    map 정보 저장

num(섬 번호)
sumlist(모든 섬 정보 이중 리스트)
mlist(1개의 섬 정보 리스트)

addNode(i, j, qu):
    map에서 i, j 위치에 섬 번호 저장
    해당 위치 방문 표시
    mlist에 해당 노드 추가
    BFS를 위한 큐에 해당 노드 추가

BFS(i, j):
    i, j위치에서 네 방향으로 연결된 모든 노드를 탐색하여 1개 섬의 영역을 저장
    연결된 새로운 노드가 확인하면 addNode를 통해 정보 저장

for i n 반복:
    for j m 반복:
        BFS(i, j)  # BFS를 실행해 하나의 섬 정보를 가져오기
        BFS 결과(하나의 섬 정보)를 sumlist에 추가
        섬 번호 1 증가

pq(우선순위 큐)

for sumlist:
    now(sumlist에서 추출)
    for now :
        1개의 섬의 모든 위치에서 만들 수 있는 다리 정보 저장
        # 우선순위 큐에 에지 정보 저장

find(a):
    a가 대표 노드면 리턴
    아니면 a의 대표 노드값을 find(parent[a])값으로 저장

union(a, b):
    a와 b의 대표 노드 찾기
    두 원소의 대표 노드끼리 연결
    
parent(대표 노드 저장 리스트)

useEdge(에지 사용 횟수 변수)
result(정답 변수)

while 큐가 빌 때까지:
    큐에서 에지 정보 가져오기
    if 에지 시작점과 끝점의 부모 노드가 다르면:
        union 수행
        에지 가중치 result에 더하기
        에지 사용 횟수 1 증가

if 사용한 에지의 수가 노드-1이면:
    result 출력
else:
    -1 출력
```

### 코드 구현 - 파이썬
```python
import copy
import sys
from collections import deque
from queue import PriorityQueue

input = sys.stdin.readline
dx = [-1, 1, 0, 0]
dy = [0, 0, -1, 1]

n, m = map(int, input().split())
myMap = [[0 for _ in range(m)] for _ in range(n)]
visit = [[False for _ in range(m)] for _ in range(n)]

for i in range(n):
    myMap[i] = list(map(int, input().split()))

num = 1
sumList = list([])  # 모든 섬 정보 리스트
mlist = []  # 각각의 섬 정보 리스트, BFS를 통해 하나의 mlist가 sumList의 한 공간에 들어간다.


def addNode(i, j, qu):
    myMap[i][j] = num
    visit[i][j] = True
    temp = [i, j]
    mlist.append(temp)
    qu.append(temp)


def BFS(i, j):
    queue = deque()
    mlist.clear()
    start = [i, j]
    queue.append(start)
    mlist.append(start)

    visit[i][j] = True
    myMap[i][j] = num

    while queue:
        x, y = queue.popleft()
        for k in range(4):
            nx = x + dx[k]
            ny = y + dy[k]

            if nx >= 0 and nx < n and ny >= 0 and ny < m:
                if not visit[nx][ny] and myMap[nx][ny] != 0:
                    addNode(nx, ny, queue)

    return mlist


for i in range(n):
    for j in range(m):
        if myMap[i][j] != 0 and not visit[i][j]:
            tempList = copy.deepcopy(BFS(i, j))
            num += 1  # 섬 번호
            sumList.append(tempList)

pq = PriorityQueue()

for now in sumList:
    for temp in now:
        x = temp[0]
        y = temp[1]

        now_S = myMap[x][y]

        for i in range(4):
            nx = x + dx[i]
            ny = y + dy[i]

            blength = 0
            while nx >= 0 and nx < n and ny >= 0 and ny < m:

                if myMap[nx][ny] == now_S:
                    break

                elif myMap[nx][ny] != 0:
                    if blength > 1:
                        pq.put((blength, now_S, myMap[nx][ny]))
                    break

                else:
                    blength += 1

                nx += dx[i]
                ny += dy[i]


def find(a):
    if parent[a] == a:
        return a
    parent[a] = find(parent[a])
    return parent[a]


def union(a, b):
    a = find(a)
    b = find(b)
    if a != b:
        parent[b] = a


parent = [0] * num

for i in range(len(parent)):
    parent[i] = i

useEdge = 0
result = 0

while pq.qsize() > 0:
    v, s, e = pq.get()
    if find(s) != find(e):
        union(s, e)
        result += v
        useEdge += 1

if useEdge == num - 2:
    print(result)
else:
    print(-1)
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    //가중치 기준으로 오름차순 정렬하는 에지 정보 클래스
    static class Edge implements Comparable<Edge>{
        int start, end, dist;

        public Edge(int start, int end, int dist) {
            this.start = start;
            this.end = end;
            this.dist = dist;
        }

        @Override
        public int compareTo(Edge o) {
            return this.dist - o.dist;
        }
    }

    static int n, m;
    static int[] parent;
    static boolean[][] visit;
    static int[][] map;
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static ArrayList<ArrayList<int[]>> sumList = new ArrayList<>(); //모든 섬 정보 리스트
    static ArrayList<int[]> mlist = new ArrayList<>();//각각의 섬 정보 리스트

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][m];
        visit = new boolean[n][m];

        //입력
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        //섬을 구분하기 위해 각 섬 시작 부분에서 BFS 수행
        int sumNum = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (map[i][j] == 1 && !visit[i][j]) {
                    BFS(i, j, sumNum++);
                }
            }
        }

        //섬의 개수(노드 개수)만큼 부모 노드 배열 생성
        parent = new int[sumNum];
        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        Queue<Edge> pq = new PriorityQueue<>();

        //각 섬의 각 육지 부분마다 탐색을 수행하여 다른 섬으로 이동할 수 있는 경로 우선순위 큐에 저장
        for (ArrayList<int[]> now : sumList) {
            for (int[] temp : now) {
                int x = temp[0];
                int y = temp[1];
                int nowS = map[x][y];

                for (int i = 0; i < 4; i++) {
                    int nx = x + dx[i];
                    int ny = y + dy[i];

                    int length = 0;
                    while (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                        if (map[nx][ny] == nowS) {
                            break;
                        } else if (map[nx][ny] != 0) {
                            if (length >= 2) { //다리의 길이는 2 이상이어야 한다는 조건이 있었다.
                                pq.offer(new Edge(nowS, map[nx][ny], length));//에지 정보 저장(시작, 도착, 가중치)
                            }
                            break;
                        } else {
                            length++;
                        }
                        nx += dx[i];
                        ny += dy[i];
                    }
                }
            }
        }

        int useEdge = 0;
        int result = 0;

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            int start = edge.start;
            int end = edge.end;
            int dist = edge.dist;

            if (find(start) != find(end)) {
                union(start, end);
                result += dist;
                useEdge++;
            }
        }

        if (useEdge == sumNum - 2) { //실제 섬 개수는 섬 개수 + 1만큼으로 바뀌기 때문에 사용된 에지가 n-1을 확인하려면 섬 개수 - 2를 해주어야 한다.
            System.out.println(result);
        } else {
            System.out.println(-1);
        }

    }

        private static void BFS(int i, int j, int sNum) {
            Queue<int[]> qu = new LinkedList<>();
            qu.offer(new int[]{i, j});
            mlist.clear();
            visit[i][j] = true;
            map[i][j] = sNum;
            mlist.add(new int[]{i, j});

            while (!qu.isEmpty()) {
                int[] now = qu.poll();
                int x = now[0];
                int y = now[1];

                for (int k = 0; k < 4; k++) {
                    int nx = x + dx[k];
                    int ny = y + dy[k];

                    if (nx >= 0 && nx < n && ny >= 0 && ny < m) {
                        if (!visit[nx][ny] && map[nx][ny] == 1) {
                            visit[nx][ny] = true;
                            qu.offer(new int[]{nx, ny});
                            mlist.add(new int[]{nx, ny});
                            map[nx][ny] = sNum;
                        }
                    }
                }
            }
            sumList.add(new ArrayList<>(mlist)); //깊은 복사
    }

    private static int find(int a) {
        if (parent[a] == a) {
            return a;
        }
        return parent[a] = find(parent[a]);
    }

    private static void union(int a, int b) {
        a = find(a);
        b = find(b);
        if (a != b) {
            parent[b] = a;
        }
    }
}
```