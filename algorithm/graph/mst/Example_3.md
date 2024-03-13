# 최소 신장 트리 예제 - 3

### [문제(백준(1414번 - 불우이웃 돕기))](https://www.acmicpc.net/problem/1414)

### 문제 분석
- 인접 행렬의 형태로 데이터가 들어오기 때문에 이 부분을 최소 신장 트리가 가능한 형태로 변형하는 것이 이 문제의 핵심이다.
- 먼저 문자열로 주어진 랜선의 길이를 숫자로 변형해 랜선의 총합을 저장한다.
- 이때 i와 j가 같은 값은 같은 컴퓨터를 연결한다는 의미이므로 넘어가고, 나머지 위치의 값들을 `i -> j`로 가는 에지로 생성하고, 에지 리스트에
    저장하면 최소 신장 트리 문제로 변형할 수 있다.

### 손으로 풀어보기
1. **입력 데이터의 정보를 리스트에 저장한다. 먼저 입력으로 주어진 문자열을 숫자로 변홚해 총합으로 저장한다. 소문자는 `현재 문자 - 'a' + 1`, 대문자는 `현재 문자 - 'A' + 27`로 변환한다.
   i와 j가 다른 경우에는 다른 컴퓨터를 연결하는 랜선이므로 에지 리스트에 저장한다.**
2. **저장된 에지 리스트를 바탕으로 최소 신장 트리 알고리즘을 수행한다.**
3. **최소 신장 트리에서 사용한 에지 개수가 `노드 개수 - 1`개면 처음 저장해 둔 랜선 길이의 총합에서 최소 신장 트리의 결괏값을 뺀 값을 출력하면 된다. 사용한 에지 개수가
    `노드 개수 - 1`개가 아니면 -1을 출력한다.**

### 슈도코드
```text
n(노드 개수)
pq(우선순위 큐)
sum(랜선의 합)

for n 반복:
    한 줄씩 입력
    for n 반복:
        if 소문자:
            현재 문자 - 'a' + 1
        elif 대문자:
            현재 문자 - 'A' + 27
        sum에 현재 랜선 길이 더하기
        if i와 j가 다르고 연결 랜선이 있다면:
            랜선 정보 큐 저장

parent(대표 노드 저장 리스트)

find(a):
    a가 대표 노드면 리턴
    아니면 a의 대표 노드값을 find(parent[a])값으로 저장

union(a, b):
    a와 b의 대표 노드 찾기
    두 원소의 대표 노드끼리 연결
    
useEdge(엣지 사용 횟수 변수)
result(정답 변수)

while 사용한 엣지의 횟수가 노드 개수 - 1이 될 때까지:
    큐에서 에지 정보 가져오기
    if 에시 시작점과 끝점의 부모 노드가 다르면:
        union 연산 수행
        에지의 가중치를 정답에 더하기
        엣지 사용 횟수 1 증가

if 사용한 에지가 노드-1 만큼:
    sum - result 출력
else:
    -1 출력
```

### 코드 구현 - 파이썬
```python
import sys
from queue import PriorityQueue

input = sys.stdin.readline

n = int(input())
pq = PriorityQueue()
sum = 0

for i in range(n):
    data = list(input())
    for j in range(n):
        temp = 0
        if 'a' <= data[j] <= 'z':
            temp = ord(data[j]) - ord('a') + 1  # ord(): 유니코드를 정수로 반환
        elif 'A' <= data[j] <= 'Z':
            temp = ord(data[j]) - ord('A') + 27
        sum += temp

        if i != j and temp != 0:
            pq.put((temp, i, j))


parent = [0] * n

for i in range(n):
    parent[i] = i


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


useEdge = 0
result = 0

while pq.qsize() > 0:
    v, s, e = pq.get()
    if find(s) != find(e):
        union(s, e)
        result += v
        useEdge += 1

if useEdge == n - 1:
    print(sum - result)
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

    static int[] parent;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        Queue<Edge> pq = new PriorityQueue<>();

        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int sum = 0;
        for (int i = 0; i < n; i++) {
            String input = br.readLine();

            for (int j = 0; j < n; j++) {
                int temp = 0;
                char ch = input.charAt(j);

                if ('a' <= ch && ch <= 'z') {
                    temp = ch - 'a' + 1 ;
                } else if ('A' <= ch && ch <= 'Z') {
                    temp = ch - 'A' + 27;
                }
                sum += temp;

                if (i != j && temp != 0) {
                    pq.add(new Edge(i, j, temp));
                }
            }
        }

        int useEdge = 0;
        int result = 0;

        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            int start = edge.start;
            int end = edge.end;
            int len = edge.dist;

            if (find(start) != find(end)) {
                union(start, end);
                result += len;
                useEdge++;
            }
        }

        if (useEdge == n - 1) {
            System.out.println(sum - result);
        } else {
            System.out.println(-1);
        }
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