# 스택 예제 - 1

### [예제 문제(백준(1874번 - 스택 수열))](https://www.acmicpc.net/problem/1874)

### 문제 분석
- 스택의 원리를 정확하게 알고 있는지를 묻는 문제다.
- 스택의 `pop`, `push` 연산과 후입선출 개념을 이해한다면 쉽게 풀 수 있다.
- 스택에 넣는 값은 오름차순 정렬이어야 한다.

### 손으로 풀어보기
1. **`1`부터 자연수를 증가시키면서 입력으로 주어진 숫자와 비교하여 증가시킨 자연수를 스택에 추가하거나 빼는 방식으로 풀면 된다.**
   - **스택 연산 수행 방법**
     1. `현재 수열 값 >= 자연수`
     : 현재 수열 값이 자연수보다 크거나 같을 때까지 자연수를 1씩 증가시키며 자연수를 스택에 `push`한다. 
     : 그리고 `push`가 끝나면 수열을 출력하기 위해 마지막 1회만 `pop`한다.
     : 예를 들어 현재 수열 값이 4면 스택에는 1, 2, 3, 4를 `push`하고 마지막에 1회만 `pop`하여 4를 출력한 뒤 조건문을 빠져나온다. 그리고 자연수는 5가 된다. 
     
     2. `현재 수열 값 <= 자연수`
     : 현재 수열 값보다 자연수가 크다면 `pop`으로 스택에 있는 값을 꺼낸다.
     : 꺼낸 값이 현재 수열 값이나 아닐수 있다. 만약 아니라면 후입선출 원리에 따라 수열을 표현할 수 없으므로 `NO`를 출력한 후 종료하고, 현재 수열 값이라면
        그대로 조건문을 빠져나온다.
     
### 슈도코드
```text
n(수열 개수), a(수열 리스트)
a 수열 리스트 채우기

for n 반복:
    if 현재 수열 값 >= 오름차순 자연수:
        while(현재 수열 값 >= 오름차순 자연수):
            append()
            오름차순 자연수 += 1
            결과에 (+) 저장
        pop()
        결과에 (-) 저장
    else 현재 수열 값 < 오름차순 자연수:
        pop()
        if 스택 pop 결과값 > 수열의 수:
            NO 출력
        else :
            결과에 (-) 저장
if NO를 출력한 적이 없으면:
    결과 출력
```

### 코드 구현 - 파이썬
```python
n = int(input())
a = [0] * n

for i in range(n):
    a[i] = int(input())

stack = []
num = 1
result = True
answer = ""

for i in range(n):
    now = a[i]
    if now >= num:  # 현재 수열값 >= 오름차순 자연수: 값이 같아질 때까지 append(push) 수행
        while now >= num:
            stack.append(num)
            num += 1
            answer += "+\n"
        stack.pop()
        answer += "-\n"
    else:  # 현재 수열값 < 오름차순 자연수: pop을 수행해 수열 원소를 꺼냄
        pop = stack.pop()
        if pop > now:  # 스택의 가장 위의 수가 만들어야 하는 수열의 수보다 크면 수열을 출력할 수 없음
            print("NO")
            result = False
            break
        else:
            answer += "-\n"

if result:
    print(answer)

```

### 코드 구현 - 자바
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }

        int cnt = 1;
        Stack<Integer> stack = new Stack<>();

        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < n; i++) {
            int now = arr[i]; //현재 수열값
            if (now >= cnt) { //현재 수열값 >= 오름차순 자연수
                while (now >= cnt) { //현재 수열값이 자연수와 같아질 때까지
                    stack.push(cnt++);//push 후 자연수 증가
                    sb.append("+").append("\n");
                }
                stack.pop();//top pop
                sb.append("-").append("\n");
            } else { //현재 수열값 < 오름차순 자연수
                int pop = stack.pop();
                if (pop > now) { //스택의 가장 위의 값이 더 크다면 정상적으로 꺼내야 할 값이 현재 top보다 밑에 있음
                    System.out.println("NO"); //스택 구조상 불가능하니 "NO"를 출력하고 프로세스 종료
                    System.exit(0);
                } else { //pop <= now : 스택의 가장 위의 값 <= 현재 수열값
                    sb.append("-").append("\n");
                }
            }
        }
        System.out.println(sb);
    }
}
```