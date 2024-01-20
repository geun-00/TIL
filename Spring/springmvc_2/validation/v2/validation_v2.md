# 검증 V2
> ``BindingResult``

### V1
- 컨트롤러
```java
 @PostMapping("/add")
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000) {
            bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```
``BindingResult bindingResult``의 위치가 중요하다. ``@ModelAttribute Item item`` 다음에 와야 한다.

model.addAtrribute()를 해주지 않아도 뷰에 넘어간다.

- ``FieldError(objectName, field, defaultMessage)``
- ``ObjectError(objectName, defaultMessage)``

objectName은 @ModelAttribute의 이름이다.("item")


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
        .field-error {
            border-color: #dc3545;
            color: #dc3545;
        }
    </style>
</head>
<body>

<div class="container">

    <div class="py-5 text-center">
        <h2 th:text="#{page.addItem}">상품 등록</h2>
    </div>

    <form action="item.html" th:action th:object="${item}" method="post">

        <div th:if="${#fields.hasGlobalErrors()}">
            <p class="field-error" th:each="err : ${#fields.globalErrors()}" th:text="${err}">글로벌 오류 메시지</p>
        </div>

        <div>
            <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
            <input type="text" id="itemName" th:field="*{itemName}"
                   th:errorclass="field-error" class="form-control" placeholder="이름을 입력하세요">
            <div class="field-error" th:errors="*{itemName}">
                상품명 오류
            </div>
        </div>
        <div>
            <label for="price" th:text="#{label.item.price}">가격</label>
            <input type="text" id="price" th:field="*{price}"
                   th:errorclass="field-error" class="form-control" placeholder="가격을 입력하세요">
            <div class="field-error" th:errors="*{price}">
                가격 오류
            </div>
        </div>

        <div>
            <label for="quantity" th:text="#{label.item.quantity}">수량</label>
            <input type="text" id="quantity" th:field="*{quantity}"
                   th:errorclass="field-error" class="form-control" placeholder="수량을 입력하세요">
            <div class="field-error" th:errors="*{quantity}">
                수량 오류
            </div>

        </div>

        <hr class="my-4">

        <div class="row">
            <div class="col">
                <button class="w-100 btn btn-primary btn-lg" type="submit" th:text="#{button.save}">상품 등록</button>
            </div>
            <div class="col">
                <button class="w-100 btn btn-secondary btn-lg"
                        onclick="location.href='items.html'"
                        th:onclick="|location.href='@{/validation/v2/items}'|"
                        type="button" th:text="#{button.cancel}">취소</button>
            </div>
        </div>

    </form>

</div> <!-- /container -->
</body>
</html>
```

타임리프는 스프링의 ``BindingResult``를 활용해서 편리하게 검증 오류를 표현할 수 있는 기능을 제공한다.
- ``#fields`` 
  - ``BindingResult``가 제공하는 검증 오류에 접근할 수 있다.
- ``th:errors`` 
  - 해당 필드에 오류가 있는 경우 태그를 출력한다.(``th:if``의 편의 버전)
- ``th:errorclass``
  - ``th:field``에서 지정한 필드에 오류가 있으면 class 정보를 추가한다.

<br>

### V2
> ``BindingResult``는 스프링이 제공하는 검증 오류를 보관하는 객체다. ``@ModelAttribute``에 데이터 바인딩 시 오류가 발생해도 컨트롤러는 호출되기 때문에
> 화이트라벨 에러 화면을 보여주지 않는다.
> 
> ``BindingResult``와 ``Errors``가 있는데 둘 다 인터페이스이고 ``BindingResult``가 ``Errors``를 상속받고 있다.<br>
> ``Errors`` 인터페이스는 단순한 오류 저장과 조회 기능을 제공하고 ``BindingResult``는 여기에 더해 추가적인 기능들을 제공한다.

- 컨트롤러
```java
@PostMapping("/add")
public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수 입니다."));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null ,null, "수량은 최대 9,999 까지 허용합니다."));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",null ,null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
}
```

``FieldError``는 두 가지 생성자가 있다.
```java
public FieldError(String objectName, String field, String defaultMessage) {
		this(objectName, field, null, false, null, null, defaultMessage);
	}

