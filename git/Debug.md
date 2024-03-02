# 분석과 디버깅

### log

각 커밋마다 변경사항과 함께 보기
```text
git log -p
```

최근 n개 커밋만 보기
```text
git log -(갯수)
```
- 예) `git log -2 -p` : 최근 2개의 커밋을 변경사항과 함께 본다.

통계와 함께 보기
```text
git log --stat
```
- 더 간략히 보려면 `--stat`대신 `--shortstat`입력

한 줄로 보기
```text
git log --oneline
```
- 커밋 해시 요약과 커밋 메시지를 보여준다.
- 커밋 해시를 요약해서 보여주는 `--abbrev-commit`와 전체 커밋 해시와 커밋 메시지를 보여주는 `--pretty=oneline` 두 옵션의 줄임이다.

변경사항 내 단어 검색
```text
git log -S (검색어)
```
- 변경사항 내역 중에 검색어에 해당하는 단어가 있는 커밋 정보를 보여준다.

커밋 메시지로 검색
```text
git log --grep (검색어)
```
- 커밋 메시지에 검색어가 포함된 커밋 정보를 보여준다.

[더 다양한 옵션 - 문서](https://git-scm.com/book/ko/v2/Git%EC%9D%98-%EA%B8%B0%EC%B4%88-%EC%BB%A4%EB%B0%8B-%ED%9E%88%EC%8A%A4%ED%86%A0%EB%A6%AC-%EC%A1%B0%ED%9A%8C%ED%95%98%EA%B8%B0#limit_options)

그래프 로그 보기
```text
git log --all --decorate --oneline --graph
```
- `--all` : 모든 브랜치 보기
- `--graph` : 그래프 표현
- `--decorate` : 브랜치, 태그 등 모든 레퍼런스 표시
  - `--decorate=no`
  - `--decorate=short`(기본)
  - `--decorate=full`

포맷된 로그 보기
```text
git log --pretty=format: (포맷팅)
```
- [포맷팅 옵션들(문서)](https://git-scm.com/book/ko/v2/Git%EC%9D%98-%EA%B8%B0%EC%B4%88-%EC%BB%A4%EB%B0%8B-%ED%9E%88%EC%8A%A4%ED%86%A0%EB%A6%AC-%EC%A1%B0%ED%9A%8C%ED%95%98%EA%B8%B0#pretty_format)
- 취향에 맞게 로그 포맷팅을 단축키로 지정해놓고 사용할 수 있다.


### 차이 살펴보기

`Working directory`의 변경사항 확인
```text
git diff
```

파일명만 확인
```text
git diff --name-only
```

스테이지 확인
```text
git diff --staged
```
- `--cached`랑 같다.

두 커밋간 차이 확인
```text
git diff (커밋1) (커밋2)
```
- 커밋 해시 또는 HEAD 번호를 쓸 수 있다.
- 현재 커밋과 비교하려면 이전 커밋 정보만 명시하면 된다.

브랜치간의 차이 확인
```text
git diff (브랜치1) (브랜치2)
```

파일별 작성자 확인하기
```text
git blame (파일명)
```
- 파일명을 적을 때에는 폴더를 포함해야 한다.

특정 부분 지정해서 작성자 확인하기
```text
git blame -L (시작줄) (끝즐, 또는 +줄수) (파일명)
```
- 이렇게 CLI로 작성자를 확인하는 것보다 IDE나 플러그인들이 훨씬 좋은 게 많이 있기 때문에 이 방법을 사용하는 것이 좋다. 

### 오류 발생 시점 찾기

탐색을 시작한다.
```text
git bisect start
```
- 이진 탐색 알고리즘(반씩 나누어서 찾는)으로 문제의 발생 시점을 찾기 시작한다.

오류가 발생하면 오류발생 지점임을 표시한다.
```text
git bisect bad
```

의심되는 지점으로 이동한다.
```text
git checkout (커밋 해시)
```

오류가 발생하지 않으면 양호함을 표시한다.
```text
git bisect good
```

이렇게 `git bisect bad/good`을 원인을 찾을 때까지 반복한다.

탐색이 종료되면 종료를 한다.
```text
git bisect reset
```
