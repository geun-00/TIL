# 그리디 알고리즘 예제 - 3

### [문제(백준(1744번 - 수 묶기))](https://www.acmicpc.net/problem/1744)

### 문제 분석
- `N`의 범위가 작으므로 시간 복잡도와 관련된 제약은 적은 문제다.
- 가능한 한 큰 수들끼리 묶어야 결괏값이 커진다는 것을 알 수 있다.
- 음수끼리 곱하면 양수로 변하는 성질도 고려하여 문제를 해결한다.

### 손으로 풀어보기
1. **수의 집합을 1보다 큰 수, 1, 0, 음수 4가지 유형으로 나눠 저장한다.**
2. **양수의 집합을 정렬해 최댓값부터 차례대로 곱합 후에 더한다. 원소의 개수가 홀수일 때 마지막 남은 수는 그대로 더한다.**
3. **음수의 집합을 정렬해 최솟값부터 차례대로 곱한 후에 더한다. 원소의 개수가 홀수일 때 수열에 0이 있다면 1개 남은 음수는 0과 곱해 0을 만들고, 수열에 0이 없다면 그대로 더한다.**
4. **과정 2 ~ 3에서 구한 값에 숫자 1의 개수를 더해주면 답이 된다.**

### 슈도코드
```text
n(수열의 크기)
pluspq(양수 우선순위 큐) minuspq(음수 우선순위 큐)
one(1의 개수) zero(0의 개수)

for n 반복:
    데이터를 4개의 그룹에 분리 저장
    # 양수 우선순위 큐는 내림차순 정렬을 위해 -1을 곱하여 저장
    
while 양수 우선순위 큐 크기가 1개가 될 때까지:
    수 2개를 큐에서 뽑음(뽑은 수가 -1을 곱해줌)
    2개의 수를 곱한 값을 결괏값에 더함
    
if 양수 우선순위 큐에 데이터가 남아 있으면:
    해당 데이터를 결괏값에 더함
    
while 음수 우선순위 큐 크기가 2보다 작을 때까지:
    수 2개를 큐에서 뽑음
    2개의 수를 곱한 값을 결괏값에 더함

if 양수 우선순위 큐에 데이터가 남아 있고, 데이터 0이 1개도 없으면:
    해당 데이터를 결괏값에 더함
    
숫자 1의 개수를 결괏값에 더함
결괏값 출력
```

### 코드 구현 - 파이썬
```python
from queue import PriorityQueue

n = int(input())
plusPq = PriorityQueue()
minusPq = PriorityQueue()
one = 0
zero = 0

for _ in range(n):
    num = int(input())

    if num > 1:
        plusPq.put(num * -1)
    elif num == 1:
        one += 1
    elif num == 0:
        zero += 1
    else:
        minusPq.put(num)

sum = 0

while plusPq.qsize() > 1:
    d1 = plusPq.get() * -1
    d2 = plusPq.get() * -1
    sum += d1 * d2

if plusPq.qsize() > 0:
    sum += plusPq.get() * -1

while minusPq.qsize() > 1:
    d1 = minusPq.get()
    d2 = minusPq.get()
    sum += d1 * d2

if minusPq.qsize() > 0:
    if zero == 0:
        sum += minusPq.get()

sum += one

print(sum)
```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        Queue<Integer> plusPq = new PriorityQueue<>(Collections.reverseOrder());  //최대힙
        Queue<Integer> minusPq = new PriorityQueue<>();
        int one = 0;
        int zero = 0;

        for (int i = 0; i < n; i++) {
            int num = Integer.parseInt(br.readLine());
            if (num > 1) {
                plusPq.add(num);
            } else if (num == 1) {
                one++;
            } else if (num == 0) {
                zero++;
            } else {
                minusPq.add(num);
            }
        }

        int sum = 0;
        while (plusPq.size() > 1) {
            int d1 = plusPq.poll();
            int d2 = plusPq.poll();

            sum += d1 * d2;
        }
        
        if (!plusPq.isEmpty()) {
            sum += plusPq.poll();
        }

        while (minusPq.size() > 1) {
            int d1 = minusPq.poll();
            int d2 = minusPq.poll();

            sum += d1 * d2;
        }

        //위 과정이 끝나면 데이터가 무조건 1개 또는 0개가 남는다.
        //음수를 더하는 것보다 0을 곱해서 0을 더하는 것이 큰 수를 만들기에 유리하므로
        //수열에 0이 하나라도 있으면 0을 더하는 것과 같기 때문에 결과에는 영향이 없다.
        //그래서 0이 없을 때만 결과에 음수를 더해준다.
        if (!minusPq.isEmpty()) {
            if (zero == 0) {
                sum += minusPq.poll();
            }
        }

        sum += one;
        System.out.println(sum);
    }
}
```