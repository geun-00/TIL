# 자바 - 파일 입출력 성능 최적화

파일을 효과적으로 더 빨리 읽고 쓰는 방법에 대해서 점진적으로 알아보자.

다음은 공통으로 사용할 상수 정보이다.

```java
public abstract class BufferedConst {
    public static final String FILE_NAME = "temp/buffered.dat";
    public static final int FILE_SIZE = 10 * 1024 * 1024; //10MB
    public static final int BUFFER_SIZE = 8192; //8KB
}
```

---

## V1

먼저 가장 단순한 `FileOutputStream`을 사용해서 1byte씩 파일을 저장해본다.

```java
import java.io.FileOutputStream;
import java.io.IOException;

import static io.buffered.BufferedConst.FILE_NAME;
import static io.buffered.BufferedConst.FILE_SIZE;

public class CreateFileV1 {
    public static void main(String[] args) throws IOException {

        FileOutputStream fos = new FileOutputStream(FILE_NAME);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < FILE_SIZE; i++) {
            fos.write(1);
        }

        fos.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File created: " + FILE_NAME);
        System.out.println("File size: " + FILE_SIZE / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```

```text
File created: temp/buffered.dat
File size: 10MB
Time taken: 67212ms
```

- 약 1,000만번(10 * 1024 * 1024)의 반복문으로 10MB의 파일이 만들어진다.
- 실행 시간이 매우 오래 걸리는 것을 확인할 수 있다.

이번에도 가장 단순한 `FileInputStream`을 사용해서 1byte씩 파일에서 데이터를 읽어보자.

```java
import java.io.FileInputStream;
import java.io.IOException;

import static io.buffered.BufferedConst.FILE_NAME;

public class ReadFileV1 {
    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream(FILE_NAME);
        long startTime = System.currentTimeMillis();

        int fileSize = 0;

        int data;
        while ((data = fis.read()) != -1) {
            fileSize++;
        }

        fis.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File created: " + FILE_NAME);
        System.out.println("File size: " + fileSize / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```

```text
File created: temp/buffered.dat
File size: 10MB
Time taken: 22798ms
```

10MB 파일 하나를 읽고 쓰는 데 상당한 시간이 걸렸다. 이렇게 오래 걸리는 이유는
자바에서 1byte씩 디스크에 데이터를 전달하기 때문이다. 디스크는 1,000만 번의 반복으로
1byte의 데이터를 받아서 1byte의 데이터를 쓴다. (실제로는 내부적으로 운영 체제나 하드웨어 레벨에서 최적화가 발생한다.)

`write()`나 `read()`를 호출할 때마다 OS의 시스템 콜을 통해 파일을 읽거나 쓰는 명령어를 전달한다. 
이러한 시스템 콜은 상대적으로 무거운 작업이다.

HDD, SSD 같은 장치들도 하나의 데이터를 읽고 쓸 때마다 필요한 시간이 있다. HDD의 경우 더욱 느린데,
물리적으로 디스크의 회전이 필요하다. 이러한 무거운 작업을 무려 1,000만 번 반복하는 것이다.

---

## V2

이번에는 1byte씩 데이터를 전달하는 것이 아니라 `byte[]`를 통해 배열에 담아서
한 번에 여러 byte를 전달해보자.

```java
import java.io.FileOutputStream;
import java.io.IOException;

import static io.buffered.BufferedConst.BUFFER_SIZE;
import static io.buffered.BufferedConst.FILE_NAME;
import static io.buffered.BufferedConst.FILE_SIZE;

public class CreateFileV2 {
    public static void main(String[] args) throws IOException {

        FileOutputStream fos = new FileOutputStream(FILE_NAME);

        long startTime = System.currentTimeMillis();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bufferIndex = 0;

        for (int i = 0; i < FILE_SIZE; i++) {
            buffer[bufferIndex++] = 1;

            //버퍼가 가득 차면 쓰고, 버퍼를 비운다.
            if (bufferIndex == BUFFER_SIZE) {
                fos.write(buffer);
                bufferIndex = 0;
            }
        }

        //끝 부분에 오면 버퍼가 가득차지 않고 남아있을 수 있다. 버퍼에 남은 부분 쓰기
        if (bufferIndex > 0) {
            fos.write(buffer, 0, bufferIndex);
        }

        fos.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File created: " + FILE_NAME);
        System.out.println("File size: " + FILE_SIZE / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```

