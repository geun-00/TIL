# 이진 탐색 예제 - 3

### [문제(백준(1300번 - K번째 수))](https://www.acmicpc.net/problem/1300)

### 문제 분석
- `k`의 범위가 `1 ~ min(10^9, N^2)`이므로 시간 복잡도가 `N^2`인 알고리즘은 사용할 수 없다.
- 이진 탐색으로 중앙값보다 작은 수의 개수를 세면서 범위를 절반씩 줄이는 방법으로 `B[k]`를 구한다.
- 즉 작은 수의 개수가 `k - 1`개인 중앙값이 정답이 된다.
- 작은 수의 개수를 세는 아이디어가 이 문제를 푸는 열쇠가 된다.

### 손으로 풀어보기
- **2차원 리스트는 `N`행이 `N`의 배수로 구성되어 있으므로 2차원 리스트에서의 `k`번째수는 `k`를 넘지 않는다. 즉 2차원 리스트의 `1 ~ k`번째 안에 정답이 있다.
    이진 탐색의 시작 인덱스를 `1`, 종료 인덱스를 `k`로 정한다.**

|1|2| 3 |
|--|--|---|
|2|4|6|
|3|6|9|

- 예제 `N = 3`, `k = 7`일 때 정답을 구해보자.
- 최초의 중앙값은 `(1 + 7) / 2 = 4`가 된다. 각 행에서 중앙값보다 작거나 같은 수의 개수는 중앙값을 각 행의 첫 번째 값으로 나눈 값이다.
  - 단, 나눈 값이 `N`보다 크면 `N`으로 정한다.
- 1열은 1로 나눠 4이지만 `N`보다 크므로 `3`, 2열은 2로 나눠 `2`, 3열은 3으로 나눠 `1`을 얻고, 그 결과 각 열에서 중앙값 4보다 작은 수의 개수는 `3 + 2 + 1 = 6`개가 된다.
- 중앙값보다 작거나 같은 수의 개수는 `min(mid / i, N)`으로 계산한다.
- 이를 통해 중앙값 `4`는 6번째 수보다 큰 수가 될 수 없다는 것을 알 수 있으며, 중앙값 `4`보다 큰 범위에 정답이 있다는 것을 유추할 수 있다.
- **정리하면**, **다음 이진 탐색 조건**으로 정답을 중앙값으로 업데이트하며 `시작 인덱스 > 종료 인덱스`까지 이진 탐색을 진행한다.
  - 중앙값 크기보다 작은 수가 `k`보다 작으면 `시작 인덱스 = 중앙값 + 1`
  - 중앙값 크기보다 작은 수가 `k`보다 크거나 같으면 `종료 인덱스 = 중앙값 - 1`, `정답 변수 = 중앙값` 

**위 조건으로 `N = 3`, `k = 7`을 찾는 과정**
- 1번째 중앙값은 `(1 + 7) / 2 = 4`이며 4보다 작거나 같은 수의 개수는 `6 < k`이므로 시작 인덱스를 `중앙값 + 1 = 5`, 종료 인덱스를 `7`로 하고 정답 업데이트는 하지 않는다.
- 2번째 중앙값은 `(5 + 7) / 2 = 6`이며 6보다 작거나 같은 수의 개수는 `8 > k`이므로 시작 인덱스를 5, 종료 인덱스를 `중앙값 - 1 = 5`로 하고 정답을 6으로 업데이트 한다.
- 아직 `시작 인덱스 > 종료 인덱스`조건에 맞지 않으므로 계속 이진 탐색을 진행한다.
- 3번째 중앙값은 `(5 + 5) / 2 = 5`이며 5보다 작거나 같은 수의 개수는 `6 < k`이므로 시작 인덱스를 `중앙값 + 1 = 6`, 종료 인덱스를 `5`로 한다.
- `시작 인덱스 > 종료 인덱스`이므로 이진 탐색을 종료한다.
- 이 과정에서 업데이트한 정답은 6이므로 정답은 6이 된다.

### 슈도코드
```text
N(리스트 크기), k(구하고자 하는 index)
start(시작 인덱스 = 1)
end(종료 인덱스 = k)

while 시작 인덱스 <= 종료 인덱스:
    mid(중간 인덱스)
    count(중앙값보다 작은 수)
    
    for i 1~N:
        count에 중앙 인덱스를 i로 나눈 값과 N 중 작은 값을 더함
    
    if count < k:
        시작 인덱스 = 중앙 인덱스 + 1
    else:
        종료 인덱스 = 중앙 인덱스 - 1
        정답 변수에 중앙값 저장
        
정답 출력
```

### 코드 구현 - 파이썬
```python
n = int(input())
k = int(input())

start = 1
end = k
result = 0

while start <= end:
    mid = int((start + end) / 2)
    count = 0  # 중앙값보다 작은 수의 개수

    for i in range(1, n + 1):  # 중앙값보다 작은 수 계산
        count += min(int(mid / i), n)

    if count < k:
        start = mid + 1
    else:
        result = mid
        end = mid - 1

print(result)
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int k = Integer.parseInt(br.readLine());

        int start = 1;
        int end = k;

        int result = 0;

        while (start <= end) {
            int mid = (start + end) / 2;
            int count = 0;

            for (int i = 1; i < n + 1; i++) {
                count += Math.min(mid / i, n);
            }

            if (k > count) {
                start = mid + 1;
            } else {
                result = mid;
                end = mid - 1;
            }
        }
        System.out.println(result);
    }
}
```