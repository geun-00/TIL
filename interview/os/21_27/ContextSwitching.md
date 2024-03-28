# 문맥 교환(Context Switching)이란 무엇인가요?

- [Context Switching](https://github.com/genesis12345678/TIL/blob/main/OS/PCB_and_ContextSwitching/pcb.md#pcb--context-switching)이란 **프로세스의 상태를 변경하는 것**을 말한다.
- 하나의 프로세스가 CPU를 사용 중인 상태에서 다른 프로세스가 CPU를 사용하도록 하기 위해 이전 프로세스의 상태를 보관하고 새로운 프로세스의 상태를 적재하는 방법이다.
- 스케줄링에 의해 실행 중인 코드, 자원 등을 저장하고 현재 상태를 대기 상태(`idle`)로 만들고, 다른 프로세스를 실행시키는 과정이라고 할 수 있다.
- CPU가 현재 처리중인 프로세스의 **PCB**를 따로 저장하고 다른 PCB를 가져온다.
- **PCB**란, 특정 프로세스에 대한 중요한 정보를 저장하고 있는 운영체제의 자료구조이다.
- 운영체제는 프로세스를 관리하기 위해 **프로세스의 생성과 동시에** 고유한 [PCB](https://github.com/genesis12345678/TIL/blob/main/OS/PCB_and_ContextSwitching/pcb.md#%ED%94%84%EB%A1%9C%EC%84%B8%EC%8A%A4-%EC%A0%9C%EC%96%B4-%EB%B8%94%EB%A1%9D-process-control-block-pcb)를 생성한다.