public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure,
			      @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage) {

		super(objectName, codes, arguments, defaultMessage);
		Assert.notNull(field, "Field must not be null");
		this.field = field;
		this.rejectedValue = rejectedValue;
		this.bindingFailure = bindingFailure;
}
```

파라미터 목록
- ``objectName`` : 오류가 발생한 객체 이름
- ``field`` : 오류 필드
- ``rejectedValue`` : 사용자가 입력한 값(거절된 값)
- ``bindingFailure`` : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
- ``codes`` : 메시지 코드
- ``arguments`` : 메시지에서 사용하는 인자
- ``defaultMessage`` : 기본 오류 메시지

``FieldError``의 ``rejectedValue``에 오류 발생 시 사용자가 입력한 값을 저장하는데 ``th:field``에서 정상 상황이면 모델 객체의 값을 사용하고
오류가 발생하면 ``FieldError``보관한 값을 사용해서 값을 출력해준다.

<br>

### V3
> ``BindingResult``의 오류 메시지들을 관리할 수 있다.

- application.properties
```properties
spring.messages.basename=messages,errors
```

- errors.properties
```properties
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.
totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}
```

- 컨트롤러
```java
@PostMapping("/add")
public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000) {
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1_000_000}, null));
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"} ,new Object[]{9999}, null));
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",new String[]{"totalPriceMin"} ,new Object[]{10000, resultPrice}, null));
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```

<br>

### V4
> ``BindingResult``가 제공하는 ``rejectValue()``,``reject()``를 사용하여 더욱 단순화할 수 있다.

- 컨트롤러
```java
@PostMapping("/add")
public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        log.info("objectName={}", bindingResult.getObjectName());
        log.info("target={}", bindingResult.getTarget());

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 10000000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
}
```

- rejectValue()
```java
void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```
``BindingResult``는 ``@ModelAttribute``바로 뒤에 오기 때문에 어떤 객체를 대상으로 검증하는지 ``target``을 이미 알고 있다. 그래서 ``target``에 대한 정보는 없어도 된다.

``rejectValue()``를 사용하고 부터는 errorCode를 ``range``만으로 해결됐다. 비밀은 ``MessageCodesResolver``에 있다.

#### MessageCodesResolver
> 오류코드를 만들 때는 객체명과 필드명까지 생각해서 자세하게 만들 수도 있고 범용적으로 쓸 수 있게 단순하게 만들 수도 있다.

``MessageCodesResolver``는 인터페이스이고 ``DefaultMessageCodesResolver``는 기본 구현체이다.

``DefaultMessageCodesResolver``는 메시지 생성 규칙이 있다.
- 객체 오류(예: 오류 코드: `required`, object name: `item`)
  1. code + `.` + object name -> required.item
  2. code -> required
- 필드 오류(예: 오류 코드: `typeMissMatch`, object name: `"user"`, field: `"age"`, field type: `int`)
  1. code + `.` + object name + field -> `"typeMissMatch.user.age"`
  2. code + `.` + field -> `"typeMissMatch.age"`
  3. code + `.` + field type -> `"typeMissMatch.int"`
  4. code -> `"typeMissMatch`

동작 방식
- ``rejectValue()``, ``reject()``는 내부에서 ``MessageCodesResolver``를 사용하는데 여기에서 메시지 코드들을 생성한다.
- ``FieldError``, ``ObjectError``의 생성자는 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있고 ``MessageCodesResolver``를 통해서 생성된 순서대로
오류 코드를 보관한다.

타임리프 화면이 렌더링 될 때 `th:errors`가 실행되는데 이 때 오류가 있다면 생성된 오류 메시지 코드를 순서대로 돌아가면서 메시지를 찾는다. 없으면 디폴트 메시지를 출력한다.

