# HTTP GET과 POST의 차이는 무엇인가요?

- `GET`과 `POST`는 [Http Method](https://github.com/genesis12345678/TIL/blob/main/Http/httpMethod/httpMethod.md#http-method) 들 중에서 가장 많이 쓰이는 Method 이다.
- `GET`은 **가져온다는 개념**으로, 서버에서 어떤 데이터를 가져와서 보여줄 때 사용한다.
  - 어떤 값이나 내용, 상태 등을 바꾸지 않는 경우에 사용한다.
- `POST`는 **수행한다는 개념**으로, 서버상의 데이터 값이나 상태를 바꾸기 위해서 사용한다.
- 게시판으로 예를 들면, 글의 내용에 대한 목록을 보여주는 경우나 글의 내용을 보는 경우는 `GET`에 해당하고, 글의 내용을 저장하고, 수정할 때는 `POST`에 해당한다.