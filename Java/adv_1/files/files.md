# 자바 - File, Files

자바에서 파일 또는 디렉토리를 다룰 때 `File` 또는 `Files`, `Path` 클래스를
사용할 수 있다. 이 클래스들을 사용하여 파일이나 폴더를 생성 및 삭제, 정보를 확인할 수 있다.

---

## File

```java
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class OldFileMain {
    public static void main(String[] args) throws IOException {

        File file = new File("temp/example.txt");
        File directory = new File("temp/exampleDir");

        System.out.println("File exists: " + file.exists());

        boolean created = file.createNewFile();
        System.out.println("File created: " + created);

        boolean dirCreated = directory.mkdir();
        System.out.println("dirCreated = " + dirCreated);

//        boolean deleted = file.delete();
//        System.out.println("deleted = " + deleted);

        System.out.println("Is file: " + file.isFile());
        System.out.println("Is directory: " + directory.isDirectory());
        System.out.println("File name: " + file.getName());
        System.out.println("File size: " + file.length() + " bytes");

        File newFile = new File("temp/newExample.txt");
        boolean renamed = file.renameTo(newFile);
        System.out.println("renamed = " + renamed);

        long lastModified = newFile.lastModified();
        System.out.println("lastModified = " + new Date(lastModified));
    }
}
```

```text
File exists: false
File created: true
dirCreated = true
Is file: true
Is directory: true
File name: example.txt
File size: 0 bytes
renamed = true
lastModified = Mon Feb 03 14:56:34 KST 2025
```

- 위 코드를 실행하면 `temp/example.txt` 파일과 `temp/exampleDir` 폴더를 생성한다.
- `File`은 파일과 디렉토리를 둘 다 다루며, `File` 객체를 생성했다고 해서 파일이나 디렉토리가
바로 만들어지는 것은 아니다. 그리고 경로에 실제 파일이나 디렉토리가 없더라도 예외가 발생하지 않는다.
- `exists()`가 `false`를 반환하면 `createNewFile()`, `mkdir()`같은 메서드를 호출해
파일 또는 폴더를 생성할 수 있다.

---

## Files

`File` 클래스는 자바 1.0에서 등장했고, 이후 자바 1.7에서 `File` 클래스를
대체할 `Files`와 `Path`가 등장했다.

`Files`의 특징은 다음과 같다.

- 성능과 편의성이 모두 개선되었다.
- `File`은 과거의 호환을 유지하기 위해 남겨둔 기능이다.
- `Files`에는 수 많은 유틸리티 기능이 있다.

`Files`를 사용할 때 파일이나 디렉토리의 경로는 `Path` 클래스를 사용해야 한다.

```java
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

public class NewFilesMain {
    public static void main(String[] args) throws IOException {

        Path file = Path.of("temp/example.txt");
        Path directory = Path.of("temp/exampleDir");

        //파일이나 디렉토리 존재 여부
        boolean exists = Files.exists(file);
        System.out.println("exists = " + exists);

        //새 파일 생성
        try {
            Files.createFile(file);
            System.out.println("File created");
        } catch (FileAlreadyExistsException e) {
            System.out.println(file + " File already exists");
        }

        //새 디렉토리 생성
        try {
            Files.createDirectory(directory);
        } catch (FileAlreadyExistsException e) {
            System.out.println(directory + " Directory already exists");
        }

        //파일이나 디렉토리 삭제
//        Files.delete(file);
//        System.out.println("File deleted");

        System.out.println("Is regular file: " + Files.isRegularFile(file)); //일반 파일인지 확인
        System.out.println("Is Directory: " + Files.isDirectory(directory)); //디렉토리인지 확인
        System.out.println("File name: " + file.getFileName()); //파일이나 디렉토리의 이름을 반환
        System.out.println("File size: " + Files.size(file) + " bytes"); //파일의 크기를 바이트 단위로 반환

        Path newFile = Path.of("temp/newExample.txt");
        //파일의 이름을 변경하거나 이동
        Files.move(file, newFile, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File moved/renamed");

        System.out.println("Last modified: " + Files.getLastModifiedTime(newFile)); //마지막으로 수정된 시간을 반환

        //파일의 기본 속성들을 한 번에 읽기
        BasicFileAttributes attrs = Files.readAttributes(newFile, BasicFileAttributes.class);

        System.out.println("===== Attributes =====");
        System.out.println("Creation time: " + attrs.creationTime());
        System.out.println("isDirectory: " + attrs.isDirectory());
        System.out.println("isRegularFile: " + attrs.isRegularFile());
        System.out.println("isSymbolicLink: " + attrs.isSymbolicLink());
        System.out.println("size: " + attrs.size());
    }
}
```

```text
exists = false
File created
Is regular file: true
Is Directory: true
File name: example.txt
File size: 0 bytes
File moved/renamed
Last modified: 2025-02-03T06:21:47.9724475Z
===== Attributes =====
Creation time: 2025-02-03T06:21:47.9724475Z
isDirectory: false
isRegularFile: true
isSymbolicLink: false
size: 0
```

