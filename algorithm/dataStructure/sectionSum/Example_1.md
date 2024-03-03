# 구간 합 예제 - 1

### [문제(백준(11659번 - 구간 합 구하기 4))](https://www.acmicpc.net/problem/11659)

### 문제 분석
- 수의 개수와 질의의 개수는 최대 100,000이다.
- 구간마다 합을 매번 계산하면 시간 제한(`0.5초`)안에 절대 끝낼 수 없다.
  - 매번 계산한다면 최대 `100,000^2` 정도의 연사을 수행한다.
- 구간 합을 사용해야 한다.

### 손으로 풀어보기
1. `N`개의 수를 입력받으면서 동시에 합 배열을 생성한다.
2. 구간 `i~j`가 주어지면 구간 합을 구하는 공식으로 정답을 출력한다.

### 슈도코드
```text
n(숫자 개수), m(질의 개수)
numbers 변수에 숫자 데이터 저장
sumList 합 배열 변수 선언
temp 변수 선언

for numbers 데이터 차례대로 검색:
    temp에 현재 숫자 누적
    합 배열에 temp값 저장

for 질의 개수 만큼:
    질의 범위 받기(start ~ end)
    구간 합 출력하기(sumList[end] - sumList[start-1]
```

### 코드 구현
```python
import sys

input = sys.stdin.readline
n, m = map(int, input().split())
numbers = list(map(int, input().split()))
sumList = [0]
temp = 0
str_list = []

for i in numbers:
    temp += i
    sumList.append(temp)

for i in range(m):
    start, end = map(int, input().split())
    str_list.append(str(sumList[end] - sumList[start - 1]))

result = "\n".join(str_list)
print(result)
```
- 리스트와 `.join()`은 자바의 `StringBuilder`같은 역할을 한다.