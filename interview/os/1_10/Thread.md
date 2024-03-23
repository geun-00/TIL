# 쓰레드를 만들 때 어떤 자원을 많이 사용하나요?

- **쓰레드를 만들 때 메모리와 CPU 사용량이 증가한다.**
- 멀티 쓰레드 환경에서 동기화, 공유 자원 등의 문제가 발생할 수 있다.

> **꼬리 질문**
> 
> 각각의 문제에 대한 해결법
> - [동기화 문제 & 공유 자원](https://gyoogle.dev/blog/computer-science/operating-system/Semaphore%20&%20Mutex.html#%E1%84%89%E1%85%A6%E1%84%86%E1%85%A1%E1%84%91%E1%85%A9%E1%84%8B%E1%85%A5-semaphore-%E1%84%86%E1%85%B2%E1%84%90%E1%85%A6%E1%86%A8%E1%84%89%E1%85%B3-mutex)
> - [동기화 문제 & 공유 자원](https://velog.io/@wonseok97/%EC%8A%A4%EB%A0%88%EB%93%9C%EC%9D%98-%EB%8F%99%EA%B8%B0%ED%99%94-%EB%AC%B8%EC%A0%9C%EC%99%80-%ED%95%B4%EA%B2%B0%EB%B2%95)
> - [쓰레드 부족 & 동시 요청](https://github.com/genesis12345678/TIL/blob/main/Spring/springmvc_1/web_application/web_application.md#%EB%8F%99%EC%8B%9C-%EC%9A%94%EC%B2%AD---%EB%A9%80%ED%8B%B0-%EC%93%B0%EB%A0%88%EB%93%9C)