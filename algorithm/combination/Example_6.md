# 조합 예제 - 6

### [문제(백준(1722번 - 순열의 순서))](https://www.acmicpc.net/problem/1722)

### 문제 분석
- 조합 문제와는 다르게 순열의 개념을 알아야 풀 수 있다.
- 순열은 순서가 다르면 다른 경우의 수로 인정한다. N자리로 만들 수 있는 순열의 경우의 수를 구해야 한다는 것이 문제의 핵심이다.
- 가장 먼저 각 자리에서 사용할 수 있는 경우의 수를 구한다.(예: N = 4)
  - **1번째 자리** : 앞에서부터 채운다고 가정하면 제일 앞에서 사용할 수 있는 수는 4가지다.(1, 2, 3, 4)
  - **2번째 자리** : 앞자리에서 사용한 수를 제외한 수를 사용할 수 있으므로 사용할 수 있는 수는 3가지
  - **3번째 자리** : 앞자리에서 사용한 수 2개를 제외한 수를 사용할 수 있으므로 사용할 수 있는 수는 2가지
  - **4번째 자리** : 앞자리에서 사용한 수 3개를 제외한 수를 사용할 수 있으므로 사용할 수 있는 수는 1가지
- 이렇게 각 자리에서 구한 경우의 수를 모두 곱하면 모든 경우의 수가 나온다.
- 4자리로 표현되는 모든 경우의 수는 `4 * 3 * 2 * 1 = 4! = 24`이다.
- 이를 일반화하면 N자리로 만들 수 있는 순열의 모든 경우의 수는 `N!`이라는 것을 알 수 있다.


### 손으로 풀어보기
1. **자릿수에 따른 순열의 경우의 수를 1부터 N자리까지 미리 계산한다.**
2. **소문제 1, K번째 순열 출력하기**
   - **K번째 순열 출력하기**
     1. 주어진 값(K)과 현재 자리수(N) - 1에서 만들 수 있는 경우의 수를 비교한다.
     2. a번에서 K가 더 작아질 때까지 경우의 수를 배수(cnt)로 증가시킨다.(순열의 순서를 1씩 늘림)
     3. K가 더 작아지면 순열에 값을 저장하고 K를 `K = K - (경우의 수 * (cnt - 1))`로 업데이트한다.
     4. 순열이 완성될 때까지 a ~ c 과정을 반복하고 완료된 순열을 출력한다.

3. **소문제 2, 입력된 순열의 순서 구하기**
   - **입력된 순열의 순서 구하기**
     1. 현재 자릿수의 숫자를 확인하고 해당 숫자보다 앞 숫자 중 미사용 숫자를 카운트한다.
     2. `미사용 숫자 개수 * (현재 자리 - 1에서 만들 수 있는 순열의 개수)`를 K에 더한다.
     3. 모든 자릿수에 관해 과정 a ~ b를 반복하고 K 값을 출력한다.

### 슈도코드
```text
factorial(자리별로 만들 수 있는 경우의 수 : 팩토리얼 형태)
permut(순열 리스트)
visit(숫자 사용 여부 리스트)
n(순열의 길이)

# factorial 초기화
for i 1 ~ n:
    factorial[i] = factorial[i - 1] * i

data(문제 종류 및 순열 데이터 입력)

if data[0] == 1:    # 순열 출력 문제
    k(몇 번째 순열을 출력할지)  # 길이가 n인 순열의 k번째 순서의 순열
    for i n만큼:
        cnt = 1     # 해당 자리에서 몇 번째 숫자를 사용해야 할지를 정하는 변수
        for j n만큼:
            이미 사용한 숫자는 pass
            if 현재 순서 < 해당 자리 순열 수 * cnt:
                현재 순서 = 현재 순서 - 해당 자리 순열 수 * (cnt - 1)
                현재 자리(permut[i])에 j값 저장
                숫자 j 사용 숫자 표시
                반복문 종료
            cnt 1 증가
    permut 출력
else:   # 순열의 순서를 출력하는 문제
    k(순열의 순서 저장 변수)
    for i n만큼:
        cnt(0으로 초기화)
        for j data[i]의 수만큼:
            if 미사용 숫자:
                cnt 1 증가    # 미사용 숫자 1 증가
        
        k = k + cnt * 현재 자릿수에서 만들 수 있는 경우의 수
        data[i]번째 숫자를 사용 숫자로 변경
    
    k 출력
```

