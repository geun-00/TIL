# 유니온 파인드 예제 - 2

### [문제(백준(1976번 - 여행 가자))](https://www.acmicpc.net/problem/1976)

### 문제 분석
- 도시의 연결 유무를 유니온 파인드 연산을 이용해 해결할 수 있다는 아이디어를 떠올릴 수 있으면 쉽게 해결할 수 있는 문제다.
- 일반적으로 유니온 파인드는 그래프 영역에서 많이 활용되지만, 이 문제처럼 단독으로도 활용할 수 있다.
- 문제에서 도시 간 연결 데이터를 인접 행렬의 형태로 주었기 때문에 인접 행렬을 탐색하면서 연결될 때마다 `union` 연산을 수행하는 방식으로 문제에 접근하면 된다.

### 손으로 풀어보기
1. **도시와 여행 경로 데이터를 저장하고, 각 노드와 관련된 대표 노드 리스트의 값을 초기화한다.**
2. **도시 연결 정보가 저장된 인접 행렬을 탐색하면서 도시가 연결돼 있을 때 `union` 연산을 수행한다.**
3. **여행 경로에 포함된 도시의 대표 노드가 모두 같은지 확인한 후 결과를 출력한다.**

### 슈도코드
```text
n(도시의 수) m(여행 계획에 속한 도시의 수)
city(도시 연결 인접 행렬)

find(a):
    a가 대표 노드면 a 리턴
    아니면 a의 대표 노드 값을 find(parent[a])값으로 저장(재귀 형태)

union(a, b):
    a와 b의 대표 노드 찾기
    두 원소의 대표 노드끼리 연결

for i 1~n+1 반복:
    city 데이터 저장

route(여행 계획 도시 저장 리스트)

parent(대표 노드 저장 리스트)

for n 반복:
    parent 대표 노드 자기 자신으로 초기화
    
for i n 반복:
    for j n 반복:
        if city[i][j]가 1이면:
            union(i, j)

isConnect(연결 여부 변수)

for 2~m 반복:
    route에 포함되는 노드들의 대표 노드가 모두 동일한지 확인한 후 isConnect 저장

isConnect 값에 따라 YES 또는 NO 출력
```

### 코드 구현 - 파이썬
```python
n = int(input())
m = int(input())
city = [[0 for _ in range(n + 1)] for _ in range(n + 1)]

for i in range(1, n + 1):
    city[i][1:] = list(map(int, input().split()))

route = list(map(int, input().split()))

parent = [0] * (n + 1)
for i in range(1, n + 1):
    parent[i] = i


def find(a):
    if a == parent[a]:
        return a
    else:
        parent[a] = find(parent[a])
        return parent[a]


def union(a, b):
    a = find(a)
    b = find(b)
    if a != b:
        parent[b] = a


for i in range(1, n + 1):
    for j in range(1, n + 1):
        if city[i][j] == 1:  # 두 도시가 연결되어 있다면 union 수행
            union(i, j)


index = find(route[0])

isConnect = True
for i in range(1, m):
    if index != find(route[i]):
        isConnect = False
        break

if isConnect:
    print("YES")
else:
    print("NO")
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static int[] parent;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());

        parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }

        int[][] city = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                city[i][j] = Integer.parseInt(st.nextToken());
                if (city[i][j] == 1) {
                    union(i + 1, j + 1);
                }
            }
        }

        int[] route = Arrays.stream(br.readLine().split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray();

        boolean isConnect = true;
        int index = find(route[0]);

        for (int i = 1; i < m; i++) {
            if (index != find(route[i])) {
                isConnect = false;
                break;
            }
        }
        System.out.println(isConnect ? "YES" : "NO");
    }

    private static int find(int a) {
        if (a == parent[a]) {
            return a;
        } else {
            parent[a] = find(parent[a]);
            return parent[a];
        }
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