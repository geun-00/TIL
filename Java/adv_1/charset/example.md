# 자바 - 문자 인코딩 예제

## 문자 집합 조회

```java
public static void main(String[] args) {

    //이용 가능한 모든 Charset, 자바 + OS
    SortedMap<String, Charset> charsets = Charset.availableCharsets();

    for (String charsetName : charsets.keySet()) {
        System.out.println("charsetName = " + charsetName);
    }

    System.out.println("=======================================");

    //문자로 조회(대소문자 구분 X)
    Charset ms949 = Charset.forName("MS949");
    System.out.println("ms949 = " + ms949);

    System.out.println("=======================================");

    //별칭 조회
    Set<String> aliases = ms949.aliases();
    for (String alias : aliases) {
        System.out.println("alias = " + alias);
    }

    System.out.println("=======================================");

    //UTF-8 문자로 조회
    Charset utf8 = Charset.forName("UTF-8");
    System.out.println("utf8(문자 조회) = " + utf8);

    //UTF-8 상수로 조회
    utf8 = StandardCharsets.UTF_8;
    System.out.println("utf8(상수 조회) = " + utf8);

    System.out.println("=======================================");

    //현재 시스템에서 사용하는 기본 문자 집합
    Charset defaultCharset = Charset.defaultCharset();
    System.out.println("defaultCharset = " + defaultCharset);
}
```

```text
charsetName = EUC-JP
charsetName = EUC-KR
...
charsetName = ISO-8859-1
charsetName = ISO-8859-13
charsetName = ISO-8859-15
...
charsetName = US-ASCII
charsetName = UTF-16
charsetName = UTF-16BE
charsetName = UTF-16LE
charsetName = UTF-32
charsetName = UTF-32BE
charsetName = UTF-32LE
charsetName = UTF-8
=======================================
ms949 = x-windows-949
=======================================
alias = ms_949
alias = windows-949
alias = windows949
alias = ms949
=======================================
utf8(문자 조회) = UTF-8
utf8(상수 조회) = UTF-8
=======================================
defaultCharset = UTF-8
```

UTF-8과 같이 자주 사용하는 문자 집합은 `StandardCharsets`에 상수로 지정되어 있다.

```java
package java.nio.charset;

public final class StandardCharsets {
    public static final Charset US_ASCII = sun.nio.cs.US_ASCII.INSTANCE;
    public static final Charset ISO_8859_1 = sun.nio.cs.ISO_8859_1.INSTANCE;
    public static final Charset UTF_8 = sun.nio.cs.UTF_8.INSTANCE;
    public static final Charset UTF_16BE = new sun.nio.cs.UTF_16BE();
    public static final Charset UTF_16LE = new sun.nio.cs.UTF_16LE();
    public static final Charset UTF_16 = new sun.nio.cs.UTF_16();   
}
```

---

## 인코딩 예제 - 1

```java
import java.nio.charset.Charset;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_8;

public class EncodingMain1 {

    private static final Charset EUC_KR = Charset.forName("EUC-KR");
    private static final Charset MS_949 = Charset.forName("MS949");

    public static void main(String[] args) {

        System.out.println("=== ASCII 영문 처리 ===");
        encoding("A", US_ASCII);
        encoding("A", ISO_8859_1);
        encoding("A", EUC_KR);
        encoding("A", UTF_8);
        encoding("A", UTF_16BE);

        System.out.println();

        System.out.println("=== 한글 지원 ===");
        encoding("가", EUC_KR);
        encoding("가", MS_949);
        encoding("가", UTF_8);
        encoding("가", UTF_16BE);
    }

    private static void encoding(String text, Charset charset) {
        byte[] bytes = text.getBytes(charset);
        System.out.printf("%s -> [%s] 인코딩 -> %s %sbyte\n",
            text, charset, Arrays.toString(bytes), bytes.length);
    }
}
```

- 문자를 컴퓨터가 이해할 수 있는 숫자(byte)로 변경하는 **인코딩**을 할 때는 중요한 점이 있는데,
문자를 byte로 변경하려면 반드시 문자 집합이 필요하다는 점이다.
- `String.getBytes()`는 인자가 없는 메서드를 포함해서 다양한 방식으로 문자 집합을
인자로 받도록 오버로딩 되어 있는데, **문자 집합을 지정하지 않으면 현재 시스템에서
사용하는 기본 문자 집합을 인코딩에 사용한다.**

