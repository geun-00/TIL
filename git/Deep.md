# Git의 3가지 공간

![img.png](img.png)

**Working directory**
- `untracked` : `add`된 적 없는 파일 또는 `.gitignore`된 파일
- `tracked` : `add`된 적 있고 변경내역이 있는 파일
- `git add` 명령어로 `Staging area`로 이동

**Staging area**
- 커밋을 위한 준비 단계
  - 예) 작업을 위해 선택된 파일들
- `git commit` 명령어로 `Repository`로 이동

**Repository**
- 커밋된 상태의 파일들이 모여있는 저장소
- `.git directory`라고도 불림
- `commit`되어 레포지토리에 들어간 후 수정사항이 발생하면 `tracked`파일로써 스테이징을 기다리게 된다.

이 개념을 **그릇**으로 비유하면 이렇다.

| 상태                | 설명                                             |
|-------------------|------------------------------------------------|
| untracked         | 식기세척기에 들어가 본 적이 없거나 식기세척기 사용이 불가(`ignore`)한 그릇 |
| tracked           | 식기세척기에 들어가 본 적이 있고 식기세척기 사용이 가능한 그릇            |
| add               | 식기세척기에 넣는 행위                                   |
| staging area      | 식기세척기 안(에 들어간 상태)                              |
| commit            | 세척(식기세척기 가동)                                   |
| repository        | 세척되어 깨끗해진 상태                                   |
| 파일 수정             | 그릇이 사용되어 이물질(커밋되지 않은 변경사항)이 묻음                 |
| working directory | 세척되어야 하는 상태                                    |

- `tracked`가 된다는 건 `Git`의 관리대상에 정식으로 등록됨을 의미한다.
- 새로 추가되는 파일은 반드시 `add`해줌으로써, 해당 파일이 `tracked`될 것임을 명시해야 한다.
  - `Git`이 새 파일들을 무조건 다 관리해버리는 것을 방지

<br>

- 파일 삭제
```text
git rm (파일 이름)
```
마우스로 직접 삭제하는 것과 차이는 직접 하는 것은 파일의 삭제(작업)가 `Working directory`에 있고 `add`명령어로 `Staging area`로 이동해야 한다.<br>
`rm` 명령어로 하면 파일의 삭제(작업)가 바로 `Staging area`로 이동한다.

- 파일 이동(이름 변경)
```text
git mv (기존 파일 명) (새로운 파일 명)
```
마우스로 직접 하는 것과 차이는 `rm`과 마찬가지다.

- 파일을 `Staging area`에서 `Working directory`로 되돌리기
```text
git restore --staged (파일명)
```
이 명령은 `Staging area`에서 해당 파일을 제거하여 이전 커밋 상태로 되돌린다. 단 `Working directory`에서는 변경사항이 남아있는다.

이떄 `--staged`옵션을 생략하면 `Working directory`에서도 해당 파일의 변경사항이 제거된다. 즉 `Staging area`와 `Working directory`에서 모두
이전 커밋 상태로 되돌아간다.

**정리하면 파일을 `Staging area`에서 `Working directory`로 되돌리고 싶다면 `--staged`옵션을 사용하고, 옵션을 생략하면 `Working directory`에서도 변경사힝이 제거된다.**

**`git reset`의 3가지 옵션**
- `--soft` : `Repository`에서 `Staging area`로 이동
- `--mixed`(default) : `Repository`에서 `Working directory`로 이동
- `--hard` : 수정사항 완전히 삭제

<br>

# Git의 HEAD

**HEAD란?** - 현재 속한 브랜치의 가장 최신 커밋

- HEAD를 기준으로 이전 커밋으로 이동하기
```text
git checkout HEAD^
```
`^` 또는 `~`의 개수만큼 이전으로 이동한다.<br>
예) `git checkout HEAD^^^` : 이전으로 3개 이동, `git checkout HEAD~5` : 이전으로 5개 이동<br>
**커밋 해시**를 사용해서도 이동할 수 있다. -> `git checkout (커밋 해시)`

- 이동을 한 단계 되돌리기
```text
git checkout -
```
`Ctrl Z`와 비슷

**이전으로 `checkout`된 상태에서 터미널로 어떤 브랜치에 있나를 확인해보면 생성한 적 없는 익명의 브랜치에 위치해 있다는 걸 볼 수 있다.**

