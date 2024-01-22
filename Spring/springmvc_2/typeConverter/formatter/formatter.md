# 포맷터 - Formatter
> 객체를 특정한 포맷에 맞추어 문자로 출력하거나 또는 그 반대의 역할을 하는 것에 특화된 기능으로 컨버터의 특별한 버전이라고 볼 수 있다.

- `Converter` : 범용(객체 -> 객체)
- `Formatter` : 문자에 특화(객체->문자, 문자->객체) + 현지화(Locale)

- Formatter 인터페이스
```java
public interface Formatter<T> extends Printer<T>, Parser<T> {
}


public interface Printer<T> {
    String print(T object, Locale locale);
}

public interface Parser<T> {
    T parse(String text, Locale locale) throws ParseException;
}
```
- ` String print(T object, Locale locale)` : 객체를 문자로 변경
- `T parse(String text, Locale locale)` : 문자를 객체로 변경

<br>

- MyNumberFormatter
```java
@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text={}, locale={}", text, locale);
        //"1,000" -> 1000
        return NumberFormat.getInstance(locale).parse(text);
    }

    @Override
    public String print(Number object, Locale locale) {
        log.info("object={}, locale={}", object, locale);
        return NumberFormat.getInstance(locale).format(object);
    }
}
```
나라별로 다른 숫자 포맷을 만들어준다.

- 테스트 코드
```java
class MyNumberFormatterTest {

    MyNumberFormatter formatter = new MyNumberFormatter();

    @Test
    void parse() throws ParseException {
        Number result = formatter.parse("1,000", Locale.KOREA);
        assertThat(result).isEqualTo(1000L);
    }

    @Test
    void print() {
        String result = formatter.print(1000, Locale.KOREA);
        assertThat(result).isEqualTo("1,000");
    }
}
```

컨버전 서비스에는 컨버터만 등록할 수 있고 포맷터는 등록할 수 없다. 그런데 포맷터는 단순히 생각하면 특별한 컨버터일 뿐이다.

포맷터를 지원하는 컨버전 서비스를 사용하면 컨버전 서비스에 포맷터를 추가할 수 있다. 내부에서 어댑터 패턴을 사용해서 `Formatter`가 `Converter`처럼 동작하도록 지원한다.

`FormattingConversionService`는 포맷터를 지원하는 컨버전 서비스이다.<br>
`DefaultFormattingConversionService`는 `FormattingConversionService`에 기본적인 통화, 숫자 관련 몇 가지 기본 포맷터를 추가해서 제공한다.

- 테스트 코드
```java
@Test
void formattingConversionService() {
    DefaultFormattingConversionService service = new DefaultFormattingConversionService();
    service.addConverter(new StringToIpPortConverter());
    service.addConverter(new IpPortToStringConverter());

    service.addFormatter(new MyNumberFormatter());

    assertThat(service.convert("127.0.0.1:8080", IpPort.class)).isEqualTo(new IpPort("127.0.0.1", 8080));

    assertThat(service.convert(1000, String.class)).isEqualTo("1,000");
    assertThat(service.convert("1,000", Long.class)).isEqualTo(1000L);
}
```
`FormattingConversionService`는 `ConversionService` 관련 기능을 상속 받기 때문에 결과적으로 컨버터, 포맷터 모두 등록할 수 있다. 사용할 때는
`ConversionService`가 제공하는 `convert`를 사용하면 된다. 스프링 부트는 `DefaultFormattingConversionService`를 상속 받은 `WebConversionService`를 내부에서 사용한다.

### 포맷터 적용
- WebConfig
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 우선순위 주석처리
//        registry.addConverter(new StringToIntegerConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        registry.addFormatter(new MyNumberFormatter());
    }
}
```
`MyNumberFormatter`를 적용하기 위해 기존 컨버터는 주석처리한다.(컨버터가 우선하다.)

## 스프링이 제공하는 기본 포맷터
스프링은 어노테이션 기반으로 원하는 형식을 지정해서 사용할 수 있는 유용한 포맷터 두 가지를 기본으로 제공한다.

- 컨트롤러
```java
@Controller
public class FormatterController {

    @GetMapping("/formatter/edit")
    public String formatterForm(Model model) {
        Form form = new Form();
        form.setNumber(10000);
        form.setLocalDateTime(LocalDateTime.now());
        model.addAttribute("form", form);

        return "formatter-form";
    }

    @PostMapping("/formatter/edit")
    public String formatterEdit(@ModelAttribute("form") Form form) {
        return "formatter-view";
    }

    @Data
    static class Form {

        @NumberFormat(pattern = "###,###")
        private Integer number;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;
    }
}
```
- formatter-form.html
```java
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form th:object="${form}" th:method="post">
    number <input type="text" th:field="*{number}"><br/>
    localDateTime <input type="text" th:field="*{localDateTime}"><br/>
    <input type="submit"/>
</form>
</body>
</html>
```
- formatter-view.html
```java
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<ul>
    <li>${form.number}: <span th:text="${form.number}" ></span></li>
    <li>${{form.number}}: <span th:text="${{form.number}}" ></span></li>
    <li>${form.localDateTime}: <span th:text="${form.localDateTime}" ></span></li>
    <li>${{form.localDateTime}}: <span th:text="${{form.localDateTime}}" ></span></li>
</ul>
</body>
</html>
```
![img.png](image/img.png)

![img_1.png](image/img_1.png)