```text
=== ASCII 영문 처리 ===
A -> [US-ASCII] 인코딩 -> [65] 1byte
A -> [ISO-8859-1] 인코딩 -> [65] 1byte
A -> [EUC-KR] 인코딩 -> [65] 1byte
A -> [UTF-8] 인코딩 -> [65] 1byte
A -> [UTF-16BE] 인코딩 -> [0, 65] 2byte

=== 한글 지원 ===
가 -> [EUC-KR] 인코딩 -> [-80, -95] 2byte
가 -> [x-windows-949] 인코딩 -> [-80, -95] 2byte
가 -> [UTF-8] 인코딩 -> [-22, -80, -128] 3byte
가 -> [UTF-16BE] 인코딩 -> [-84, 0] 2byte
```

- 영문의 경우, UTF-16은 ASCII와 호환되지 않아 2byte를 사용하는 것을 알 수 있다.
- 한글의 경우, EUC-KR과 MS949(CP949)는 한글 인코딩에 2byte를 사용하고 같은 값으로 인코딩한다.
  - EUC-KR을 확장해서 만든 것이 MS949(CP949)이다.

## 인코딩 예제 - 2

```java
import java.nio.charset.Charset;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_16BE;
import static java.nio.charset.StandardCharsets.UTF_8;

public class EncodingMain2 {

    private static final Charset EUC_KR = Charset.forName("EUC-KR");
    private static final Charset MS_949 = Charset.forName("MS949");

    public static void main(String[] args) {
        System.out.println("=== 영문 ASCII 인코딩 ===");
        test("A", US_ASCII, US_ASCII);
        test("A", US_ASCII, ISO_8859_1); //ASCII 확장(LATIN-1)
        test("A", US_ASCII, EUC_KR);     //ASCII 포함
        test("A", US_ASCII, MS_949);     //ASCII 포함
        test("A", US_ASCII, UTF_8);      //ASCII 포함
        test("A", US_ASCII, UTF_16BE);   //UTF-16 디코딩 실패

        System.out.println("\n=== 한글 인코딩 - 기본 ===");
        test("가", US_ASCII, US_ASCII);
        test("가", ISO_8859_1, ISO_8859_1);
        test("가", EUC_KR, EUC_KR);
        test("가", MS_949, EUC_KR);
        test("가", UTF_8, UTF_8);
        test("가", UTF_16BE, UTF_16BE);

        System.out.println("\n=== 한글 인코딩 - 복잡한 문자 ===");
        test("쀍", EUC_KR, EUC_KR);
        test("쀍", MS_949, MS_949);
        test("쀍", UTF_8, UTF_8);
        test("쀍", UTF_16BE, UTF_16BE);

        System.out.println("\n=== 한글 인코딩 - 디코딩이 다른 경우 ===");
        test("가", EUC_KR, MS_949);
        test("쀍", MS_949, EUC_KR); //인코딩 가능, 디코딩 X
        test("가", EUC_KR, UTF_8);
        test("가", MS_949, UTF_8);
        test("가", UTF_8, MS_949);

        System.out.println("\n=== 영문 인코딩 - 디코딩이 다른 경우 ===");
        test("A", EUC_KR, UTF_8);
        test("A", MS_949, UTF_8);
        test("A", UTF_8, MS_949);
        test("A", UTF_8, UTF_16BE);
    }

    private static void test(String text, Charset encodingCharset, Charset decodingCharset) {

        byte[] encoded = text.getBytes(encodingCharset);
        String decoded = new String(encoded, decodingCharset);

        System.out.printf(
            "%s -> [%s] 인코딩 -> %s %sbyte -> [%s] 디코딩 -> %s\n",
            text, encodingCharset, Arrays.toString(encoded), encoded.length, decodingCharset, decoded
        );
    }
}
```

**영문 ASCII 인코딩**

```text
=== 영문 ASCII 인코딩 ===
A -> [US-ASCII] 인코딩 -> [65] 1byte -> [US-ASCII] 디코딩 -> A
A -> [US-ASCII] 인코딩 -> [65] 1byte -> [ISO-8859-1] 디코딩 -> A
A -> [US-ASCII] 인코딩 -> [65] 1byte -> [EUC-KR] 디코딩 -> A
A -> [US-ASCII] 인코딩 -> [65] 1byte -> [x-windows-949] 디코딩 -> A
A -> [US-ASCII] 인코딩 -> [65] 1byte -> [UTF-8] 디코딩 -> A
A -> [US-ASCII] 인코딩 -> [65] 1byte -> [UTF-16BE] 디코딩 -> �
```

- ASCII는 UTF-16을 제외한 대부분의 문자 집합에 호환된다.

**한글 인코딩 - 기본**