데이터를 `BUFFER_SIZE`만큼 `byte[]`에 모아서 전달한다.

```text
File created: temp/buffered.dat
File size: 10MB
Time taken: 35ms
```

1byte씩 전달하는 방식과 비교했을 때 월등히 수행 시간이 빨라졌다.

> **버퍼 크기에 따른 쓰기 성능**
> 
> `BUFFER_SIZE`에 따라 쓰기 성능은 다음과 같다.
> 
> - **1** : 68616ms
> - **2** : 33745ms
> - **3** : 22398ms
> - **10** : 7013ms
> - **100** : 837ms
> - **1000** : 141ms
> - **2000** : 75ms
> - **4000** : 57ms
> - **8000** : 34ms
> - **80000** : 20ms
> 
> 많은 데이터를 한 번에 전달하면 성능을 최적화할 수 있다. 시스템 콜도 줄어들고, HDD, SSD 같은 장치들의
> 작동 횟수도 줄어든다.
> 
> 그런데 버퍼의 크기가 커진다고 해서 속도가 계속 줄어들지는 않는다. 왜냐하면 디스크나
> 파일 시스템에서 데이터를 읽고 쓰는 기본 단위가 보통 4KB(4096byte)나 8KB(8192byte) 이기 때문이다.
> 
> 결국 버퍼에 많은 데이터를 담아서 보내도 디스크나 파일 시스템에서 해당 단위로 나누어
> 저장하기 때문에 효율에는 한계가 있다. 버퍼의 크기는 보통 4KB, 8KB 정도로 잡는 것이 효율적이다.

이제 버퍼를 사용해 데이터를 읽어보자.

```java
import java.io.FileInputStream;
import java.io.IOException;

import static io.buffered.BufferedConst.BUFFER_SIZE;
import static io.buffered.BufferedConst.FILE_NAME;

public class ReadFileV2 {
    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream(FILE_NAME);
        long startTime = System.currentTimeMillis();

        byte[] buffer = new byte[BUFFER_SIZE];

        int fileSize = 0;

        int size;
        while ((size = fis.read(buffer)) != -1) {
            fileSize += size;
        }

        fis.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File created: " + FILE_NAME);
        System.out.println("File size: " + fileSize / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```

```text
File created: temp/buffered.dat
File size: 10MB
Time taken: 5ms
```

읽기의 경우에도 버퍼를 사용하면 큰 성능 향상을 확인할 수 있다.

---

## V3

버퍼를 사용하여 큰 성능 향상을 얻을 수 있는 것을 확인했다. 하지만 직접 버퍼를 만들고
관리해야 하는 번거로운 단점이 있다.

이번에는 버퍼 기능을 내부에서 처리해주는 `BufferedOutputStream`을 사용해보자.

```java
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static io.buffered.BufferedConst.BUFFER_SIZE;
import static io.buffered.BufferedConst.FILE_NAME;
import static io.buffered.BufferedConst.FILE_SIZE;

public class CreateFileV3 {
    public static void main(String[] args) throws IOException {

        FileOutputStream fos = new FileOutputStream(FILE_NAME);
        BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < FILE_SIZE; i++) {
            bos.write(1);
        }
        bos.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File created: " + FILE_NAME);
        System.out.println("File size: " + FILE_SIZE / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```

- `BufferedOutputStream`은 내부에서 단순히 버퍼 기능만 제공한다. 따라서 반드시 대상 `OutputStream`이 있어야 한다.
- 추가로 사용할 버퍼의 크기도 함께 전달할 수 있다.
- 코드를 보면 버퍼를 위한 `byte[]`을 직접 다루지 않고, `V1`과 같이 단순하게 코드를 작성할 수 있다.

```text
File created: temp/buffered.dat
File size: 10MB
Time taken: 237ms
```

