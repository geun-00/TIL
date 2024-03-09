# 소수 구하기 예제 - 4

### [문제(백준(1016번 - 제곱 ㄴㄴ 수))](https://www.acmicpc.net/problem/1016)

### 문제 분석
- `min`의 최댓값이 `10^14`으로 매우 큰 것 같지만 실제로는 `min`과 `max`사이의 수들 안에서 구하는 것이므로 최대 1,000,000의 데이터만 확인하면 된다.
- 제곱수 판별을 일반적인 반복문으로 구하면 시간 초과가 발생하므로 에라토스테네스의 체 알고리즘 방식으로 문제를 해결한다.

### 손으로 풀어보기
1. **2부터 제곱수를 max 사이에서 찾는다.**
2. **탐색한 리스트에서 제곱수로 확인되지 않은 수의 개수를 센 후 출력한다.**

- **데이터를 순차적으로 탐색하는 것이 아니라 에라토스테네스의 체 방식으로 제곱수의 배수 형태로 탐색해 시간 복잡도를 최소화하는 것이 이 문제 풀이의 핵심이다.**

### 슈도코드
```text
min(최솟값) max(최댓값)
check(min ~ max 사이에 제곱수 판별 리스트)

for i 2~max의 제곱근:
    temp(제곱수)
    start_index(최솟값/제곱수)  # 나머지가 있는 경우 1 더함
    for j start_index ~ max:
        j * temp < max 일떄 최솟값, 최댓값 사이의 제곱수이므로
        check 리스트에 저장

count(제곱 ㄴㄴ수 카운티)
for 0~max-min:
    check 리스트에서 제곱이 아닌 수라면 count 증가

count 출력
```

### 코드 구현 - 파이썬
```python
import math

Min, Max = map(int, input().split())

check = [False] * (Max - Min + 1)

for i in range(2, int(math.sqrt(Max) + 1)):
    temp = i * i
    start_index = int(Min / temp)
    if Min % temp != 0:
        start_index += 1

    for j in range(start_index, int(Max / temp) + 1):
        check[j * temp - Min] = True

count = 0
for i in check:
    if not i:
        count += 1

print(count)
```
- `Min % temp != 0`이면 `start_index`를 1 더하는 이유
  - `Min % temp == 0`이면 현재 `Min`값은 현재 제곱수로 나누어 떨어진다는 것을 의미한다.
  - 따라서 `Min` 이상에서 가장 작은 제곱수의 값에서 시작할 수 있다.
  - `Min % temp != 0`이면 현재 `Min`값은 현재 제곱수로 나누어 떨어지지 않다는 것을 의미한다.
  - 따라서 `Min`보다 크거나 같으면서 현재 제곱수로 나누어지는 가장 작은 값으로 시작하기 위해 1을 더해준다.
- 안쪽 for문 `Max / temp`까지만 하는 이유
  - temp(제곱수)의 배수가 `Max`를 넘어가는 값은 체크할 필요가 없다.
- `check[j * temp - Min] = True` 
  - 배열 인덱스 범위를 맞추기 위해 `Min`을 빼준다.

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        long min = Long.parseLong(st.nextToken());
        long max = Long.parseLong(st.nextToken());

        boolean[] check = new boolean[(int) (max - min + 1)];

        for (long i = 2; i <= Math.sqrt(max); i++) {
            long temp =  i * i;
            long start_index = min / temp;
            if (min % temp != 0) {
                start_index += 1;
            }
            for (long j = start_index; j <= max / temp; j++) {
                check[(int) (j * temp - min)] = true;
            }
        }

        int count = 0;
        for (boolean b : check) {
            if (!b) {
                count++;
            }
        }
        System.out.println(count);
    }
}
```