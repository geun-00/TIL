# 세그먼트 트리 예제 - 3

### [문제(백준(11505번 - 구간 곱 구하기))](https://www.acmicpc.net/problem/11505)

### 문제 분석
- 대부분의 세그먼트 트리는 구간 합, 최댓값, 최솟값에 대해 많은 문제가 출제된다.
- 이 문제는 조금은 색다른 구간 곱과 관련된 문제이다.
- 기본 틀은 세그먼트 트리의 다른 문제와 동일하고, 조건에 따라 기존의 알고리즘 코드를 수정할 수 있어야 한다.

### 손으로 풀어보기
1. **1차원 리스트로 트리의 값을 초기화한다. 트리 리스트 크기가 예제 입력 기준 트리 리스트 크기가 `N = 5`이므로 `2^k >= N`을 만족하는 k값은 3이고, 리스트의 크기는
    `2^3 * 2 = 16`이 된다. 시작 인덱스는 `2^3 = 8`이다. 곱셈이기 때문에 초깃값은 1로 저장하고, 부모 노드를 양쪽 자식 노드의 곱으로 표현한다. 이때 MOD 연산을 지속적으로 수행해
    값의 범위가 1,000,000,007을 넘지 않도록 해야 한다.**
2. **질의값 연산 함수와 데이터 업데이트 함수를 수행하고 결괏값을 출력한다. 이때 값을 업데이트하거나 구간 곱을 구하는 각 곱셈마다 모두 MOD 연산을 수행한다.**
   - 곱셈의 성질에 따른 세부 코드가 변경돼야 하며, MOD 연산 로직을 수행해야 한다.
   - **곱셈과 관련된 % 연산의 성질**
   - `(A * B) % C = (A % C) * (B % C) % C`
   - 두 값을 곱셈한 후 % 연산한 결과는 각각 % 연산한 값을 곱해 %로 나눈 것과 동일함

> **업데이트할 때 기존 값이 0일 때**
> - 이 문제에서 고민해야 할 부분은 값 업데이트에서 기존의 값이 0일 때이다.
> - 기존의 값이 0이었다면 이 부모 노드는 모두 0으로 저장돼 있는 상태다. 따라서 기존의 구간 합과 같이 변경된 값을 부모 노드에 적용해도 `0 * 변경된 데이터`의 형태이므로
>   업데이트되지 않는 현상이 발생한다.
> - 따라서 이 부분은 부모 노드의 값을 업데이트할 때 양쪽 자식의 곱으로 업데이트해 주도록 세부 로직을 고민해야 한다.
> - 또한 각 프로세스마다 꼼꼼하게 MOD 연산을 수행하는 것도 잊지 말아야 한다.

### 슈도코드
```text
n(수의 개수) m(수의 변경 횟수) k(구간 곱 구하는 횟수)

treeSize = 1
while 2^treeSize < n:
    treeSize += 1

leafNodeStartIndex = pow(2, treeSize)
tree(인덱스 트리 저장 리스트)

tree 리스트의 리프 노드 영역에 데이터 입력

setTree(인덱스):
    while 인덱스 > 0:
        부모 노드(인덱스/2)에 현재 index의 트리값 곱하고 나머지 연산 후 저장
        인덱스 1 감소

setTree(treeSize - 1)

changValue(인덱스, 변경값):
    현재 인덱스 위치에 변경값 저장
    while 인덱스 > 0:
        시작 인덱스 /= 2
        부모 노드에 두 자식 노드값을 곱하고 나머지 연산 후 저장

getMulitple(시작 인덱스, 종료 인덱스):
    while 시작 인덱스 <= 종료 인덱스:
        if 시작 인덱스 % 2 == 1:
            해당 노드값을 구간 곱 변수에 곱하고 나머지 연산 후 저장
            시작 인덱스 1 증가
        if 종료 인덱스 % 2 == 0:
            해당 노드값을 구간 곱 변수에 곱하고 나머지 연산 후 저장
            종료 인덱스 1 감소   
        
        시작 인덱스 /= 2
        종료 인덱스 /= 2
        
    구간 곱 변수 리턴

for m+k 반복:
    q(질의 유형) s(시작 인덱스) e(종료 인덱스 또는 변경값)
    if q == 1:
        changeValue(트리에서의 시작 인덱스, e)
    else:
        print(getMultiple(트리에서의 시작 인덱스, 트리에서의 종료 인덱스))         
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline
print = sys.stdout.write
n, m, k = map(int, input().split())
MOD = 1_000_000_007

exp = 1
while pow(2, exp) < n:
    exp += 1

treeSize = pow(2, exp) * 2
leafNodeStartIndex = pow(2, exp)
tree = [1] * treeSize

for i in range(leafNodeStartIndex, leafNodeStartIndex + n):
    tree[i] = int(input())


def setTree(index):
    while index > 0:
        tree[index // 2] *= tree[index] % MOD
        index -= 1


setTree(treeSize - 1)


def changeValue(index, value):
    tree[index] = value
    while index > 0:
        index //= 2
        tree[index] = (tree[index * 2] * tree[index * 2 + 1]) % MOD


def getMultiple(s, e):
    temp = 1
    while s <= e:
        if s % 2 == 1:
            temp = temp * tree[s] % MOD
            s += 1
        if e % 2 == 0:
            temp = temp * tree[e] % MOD
            e -= 1

        s //= 2
        e //= 2

    return temp


result = []

for _ in range(m + k):
    q, s, e = map(int, input().split())

    if q == 1:
        s = s + leafNodeStartIndex - 1
        changeValue(s, e)
    else:
        s = s + leafNodeStartIndex - 1
        e = e + leafNodeStartIndex - 1
        result.append(str(getMultiple(s, e)))

print("\n".join(result))
```

### 코드 구현 - 자바
```java
import java.io.*;
import java.util.*;

public class Main {

    static long[] tree;
    static final int MOD = 1_000_000_007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        int exp = 1;
        while (Math.pow(2, exp) < n) {
            exp++;
        }

        int treeSize = (int) Math.pow(2, exp) * 2;
        int leafNodeStartIndex = (int) Math.pow(2, exp);

        tree = new long[treeSize];
        Arrays.fill(tree, 1);

        for (int i = leafNodeStartIndex; i < leafNodeStartIndex + n; i++) {
            tree[i] = Long.parseLong(br.readLine());
        }

        setTree(tree.length - 1);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m + k; i++) {
            st = new StringTokenizer(br.readLine());
            int q = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            long e = Long.parseLong(st.nextToken());

            if (q == 1) {
                changeValue(s + leafNodeStartIndex - 1, e);
            } else if (q == 2) {
                s = s + leafNodeStartIndex - 1;
                e = e + leafNodeStartIndex - 1;
                sb.append(getMultiple(s, (int) e)).append("\n");
            }
        }

        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }

    private static long getMultiple(int s, int e) {
        long temp = 1;

        while (s <= e) {
            if (s % 2 == 1) {
                temp = temp * tree[s] % MOD;
                s++;
            }
            if (e % 2 == 0) {
                temp = temp * tree[e] % MOD;
                e--;
            }
            s /= 2;
            e /= 2;
        }

        return temp;
    }

    private static void changeValue(int index, long value) {
        tree[index] = value;
        while (index > 0) {
            index /= 2;
            tree[index] = tree[index * 2] % MOD * tree[index * 2 + 1] % MOD;
        }
    }

    private static void setTree(int index) {
        while (index > 0) {
            tree[index / 2] *= tree[index] % MOD;
            index--;
        }
    }
}
```