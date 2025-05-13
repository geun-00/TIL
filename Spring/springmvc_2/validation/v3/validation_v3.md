# 검증 V3
- `Bean Validation` 
  - Java 애플리케이션에서 객체의 유효성 검증을 위해 도입된 기술로, 데이터 검증 로직을 일관되게 적용할 수 있도록 하는 표준화된 API를 제공한다.
  - 다양한 Java 프레임워크 (Spring, Jakarta EE 등)에서 공통으로 사용할 수 있도록 JSR-303(Bean Validation 1.0)과 JSR-380(Bean Validation 2.0) 표준이 제정되었다.

> 참고 : [어노테이션 모음](https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#validator-defineconstraints-spec)

---

## V1

의존성 추가
```text
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

> `Bean Validation` 표준을 구현한 기술들은 여러 가지가 있는데, 주로 `Hibernate Valiator`를 사용한다.
> 
> **Java 표준**
> 
> ![img.png](image/img.png)
> 
> **구현 기술**
> 
> ![img_1.png](image/img_1.png)
> 
> **주요 클래스**
> 1. `Validation` : `Bean Validation`의 진입점
> 2. `ValidatorFactory` : 빈 검증기 생성자
> 3. `Validator` : 빈 검증기
> 4. `ConstraintViolation` : 제약 조건 위반을 표현

사용 예시
```java
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class Item {
    
    private Long id;
    
    @NotBlank
    private String itemName;
    
    @NotNull @Range(min = 1000, max = 1_000_000)
    private Integer price;
    
    @NotNull @Max(9999)
    private Integer quantity;
    
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
```
- `@NotBlank` : 빈 값 + 공백만 있는 경우를 허용하지 않는다.
- `@NotNull` : `null`을 허용하지 않는다.
- `@Range` : 범위 안에 값이어야 한다.
- `@Max` : 최대값 지정

컨트롤러
```java
@PostMapping("/add")
public String addItem(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

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
        return "validation/v3/addForm";
    }

    //성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v3/items/{itemId}";
}
```

- 스프링 부트는 `spring-boot-starter-validation` 라이브러리를 넣으면 자동으로 `Bean Validator`를 인지하고 스프링에 통합한다.
- 스프링 부트는 자동으로 글로벌 `Validator`로 등록하기 때문에 `@Valid`나 `@Validated`만 적용하면 된다. 검증 오류가 발생하면 알아서 
`FieldError`, `ObjectError`를 생성해서 `BindingResult`에 담아준다.

**검증 순서**
- `@ModelAttribute` 각각의 필드에 타입 변환을 시도한다.
  - 성공하면 다음으로
  - 실패하면 `typeMissMatch`로 `FieldError`추가
- `Validator` 적용

`@ModelAttribute` 각각의 필드에 타입 변환에 성공해야 `BeanValidation`이 적용된다. 예를 들어 Integer 타입 필드에 String으로 들어오면 Integer 타입 필드는
`BeanValidation`이 적용 되는 것이 아닌 `typeMissMatch FieldError`가 추가된다.

### Bean Validation 에러 코드

`Bean Validation`에서 검증 메시지를 하드 코딩하지 않고 `MessageResource`를 통해 다양한 언어로 메시지를 관리할 수 있다.

errors.properties
```properties
NotBlank={0} 공백X
Range={0}, {2} ~ {1} 허용
Max={0}, 최대 {1}
```
`{0}`은 필드명이고 `{1}`,`{2}`... 는 각 어노테이션 마다 다르다.

**BeanValidator**는 다음 순서로 메시지를 찾는다.
1. 생성된 메시지 에러 코드 순서대로 `messageSource`에서 메시지를 찾는다.
2. 어노테이션의 `message` 속성에서 메시지를 찾는다.
3. 라이브러리가 제공하는 기본 값을 사용한다.

![img_2.png](image/img_2.png)

---

## V2

### Bean Validation 한계
등록과 수정은 서로 요구사항이 다를 수 있다. 그래서 검증 조건의 충돌이 발생할 수 있고 같은 `BeanValidation`을 적용할 수 없다.

이럴 때는 두 가지 해결방안이 있다.
1. BeanValidation에 groups 적용하기
2. 저장용, 수정용 DTO를 각각 만들기 ([V4](https://github.com/geun-00/TIL/blob/main/Spring/springmvc_2/validation/v4/validation_v4.md)에서)

### BeanValidation에 groups 적용하기

저장용 groups
```java
public interface SaveCheck { }
```

수정용 groups
```java
public interface UpdateCheck { }
```

Item groups 적용
```java
@Data
public class Item {

    @NotNull(groups = UpdateCheck.class)
    private Long id;

    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    @NotBlank
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() { }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
```

컨트롤러
```java
// 저장
@PostMapping("/add")
public String addItem2(@Validated(SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

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
        return "validation/v3/addForm";
    }

    //성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v3/items/{itemId}";
}
// 수정
@PostMapping("/{itemId}/edit")
public String editV2(@PathVariable("itemId") Long itemId, @Validated(UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {

    //특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
        int resultPrice = item.getPrice() * item.getQuantity();
        if (resultPrice < 10000) {
            bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
        }
    }

    if (bindingResult.hasErrors()) {
        log.info("errors={}", bindingResult);
        return "validation/v3/editForm";
    }

    itemRepository.update(itemId, item);
    return "redirect:/validation/v3/items/{itemId}";
}
```
`@Validated`에 각각 목적에 맞게 `SaveCheck`, `UpdateCheck`를 적용해준다.

> `@Valid`는 groups를 적용할 수 있는 기능이 없다. groups를 사용하려면 `@Validated`를 사용해야 한다.