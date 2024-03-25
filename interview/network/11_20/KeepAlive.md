# Keep-Alive 헤더에 대해 설명해 주세요.

- HTTP 헤더와 관련된 값으로 `Keep-Alive`가 있다.
- HTTP는 TCP 위에서(기반으로) 동작을 하는데, TCP가 전송이 끝나면 연결이 끊어지듯이 HTTP도 서로 전송이 끝나면 끊어진다.
- 그런데 매번 이렇게 똑같은 주소로 요청을 할 때마다 새로운 연결을 설정하고 끊어야 한다면 자원이 낭비된다.
- 이런 문제를 막고자 `Keep-Alive`가 생겼다. 말 그대로 **연결을 계속 유지해라**라는 의미를 가지고 있다.
- 최소 특정 시간(`time`) 동안 최대 요청 (`max`)의 수를 알려줄 수 있다.

```http request
HTTP/1.1 200 OK
Connection: Keep-Alive
Content-Encoding: gzip
Content-Type: text/html; charset=utf-8
Date: Thu, 11 Aug 2016 15:23:13 GMT
Keep-Alive: timeout=5, max=1000
Last-Modified: Mon, 25 Jul 2016 04:32:39 GMT
Server: Apache

(body)
```
- `timeout` : 유휴 연결이 계속 열려 있어야 하는 최소한의 시간(초 단위)을 가리킨다.
- `max` : 연결이 닫히기 이전에 전송될 수 있는 최대 요청 수를 가리킨다.

최소 5초 동안 1000번의 요청을 할 경우에는 http connection 이 끊어지지 않는다.