### 코드 구현 - 파이썬
```python
factorial = [0] * 21
permut = [0] * 21
visit = [False] * 21
n = int(input())

factorial[0] = 1
for i in range(1, n + 1):
    factorial[i] = factorial[i - 1] * i

data = list(map(int, input().split()))

if data[0] == 1:
    k = data[1]
    for i in range(1, n + 1):
        cnt = 1
        for j in range(1, n + 1):
            if not visit[j]:            # N자리의 순열을 정할 때는 N-1번째 순열의 경우의 수를 이용한다.
                # 주어진 k에 따라 각 자리에 들어갈 수 있는 수 찾기
                if k <= cnt * factorial[n - i]:     # 주어진 값(k)과 현재 자리 - 1에서 만들 수 있는 경우의 수 비교
                    k -= factorial[n - i] * (cnt - 1)   # k가 더 작아지면 k를 k = k - 경우의 수 * (cnt - 1)로 업데이트한다.
                    permut[i] = j       # k가 더 작아지면 순열에 값을 저장한다.
                    visit[j] = True
                    break
                cnt += 1

    for i in range(1, n + 1):
        print(permut[i], end=' ')

else:
    k = 1
    for i in range(1, n + 1):
        cnt = 0
        for j in range(1, data[i]):
            if not visit[j]:        # 현재 자릿수의 숫자를 확인하고 해당 숫자보다 앞 숫자 중 미사용 숫자를 카운트
                cnt += 1
        k += cnt * factorial[n - i]     # 미사용 숫자 * (현재 자리 - 1에서 만들 수 있는 순열의 개수) 를 k에 더한다.
        visit[data[i]] = True

    print(k)
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

        //각 자리마다 가능한 경우의 수
        long[] factorial = new long[21]; //팩토리얼을 진행하면서 값이 커지므로 long 사용
        boolean[] visit = new boolean[21];

        int n = Integer.parseInt(br.readLine());
        int[] result = new int[n];
        factorial[0] = 1;

        for (int i = 1; i < 21; i++) {
            factorial[i] = factorial[i - 1] * i;
        }

        StringTokenizer st = new StringTokenizer(br.readLine());
        int num = Integer.parseInt(st.nextToken());

        //k번째 수열 찾기
        if (num == 1) {
            // 최대 20!로 long으로 받아야 함
            long k = Long.parseLong(st.nextToken());

            //n자리 수 탐색
            for (int i = 0; i < n; i++) {

                for (int j = 1; j < n + 1; j++) {
                    if (visit[j]) { //이미 사용한 숫자는 continue
                        continue;
                    }
                    //각 자리의 경우의 수는 이전 자리의 경우의 수에 영향을 받음
                    if (factorial[n - i - 1] < k) { //이전 자리의 경우의 수와 k를 비교
                        /**
                         * k가 더 크다는 것은
                         * 현재 자리에 해당하는 숫자를 결정할 떄,
                         * 남은 경우의 수보다 큰 순서를 찾아야 한다는 것을 의미
                         * --------------------------------------------------
                         * k가 더 큰 경우 이전 자리의 경우의 수를 빼는 이유
                         * 대상 자리에 대한 경우의 수를 결정할 떄 이미 이전 자리에서 고려되었던 경우의 수를 제외하고
                         * 새로운 경우의 수를 고려해야하기 때문
                         */
                        k -= factorial[n - i - 1];
                    } else {
                        /**
                         * k가 더 작다는 것은
                         * 현재 자리에 해당하는 숫자를 결정할 때,
                         * 이미 이전 자리에서 고려되었던 경우의 수를 포함하여 현재 자리에 올 수 있는 숫자의 범위 안에서 결정할 수 있다는 것을 의미
                         */
                        result[i] = j;
                        visit[j] = true;
                        break;
                    }
                }
            }

            for (int i = 0; i < n; i++) {
                System.out.print(result[i] + " ");
            }

        //몇 번째 수열인지
        } else {
            long k = 1L; //순열의 순서

            int[] arr = new int[n]; //순열을 구성하는 각 숫자
            for (int i = 0; i < n ; i++) {
                arr[i] = Integer.parseInt(st.nextToken());
            }

            for (int i = 0; i < n; i++) {
                for (int j = 1; j < arr[i]; j++) { //각 자리마다 해당 숫자가 순열에서 몇 번째로 작은 숫자인지를 계산
                    if (!visit[j]) {
                        k += factorial[n - i - 1];
                    }
                }
                visit[arr[i]] = true;
            }

            System.out.println(k);
        }
    }
}
```