# 그리디 

- 그리디 알고리즘은 현재 상태에서 보는 선택지 중 최선의 선택지가 전체 선텍지 중 최선의 선택지라고 가정하는 알고리즘이다.
- 그리디 알고리즘은 동적 계획법보다 구현하기 쉽고 시간 복잡도가 우수하다.
- 하지만 항상 최적의 해를 보장하지 못한다는 단점이 있다.
- 코딩 테스트에서 그리디 알고리즘을 사용하기 전에는 항상 그리디 알고리즘을 적용할 때의 논리 유무를 충분히 살펴야 한다.

## 그리디 알고리즘 핵심 이론

- 그리디 알고리즘은 다음 3단계를 반복하면서 문제를 해결한다.

1. **해 선택** : **현재 상태에서 가장 최선**이라고 생각되는 해를 선택한다.
2. **적절성 검사** : 현재 선택한 해가 전체 문제의 제약 조건에 벗어나지 않는지 검사한다.
3. **해 검사** : 현재까지 선택한 해 집합이 전체 문제를 해결할 수 있는지 검사한다. 전체 문제를 해결하지 못한다면 `1`번으로 돌아가 같은 과정을 반복한다.


### [예제 문제(백준 - 동전 0)](https://github.com/genesis12345678/TIL/blob/main/algorithm/greedy/Example_1.md#%EA%B7%B8%EB%A6%AC%EB%94%94-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98-%EC%98%88%EC%A0%9C---1)

### [예제 문제(백준 - 카드 정렬하기)](https://github.com/genesis12345678/TIL/blob/main/algorithm/greedy/Example_2.md#%EA%B7%B8%EB%A6%AC%EB%94%94-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98-%EC%98%88%EC%A0%9C---2)

### [예제 문제(백준 - 수 묶기)](https://github.com/genesis12345678/TIL/blob/main/algorithm/greedy/Example_3.md#%EA%B7%B8%EB%A6%AC%EB%94%94-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98-%EC%98%88%EC%A0%9C---3)

### [예제 문제(백준 - 회의실 배정)](https://github.com/genesis12345678/TIL/blob/main/algorithm/greedy/Example_4.md#%EA%B7%B8%EB%A6%AC%EB%94%94-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98-%EC%98%88%EC%A0%9C---4)

### [예제 문제(백준 - 잃어버린 괄호)](https://github.com/genesis12345678/TIL/blob/main/algorithm/greedy/Example_5.md#%EA%B7%B8%EB%A6%AC%EB%94%94-%EC%95%8C%EA%B3%A0%EB%A6%AC%EC%A6%98-%EC%98%88%EC%A0%9C---5)