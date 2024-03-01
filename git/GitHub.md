# 원격 저장소(GitHub)

- 로컬의 `Git` 저장소에 원격 저장소 연결 추가
```text
git remote add (원격 저장소 이름) (원격 저장소 주소)
```
원격 저장소 이름은 마음대로 지을 수 있지만, 보통은 `origin`을 많이 사용한다.

- 기본 브랜치명 `main` 설정
```text
git branch -M main
```

- 로컬 저장소의 커밋 내역을 원격 저장소로 `push`할 때 기본 연결
```text
git push -u origin main 
```
이 설정을 해주면 `git push`만으로 `origin`에 설정된 저장소 주소의 `main`브랜치로 `push`하게 된다.

- 원격 목록 보기
```text
git remote
```
자세히 보려면 끝에 `-v`를 붙여준다.

- 원격 지우기
```text
git remote remove (원격 이름(origin))
```
로컬 프로젝트와 연결만 없애는 것이지 `Github`의 레포지토리는 그대로다.

- `Github`에서 프로젝트 다운받기

`Download ZIP`은 파일들만 다운 받는다.(`Git` 관리내역 제외) <br>
`clone`을 해야 `Git` 관리내역을 포함하여 다운로드 한다.

대상 폴더에서 `Git Bash`에 입력
```text
git clone (원격 저장소 주소)
```

- 원격의 커밋 당겨오기
```text
git pull
```

**`pull`할 것이 있을 때 `push`를 하면?**
- 원격에 먼저 적용된 새 버전이 있으므로 `push`할 수 없다.
- `pull`해서 먼저 원격의 버전을 받아온 다음 `push`를 할 수 있다.

`pull`을 할 때 두 가지 방법이 있다.

- `git pull --no-rebase` : **merge** 방식
  - 로컬의 `main`브랜치와 원격의 `main`브랜치를 **merge**한다.
  - 로컬과 원격의 어긋난 시간선을 한군데로 모은 다음(커밋)에 그걸 `push`하는 방식
  - 환경에 따라 `git pull`만 해줘도 이 방식으로 동작할 수 있음
- `git pull --rebase` : **rebase** 방식
  - 원격에 맞춰 일단 원격의 것을 붙인 다음 로컬에서 한 것을 잘라서 그 다음에 붙이는 방식
  - 충돌이 났을 때 어떤 것을 커밋 하느냐에 따라 생성되는 커밋의 수가 달라질 수 있다.
    - 원격의 것으로 하면 1개, 로컬의 것으로 하면 1+n개
  - 브랜치를 합칠 때 사용하는 `rebase`와는 달리 `pull`할 때 `rebase`는 협업 시 사용해도 문제가 없다.

<br>

- 로컬의 내역 강제 `push`
```text
git push --force
```
원격의 내용을 현재 내 로컬의 내용으로 강제로 맞춰버리는 강력한 기능이다. **조심해서 사용해야 한다. 협업 시 반드시 팀원들의 동의가 있어야 한다.**

- 로컬과 원격의 브랜치들 확인
```text
git branch --all
또는 
git branch -a
```

- 원격의 변경사항을 확인하기
```text
git fetch
```
원격에서 새로 생긴 브랜치 등 프로젝트의 변경 사항을 확인할 수 있다.(**적용이 아닌 저장소에만 저장**)

- 로컬에다가 원격에 새로 생긴 브랜치와 같은 이름으로 생성하여 연결하고 `switch`
```text
git switch -t origin/(브랜치 이름)
```

- 원격의 브랜치 삭제
```text
git push origin --delete (원격의 브랜치 이름)
```