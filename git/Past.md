# 과거로 가기
**reset** : 원하는 시점으로 돌아간 뒤 이후 내역들을 지운다.<br>
**revert** : 되돌리기 원하는 시점의 커밋을 거꾸로 실행한다.

**reset**

- 다음 명령어로 커밋 내역 확인
```text
git log
```
되돌아갈 시점(커밋)의 커밋 해시를 복사한다.

- 다음 명령어로 원하는 커밋 시점으로 돌아간다.
```text
git reset --hard (돌아갈 커밋 해시)
```
만약 커밋 해시를 생략하면 자동으로 마지막 커밋을 가리킨다.<br>
그리고 `reset`에는 `hard`를 포함해서 3가지 옵션이 있다.
- [`Git`의 3가지 공간 참고](https://github.com/genesis12345678/TIL/blob/main/git/Deep.md#git%EC%9D%98-3%EA%B0%80%EC%A7%80-%EA%B3%B5%EA%B0%84)

**revert**
```text
git revert (돌아갈 커밋 해시)
```
충돌이 발생하면 충돌 해결을 하고 `git revert --continue`로 마무리한다.

만약 여러 커밋을 `revert`하는 경우에는 각 `revert`마다 커밋 메시지를 작성해야 하는 번거로움이 생기는데 이때 다음 옵션을 줄 수 있다.
```text
git revert --no-commit (되돌릴 커밋 해시)
```
이렇게 하면 원하는 다른 작업을 추가한 다음에 커밋을 하나만 생성할 수 있다.

**언제 `reset`을 사용하고 언제 `revert`를 사용해야 할까?**
- `reset` : 로컬 저장소에서 작업 시(아직 `push` 전)
- `revert` : 팀원들과 협업 중이고 이미 원격 저장소에 올라갔을 때(`push` 후)
- [참고](https://han-joon-hyeok.github.io/posts/git-reset-revert/)
- [참고](https://www.devpools.kr/2017/02/05/%EC%B4%88%EB%B3%B4%EC%9A%A9-git-%EB%90%98%EB%8F%8C%EB%A6%AC%EA%B8%B0-reset-revert/)
- [참고](https://www.devpools.kr/2017/01/31/%EA%B0%9C%EB%B0%9C%EB%B0%94%EB%B3%B4%EB%93%A4-1%ED%99%94-git-back-to-the-future/)