`Files`는 직접 생성할 수 없고, 정적 메서드를 통해 기능을 제공한다.

---

## 경로 표시

```java
import java.io.File;
import java.io.IOException;

public class OldFilePath {
    public static void main(String[] args) throws IOException {

        File file = new File("temp/..");
        System.out.println("path = " + file.getPath());

        //절대 경로
        System.out.println("Absolute path = " + file.getAbsolutePath());

        //정규 경로
        System.out.println("Canonical path = " + file.getCanonicalPath());

        File[] files = file.listFiles();
        for (File f : files) {
            System.out.println((f.isFile() ? "F" : "D") + " | " + f.getName());
        }
    }
}
```
```text
path = temp\..
Absolute path = C:\Users\User\Desktop\study\java-adv2\temp\..
Canonical path = C:\Users\User\Desktop\study\java-adv2
F | .gitignore
D | .idea
F | java-adv2.iml
D | out
D | src
D | temp
```

- **절대 경로** : 경로의 처음부터 입력한 모든 경로를 다 표현
- **정규 경로** : 경로의 계산이 모두 끝난 경로, 하나만 존재한다.
  - 위 코드에서 `..`은 바로 위의 상위 디렉토리를 뜻한다. 이런 경로의 계산을
    모두 처리하면 하나의 경로만 남는다.
  - 예를 들어 절대 경로는 다음 2가지 경로가 모두 가능하다.
    - `C:\Users\User\Desktop\study\java-adv2`
    - `C:\Users\User\Desktop\study\java-adv2\temp\..`
  - 하지만 정규 경로는 다음 하나만 가능하다.
    - `C:\Users\User\Desktop\study\java-adv2`
- `listFiles()` 메서드는 현재 경로에 있는 모든 파일 또는 디렉토리를 반환한다.
위 코드에서 파일이면 `F`, 디렉토리면 `D`로 표현했다.

위 예시를 `Files` 클래스로 만들면 다음과 같다.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class NewFilesPath {
    public static void main(String[] args) throws IOException {

        Path path = Path.of("temp/..");
        System.out.println("path = " + path);

        //절대 경로
        System.out.println("Absolute path = " + path.toAbsolutePath());

        //정규 경로
        System.out.println("Canonical path = " + path.toRealPath());

        Stream<Path> pathStream = Files.list(path);
        List<Path> list = pathStream.toList();
        pathStream.close();

        for (Path p : list) {
            System.out.println((Files.isRegularFile(p) ? "F" : "D") + " | " + p.getFileName());
        }
    }
}
```
```text
path = temp\..
Absolute path = C:\Users\User\Desktop\study\java-adv2\temp\..
Canonical path = C:\Users\User\Desktop\study\java-adv2
F | .gitignore
D | .idea
F | java-adv2.iml
D | out
D | src
D | temp
```

`Files.list(path)` 메서드는 현재 경로에 있는 모든 파일 또는 디렉토리를
`Stream`으로 반환한다.

---

## Files 문자 파일 읽기

문자로 된 파일을 읽고 쓸 때 `FileReader`, `FileWriter`같은 복잡한 스트림 클래스를 사용해야 했다.
그리고 모든 문자를 읽기 위한 반복문이 필요하고, 한 줄 단위로 파일을 읽기 위해
`BufferedReader`와 같은 스트림 클래스를 추가해야 했다. ([참고](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/io/text.md#v4))

`Files`는 이런 문제를 깔끔하게 해결해준다.

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Files 모든 문자 읽기
 */
public class ReadTextFileV1 {

    public static final String PATH = "temp/hello2.txt";

    public static void main(String[] args) throws IOException {

        String writeString = "abc\n가나다";
        System.out.println("=== Write String ===");
        System.out.println(writeString);

        Path path = Path.of(PATH);

        //파일에 쓰기
        Files.writeString(path, writeString, UTF_8);

        //파일에서 읽기
        String readString = Files.readString(path, UTF_8);
        System.out.println("=== Read String === ");
        System.out.println(readString);
    }
}
```
```text
=== Write String ===
abc
가나다
=== Read String === 
abc
가나다
```

`Files` 클래스의 `writeString()`, `readString()`을 사용하여 편리하게
문자를 쓰고 읽을 수 있다.

```java
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Files 라인 단위로 읽기
 */
public class ReadTextFileV2 {

    public static final String PATH = "temp/hello2.txt";

    public static void main(String[] args) throws IOException {

        String writeString = "abc\n가나다";
        System.out.println("=== Write String ===");
        System.out.println(writeString);

        Path path = Path.of(PATH);

        //파일에 쓰기
        Files.writeString(path, writeString, UTF_8);

        //파일에서 읽기
        System.out.println("=== Read String === ");

        List<String> lines = Files.readAllLines(path, UTF_8);

        for (int i = 0; i < lines.size(); i++) {
            System.out.println((i + 1) + ": " + lines.get(i));
        }

        //파일에서 읽기 - 최적화
        System.out.println("=== Read String(최적화) === ");

        try (Stream<String> lineStream = Files.lines(path)) {
            lineStream.forEach(System.out::println);
        }
    }
}
```
```text
=== Write String ===
abc
가나다
=== Read String === 
1: abc
2: 가나다
=== Read String(최적화) === 
abc
가나다
```