- `BufferedOutputStream`은 `OutputStream`을 상속받는다. 따라서 `OutputStream`과 같은 기능을 그대로 사용할 수 있다.
- `BufferedOutputStream`은 내부에 `byte[] buf`라는 버퍼를 가지고 있다. 버퍼가 가득 차면
`FileOutputStream`에 있는 `write()` 메서드를 호출한다. (생성자에서 `fos`를 전달함)
- `FileOutputStream`의 `write()`를 호출하면, 전달된 모든 `byte[]`를 시스템 콜로 OS에 전달하고 버퍼의 내용을 비운다.
- 만약에 버퍼가 다 차지 않아도 버퍼에 남아있는 데이터를 전달하려면 `flush()` 메서드를 호출하면 된다.
- 만약 버퍼에 데이터가 남아있는 상태에서 `close()` 메서드를 호출하면 먼저 내부에서 `flush()`를 호출한다.
따라서 버퍼에 남아있는 데이터를 모두 전달하고 비운다. 즉 `close()`를 호출해도 남은 데이터를
안전하게 저장할 수 있다.
- 버퍼가 비워지고 나면 `BufferedOutputStream`의 자원을 정리한다. 그리고 나서 다음 연결된 스트림(여기서는 `FileOutputStream`)의 `close()`를 호출한다.
여기서 핵심은 `close()`를 호출하면 연쇄적으로 `close()`가 호출된다는 점이다. 따라서 마지막에
연결한 `BufferedOutputStream`만 닫아주면 된다.

**만약 `BufferedOutputStream`을 닫지 않고, `FileOutputStream`만 직접 닫으면 어떻게 될까?**

- 이 경우 `BufferedOutputStream`의 `flush()`도 호출되지 않고, 자원도 정리되지 않는다. 따라서 남은 byte가
버퍼에 남아있게 되고, 파일에 저장되지 않는 문제가 발생한다.
- 따라서 스트림을 연결해서 사용하는 경우에는 마지막에 연결한 스트림을 반드시 닫아주어, 연쇄적으로 `close()`가 호출되도록
해야 한다.

### 기본 스트림, 보조 스트림

- `FileOutputStream`과 같이 단독으로 사용할 수 있는 스트림을 **기본 스트림**이라고 한다.
- `BufferedOutputStream`과 같이 단독으로 사용할 수 없고, 보조 기능을 제공하는 스트림을 **보조 스트림**이라고 한다.

보조 스트림은 다른 스트림과 연결되어 여러 가지 편리한 기능을 제공해주는 스트림이다. 보조 스트림은
자체적으로 입출력을 수행할 수 없기 때문에 입출력 소스로부터 직접 생성된 입출력 스트림에 연결해서
사용해야 한다.

입출력 스트림에 보조 스트림을 연결하려면 보조 스트림을 생성할 때 생성자 매개변수로 입출력 스트림을 제공하면 된다.

```java
보조스트림 변수 = new 보조스트림(입출력스트림);
```

예를 들어 위에서는 다음과 같다.

```java
FileOutputStream fos = new FileOutputStream(FILE_NAME);
BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER_SIZE); //입출력 스트림에 보조 스트림 연결
```

보조 스트림은 또 다른 보조 스트림과 연결되어 스트림 체인으로 구성할 수 있다.

```java
보조스트림2 변수 = new 보조스트림2(new 보조스트림1(new 입출력스트림));
```

자주 사용되는 보조 스트림은 다음과 같다.

| 보조 스트림                                                                        | 기능                   |
|-------------------------------------------------------------------------------|----------------------|
| InputStreamReader                                                             | 바이트 스트림을 문자 스트림으로 변환 |
| BufferedInputStream, BufferedOutputStream,<br/>BufferedReader, BufferedWriter | 입출력 성능 향상            |
| DataInputStream, DataOutputStream                                             | 기본 타입 데이터 입출력        |
| PrintStream, PrintWriter                                                      | 줄바꿈 처리 및 형식화된 문자열 출력 |
| ObjectInputStream, ObjectOutputStream                                         | 객체 입출력               |

이제 `BufferedInputStream`을 사용해서 데이터를 읽어보자.

```java
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static io.buffered.BufferedConst.BUFFER_SIZE;
import static io.buffered.BufferedConst.FILE_NAME;

public class ReadFileV3 {
    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream(FILE_NAME);
        BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
        
        long startTime = System.currentTimeMillis();

        int fileSize = 0;

        int data;
        while ((data = bis.read()) != -1) {
            fileSize++;
        }

        bis.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File created: " + FILE_NAME);
        System.out.println("File size: " + fileSize / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```