```text
=== 한글 인코딩 - 기본 ===
가 -> [US-ASCII] 인코딩 -> [63] 1byte -> [US-ASCII] 디코딩 -> ?
가 -> [ISO-8859-1] 인코딩 -> [63] 1byte -> [ISO-8859-1] 디코딩 -> ?
가 -> [EUC-KR] 인코딩 -> [-80, -95] 2byte -> [EUC-KR] 디코딩 -> 가
가 -> [x-windows-949] 인코딩 -> [-80, -95] 2byte -> [EUC-KR] 디코딩 -> 가
가 -> [UTF-8] 인코딩 -> [-22, -80, -128] 3byte -> [UTF-8] 디코딩 -> 가
가 -> [UTF-16BE] 인코딩 -> [-84, 0] 2byte -> [UTF-16BE] 디코딩 -> 가
```

- 한글은 ASCII, ISO-8859-1로 인코딩할 수 없다.
- 그 외 문자 집합은 모두 한글 인코딩, 디코딩이 잘 수행되는 것을 확인할 수 있다.
- 그리고 EUC-KR, MS949 모두 같은 값을 반환하는 것을 확인할 수 있다.

**한글 인코딩 - 복잡한 문자**

```text
=== 한글 인코딩 - 복잡한 문자 ===
쀍 -> [EUC-KR] 인코딩 -> [63] 1byte -> [EUC-KR] 디코딩 -> ?
쀍 -> [x-windows-949] 인코딩 -> [-105, -51] 2byte -> [x-windows-949] 디코딩 -> 쀍
쀍 -> [UTF-8] 인코딩 -> [-20, -128, -115] 3byte -> [UTF-8] 디코딩 -> 쀍
쀍 -> [UTF-16BE] 인코딩 -> [-64, 13] 2byte -> [UTF-16BE] 디코딩 -> 쀍
```

- EUC-KR은 자주 사용하는 한글 2,350개만 표현할 수 있기 때문에, 복잡한 문자는 인코딩할 수 없다.

**한글 인코딩 - 디코딩이 다른 경우**

```text
=== 한글 인코딩 - 디코딩이 다른 경우 ===
가 -> [EUC-KR] 인코딩 -> [-80, -95] 2byte -> [x-windows-949] 디코딩 -> 가
쀍 -> [x-windows-949] 인코딩 -> [-105, -51] 2byte -> [EUC-KR] 디코딩 -> ��
가 -> [EUC-KR] 인코딩 -> [-80, -95] 2byte -> [UTF-8] 디코딩 -> ��
가 -> [x-windows-949] 인코딩 -> [-80, -95] 2byte -> [UTF-8] 디코딩 -> ��
가 -> [UTF-8] 인코딩 -> [-22, -80, -128] 3byte -> [x-windows-949] 디코딩 -> 媛�
```

- '쀍'과 같이 특수한 한글은 MS949로 인코딩 할 수 있지만, EUC-KR 문자 집합에 없으므로 EUC-KR로 디코딩할 수 없다.
- 한글을 인코딩할 때 UTF-8과 EUC-KR(MS949)은 서로 호환되지 않는다.

**영문 인코딩 - 디코딩이 다른 경우**

```text
=== 영문 인코딩 - 디코딩이 다른 경우 ===
A -> [EUC-KR] 인코딩 -> [65] 1byte -> [UTF-8] 디코딩 -> A
A -> [x-windows-949] 인코딩 -> [65] 1byte -> [UTF-8] 디코딩 -> A
A -> [UTF-8] 인코딩 -> [65] 1byte -> [x-windows-949] 디코딩 -> A
A -> [UTF-8] 인코딩 -> [65] 1byte -> [UTF-16BE] 디코딩 -> �
```

- ASCII에 포함되는 영문은 UTF-16을 제외한 대부분의 문자 집합에서 호환된다.

> **한글이 깨지는 가장 큰 2가지 이유**
> 
> - **EUC-KR(MS949)** 과 **UTF-8**이 서로 호환되지 않는다.
>   - **UTF-8**로 인코딩한 한글을 **EUC-KR(MS949)** 로 디코딩하거나, **EUC-KR(MS949)** 로 인코딩한 한글을
>     **UTF-8**로 디코딩하는 경우
> - **EUC-KR(MS949)** 또는 **UTF-8**로 인코딩한 한글을 한글을 지원하지 않는 **ISO-8859-1**로 디코딩 하는 경우

---

[이전 ↩️ - 컴퓨터와 문자 인코딩](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/charset/encoding.md)

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/Main.md)

[다음 ↪️ - I/O]()