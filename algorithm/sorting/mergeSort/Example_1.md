# 병합 정렬 예제 - 1

### [문제(백준(2751번 - 수 정렬하기 2))](https://www.acmicpc.net/problem/2751)

### 문제 분석

- `N`의 최대 범위가 1,000,000이므로 `O(nlogn)`의 시간 복잡도로 정렬을 수행하면 된다.

### 손으로 풀어보기
1. **정렬할 그룹을 최소 길이로 나눈다. 예제 입력으로 예를 들면 2, 2, 1로 나눌 수 있다. 그리고 나눈 그룹마다 병합 정렬을 한다. 각 그룹마다 index 값을 비교하면서
    정렬 용도로 선언한 `temp`배열에 병합 정렬한다.**
2. **병합된 그룹을 대상으로 정렬한다.**


### 슈도코드
```text
병합 정렬(start, end)
    start(시작점), end(종료점), mid(중간점)
    # 재귀 함수
    병합 정렬(start, mid)
    병합 정렬(mid + 1, end)
    for start ~ end:
        temp 리스트 저장
    
    # 두 그룹 병합
    index1(앞쪽 그룹 시작점)
    index2(뒤쪽 그룹 시작점)
    while index1 <= 중간점 and index2 <= 종료점:
        양쪽 그룹의 index가 가리키는 값을 비교한 후 더 작은 수를 선택해 리스트에 저장하고
        선택된 데이터의 index값을 오른쪽으로 한 칸 이동
        반복문이 끝난 후 남아있는 데이터 정리

n(수의 개수)
a(리스트 선언)
temp(정렬할 때 잠시 사용할 리스트 선언)

for n:
    a 리스트 저장
    
병합 정렬 수행

결과 출력
```

### 코드 구현 - 파이썬
```python
import sys

input = sys.stdin.readline
print = sys.stdout.write


def merge_sort(start, end):
    if start >= end:
        return

    mid = int(start + (end - start) / 2)
    merge_sort(start, mid)
    merge_sort(mid+1, end)

    for i in range(start, end + 1):
        temp[i] = a[i]

    k = start
    index1 = start
    index2 = mid + 1

    while index1 <= mid and index2 <= end:
        if temp[index1] > temp[index2]:
            a[k] = temp[index2]
            k += 1
            index2 += 1
        else:
            a[k] = temp[index1]
            k += 1
            index1 += 1

    while index1 <= mid:
        a[k] = temp[index1]
        k += 1
        index1 += 1
    while index2 <= end:
        a[k] = temp[index2]
        k += 1
        index2 += 1


n = int(input())
a = [0] * int(n+1)
temp = [0] * int(n+1)

for i in range(1,n+1):
    a[i] = int(input())

merge_sort(1, n)

result = []

for i in range(1, n+1):
    result.append(str(a[i]))

print("\n".join(result))
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

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }

        mergeSort(arr, n);
        StringBuilder sb = new StringBuilder();

        for (int num : arr) {
            sb.append(num).append("\n");
        }
        System.out.println(sb);
    }

    private static void mergeSort(int[] arr, int length) {
        // 더 이상 2개로 나눌 수 없다면 나누기 종료
        if (length < 2) {
            return;
        }
        int mid = length / 2; // 중간 값
        int[] leftArr = new int[mid]; // 중간 크기 만큼 왼쪽 그룹 생성
        int[] rightArr = new int[length - mid]; //전체 길이에서 중간 길이 만큼 뺀 나머지 오른쪽 그룹 생성

        for (int i = 0; i < mid; i++) {
            leftArr[i] = arr[i]; //왼쪽 그룹에 원본 배열 값 복사
        }

        for (int i = mid; i < length; i++) {
            rightArr[i - mid] = arr[i]; //오른쪽 그룹에 원본 배열 값 복사
        }

        mergeSort(leftArr, leftArr.length); //그룹을 또 나눌 수 있을 떄까지 시도
        mergeSort(rightArr, rightArr.length); //그룹을 또 나눌 수 있을 떄까지 시도

        merge(arr, leftArr, rightArr);//더 이상 나눌 수 없을 때 두 그룹 병합(나누기 전 배열, 나눈 후 왼쪽 배열, 나눈 후 오른쪽 배열), 메서드를 마치면 arr이 정렬된다.
    }

    private static void merge(int[] arr, int[] leftArr, int[] rightArr) {
        int leftIdx = 0; //왼쪽 그룹 index
        int rightIdx = 0; //오른쪽 그룹 index
        int curIdx = 0; //원본 배열 index

        while (leftIdx < leftArr.length && rightIdx < rightArr.length) {
            if (leftArr[leftIdx] < rightArr[rightIdx]) { //왼쪽이 더 작다면
                arr[curIdx++] = leftArr[leftIdx++]; //원본 배열에 왼쪽 그룹의 값을 저장하고 왼쪽 그룹 index 이동
            } else {                                     //오른쪽이 더 작다면
                arr[curIdx++] = rightArr[rightIdx++];//원본 배열에 오른쪽 그룹의 값을 저장하고 오른쪽 그룹 index 이동
            }
        }

        //왼쪽 그룹에 아직 정렬하지 못한 게 남아있다면 마저 정렬
        while (leftIdx < leftArr.length) {
            arr[curIdx++] = leftArr[leftIdx++];
        }
        
        //오른쪽 그룹에 아직 정렬하지 못한 게 남아있다면 마저 정렬
        while (rightIdx < rightArr.length) {
            arr[curIdx++] = rightArr[rightIdx++];
        }
    }
}
```