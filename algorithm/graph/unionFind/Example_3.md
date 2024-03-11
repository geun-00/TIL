# 유니온 파인드 예제 - 3

### [문제(백준(1043번 - 거짓말))](https://www.acmicpc.net/problem/1043)

### 문제 분석
- 이 문제의 핵심은 파티에 참석한 사람들을 1개의 집합으로 생각하고, 각각의 파티마다 `union` 연산을 이용해 사람들을 연결하는 것이다.
- 이 작업을 하면 1개의 파티에 있는 모든 사람들은 같은 대표 노드를 바라보게 된다.
- 이후 각 파티의 대표 노드와 진실을 알고 있는 사람들의 각 대표 노드가 동일한지 `find` 연산을 이용해 확인함으로써 과장된 이야기를 할 수 있는지 판단할 수 있다.

### 손으로 풀어보기
1. **진실을 아는 사람 데이터, 파티 데이터, 유니온 파인드를 위한 대표 노드 자료구조를 초기화한다.**
2. **`union` 연산으로 각 파티에 참여한 사람들을 1개의 그룹으로 만든다.**
3. **`find` 연산을 수행해 각 파티의 대표 노드와 진실을 아는 사람들이 같은 그룹에 있는지 확인한다. 파티 사람 노드는 모두 연결돼 있으므로 아무 사람이나 지정해 `find` 연산을 수행해도 된다.**
4. **모든 파티에 관해 과정 3을 반복해 수행하고, 모든 파티의 대표 노드가 진실을 아는 사람들과 다른 그룹에 있다면 결괏값을 증가시킨다.**
5. **결괏값을 출력한다.**

### 슈도코드
```text
n(사람의 수) m(파티의 수)
trues(진실을 아는 사람)
t (진실을 아는 사람 수)
party(파티 데이터)

find(a):
    a가 대표 노드면 리턴
    아니면 a의 대표 노드값을 find(parent[a])값으로 저장(재귀 형태)
    
union(a, b):
    a와 b의 대표 노드 찾기
    두 원소의 대표 노드끼리 연결
        
파티 데이터 저장

for n 반복:   
    대표 노드를 자기 자신으로 초기화

for m 반복:
    first(각 파티의 1번째 사람)
    for j 각 파티의 사람 수만큼: 
        union(first, j)  # 각 파티에 참여한 사람들을 1개의 그룹으로 만들기
        
for m 반복:
    first(각 파티의 1번째 사람)
    for j 진실을 아는 사람들의 수만큼:
        find(first), find(trues[j]) 비교
    모두 다른 경우 결괏값 1 증가

결괏값 출력
```

### 코드 구현 - 파이썬
```python
n, m = map(int, input().split())
trues = list(map(int, input().split()))
t = trues[0]

party = [[] for _ in range(m)]
result = 0


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

        
for i in range(m):
    party[i] = list(map(int, input().split()))

parent = [0] * (n + 1)
for i in range(1, n + 1):
    parent[i] = i

for i in range(m):
    first = party[i][1]
    for j in range(2, len(party[i])):
        union(first, party[i][j])

for i in range(m):
    isPossible = True
    first = party[i][1]

    for j in range(1, len(trues)):
        if find(first) == find(trues[j]):
            isPossible = False
            break

    if isPossible:
        result += 1

print(result)
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
        for (int i = 0; i < n + 1; i++) { //대표 노드 초기화
            parent[i] = i;
        }

        //진실을 아는 사람 리스트
        int[] trues = Arrays.stream(br.readLine().split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
        
        int[][] party = new int[m][];

        for (int i = 0; i < m; i++) {
            party[i] = Arrays.stream(br.readLine().split(" "))
                    .mapToInt(Integer::parseInt)
                    .toArray();
        }

        for (int i = 0; i < m; i++) {
            int first = party[i][1]; //첫번째(0번째)는 사람의 번호가 아닌 사람의 수이므로 1부터 시작
            for (int j = 1; j < party[i].length; j++) { //마찬가지로 1부터
                union(first, party[i][j]);
            }
        }

        int count = 0;
        for (int i = 0; i < m; i++) {
            boolean flag = true;
            int first = party[i][1];//첫번째(0번째)는 사람의 번호가 아닌 사람의 수이므로 1부터 시작

            for (int j = 1; j < trues.length; j++) {//마찬가지로 1부터
                if (find(first) == find(trues[j])) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                count++;
            }
        }

        System.out.println(count);
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