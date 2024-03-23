# 퀵 정렬에 대해 설명해 주세요.

- [퀵 정렬](https://github.com/genesis12345678/TIL/blob/main/algorithm/sorting/quickSort/QuickSort.md#%ED%80%B5-%EC%A0%95%EB%A0%AC)(Quick Sort)은 빠른 정렬 속도를 자랑하는 분할/정복 알고리즘 중 하나로 피벗(pivot)을 설정하고 피벗보다 큰 값과 작은 값으로 분할하여 정렬한다.
- 병합정렬과 달리 배열을 비균등하게 분할한다.
- 시간 복잡도는 평균 `O(N log N)`, 최악의 경우 `O(N^2)`까지 나빠질 수 있다.
- Java에서 `Arrays.sort()` 내부적으로도 `DualPivotQuickSort`로 구현되어 있을 정도로 효율적인 알고리즘이다.

![img_4.png](image/img_4.png)

[참고 영상](https://www.youtube.com/watch?v=ywWBy6J5gz8)