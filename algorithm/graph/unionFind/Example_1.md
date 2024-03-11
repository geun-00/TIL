# 유니온 파인드 예제 - 1

### [문제(백준(1717번 - 집합의 표현))](https://www.acmicpc.net/problem/1717)

### 문제 분석
- 최대 원소의 개수 1,000,000과 질의 개수 100,000으로 큰 편이므로 경로 압축이 필요한 전형적인 유니온 파인드 문제다.

### 손으로 풀어보기
1. **처음에는 노드가 연결되어 있지 않으므로 각 노드의 대표 노드는 자기 자신이다. 각 노드의 값을 자기 인덱스값으로 초기화한다.**
2. **`find`연산으로 특정 노드의 대표 노드를 찾고, `union` 연산으로 2개의 노드를 이용해 각 대표 노드를 찾아 연결한다. 그리고 질의한 값에 따라 결과를 반환한다.**

### 슈도코드
```text
n(원소 개수) m(질의 개수)
parent(대표 노드 저장 리스트)

find(a):
    a가 대표 노드면 리턴
    아니면 a의 대표 노드값을 find(parent[a])값으로 저장(재귀 형태)
    
union(a, b):
    a와 b의 대표 노드 찾기
    두 원소의 대표 노드끼리 연결
    
checkSame(a, b):
    a와 b의 대표 노드 찾기
    if 두 대표 노드가 같으면:
        true 리턴
    아니면:    
        false 리턴

for n 반복:
    대표 노드를 자기 자신으로 초기화
    
for m 반복:
    if 질의가 0:   
        union(a, b)
    else:   
        checkSame(a, b)으로 확인 후 결과 출력
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline
sys.setrecursionlimit(100_000)

n, m = map(int, input().split())
parent = [0] * (n + 1)


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


def checkSame(a, b):
    a = find(a)
    b = find(b)
    return a == b


for i in range(0, n + 1):
    parent[i] = i

result = []

for i in range(m):
    q, a, b = map(int, input().split())

    if q == 0:
        union(a, b)
    else:
        result.append("YES" if checkSame(a, b) else "NO")

print("\n".join(result))
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
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        parent = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int q = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            if (q == 0) {
                union(a, b);
            } else {
                sb.append(checkSame(a, b) ? "YES" : "NO").append("\n");
            }
        }

        System.out.println(sb);
    }

    private static boolean checkSame(int a, int b) {
        a = find(a);
        b = find(b);
        return a == b;
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