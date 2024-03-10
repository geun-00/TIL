# 유클리드 호제법 예제 - 2

### [문제(백준(1850번 - 최대공약수))](https://www.acmicpc.net/problem/1850)

### 문제 분석
- 예제 입력 3과 같이 입력값이 크면 단순한 방법으로 최대 공약수를 찾을 수 없다.
- 하지만 주어진 예제를 바탕으로 규칙을 찾을 수 있다.
  - 수의 길이를 나타내는 두 수의 최대 공약수는 A와 B의 최대 공약수의 길이를 나타낸다.
  - 3, 6의 최대 공약수 3은 A(111)와 B(111111)의 최대 공약수(111)의 길이로 나타낸다.

### 손으로 풀어보기
1. **유클리드 호제법을 이용해 주어진 A, B의 최대 공약수를 구한다.**
2. **공약수의 길이만큼 1을 반복해 출력한다.**

### 슈도코드
```text
gcd(a, b):
    if b가 0:
        a가 최대공약수
    else:
        gcd(b, a % b)
        
a(1번째 수) b(2번째 수)
result = gcd(a, b)
result 크기 만큼 1 반복 출력
```

### 코드 구현 - 파이썬
```python
a, b = map(int, input().split())

def gcd(a, b):
    if b == 0:
        return a
    else:
        return gcd(b, a % b)

result = gcd(a, b)

ans = []

for _ in range(result):
    ans.append(str(1))

print("".join(ans))
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
        StringBuilder sb = new StringBuilder();

        long a = Long.parseLong(st.nextToken());
        long b = Long.parseLong(st.nextToken());

        long result = gcd(a, b);
        for (int i = 0; i < result; i++) {
            sb.append(1);
        }
        System.out.println(sb);

    }

    private static long gcd(long a, long b) {
        if (b == 0) {
            return a;
        } else {
            return gcd(b, a % b);
        }
    }
}
```