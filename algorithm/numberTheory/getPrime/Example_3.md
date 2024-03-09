# 소수 구하기 예제 - 3

### [문제(백준(1747번 - 소수&팰린드롬))](https://www.acmicpc.net/problem/1747)

### 문제 분석
- 에라토스테네스의 체를 이용해 최대 범위에 해당하는 모든 소수를 구해 놓은 후 이 소수들의 집합에서 `N`보다 크거나 같으면서 팰린드롬인 수를 찾아내면 되는 문제다.
- 팰린드롬 수를 판별할 때 숫자값이 리스트 형태로 적절하게 변환이 가능하다는 점을 이용하면 조금 더 쉽게 로직을 구현할 수 있다.

### 손으로 풀어보기
1. **`2 ~ 10,000,000` 사이에 모든 소수를 구하고, 그중 `N`보다 크거나 같은 소수에서 팰린드롬 수인지를 판별한다.**
2. **소수의 값을 리스트 형태로 변환한 후 양끝의 투 포인터를 비교하면 쉽게 팰린드롬 수인지 판별할 수 있다. 해당 소수를 리스트 형태로 변환하고 리스트의 처음과 끝을
   가리키는 포인터를 부여해 두 값을 비교한다. 두 값이 같으면 `start++`, `end--` 연산으로 두 포인터를 이동한다. `start < end`를 만족할 때까지 반복해 모든 값이 같으면
   그 수는 팰린드롬 수다.**
3. **오름차순으로 과정 2를 실행하다가 최초로 팰린드롬 수가 나오면 프로그램을 종료한다.**

### 슈도코드
```text
n(어떤 수)
A(소수 리스트)

for 2 ~ 10,000,001:
    A 리스트 초기화

for A 리스트 길이의 제곱근까지:
    소수가 아니면 넘어감
    for 소수의 배수 값을 1,000,001까지:
        현재 수가 소수가 아니라는 것을 표시
        
팰린드롬 함수:
    숫자값을 리스트 형태로 변환
    start(시작 인덱스), end(끝 인덱스)
    while start < end:
        시작과 끝 인덱스의 해당하는 값이 다르면 return false
        start++
        end--
        반복문을 다 돌면 return true

while true:
    N부터 1씩 증가하면서 A[i]값이 소수이면서 팰린드롬 수인지 판별
    맞으면 출력 후 반복문 종료
```

### 코드 구현 - 파이썬
```python
import math

n = int(input())
A = [0] * 10_000_001

for i in range(2, len(A)):
    A[i] = i

for i in range(2, int(math.sqrt(len(A)) + 1)):
    if A[i] != 0:
        for j in range(i * 2, len(A), i):
            A[j] = 0


def isPalindrome(target):
    temp = list(str(target))
    start = 0
    end = len(temp) - 1

    while start < end:
        if temp[start] != temp[end]:
            return False
        start += 1
        end -= 1

    return True


index = n
while True:
    if A[index] != 0 and isPalindrome(A[index]):
        print(A[index])
        break
    index += 1
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

        int[] A = new int[10_000_001];
        for (int i = 2; i < A.length; i++) {
            A[i] = i;
        }

        for (int i = 2; i <= Math.sqrt(A.length); i++) {
            if (A[i] != 0) {
                for (int j = i * 2; j < A.length; j += i) {
                    A[j] = 0;
                }
            }
        }

        int index = n;
        while (true) {
            if (A[index] != 0 && isPalindrome(A[index])) {
                System.out.println(A[index]);
                break;
            }
            index++;
        }
    }

    private static boolean isPalindrome(int target) {
        char[] temp = Integer.toString(target).toCharArray();
        int start = 0;
        int end = temp.length - 1;

        while (start < end) {
            if (temp[start] != temp[end]) {
                return false;
            }
            start++;
            end--;
        }
        return true;
    }
}
```