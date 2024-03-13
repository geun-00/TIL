# 벨만-포드 예제 - 2

### [문제(백준(1219번 - 오민식의 고민))](https://www.acmicpc.net/problem/1219)

### 문제 분석
- 벨만-포드 알고리즘의 원리를 바탕으로 요구사항에 따라 내부 로직을 바꿔야 하는 문제다.
- 기존 벨만-포드는 최단 거리를 구하는 알고리즘이지만, 이 문제에서는 도착 도시에 도착할 때 돈의 액수를 최대로 해야 하기 때문에 업데이트 방식을 반대로 변경해야 한다.
- 또한 돈을 무한히 많이 버는 케이스가 있다고 하는 것을 바탕으로 음수 사이클이 아닌 양수 사이클을 찾도록 변경해야 한다.
- 그리고 양수 사이클이 있어도 출발 노드에서 이 양수 사이클을 이용해 도착 도시에 가지 못할 때를 예외 처리가 필요하다.
- 이 부분을 해결하는 방법은 여러 가지가 있는데, 에지의 업데이트를 `N-1`번이 아닌 충분히 큰 수(`N의 최댓값=50`)만큼 추가로 돌리면서 업데이트를 수행하도록 한다.
- 이유는 에지를 충분히 탐색하면서 양수 사이클에서 도달할 수 있는 모든 노드를 양수 사이클에 연결된 노드로 업데이트하기 위해서이다.

### 손으로 풀어보기
1. **에지 리스트에 에지 데이터를 저장하고, 거리 리스트값을 초기화한다. 그리고 각 도시에서 벌 수 있는 돈의 최댓값을 배열(`cityMoney`)에 저장한다. 최초 시작점에 해당하는 거리 리스트값은
    `cityMoney(시작점)`값으로 초기화한다.**
2. **다음 순서에 따라 변형된 벨만-포드 알고리즘을 수행한다.**

- **변형된 벨만-포드 알고리즘**
  1. 모든 에지와 관련된 정보를 가져와 다음 조건에 따라 거리 리스트의 값을 업데이트한다.
     - 시작 도시가 방문한 적이 없는 도시일 때 업데이트하지 않는다.
     - 시작 도시가 양수 사이클과 연결된 도시일 때 도착 도시도 양수 사이클과 연결된 도시로 업데이트한다.
     - `도착 도시 값` < `시작 도시 값 + 도착 도시 수입 - 에지 가중치`일 때 더 많이 벌 수 있는 새로운 경로로 도착한 것이므로 값을 업데이트한다.
  2. 노드보다 충분히 많은 값으로 과정1을 반복한다.

3. **도착 도시의 값에 따라 결과를 출력한다.**
- 도착 도시의 값이 `MIN`이고, 도착하지 못할 때 'gg' 출력
- 도착 도시의 값이 `MAX`이고, 무한히 많이 벌 수 있을 때 'Gee' 출력
- 이외에는 도착 도시의 값 출력
    

### 슈도코드
```text
n(노드 개수) start(시작 도시) end(도착 도시) m(에지 개수)
edges(에지 리스트)
distance(거리 리스트) # 처음에는 작은 수로 초기화

for m 반복:
    에지 리스트 데이터 저장

cityMoney(각 도시에서 벌 수 있는 돈의 최댓값 저장)

거리 리스트에 출발 노드를 cityMoney[시작 도시]로 초기화

for n+50 반복:  # 양수 사이클이 전파되도록 충분히 큰 수로 반복
    for 에지 개수만큼:
        현재 에지 데이터 가져오기
        if 출발 노드가 미방문 노드:
            continue
        elif 출발 노드가 양수 사이클에 연결된 노드:
            종료 노드를 양수 사이클에 연결된 노드로 업데이트
        elif 종료 노드 값 < 출발 노드 값 + 도착 도시 수입 - 에지 가중치:
            # 더 많은 수입을 얻는 경로가 새로 발견될 때
            종료 노드 값 = 출발 노드 값 + 도착 도시 수입 - 에지 가중치 업데이트
            
            if n-1 반복 이후 업데이트:
                종료 노드를 양수 사이클 연결 노드로 업데이트

if 도착 도시가 초깃값:
    gg 출력(도착 불가)
elif 도착 도시가 양수 사이클:
    Gee 출력(돈을 무한히 벌 수 있음)
else:   
    도착 도시의 값 출력
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline

n, startCity, endCity, m = map(int, input().split())
edges = []
distance = [-sys.maxsize] * n  # 작은 수로 초기화

for _ in range(m):
    s, e, c = map(int, input().split())  # 시작, 끝, 가격
    edges.append((s, e, c))

cityMoney = list(map(int, input().split()))

distance[startCity] = cityMoney[startCity]

for i in range(n + 51):
    for start, end, cost in edges:
        if distance[start] == -sys.maxsize:  # 출발 노드가 미방문 노드
            continue

        elif distance[start] == sys.maxsize:  # 출발 노드가 양수 사이클이면 도착 노드도 양수 사이클
            distance[end] = sys.maxsize

        elif distance[end] < distance[start] + cityMoney[end] - cost:
            distance[end] = distance[start] + cityMoney[end] - cost

            if i >= n - 1:  # n-1번 이후에 업데이트 된 것이라면 양수 사이클
                distance[end] = sys.maxsize


print(distance)

if distance[endCity] == -sys.maxsize:
    print("gg")
elif distance[endCity] == sys.maxsize:
    print("Gee")
else:
    print(distance[endCity])
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    static class Edge {
        int start, end, cost;

        public Edge(int start, int end, int cost) {
            this.start = start;
            this.end = end;
            this.cost = cost;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int startCity = Integer.parseInt(st.nextToken());
        int endCity = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        long[] distance = new long[n]; //주의! long 사용
        Arrays.fill(distance, Integer.MIN_VALUE);

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int s = Integer.parseInt(st.nextToken()); //start
            int e = Integer.parseInt(st.nextToken()); //end
            int c = Integer.parseInt(st.nextToken()); //cost

            edges[i] = new Edge(s, e, c);
        }

        int[] cityMoney = Arrays.stream(br.readLine().split(" "))
                                .mapToInt(Integer::parseInt)
                                .toArray();
        
        distance[startCity] = cityMoney[startCity];

        for (int i = 0; i < n + 51; i++) {
            for (Edge edge : edges) {
                int start = edge.start;
                int end = edge.end;
                int cost = edge.cost;

                if (distance[start] == Integer.MIN_VALUE) {
                    continue;
                } else if (distance[start] == Integer.MAX_VALUE) {
                    distance[end] = Integer.MAX_VALUE;
                } else if (distance[end] < distance[start] + cityMoney[end] - cost) {
                    distance[end] = distance[start] + cityMoney[end] - cost;

                    if (i >= n - 1) {
                        distance[end] = Integer.MAX_VALUE;
                    }
                }
            }
        }

        switch ((int) distance[endCity]) {
            case Integer.MIN_VALUE -> System.out.println("gg");
            case Integer.MAX_VALUE -> System.out.println("Gee");
            default -> System.out.println(distance[endCity]);
        }
    }
}
```