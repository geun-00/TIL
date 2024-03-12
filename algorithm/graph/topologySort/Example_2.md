# 위상 정렬 예제 - 2

### [문제(백준(1516번 - 게임 개발))](https://www.acmicpc.net/problem/1516)

### 문제 분석
- **어떤 건물을 짓기 위해 먼저 지어야 하는 건물이 있을 수 있다.** 라는 문장에 주목해야 한다.
- 각 건물을 노드라고 생각하면 그래프 형태에서 노드 순서를 정렬하는 알고리즘인 위상 정렬을 사용하는 문제라는 것을 알 수 있다.
- 건물의 수가 최대 500, 시간 복잡도가 2초 이므로 시간 제한 부담은 거의 없다.

### 손으로 풀어보기
1. **입력 데이터를 바탕으로 필요한 자료구조를 초기화한다. 인접 리스트로 그래프를 표현하고, 건물의 생산 시간을 따로 저장한다. 
    예제 입력을 기준으로 진입 차수 리스트는 `[0, 1, 1, 2, 1]`, 정답 리스트는 모두 0으로 초기화한다.**
2. **위상 정렬을 수행하면서 각 건물을 짓는 데 걸리는 최대 시간을 업데이트한다.**

- **업데이트 수행 방법** : Math.max(현재 건물에 저장된 최대 시간, 이전 건물에 저장된 최대 시간 + 현재 건물의 생산 시간)

3. **정답 리스트에 자기 건물을 짓는데 걸리는 시간을 더한 후 정답 리스트를 차례대로 출력한다.**

### 슈도코드
```text
n(건물 수)
A(인접 리스트)
inDegree(진입 차수 리스트)
build(각 건물을 짓는 데 걸리는 시간 리스트)

for n 반복:
    인접 리스트 저장
    진입 차수 리스트 저장
    각 건물을 짓는 데 걸리는 시간 리스트 저장

큐 생성

for n 반복:
    진입 차수가 0인 노드 큐에 삽입

while 큐가 빌 때까지:
    큐에서 현재 노드 가져오기
    for 현재 노드에서 갈 수 있는 노드:
        다음 노드 진입 차수 1 감소
        결과 업데이트 = max(현재 저장된 값, 현재 출발 노드 + 비용)
        if 다음 노드 진입 차수가 0이면:
            큐에 다음 노드 추가

위상 정렬 결과 출력
```

### 코드 구현 - 파이썬
```python
from collections import deque

n = int(input())
A = [[] for _ in range(n + 1)]
inDegree = [0] * (n + 1)
build = [0] * (n + 1)

for i in range(1, n + 1):
    inputList = list(map(int, input().split()))
    build[i] = inputList[0]
    index = 1

    while True:
        temp = inputList[index]
        index += 1

        if temp == -1:
            break

        A[temp].append(i)
        inDegree[i] += 1

qu = deque()

for i in range(1, n + 1):
    if inDegree[i] == 0:
        qu.append(i)

result = [0] * (n + 1)

while qu:
    now = qu.popleft()
    for next in A[now]:
        inDegree[next] -= 1

        result[next] = max(result[next], result[now] + build[now])

        if inDegree[next] == 0:
            qu.append(next)

for i in range(1, n + 1):
    print(result[i] + build[i])
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());

        ArrayList<Integer>[] A = new ArrayList[n + 1];
        int[] inDegree = new int[n + 1];
        int[] build = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            A[i] = new ArrayList<>();
        }

        for (int i = 1; i < n + 1; i++) {
            String[] input = br.readLine().split(" ");
            build[i] = Integer.parseInt(input[0]);
            int index = 1;

            while (true) {
                int temp = Integer.parseInt(input[index]);
                index++;

                if (temp == -1) {
                    break;
                }

                A[temp].add(i);
                inDegree[i]++;
            }
        }

        int[] result = new int[n + 1];
        Queue<Integer> qu = new LinkedList<>();

        for (int i = 1; i < n + 1; i++) {
            if (inDegree[i] == 0) {
                qu.add(i);
            }
        }

        while (!qu.isEmpty()) {
            int now = qu.poll();

            for (int next : A[now]) {
                inDegree[next]--;

                result[next] = Math.max(result[next], result[now] + build[now]);
                if (inDegree[next] == 0) {
                    qu.add(next);
                }
            }
        }

        for (int i = 1; i < n + 1; i++) {
            System.out.println(result[i] + build[i]);
        }
    }
}
```