- `BufferedInputStream`은 `InputStream`을 상속받는다. 따라서 `InputStream`과 같은 기능을 그대로 사용할 수 있다.
- `BufferedInputStream`도 내부에 `byte[] buf`를 가지고 있어, 버퍼의 크기만큼 데이터를 불러오고 버퍼에 보관한다.
- `read()`를 호출하면 버퍼에 있는 byte를 반환한다. 버퍼가 비면 다시 버퍼 크기만큼 조회하는 것을 반복한다.

```text
File created: temp/buffered.dat
File size: 10MB
Time taken: 230ms
```

그런데 버퍼를 직접 다루는 것(`V2`)보다 `BufferedXxx`의 성능이 약간 떨어진다. 이 이유는 
바로 **동기화** 때문이다.

예를 들어 `BufferedOutputStream`의 `write()` 메서드는 내부적으로 다음과 같이 처리한다.

```java
@Override
public void write(int b) throws IOException {
    if (lock != null) {
        lock.lock();
        try {
            implWrite(b);
        } finally {
            lock.unlock();
        }
    } else {
        synchronized (this) {
            implWrite(b);
        }
    }
}
```

- `BufferedXxx` 클래스는 모두 동기화 처리가 되어 있다.
- 즉 락을 걸고 푸는 코드가 파일을 읽거나 쓸 때마다 호출된다는 뜻이다.

`BufferedXxx` 클래스는 자바 초창기에 만들어진 클래스로, 처음부터 멀티 스레드를 고려해서 만든 클래스이다.
따라서 멀티 스레드에 안전하지만 락을 걸고 푸는 동기화 코드로 인해 성능이 약간 저하될 수 있다.
하지만 싱글 스레드 상황에서는 동기화 락이 필요하지 않기 때문에 직접 버퍼를 다룰 때와 비교해서 성능이 떨어진다.

일반적인 상황이라면 이 정도 성능 차이는 크게 문제가 되지 않기 때문에 싱글 스레드여도 `BufferedXxx`를 사용하면 충분할 것이다.
물론 매우 큰 데이터를 다루어야 하고, 성능 최적화가 중요하다면 직접 버퍼를 다루는 방법을 고려해볼 수 있다.

아쉽지만 동기화 락이 없는 `BufferedXxx` 클래스는 없다. 꼭 필요한 상황이라면 `BufferedXxx`를 참고해서
동기화 락 코드를 제거한 클래스를 직접 만들어 사용하면 된다.

---

## V5

파일의 크기가 크지 않다면 간단하게 한 번에 쓰고 읽는 것도 좋은 방법이다.
이 방법은 성능이 가장 빠르지만, 결과적으로 메모리를 한 번에 많이 사용하기 때문에
파일의 크기가 작아야 한다.

```java
import java.io.FileOutputStream;
import java.io.IOException;

import static io.buffered.BufferedConst.FILE_NAME;
import static io.buffered.BufferedConst.FILE_SIZE;

public class CreateFileV4 {
    public static void main(String[] args) throws IOException {

        FileOutputStream fos = new FileOutputStream(FILE_NAME);
        
        long startTime = System.currentTimeMillis();

        byte[] buffer = new byte[FILE_SIZE];

        for (int i = 0; i < FILE_SIZE; i++) {
            buffer[i] = 1;
        }
        fos.write(buffer);

        fos.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File created: " + FILE_NAME);
        System.out.println("File size: " + FILE_SIZE / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```

```text
File created: temp/buffered.dat
File size: 10MB
Time taken: 19ms
```

데이터를 읽는 것도 `readAllBytes()`를 사용하면 모든 데이터를 한 번에 불러올 수 있다.

```java
import java.io.FileInputStream;
import java.io.IOException;

import static io.buffered.BufferedConst.FILE_NAME;

public class ReadFileV4 {
    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream(FILE_NAME);
        long startTime = System.currentTimeMillis();

        byte[] bytes = fis.readAllBytes();

        fis.close();

        long endTime = System.currentTimeMillis();
        
        System.out.println("File created: " + FILE_NAME);
        System.out.println("File size: " + bytes.length / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```

```text
File created: temp/buffered.dat
File size: 10MB
Time taken: 11ms
```

---

[이전 ↩️ - InputStream, OutputStream](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/io/iostream.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/Main.md)

[다음 ↪️ - 문자 다루기](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/io/text.md)