# 조합 예제 - 5

### [문제(백준(13251번 - 조약돌 꺼내기))](https://www.acmicpc.net/problem/13251)

### 문제 분석
- 단순한 조합을 이용해 색깔별 조약돌의 개수에서 K개를 뽑을 수 있는 경우의 수를 구한 후, 전체 돌에 관해 n개를 뽑는 경우의 수를
    나눠도 이 문제를 해결할 수 있다.(`(aCk + bCk + cCk +...)` / `nCk` (`n = a+b+c+...`))
- 하지만 단순하게 확률식을 세워서 계산해도 풀 수 있다.
- 알고 있는 알고리즘에 문제를 맞추려 하지 말고 넓은 시야로 문제를 분석할 수 있어야 한다.

### 손으로 풀어보기
1. **색깔별 조약돌의 개수를 배열에 저장하고, 전체 조약돌 개수를 변수에 저장한다.**
   - 예제 입력 3 기준 : `5 + 6 + 7 = 18` 
2. **한 색깔의 조약돌만 뽑을 확률을 색깔별로 모두 구한다. `k = 2`이므로 2번 뽑을 동안 같은 색이 나올 확률을 구하면 된다.**
   - 5개의 조약돌이 있는 색깔만 뽑을 확률 : `5 / 18` * `4 / 17`
   - 6개의 조약돌이 있는 색깔만 뽑을 확률 : `6 / 18` * `5 / 17`
   - 7개의 조약돌이 있는 색깔만 뽑을 확률 : `7 / 18` * `6 / 17`

3. **각각의 확률을 더해 정답을 출력한다.**


### 슈도코드
```text
stone(색깔별 조약돌 개수 저장 리스트)
probability(색깔별 확률 저장 리스트)
m(색의 종류)
sum(전체 조약돌 개수)

for m 반복:
    sum에 조약돌 개수 더하기

k(선택한 조약돌 개수)

for m 반복:
    if 현재 색깔의 조약돌의 개수가 선택해야 할 개수보다 크면:      # 선택한 조약돌 개수(k)보다 현재 색 조약돌 개수가 적으면 모두 같은 색으로 뽑힐 확률은 0
        probability[i]를 1로 저장  # 확률 초기 값
        for j k만큼:
            i 색깔을 모두 뽑을 확률 = i 색깔을 모두 뽑을 확률 * (현재 색깔 개수 - j) / (전체 색깔 개수 - j)
        정답에 현재 색깔을 모두 뽑을 확률(probability[i])을 더하기

정답 출력
```

### 코드 구현 - 파이썬
```python
m = int(input())  # 색깔 개수
stone = list(map(int, input().split()))  # 색깔별 조약돌 개수
probability = [0] * 51  # 색깔별 확률
sum = 0  # 전체 조약돌 개수

for i in range(m):
    sum += stone[i]

k = int(input())  # 선택한 조약돌 개수

result = 0
for i in range(m):
    if stone[i] >= k:  # 최소한 조약돌이 선택한 조약돌의 개수보다 크거나 같아야 뽑힐 확률이 있다.
        probability[i] = 1
        
        for j in range(k):
            probability[i] *= (stone[i] - j) / (sum - j)

        result += probability[i]

print(result)
```

### 코드 구현 - 자바
```java
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int m = Integer.parseInt(br.readLine());
        
        int[] stone = Arrays.stream(br.readLine().split(" "))
                            .mapToInt(Integer::parseInt)
                            .toArray();
        
        double[] probability = new double[51];

        int sum = 0;
        for (int i = 0; i < m; i++) {
            sum += stone[i];
        }

        int k = Integer.parseInt(br.readLine());

        double result = 0;
        for (int i = 0; i < m; i++) {
            if (stone[i] >= k) {
                probability[i] = 1;

                for (int j = 0; j < k; j++) {
                    probability[i] *= (double) (stone[i] - j) / (sum - j);
                }
                result += probability[i];
            }
        }
        System.out.println(result);

    }
}
```