# 변경사항 저장하기

- 변경사항 확인하기
```text
git status
```
이때 `untracked`파일은 추적되지 않는 파일로, `Git`의 관리에 들어간 적 없는 파일이다.(새로 만든 파일)

- 파일 하나 담기
```text
git add {파일명}
```
- 모든 파일 담기
```text
git add .
```

- 다음 명령어로 `commit`
```text
git commit
```
이후 `git commit` 편집기로 이동을 한다.([참고(편집 에디터 변경)](https://dev-jejecrunch.tistory.com/29))<br>
`Vi` 입력 모드([참고(Vim 강좌)](https://www.yalco.kr/10_vim/))에서 커밋 메시지를 입력하고 종료(`q`또는 `:q`)하면 된다.

- 커밋 메시지까지 함께 작성
```text
git commit -m "message"
```
- 추가로 `add`와 `commit`을 한번에 할 수도 있다.
```text
git commit -am "message"
```
**단 새로 추가된(`untracked`)파일이 없을 때만 된다.**