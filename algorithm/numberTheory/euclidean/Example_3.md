# 유클리드 호제법 예제 - 3

### [문제(백준(1033번 - 칵테일))](https://www.acmicpc.net/problem/1033)

### 문제 분석
- `N - 1`개의 비율로 `N`개의 재료와 관련된 전체 비율을 알아낼 수 있다고 한다.
- 이것을 그래프 관점으로 생각하면 사이클이 없는 트리 구조로 이해할 수 있다.
- 임의의 노드에서 DFS를 진행하면서 정답을 찾을 수 있다.
- DFS 과정에서 유클리드 호제법을 이용해 비율들의 최소 공배수와 최대 공약수를 구하고, 재료의 최소 질량을 구하는 데 사용해 문제를 해결할 수 있다.


### 손으로 풀어보기
1. **인접 리스트를 이용해 각 재료의 비율 자료를 그래프로 저장한다.**

- `0` -> `4(1:1)`
- `1` -> `4(3:1)`
- `2` -> `4(5:1)`
- `3` -> `4(7:1)`
- `4` -> `0(1:1)`, `1(3:1)`, `2(5:1)`, `3(7:1)`

2. **데이터를 저장할 때마다 비율과 관련된 수들의 최소 공배수를 업데이트한다.**

- A, B의 최소 공배수 = `A * B / 최대 공약수`
- 1, 3, 5, 7의 최소 공배수 = 105

3. **임의의 시작점에 최대 공배수 값을 저장한다.**
4. **임의의 시작점에서 DFS로 탐색을 수행하면서 각 노드의 값을 이전 노드의 값과의 비율 계산을 통해 계산하고 저장한다.**

- `0`을 시작점으로 정하고 DFS 탐색을 수행하면 `0 -> 4 -> 1 -> 2 -> 3` 순으로 탐색
- `4` -> 0번 노드 값 * 1/1 = 105
- `1` -> 4번 노드 값 * 1/3 = 35
- `2` -> 4번 노드 값 * 1/5 = 21
- `3` -> 4번 노드 값 * 1/7 = 15

| 0   | 1  | 2  | 3  | 4   |
|-----|----|----|----|-----|
| 105 | 35 | 21 | 15 | 105 |

5. **각 노드의 값을 모든 노드의 최대 공약수로 나눈 뒤 출력한다.**

### 슈도코드
```text
n(재료 개수)
A(인접 리스트)
visit(방문 기록 리스트)
D(각 노드 값 저장 리스트)
lcm(최소공배수)  # least common multiple

# 최대 공약수
gcd(a, b):
    if b가 0이면:
        a가 최대공약수
    else:
        gcd(작은 수, 큰 수 % 작은 수)

# DFS 구현
DFS:
    visit 방문 기록
    if 현재 노드의 연결 노드 중 방문하지 않은 노드:
        다음 노드의 값 = 현재 노드의 값 * 비율
        DFS(다음 노드)

for 에지 개수(n-1):
    인접 리스트에 에지 정보 저장
    최소 공배수 업데이트

0번 노드에 최소 공배수 저장
0번에서 DFS 탐색 수행
DFS를 이용해 업데이트된 D 리스트의 값들의 최대 공약수 계산
D 리스트의 각 값을 최대 공약수로 나눠 정답 출력
```

### 코드 구현 - 파이썬
```python
n = int(input())
A = [[] for _ in range(n)]
visit = [False] * n
D = [0] * n  # 각 노드 값 저장 리스트
lcm = 1  # 최소공배수


def gcd(a, b):
    if b == 0:
        return a
    else:
        return gcd(b, a % b)


def DFS(node):
    visit[node] = True

    for i in A[node]:
        next_node = i[0]
        if not visit[next_node]:
            D[next_node] = D[node] * i[2] // i[1]
            DFS(next_node)


for i in range(n - 1):
    a, b, p, q = map(int, input().split())
    A[a].append((b, p, q))
    A[b].append((a, q, p))
    lcm *= (p * q // gcd(p, q))

D[0] = lcm
DFS(0)
mgcd = D[0]  # 최대 공약수

for i in range(1, n):
    mgcd = gcd(mgcd, D[i])

for i in range(n):
    print(int(D[i] // mgcd), end=' ')
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
    static class Node{
        int adjNode, p, q;

        public Node(int adjNode, int p, int q) {
            this.adjNode = adjNode;
            this.p = p;
            this.q = q;
        }
    }
    static ArrayList<Node>[] A;
    static boolean[] visit;
    static long[] D;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        D = new long[n];
        visit = new boolean[n];
        A = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            A[i] = new ArrayList<>();
        }

        long lcm = 1;

        for (int i = 0; i < n - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            int q = Integer.parseInt(st.nextToken());

            A[a].add(new Node(b, p, q));
            A[b].add(new Node(a, q, p));

            lcm *= (long) p * q / gcd(p, q); //비율들의 최소 공배수 계산
        }

        System.out.println("lcm = " + lcm);
        D[0] = lcm;
        DFS(0);

        long mgcd = D[0]; //모든 노드의 최대 공약수

        for (int i = 1; i < D.length; i++) {
            mgcd = gcd(mgcd, D[i]);
        }

        for (int i = 0; i < n; i++) {
            System.out.print( D[i] / mgcd  + " ");
        }
    }

    private static void DFS(int node) {
        visit[node] = true;
        for (Node next : A[node]) {
            int nextNode = next.adjNode;
            if (!visit[nextNode]) {
                D[nextNode] = D[node] * next.q / next.p;
                DFS(nextNode);
            }
        }
    }

    private static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }
}
```