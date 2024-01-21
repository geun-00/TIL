# 메시지, 국제화

## 메시지
> 화면에 보이는 상품명, 가격 등 ``label``에 있는 단어를 변경하려면 파일들을 다 찾아가면서 일일이 변경해야 한다. 왜냐하면 해당 HTML 파일에 모두 
> 하드코딩이 되어 있다.
> 
> 메시지 기능은 이런 메시지를 한 곳에서 관리할 수 있다.

## 국제화
> 메시지 기능을 각 나라별로 별도로 관리해서 서비스를 국제화할 수 있다.

## 메시지 파일 만들기
> 별도의 설정을 하지 않으면 ``messages``라는 이름으로 기본 등록된다.

- messages.properties(한글)
```properties
hello=안녕
hello.name=안녕 {0}
```

- messages_en.properties(영어)
```properties
hello=hello
hello.name=hello {0}
```

- MessageSource 인터페이스
```java
public interface MessageSource {
	@Nullable
	String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale);

	String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;
    
	String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException;
}
```

- 테스트 코드
```java
@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void helloMessage() {
        String result = ms.getMessage("hello", null, null);
        assertThat(result).isEqualTo("안녕");
    }
}
```
``Locale``이 ``null``이면 기본 이름 메시지 파일(messages)을 조회한다. 

```java
@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void notFoundMessageCode() {
        assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    @Test
    void notFoundMessageCodeDefaultMessage() {
        String result = ms.getMessage("no_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }
}
```
- ``notFoundMessageCode()``
  - ``no_code``라는 메시지가 없기 때문에 예외를 던진다.
- ``notFoundMessageCodeDefaultMessage()``
  - 메시지를 못 찾으면 기본 메시지(``defaultMessage``)가 반환된다.

```java
@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;
    
    @Test
    void argumentMessage() {
        String message = ms.getMessage("hello.name", new Object[]{"Spring"}, null);
        assertThat(message).isEqualTo("안녕 Spring");
    }
}
```
{0} 부분에 매개변수를 전달해서 치환할 수 있다.

```java
@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource ms;

    @Test
    void defaultLang() {
        assertThat(ms.getMessage("hello", null, null)).isEqualTo("안녕");
        assertThat(ms.getMessage("hello", null, Locale.KOREA)).isEqualTo("안녕");
    }

    @Test
    void enLang() {
        assertThat(ms.getMessage("hello", null, Locale.ENGLISH)).isEqualTo("hello");
    }
}
```
Locale 정보가 null이면 ``Locale.getDefault()``를 호출해서 시스템의 기본 Locale을 사용한다. messages_ko가 없으므로 messages,<br>
Locale 정보가 있지만 messages_ko가 없으므로 messages,<br>
Locale 정보가 ENGLISH이므로 message_en을 찾아서 사용

<br>

## 메시지 기능 적용
- messages.properties
```properties
label.item=상품
label.item.id=상품 ID
label.item.itemName=상품명
label.item.price=가격
label.item.quantity=수량

page.items=상품 목록
page.item=상품 상세
page.addItem=상품 등록
page.updateItem=상품 수정

button.save=저장
button.cancel=취소
```
타임리프의 메시지 표현식 ``#{...}``을 사용하면 된다. ``#{label.item}``

- HTML
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <link th:href="@{/css/bootstrap.min.css}"
          href="../css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container {
            max-width: 560px;
        }
    </style>
</head>
<body>

<div class="container">

    <div class="py-5 text-center">
        <h2 th:text="#{page.addItem}">상품 등록</h2>
    </div>

    <form action="item.html" th:action th:object="${item}" method="post">
        <div>
            <label for="itemName" th:text="#{label.item.itemName}"></label>
            <input type="text" th:field="*{itemName}" class="form-control" placeholder="이름을 입력하세요">
        </div>
        <div>
            <label for="price" th:text="#{label.item.price}"></label>
            <input type="text" th:field="*{price}" class="form-control" placeholder="가격을 입력하세요">
        </div>
        <div>
            <label for="quantity" th:text="#{label.item.quantity}"></label>
            <input type="text" th:field="*{quantity}" class="form-control" placeholder="수량을 입력하세요">
        </div>

        <hr class="my-4">

        <div class="row">
            <div class="col">
                <button class="w-100 btn btn-primary btn-lg" type="submit" th:text="#{button.save}"></button>
            </div>
            <div class="col">
                <button class="w-100 btn btn-secondary btn-lg"
                        onclick="location.href='items.html'"
                        th:onclick="|location.href='@{/message/items}'|"
                        type="button" th:text="#{button.cancel}"></button>
            </div>
        </div>

    </form>

</div> <!-- /container -->
</body>
</html>
```
이제 properties 파일만 수정하면 편리하게 변경할 수 있다.

메시지에 파라미터 기능은 ``th:text="#{hello.name(${item.itemName})}"`` 처럼 쓸 수 있다.

<br>

## 국제화 적용
- messages_en.properties
```properties
label.item=Item
label.item.id=Item ID
label.item.itemName=Item Name
label.item.price=price
label.item.quantity=quantity

page.items=Item List
page.item=Item Detail
page.addItem=Item Add
page.updateItem=Item Update

button.save=Save
button.cancel=Cancel
```
이 파일만 추가해줘도 기존 HTML은 수정할 것이 없다.

웹 브라우저의 언어 설정 값을 변경하면 ``Accept-Language``의 값이 변경되는데 이것은 클라이언트가 서버에 기대하는 언어 정보를 담아서 요청하는 HTTP 요청 헤더이다.

### 스프링의 국제화 메시지 선택
> 메시지 기능은 ``Locale`` 정보를 알아야 언어를 선택할 수 있는데 스프링은 기본으로 ``Accept-Language``헤더의 값을 사용한다.

- ``LocaleResolver`` 인터페이스
```java
public interface LocaleResolver {
	Locale resolveLocale(HttpServletRequest request);
    
	void setLocale(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Locale locale);
}
```
``Locale`` 선택 방식을 변경하려면 ``LocaleResolver``의 구현체를 변경해서 쿠키가 세션 기반의 고객이 직접 Locale 선택 기능을 사용할 수 있도록 할 수 있다.