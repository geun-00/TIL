# 트리 예제 - 2

### [문제(백준(1068번 - 트리))](https://www.acmicpc.net/problem/1068)

### 문제 분석
- 이 문제의 핵심은 **리프 노드를 어떻게 제거하는가?** 이다.
- 리프 노드를 탐색하는 탐색 알고리즘을 수행할 때나 제거하는 노드가 나왔을 때 탐색을 종료하는 아이디어를 적용하면 실제 리프 노드를 제거하는 효과를 낼 수 있다.

### 손으로 풀어보기
1. **인접 리스트로 트리 데이터를 구현한다.**
2. **DFS 또는 BFS 탐색을 수행하면서 리프 노드의 개수를 센다. 단, 제거 대상을 만났을 때는 그 아래 자식 노드들과 관련된 탐색은 중지한다. 이는 제거한 노드의 범위에서
    리프 노드를 제거하는 효과가 있다.**

### 슈도코드
```text
n(노드 개수)
visit(방문 기록 리스트)
tree(인접 리스트)
ans(리프 노드 개수 변수)
p(입력 데이터)

for n 반복:
    if 루트 노드가 아니면:
        인접 리스트 데이터 저장
    else:
        루트 노드 저장

DFS(노드):
    노드 방문 처리
    for 연결 노드:
        if 미방문 노드 and 삭제 노드가 아니면:
            리프 노드 개수 증가
            DFS(연결 노드)
    if 자식 노드 개수가 0:
        리프 노드 개수 증가

deleteNode(지울 노드의 번호)

if 지울 노드의 번호가 루트 노드라면:
    0 출력
else:
    DFS(root)
    ans 출력
```

### 코드 구현 - 파이썬
```python
import sys

sys.setrecursionlimit(10**6)
input = sys.stdin.readline

n = int(input())
visit = [False] * n
tree = [[] for _ in range(n)]
p = list(map(int, input().split()))
ans = 0
root = 0

for i in range(n):
    if(p[i] != -1):
        tree[i].append(p[i])
        tree[p[i]].append(i)
    else:
        root = i


def DFS(node):
    global ans
    visit[node] = True
    child = 0

    for next in tree[node]:
        if not visit[next] and next != deleteNode:
            child += 1
            DFS(next)

    if child == 0:
        ans += 1


deleteNode = int(input())

if deleteNode == root:
    print(0)
else:
    DFS(root)
    print(ans)
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    static ArrayList<Integer>[] tree;
    static boolean[] visit;
    static int deleteNode;
    static int ans;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        tree = new ArrayList[n + 1];
        visit = new boolean[n + 1];

        for (int i = 0; i < n + 1; i++) {
            tree[i] = new ArrayList<>();
        }

        int[] input = Arrays.stream(br.readLine().split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray();

        int root = 0;
        for (int i = 0; i < n; i++) {
            if (input[i] != -1) {
                tree[i].add(input[i]);
                tree[input[i]].add(i);
            } else {
                root = i;
            }
        }

        deleteNode = Integer.parseInt(br.readLine());

        if (root == deleteNode) {
            System.out.println(0);
        } else {
            DFS(root);
            System.out.println(ans);
        }
    }

    private static void DFS(int parent) {
        visit[parent] = true;
        int child = 0;

        for (int next : tree[parent]) {
            if (!visit[next] && next != deleteNode) {
                child++;
                DFS(next);
            }
        }
        if (child == 0) {
            ans++;
        }
    }
}
```