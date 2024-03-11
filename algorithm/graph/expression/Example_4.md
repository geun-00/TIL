# 그래프의 표현 예제 - 4

### [문제(백준(2251번 - 물통))](https://www.acmicpc.net/problem/2251)

### 문제 분석
- 그래프 데이터를 저장하고 저장한 자료구조를 이용하는 방식과 달리, 그래프 원리를 적용해 그래프를 역으로 그리는 방식으로 접근하는 문제다.
- A, B, C의 특정 무게 상태를 1개의 노드로 가정하고, 조건에 따라 이 상태에서 변경할 수 있는 이후 무게 상태가 에지로 이어진 인접한 노드라고 생각하고 문제에 접근한다.


### 손으로 풀어보기
1. **처음에는 앞의 두 물통은 비어있고 C는 꽉 차 있으므로 최초 출발 노드를 `(0, 0, 3번째 물통의 용량)`으로 초기화한다.** 
2. **BFS를 다음 탐색 과정으로 수행한다.**
   1.  노드에서 갈 수 있는 6개의 경우(`A -> B`, `A -> C`, `B -> A`, `B -> C`, `C -> A`, `C -> B`)에 관해 다음 노드로 정해 큐에 추가한다. A, B, C 무게가 동일한
        노드에 방문한 이력이 있을 때는 큐에 추가하지 않는다.
   2. 보내는 물통의 모든 용량을 받는 물통에 저장하고, 보내는 물통에는 0을 저장한다. 단, 받는 물통이 넘칠 때는 초과하는 값만큼 보내는 물통에 남긴다.
   3. 큐에 추가하는 시점에 1번째 물통(A)의 무게가 0일 때가 있으면 3번째 물통(C)의 값을 정답 리스트에 추가한다.
3. **정답 리스트를 오름차순 출력한다.**

### 슈도코드
```text
send, receive(6가지 경우를 탐색하기 위한 리스트)
now(a, b, c의 값 저장)
visit(방문 기록 리스트)
ans(정답 리스트)

BFS:
    큐에 출발 노드 더하기(A와 B가 0인 상태이므로 0, 0노드에서 시작)
    visit 방문 기록
    ans 현재 C값 체크
    
    while 큐가 빌 때까지:
        큐에서 노드 데이터 가져오기
        데이터를 이용해 a, b, c값 초기화
        for 6가지 케이스 반복:
            받는 물통에 보내려는 물통의 값 더하기
            보내려는 물통의 값을 0으로 업데이트
            if 받는 물통이 넘치면:
                넘치는 만큼 보내는 물통에 다시 넣어 주고 받는 물통은 이 물통의 최댓값으로 저장
            if 현재 노드의 연결 노드 중 미 방문 노드:
                큐 데이터 삽입
                visit 방문 처리
                if 1번째 물통이 비어 있으면:
                    3번째 물통의 물의 양을 ans에 기록

BFS 실행

for ans 탐색:
    true인 index 출력
```

### 코드 구현 - 파이썬
```python
# 두 리스트를 이용하여 6가지 이동 케이스 정의
# 0=A, 1=B, 2=C
from collections import deque

send = [0, 0, 1, 1, 2, 2]
receive = [1, 2, 0, 2, 0, 1]

now = list(map(int, input().split()))
visit = [[False for j in range(201)] for i in range(201)]
ans = [False] * 201


def BFS():
    qu = deque()
    qu.append((0, 0))
    visit[0][0] = True
    ans[now[2]] = True

    while qu:
        now_node = qu.popleft()
        A = now_node[0]
        B = now_node[1]
        C = now[2] - A - B

        for i in range(6):
            next = [A, B, C]
            next[receive[i]] += next[send[i]]
            next[send[i]] = 0

            if next[receive[i]] > now[receive[i]]:
                next[send[i]] = next[receive[i]] - now[receive[i]]
                next[receive[i]] = now[receive[i]]

            if not visit[next[0]][next[1]]:
                visit[next[0]][next[1]] = True
                qu.append((next[0], next[1]))

                if next[0] == 0:
                    ans[next[2]] = True


BFS()

for i in range(len(ans)):
    if ans[i]:
        print(i, end=' ')
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static int[] maxCapacity = new int[3]; // 각 물통의 최대 용량
    static boolean[][] visit = new boolean[201][201];
    static boolean[] ans = new boolean[201];
    // 물통으로 옮기는 6가지 케이스 정의
    static int[] send = {0, 0, 1, 1, 2, 2};
    static int[] receive = {1, 2, 0, 2, 1, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        for (int i = 0; i < 3; i++) {
            maxCapacity[i] = Integer.parseInt(st.nextToken());
        }

        BFS();

        for (int i = 0; i < ans.length; i++) {
            if (ans[i]) {
                System.out.print(i + " ");
            }
        }
    }

    private static void BFS() {
        Queue<int[]> qu = new LinkedList<>();
        qu.add(new int[]{0, 0});
        visit[0][0] = true;
        ans[maxCapacity[2]] = true;

        while (!qu.isEmpty()) {
            int[] now_node = qu.poll();
            int A = now_node[0];
            int B = now_node[1];
            int C = maxCapacity[2] - A - B;  //C 물의 양은 A와 B에 넘겨주고 남은 양임

            //6가지 케이스
            //   send -> receive
            // A -> B (0 -> 1) - 0
            // A -> C (0 -> 2) - 1
            // B -> A (1 -> 0) - 2
            // B -> C (1 -> 2) - 3
            // C -> A (2 -> 0) - 4
            // C -> B (2 -> 1) - 5
            for (int i = 0; i < 6; i++) {
                int[] next = {A, B, C};
                next[receive[i]] += next[send[i]]; //물 옮기기
                next[send[i]] = 0; //보내는 쪽 물통 비우기

                if (next[receive[i]] > maxCapacity[receive[i]]) {//받는 쪽에서 물이 넘친다면
                    next[send[i]] = next[receive[i]] - maxCapacity[receive[i]];//보내는 쪽에 받는 쪽이 넘친 만큼 저장
                    next[receive[i]] = maxCapacity[receive[i]];//받는 쪽은 최대 용량으로 저장
                }


                if (!visit[next[0]][next[1]]) { //A와 B의 물의 양으로 방문 체크
                    visit[next[0]][next[1]] = true;
                    qu.add(new int[]{next[0], next[1]});

                    if (next[0] == 0) { //A의 물의 양이 0일 때 C의 물의 양 체크
                        ans[next[2]] = true;
                    }
                }
            }
        }
    }
}
```