- 기존 브랜치로 돌아오기
```text
git switch (브랜치명)
```

**HEAD의 개념을 활용하여 `reset`과 `revert`와 비교해서 깔끔하게 어느 특정 커밋 시점으로 가서 거기서 또 다른 브랜치를 만들어서 개발을 할 수 있다.**

- HEAD 사용해서 reset하기
```text
git reset HEAD(원하는 단계) (옵션)
```
원하는 단계에는 `^`이나 `~`로 단계를 조정할 수 있고, 옵션에는 `--soft`, `--mixed`, `--hard`를 주면 된다.

<br>

# fetch와 pull

**fetch vs pull**
- `fetch` : 원격 저장소의 최신 커밋을 로컬로 **가져오기만 함**
- `pull` : 원격 저장소의 최신 커밋을 가져와 `merge`또는 `rebase` 한다.(`fetch`과정을 포함)

예를 들면 `git fetch`로 먼저 원격 저장소의 변경 내용을 확인해서 실행도 해보고 괜찮은 것 같다면 내 로컬 환경에 `pull`로 적용할 수 있는 것이다.

- 원격 브랜치의 변경 내용 확인
```text
git fetch
```
후에
```text
git checkout origin/main
```
이렇게 하면 원격 저장소의 최신 커밋을 받아오고 코드 상으로 더 확실히 볼 수 있다.

- 원격의 새 브랜치를 확인하려면 `git fetch`를 하고 브랜치명을 확인한 다음 두 명령으로 새 브랜치를 확인할 수 있다.
```text
git checkout origin/(브랜치명)
```
이 명령은 로컬에서 원격 저장소의 브랜치를 확인할 수 있다.(확인만 하고 로컬에서 브랜치는 생성하지 않음)
```text
git switch -t origin/(브랜치명)
```
이 명령은 로컬에서 원격 저장소의 브랜치와 같은 이름의 브랜치를 생성함과 동시에 원격 브랜치를 추적하도록 설정할 수 있다. 

<br>

# Git help
`Git` 사용 중 모르는 부분이 있을 때 도움을 받을 수 있는 기능

- 기본적인 명령어들과 설명
```text
git help
```

- `Git`의 모든 명령어들
```text
git help -a
```
`j`로 내리고, `k`로 올릴 수 있다. `:q`(또는 `q`)로 닫는다.

- 해당 명령어의 설명과 옵션 보기
```text
git (명령어) -h
```
예) `git commit -h`, `git push -h` 등

- 해당 명령어의 설명과 옵션을 웹 사이트에서 확인하기
```text
git help (명령어)
```
또는
```text
git (명령어) --help
```
웹에서 열리지 않으면 끝에 `-w`를 붙여 명시해주면 된다.

<br>

# Git config

특정 프로젝트에서 `user.name`과 `user.email`을 `--global` 옵션 없이 지정하면 특정 프로젝트만의 설정을 지정할 수 있다.

- 현재 모든 설정값 보기
```text
git config (global) --list
```
`global`을 생략하면 모든 설정값이 나오고, 명시하면 `global`로 설정된 값들만 나온다.

- 에디터에서 보기
```text
git config (global) -e
```
`global`은 생략할 수 있다.

- 기본 에디터 수정
```text
git config --global core.editor "code --wait"
```
`code`자리에 원하는 편집 프로그램의 `.exe`파일 경로를 연결할 수 있다.<br>
`--wait`은 데이터에서 수정하는 동안 `CLI`를 정지한다.<br>
`git commit` 등의 편집도 지정된 에디터에서 연다.

- 윈도우와 맥 줄바꿈 호환 문제 해결
```text
git config --global core.autocrlf (윈도우: true / 맥: input)
```

- `pull` 기본 전략 `merge`또는 `rebase`로 설정
```text
git config pull.rebase false
```
```text
git config pull.rebase true
```
- 기본 브랜치명 설정
```text
git config --global init.defaultBranch main
```
- `push`시 로컬과 동일한 브랜치명으로 설정
```text
git config --global push.default current
```
- 단축키 설정
```text
git config --global alias.(단축키) "명령어"
```
예) `git config --global alias.cam "commit -am"` : `git commit -am`대신 `git cam`<br>
[관련 문서](https://git-scm.com/book/ko/v2/Git%EC%9D%98-%EA%B8%B0%EC%B4%88-Git-Alias)