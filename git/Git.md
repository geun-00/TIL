# Git 최초 설정

- `Git` 전역으로 사용자 이름과 이메일 주소 설정(`GitHub` 계정과는 별개)
```text
git config --global user.name "이름"
```
```text
git config --global user.email "(본인 이메일)"
```
확인하는 명령어
```text
git config --global user.name
```
```text
git config --global user.email
```
기본 브랜치명을 변경하려면
```text
git config --global init.defaultBranch main
```

- 새 프로젝트를 생성해서 `Git`의 관리 받기

해당 폴더를 IDE에서 열고 터미널에 다음 명령어를 입력하면 해당 폴더에 `.git`폴더가 생기면서 `git`이 관리하기 시작한다.
```text
git init
```
(`소스트리`로 해당 폴더를 드래그해서 추가를 해도 `.git`폴더가 생긴다.)

- `gitignore` 형식
```text
# 이렇게 #를 사용해서 주석

# 모든 file.c
file.c

# 최상위 폴더의 file.c
/file.c

# 모든 .c 확장자 파일
*.c

# .c 확장자지만 무시하지 않을 파일
!not_ignore_this.c

# logs란 이름의 파일 또는 폴더와 그 내용들
logs

# logs란 이름의 폴더와 그 내용들
logs/

# logs 폴더 바로 안의 debug.log와 .c 파일들
logs/debug.log
logs/*.c

# logs 폴더 바로 안, 또는 그 안의 다른 폴더(들) 안의 debug.log
logs/**/debug.log
```
`.gitignore`가 제대로 작동되지 않는 것 같으면 다음 명령어로 캐시 내용을 삭제 후 다시 시도해본다.
```text
git rm -r --cached .
git add .
git commit -m "fixed untracked files"
```