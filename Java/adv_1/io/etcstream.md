# 자바 - 기타 스트림

몇 가지 유용한 부가 기능을 제공하는 `PrintStream`, `DataOutputStream` 보조 스트림을 알아보자.

---

## PrintStream

`PrintStream`은 자주 사용하는 `System.out`에서 사용되는 스트림이다. `PrintStream`과
`FileOutputStream`을 조합하면 마치 콘솔에 출력하듯이 파일에 출력할 수 있다.

```java
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class PrintStreamEtcMain {
    public static void main(String[] args) throws FileNotFoundException {

        FileOutputStream fos = new FileOutputStream("temp/print.txt");
        PrintStream ps = new PrintStream(fos);

        ps.println("hello, java!");
        ps.println(10);
        ps.println(true);
        ps.printf("hello %s", "world");

        ps.close();
    }
}
```

**temp/print.txt**

```text
hello, java!
10
true
hello world
```

`PrintStream` 생성자에 `FileOutputStream`을 전달했다. 이제 이 스트림을 통해서
나가는 출력은 파일에 저장된다. 이 기능을 사용하면 마치 콘솔에 출력하는 것처럼
파일이나 다른 스트림에 문자를 출력할 수 있다.

## DataOutputStream

`DataOutputStream`을 사용하면 자바의 `String`, `int`, `double`, `boolean`같은 데이터 형을
편리하게 다룰 수 있다. 이 스트림과 `FileOutputStream`을 조합하면 파일에 자바
데이터 형을 편리하게 저장할 수 있다.

```java
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataStreamEtcMain {
    public static void main(String[] args) throws IOException {

        FileOutputStream fos = new FileOutputStream("temp/data.dat");
        DataOutputStream dos = new DataOutputStream(fos);

        dos.writeUTF("회원A");
        dos.writeInt(20);
        dos.writeDouble(10.5);
        dos.writeBoolean(true);

        dos.close();

        FileInputStream fis = new FileInputStream("temp/data.dat");
        DataInputStream dis = new DataInputStream(fis);

        //저장한 순서대로 읽기 주의
        System.out.println(dis.readUTF());
        System.out.println(dis.readInt());
        System.out.println(dis.readDouble());
        System.out.println(dis.readBoolean());

        dis.close();
    }
}
```

```text
회원A
20
10.5
true
```

자바 데이터 타입을 사용하면서 여러 가지 데이터를 편리하게 저장하고 불러올 수 있다.
이 스트림을 사용할 때 주의할 점은 저장한 순서대로 읽어야 한다는 것이다. 그렇지 않으면
잘못된 데이터가 조회될 수 있다.



---

[이전 ↩️ - 문자 다루기](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/io/text.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/Main.md)

[다음 ↪️ - I/O 활용 예제](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/io/iouse.md)