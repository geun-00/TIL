# 취소와 되돌리기

### Git에 관리되지 않는 파일들 삭제

`Git`에서 추적하지 않는 파일들 삭제
```text
git clean (옵션)
```

| 옵션 | 설명                        |
|----|---------------------------|
| -n | 삭제될 파일들 보여주기              |
| -i | 인터렉티브 모드 시작(편집 모드 비슷)     |
| -d | 폴더 포함                     |
| -f | 강제로 바로 지워버리기              |
| -x | `.gitignore`에 등록된 파일들도 삭제 |

- `git clean`은 기본적으로 `.gitignore`에 등록된 파일들은 건드리지 않는데, `-x`옵션은 `.gitignore`에 등록된 파일들도 포함한다.
- 위 옵션들을 조합해서 사용할 수 있다.
  - 예) `git clean -nd` : 폴더를 포함해서 삭제될 파일을 보여주기
  - 예) `git clean -df` : 폴더를 포함해서 강제로 지워버리기
- 흔히 쓰이는 조합은 `git clean -df`가 있다.

### 커밋하지 않은 변경사항 되돌리기

특정 파일을 복구하기
```text
git restore (파일명)
```
- `Working directory`의 특정 파일을 복구한다.
- 파일명에 `.`을 해주면 모든 파일을 복구한다.

변경상태를 `Staging area`에서 `Working directory`로 돌려놓기
```text
git restore --staged (파일명)
```

**정리하면 아직 `add`하지 않은 파일들을 `restore`로 복구하면 변경사항이 사라지게 되는 거고, `add`된 파일들(`Staging area`에 있는 파일들)은 `--staged` 옵션으로
`Working directory`로 되돌릴 수 있다.**

파일을 특정 커밋의 상태로 되돌리기
```text
git restore --source=(헤드 또는 커밋 해시) 파일명
```
- 커밋 해시 또는 `HEAD^`, `HEAD~` 옵션으로 특정 커밋 시점을 선택할 수 있다.
- 되돌리면 `git`은 변경되었다고 판단하고 해당 파일은 `Working directory`에 있는 상태다.


### reset 복구하기

```text
git reflog
```
- 이 명령어로 `git`으로 하는 모든 활동들을 기록해 놓은 것을 볼 수 있다.(`reset`포함)
- 실수로 `reset --hard`를 했을 때 위 명령어로 실수한 커밋 바로 이전 커밋 해시로 `reset --hard (커밋 해시)`로 복구를 할 수 있다.