- `Files.lines(path)`를 사용하면 파일을 한 줄 단위로 나누어 읽고, 메모리 사용량을 줄일 수 있다.
- 파일이 아주 크다면 한 번에 모든 파일을 메모리에 올리는 것보다 파일을 나누어 메모리에 
올리는 것이 더 나을 수 있다.
- `Files.readAllLines()`의 경우 모든 파일이 메모리에 불러진다.
- `Files.lines()`를 사용하면 파일을 한 줄 단위로 메모리에 올릴 수 있다. 
자바는 파일에서 한 줄당 필요한 만큼의 용량의 데이터만 메모리에 올려 처리한다. 처리가 끝나면
다음 줄을 호출하고, 기존에 사용한 데이터는 GC한다.
- `BufferedReader`로도 이렇게 한 줄 단위로 나누어 처리하는 것이 가능하다. 여기서 핵심은
매우 편리하게 문자를 나누어 처리하는 것이 가능하다는 점이다.

---

## 파일 복사 최적화

```java
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 파일 복사 최적화 - 임시 파일 생성
 */
public class CreateCopyFile {

    public static final int FILE_SIZE = 200 * 1024 * 1024; //200MB

    public static void main(String[] args) throws IOException {

        String fileName = "temp/copy.dat";

        long startTime = System.currentTimeMillis();

        FileOutputStream fos = new FileOutputStream(fileName);
        byte[] buffer = new byte[FILE_SIZE];

        fos.write(buffer);
        fos.close();

        long endTime = System.currentTimeMillis();

        System.out.println("File created: " + fileName);
        System.out.println("File size: " + FILE_SIZE / 1024 / 1024 + "MB");
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```
```text
File created: temp/copy.dat
File size: 200MB
Time taken: 922ms
```

이렇게 만든 임시 파일을 다른 파일로 복사하는 방법을 알아보자.

### V1

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 파일 복사 최적화 - V1
 */
public class FileCopyMainV1 {
    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();

        FileInputStream fis = new FileInputStream("temp/copy.dat");
        FileOutputStream fos = new FileOutputStream("temp/copy_new.dat");

        byte[] bytes = fis.readAllBytes();
        fos.write(bytes);

        fos.close();
        fis.close();

        long endTime = System.currentTimeMillis();

        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```
```text
Time taken: 910ms
```

- `FileInputStream`에서 `readAllBytes()`를 통해 한 번에 모든 데이터를 읽고
`write(bytes)`를 통해 한 번에 모든 데이터를 저장한다.
- 파일(`copy.dat`) -> 자바(`byte`) -> 파일(`copy_new.dat`)의 과정을 거친다.

### V2

```java
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 파일 복사 최적화 - V2
 */
public class FileCopyMainV2 {
    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();

        FileInputStream fis = new FileInputStream("temp/copy.dat");
        FileOutputStream fos = new FileOutputStream("temp/copy_new.dat");

        fis.transferTo(fos);

        fos.close();
        fis.close();

        long endTime = System.currentTimeMillis();

        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```
```text
Time taken: 179ms
```

- `InputStream`에는 `transferTo()`라는 메서드가 있다.(자바 9)
- `InputStream`에서 읽은 데이터를 바로 `OutputStream`으로 출력한다.
- `transferTo()`는 내부적으로 성능 최적화가 되어 있다.
- 이번에도 파일(`copy.dat`) -> 자바(`byte`) -> 파일(`copy_new.dat`)의 과정을 거친다.

### V3

```java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 파일 복사 최적화 - V3
 */
public class FileCopyMainV3 {
    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();

        Path source = Path.of("temp/copy.dat");
        Path target = Path.of("temp/copy_new.dat");

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

        long endTime = System.currentTimeMillis();

        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }
}
```
```text
Time taken: 99ms
```

- `V1`, `V2`에서는 "파일 -> 자바 -> 파일"과 같은 과정을 거쳤다.
- `Files.copy()`는 자바에 파일 데이터를 불러오지 않고, 운영체제의 
파일 복사 기능을 사용한다.
- 물론 이 기능은 파일에서 파일을 복사할 때만 유용하다. 만약 파일의 정보를 읽어서
처리해야 하거나, 스트림을 통해서 네트워크에 전달해야 한다면 스트림을 직접 사용해야 한다.

**파일을 다루어야 할 일이 있다면 항상 `Files`의 기능을 먼저 찾아보자.**

---

[이전 ↩️ - I/O 활용 예제](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/io/iouse.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/Main.md)

[다음 ↪️ - 네트워크 프로그램 예제](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/network/program.md)