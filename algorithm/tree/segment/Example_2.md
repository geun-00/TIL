# 세그먼트 트리 예제 - 2

### [문제(백준(10868번 - 최솟값))](https://www.acmicpc.net/problem/10868)

### 문제 분석
- 전형적인 세그먼트 트리 문제이다.
- 데이터를 변경하는 부분이 없기 때문에 1차원 리스트에 최솟값 기준으로 데이터를 저장하고, 질의를 수행하는 함수까지만 구현한다.

### 손으로 풀어보기
1. **1차원 리스트로 트리의 값을 최솟값 기준으로 초기화한다. 예제 입력 기준으로 트리 리스트 크기가 `N = 10` 이므로 `2^k >= N`을 만족하는 k의 값은 4이고,
    리스트의 크기는 `2^4 * 2 = 32`가 된다. 시작 인덱스는 `2^4 = 16`이 된다.**
2. **질의값 연산 함수를 수행하고, 결괏값을 출력한다.**

### 슈도코드
```text
n(수의 개수) m(질의 개수)

treeSize = 1
while 2^treeSize < n:
    treeSize += 1

leafNodeStartIndex = pow(2, treeSize)
tree(인덱스 트리 저장 리스트)

tree 리스트의 리프 노드 영역에 데이터 입력

setTree(index):
    while 인덱스가 루트가 아닐 때까지:
        if 트리[인덱스/2] 값보다 트리[인덱스]가 더 작으면
            트리[인덱스/2] = 트리[인덱스]
        인덱스 1 감소

setTree(tree 리스트 길이 - 1)

getMin(시작 인덱스, 종료 인덱스):
    min(범위의 최솟값을 나타내는 변수, 처음에는 MAX로 초기화)
    
    while 시작 인덱스 <= 종료 인덱스:
        if 시작 인덱스 % 2 == 1:
            min = min(min, 트리[현재 인덱스])
            시작 인덱스 1 증가
        if 종료 인덱스 % 2 == 0
            min = min(min, 트리[현재 인덱스])
            종료 인덱스 1 감소
        
        시작 인덱스 /= 2
        종료 인덱스 /= 2
    
    min 리턴

for m 반복:
    s(시작 인덱스) e(종료 인덱스)
    print(getMin(트리에서 시작 인덱스, 트리에서 종료 인덱스))
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline

n, m = map(int, input().split())

treeSize = 1

while pow(2, treeSize) < n:
    treeSize += 1

leafNodeStartIndex = pow(2, treeSize)
tree = [sys.maxsize] * (pow(2, treeSize) * 2)

for i in range(leafNodeStartIndex, leafNodeStartIndex + n):
    tree[i] = int(input())


def setTree(index):
    while index > 0:
        if tree[index] < tree[index // 2]:
            tree[index // 2] = tree[index]
        index -= 1


setTree(len(tree) - 1)


def getMin(s, e):
    Min = sys.maxsize
    while s <= e:
        if s % 2 == 1:
            Min = min(Min, tree[s])
            s += 1
        if e % 2 == 0:
            Min = min(Min, tree[e])
            e -= 1

        s //= 2
        e //= 2

    return Min


result = []
for _ in range(m):
    start, end = map(int, input().split())
    start = start + leafNodeStartIndex - 1
    end = end + leafNodeStartIndex - 1
    result.append(str(getMin(start, end)))

print("\n".join(result))
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static int[] tree;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        int k = 1;
        while (Math.pow(2, k) < n) {
            k++;
        }

        int treeSize = (int) (Math.pow(2, k) * 2);
        int leafNodeStartIndex = (int) Math.pow(2, k);

        tree = new int[treeSize];
        Arrays.fill(tree, Integer.MAX_VALUE);

        for (int i = leafNodeStartIndex; i < leafNodeStartIndex + n; i++) {
            tree[i] = Integer.parseInt(br.readLine());
        }

        setTree(treeSize - 1);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            int s = a + leafNodeStartIndex - 1;
            int e = b + leafNodeStartIndex - 1;
            sb.append(getMin(s, e)).append("\n");
        }

        System.out.println(sb);
    }

    private static int getMin(int s, int e) {
        int min = Integer.MAX_VALUE;

        while (s <= e) {
            if (s % 2 == 1) {
                min = Math.min(min, tree[s]);
                s++;
            }
            if (e % 2 == 0) {
                min = Math.min(min, tree[e]);
                e--;
            }
            s /= 2;
            e /= 2;
        }
        return min;
    }


    private static void setTree(int index) {
        while (index > 0) {
            if (tree[index / 2] > tree[index]) {
                tree[index / 2] = tree[index];
            }
            index--;
        }
    }
}
```