- errors.properties 추가
```properties
#==ObjectError==
#Level1
totalPriceMin.item=상품의 가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}

#Level2 - 생략
totalPriceMin=전체 가격은 {0}원 이상이어야 합니다. 현재 값 = {1}


#==FieldError==
#Level1
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.

#Level2 - 생략

#Level3
required.java.lang.String = 필수 문자입니다.
required.java.lang.Integer = 필수 숫자입니다.
min.java.lang.String = {0} 이상의 문자를 입력해주세요.
min.java.lang.Integer = {0} 이상의 숫자를 입력해주세요.
range.java.lang.String = {0} ~ {1} 까지의 문자를 입력해주세요.
range.java.lang.Integer = {0} ~ {1} 까지의 숫자를 입력해주세요.
max.java.lang.String = {0} 까지의 숫자를 허용합니다.
max.java.lang.Integer = {0} 까지의 숫자를 허용합니다.

#Level4
required = 필수 값 입니다.
min= {0} 이상이어야 합니다.
range= {0} ~ {1} 범위를 허용합니다.
max= {0} 까지 허용합니다.
```
Level이 낮을수록 덜 구체적인 것이고 높을수록 구체적인 것이다. 메시지에 1번이 없으면 2번을 찾고, 2번이 없으면 3번을 찾는다. 크게 중요하지 않은 오류 메시지는
재활용할 수 있다.

검증 오류 코드는 **개발자가 직접 설정한 오류 코드(`rejectValue()`를 직접 호출)** 와 **스프링이 직접 검증 오류에 추가한 경우**로 나뉜다.

만약 price 필드에 문자를 입력하면 스프링은 타입 오류가 발생하는데  `MessageCodesResolver`를 통하면서 ``typeMissMatch``라는 오류 코드로 4가지 메시지 코드가 입력된다.
- errors.properties 추가
```properties
#추가
typeMismatch.java.lang.Integer=숫자를 입력해주세요.
typeMismatch=타입 오류입니다.
```
위 설정이 없다면 스프링이 직접 만든 사용자 친화적이지 못한 기본 생성된 메시지가 나온다. 설정을 하면 지정한 메시지가 그대로 출력된다.


### V5
> **검증 로직 분리**: 스프링은 검증을 체계적으로 제공하기 위해 `Validator` 인터페이스를 제공한다.

- `Validator` 인터페이스
```java
public interface Validator {
	boolean supports(Class<?> clazz);

	void validate(Object target, Errors errors);

	default Errors validateObject(Object target) {
		Errors errors = new SimpleErrors(target);
		validate(target, errors);
		return errors;
	}

	static <T> Validator forInstanceOf(Class<T> targetClass, BiConsumer<T, Errors> delegate) {
		return new TypedValidator<>(targetClass, targetClass::isAssignableFrom, delegate);
	}

	static <T> Validator forType(Class<T> targetClass, BiConsumer<T, Errors> delegate) {
		return new TypedValidator<>(targetClass, targetClass::equals, delegate);
	}
}
```

- ItemValidator
```java
@Component
public class ItemValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Item.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Item item = (Item) target;

        if (!StringUtils.hasText(item.getItemName())) {
            errors.rejectValue("itemName", "required");
        }
        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1_000_000) {
            errors.rejectValue("price", "range", new Object[]{1000, 10_000_000}, null);
        }
        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //특정 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }
    }
}
```

- 컨트롤러
```java
@PostMapping("/add")
public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        if (itemValidator.supports(item.getClass())) {
             itemValidator.validate(item, bindingResult);
        }

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
            log.info("errors={} ", bindingResult);
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
}
```

<br>

### V6
> `Validator` 인터페이스를 사용해서 검증기를 만들면 스프링의 추가적인 도움을 받을 수 있다.

- 컨트롤러
```java
@InitBinder
public void init(WebDataBinder dataBinder) {
        dataBinder.addValidators(itemValidator);
}

@PostMapping("/add")
public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증에 실패하면 다시 입력 폼으로
        if (bindingResult.hasErrors()) {
        log.info("errors={} ", bindingResult);
        return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
} 
```
`WebDataBinder`에 검증기를 추가하면 해당 컨트롤러에서 검증기를 자동으로 적용할 수 있다.<br>
`@InitBinder`는 해당 컨트롤러에만 영향을 준다.

- 동작 방식
  - `@Validated`는 검증기를 실행하라는 뜻의 어노테이션이다.
  - `WebDataBinder`에 등록된 검증기를 찾아서 실행하는데 여러 검증기를 등록한다면 구분이 필요한데 이 때 `supports()`가 사용된다.