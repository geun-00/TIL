# 트라이 예제 - 1

### [문제(백준(14425번 - 문자열 집합))](https://www.acmicpc.net/problem/14425)

### 문제 분석
- 집합 S에 속해 있는 단어들을 이용해 트라이 구조를 생성하고, 트라이 검색을 이용해 문자열 M개의 포함 여부를 카운트하는 전형적인 트라이 자료구조 문제이다.


### 손으로 풀어보기
1. **트라이 자료구조를 생성한다. 현재 문자열을 가리키는 위치의 노드가 공백 상태라면 신규 노드를 생성하고, 아니라면 이동한다. 문자열의 마지막에 도달하면
    리프 노드라고 표시한다.**
2. **트라이 자료구조 검색으로 집합 S에 포함된 문자열을 센다. 부모-자식 관계 구조를 이용해 대상 문자열을 검색했을 때 문자열이 끝날 때까지 공백 상태가 없고,
   현재 문자의 마지막 노드가 트라이의 리프 노드이면 이 문자를 집합 S에 포함된 문자열로 센다.**

### 슈도코드
```text
Node 클래스:
    isEnd(마지막 문자열 여부)
    childNode(자식 노드)

Trie 클래스:
    parent(부모 노드 저장 변수)
    
    문자 삽입 함수:
        for 문자열 탐색:
            if 자식 노드에 없는 문자:
                신규 생성
            자식 노드 문자로 이동
            if 이번 문자열의 마지막 문자:  
                마지막 문자 표시
    
    문자 찾기 함수:
        for 문자열 탐색:
            존재하지 않으면 False 리턴
            마지막 문자까지 모두 존재하고 마지막 문자에 isEnd가 True인 경우 True 리턴

n(집합 S의 문자열 개수) m(검사할 문자열 개수)
Trie 생성

for n 반복:
    문자열 삽입 함수 수행

for m 반복:
    문자열 찾기 함수 수행
    if 찾으면 정답 1 증가

정답 출력
```

### 코드 구현 - 파이썬(`Trie` 사용)
```python
import sys

input = sys.stdin.readline


class Node(object):
    def __init__(self, isEnd):
        self.isEnd = isEnd
        self.childNode = [None] * 26  # 알파벳 개수만큼 크기 생성


class Trie(object):
    def __init__(self):
        self.parent = Node(None)  # Trie가 생성될 때 루트 노드 설정, 루트 노드는 공백이므로 None

    def insert(self, string):
        nowNode = self.parent  # 루트 노드

        for char in string:  # 입력 문자열을 돌면서 진행
            index = ord(char) - ord('a')  # 현재 문자의 index

            if nowNode.childNode[index] is None:  # 문자가 아직 생성된 적이 없다면 새로 생성
                nowNode.childNode[index] = Node(False)  

            nowNode = nowNode.childNode[index]  # 다음 자식 노드로 이동

        nowNode.isEnd = True  # 마지막 자식 노드에 마지막임을 표시

    def search(self, string):
        nowNode = self.parent  # 루트 노드

        for char in string:
            index = ord(char) - ord('a')

            if nowNode.childNode[index] is None:  # 자식 노드에 찾고자 하는 문자가 없으면 False 반환
                return False
            nowNode = nowNode.childNode[index]  # 다음 자식 노드로 이동

        return nowNode.isEnd  # 마지막 문자 여부를 반환


n, m = map(int, input().split())
trie = Trie()  # Trie 생성

for _ in range(n):
    word = input().strip()  # strip(): 문자열 양 끝 공백 제거
    trie.insert(word)

result = 0

for _ in range(m):
    word = input().strip()
    if trie.search(word):
        result += 1

print(result)
```

### 코드 구현 - 파이썬(`Set` 사용)
```python
import sys

input = sys.stdin.readline

n, m = map(int, input().split())
text = set([input() for _ in range(n)])

count = 0

for _ in range(m):
    data = input()
    if data in text:
        count += 1

print(count)
```

### 코드 구현 - 자바(`Trie` 사용)
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    static class Node {
        boolean isEnd;
        Map<Character, Node> childNode;

        public Node(boolean isEnd) {
            this.isEnd = isEnd;
            this.childNode = new HashMap<>();
        }
    }

    static class Trie {
        Node node;

        public Trie() {
            this.node = new Node(false);
        }

        public void insert(String string) {
            Node nowNode = this.node;
            int length = 0;

            for (char ch : string.toCharArray()) {
                if (!nowNode.childNode.containsKey(ch)) {
                    nowNode.childNode.put(ch, new Node(false));
                }
                nowNode = nowNode.childNode.get(ch);
                length++;

                if (length == string.length()) {
                    nowNode.isEnd = true;
                }
            }
        }

        public boolean search(String string) {
            Node nowNode = this.node;
            int length = 0;

            for (char ch : string.toCharArray()) {
                if (nowNode.childNode.containsKey(ch)) {
                    nowNode = nowNode.childNode.get(ch);
                    length++;

                    if (length == string.length() && nowNode.isEnd) {
                        return true;
                    }
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        Trie trie = new Trie();
        for (int i = 0; i < n; i++) {
            String input = br.readLine();
            trie.insert(input);
        }

        int count = 0;
        for (int i = 0; i < m; i++) {
            String input = br.readLine();
            if (trie.search(input)) {
                count++;
            }
        }

        System.out.println(count);
    }
}
```

### 코드 구현 - 자바(`Set` 사용)
```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        int N = Integer.parseInt(st.nextToken()), M = Integer.parseInt(st.nextToken());
        
        Set<String> set = new HashSet<>();
        for (int i = 0; i < N; i++) {
            set.add(br.readLine());
        }
        
        int count = 0;
        for (int i = 0; i < M; i++) {
            if(set.contains(br.readLine()))
                count++;
        }
        System.out.println(count);
    }
}
```