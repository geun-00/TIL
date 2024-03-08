# 이진 탐색 예제 - 2

### [문제(백준(2343번 - 기타 레슨))](https://www.acmicpc.net/problem/2343)

### 문제 분석
- **블루레이의 크기가 모두 같고 녹화 순서가 바뀌면 안 된다**라는 문제 조건이 이진 탐색 알고리즘을 선택하게 하는 실마리이다.
- 블루레이에 첫 레슨부터 마지막 레슨까지 차례대로 저장하다보면 지정한 블루레이 크기로 모든 레슨을 저장할 수 있는지 없는지 판단할 수 있기 때문이다.
- 모두 저장할 수 있다면 블루레이 크기를 줄이고 저장할 수 없다면 블루레이 크기를 늘리는 방식으로 블루레이 크기의 최솟값을 알 수 있다.

### 손으로 풀어보기
1. **이진 탐색의 시작 인덱스는 최대 길이의 레슨이고 종료 인덱스는 모든 레슨 길이의 합이다. 예제에서 시작 인덱스는 9, 종료 인덱스는 45가 된다. 
    블루레이 크기가 3일 때 9~45 사이에서 블루레이 크기의 최솟값을 이진 탐색으로 찾으면 된다.**
2. **이진 탐색을 수행한다. `시작 인덱스 > 종료 인덱스`까지 수행한다.**
: - 중앙값 크기로 모든 레슨을 저장할 수 있으면 `종료 인덱스 = 중앙값 - 1`(왼쪽 데이터셋)
: - 중앙값 크기로 모든 레슨을 저장할 수 없으면 `시작 인덱스 = 중앙값 + 1`(오른쪽 데티어셋)

    1. 1번째 탐색에서 중앙값은 `(9 + 45) / 2 = 27` 이다. 크기가 27인 블루레이에 강의를 1부터 9까지 차례대로 저장할 때 총 몇 장의 블루레이가 필요한지 확인한다.<br>
       `1 ~ 6`, `7 ~ 9` 2장의 블루레이에 모든 레슨을 저장할 수 있다. 입력에서 주어진 블루레이 개수 3장 이내에 모든 레슨을 저장할 수 있으므로 종료 인덱스를 `27 - 1 = 26`으로 수정한 뒤 다시 탐색을 수행한다.
    2. 2번째 탐색에서 중앙값은 `(9 + 26) / 2 = 17` 이다. `1 ~ 5`, `6 ~ 7`, `8 ~ 9` 총 3장의 블루레이에 모든 강의가 담긴다. 다시 종료 인덱스를 `17 - 1 = 16`으로 수정한 뒤 탐색을 수행한다.
    3. 3번째 탐색에서 중앙값은 `(9 + 16) / 2 = 12` 이다. `1 ~ 4`, `5 ~ 6`, `7`, `8`, `9` 총 5장의 블루레이에 모든 강의가 담긴다. 3장으로 모든 레슨을 저장할 수 없으므로
       시작 인덱스를 `12 + 1 = 13`으로 수정한 뒤 다시 탐색을 수행한다.
   4. **위 방식으로 이진 탐색을 진행하다가 `시작 인덱스 > 종료 인덱스` 조건이 만족할 때 이진 탐색을 종료하면 시작 인덱스가 `17`이 되는데, 이 값이 문제의 조건을 만족하는 블루레이의 크기의 최솟값이 된다.**

### 슈도코드
```text
n(강의 수) m(블루레이 개수)
A(기타 레슨 데이터 저장 리스트)
start(시작 인덱스)
end(종료 인덱스)

for A 탐색:
    시작 인덱스 저장(A 리스트 중 최댓값)
    종료 인덱스 저장(A 리스트의 총합)

while 시작 인덱스 <= 종료 인덱스:
    mid(중간 인덱스)
    sum(레슨 합)
    count(현재 사용한 블루레이 개수)
    for n 반복:
        if sum + 현재 레슨 시간 > 중간 인덱스:
            count 값을 올리고 sum 0으로 리셋  # 현재 블루레이에 저장할 수 없어 새로운 블루레이로 교체한다는 의미
        sum에 현재 레슨 시간값 더하기
    sum이 0이 아니면 마지막 블루레이가 필요하므로 count 증가
    
    if count > m:   # 중간 인덱스 값으로 모든 레슨 저장 불가능
        시작 인덱스 = 중간 인덱스 + 1
    else:           # 중간 인덱스 값으로 모든 레슨 저장 가능
        종료 인덱스 = 중간 인덱스 - 1
        
시작 인덱스 출력
```

### 코드 구현 - 파이썬
```python
n, m = map(int, input().split())
A = list(map(int, input().split()))
start = 0
end = 0

for i in A:
    if i > start:
        start = i
    end += i

while end >= start:
    mid = int((start + end) / 2)
    sum = 0
    count = 0
    for i in range(n):
        if sum + A[i] > mid:
            count += 1
            sum = 0
        sum += A[i]

    if sum != 0:
        count += 1

    if count > m:
        start = mid + 1
    else:
        end = mid - 1

print(start)
```

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

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int[] arr = new int[n];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        int start = 0;
        int end = 0;

        for (int num : arr) {
            if (num > start) { //시작 인덱스 최댓값 갱신
                start = num;
            }
            end += num;  //종료 인덱스 갱신
        }

        while (end >= start) {
            int mid = (start + end) / 2;
            int sum = 0;
            int count = 0;

            for (int i = 0; i < n; i++) {
                if (sum + arr[i] > mid) {
                    count++;
                    sum = 0;
                }
                sum += arr[i];
            }
            if (sum > 0) {
                count++;
            }
            if (count > m) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        System.out.println(start);
    }
}
```