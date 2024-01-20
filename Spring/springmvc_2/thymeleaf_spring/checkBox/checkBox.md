# 체크박스, 라디오 버튼, 셀렉트 박스

## 준비

- ItemType
```java
@Getter
@AllArgsConstructor
public enum ItemType {

    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description;
}
```

- DeliveryCode
```java
/**
 * FAST: 빠른 배송
 * NORMAL: 일반 배송
 * SLOW: 느린 배송
 */
@Data
@AllArgsConstructor
public class DeliveryCode {
    private String code;
    private String displayName;
}
```

- Item
```java
@Data
@NoArgsConstructor
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    private Boolean open; //판매 여부
    private List<String> regions; //등록 지역
    private ItemType itemType; //상품 종류
    private String deliveryCode; //배송 방식

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
```

<br>

## 체크 박스 - 단일1

```html
<div>판매 여부</div>
<div>
    <div class="form-check">
        <input type="checkbox" id="open" name="open" class="form-check-input">
        <label for="open" class="form-check-label">판매 오픈</label>
    </div>
</div>
```

이 때 체크박스를 선택하지 않으면 서버로 값 자체를 보내지 않는다. ``false``도 아니고 ``null``이 오는 것이다.

개발자의 의도에 따라 다르겠지만 혼란이 올 수도 있다. 이런 문제를 해결하기 위해 스프링 MVC는 히든 필드를 사용해서 약간의 트릭을 사용할 수 있다.

기존 체크 박스 이름 앞에 언더스코어(``_``)를 붙여서 전송하면 체크를 해제했다고 인식할 수 있다. 히든 필드는 항상 전송되는데 체크를 해제한 경우 ``open``은
전송되지 않고 ``_open``만 전송된다. 이 때 스프링 MVC는 체크를 해재했다고 판단한다.

- 히든 필드 추가 HTML
```html
<div>판매 여부</div>
<div>
    <div class="form-check">
        <input type="checkbox" id="open" name="open" class="form-check-input">
        <input type="hidden" name="_open" value="on"/> <!-- 히든 필드 추가 -->
        <label for="open" class="form-check-label">판매 오픈</label>
    </div>
</div>
```

- 체크 박스 체크
  - ``open=on&_open=on`` : 스프링 MVC가 ``open``에 값이 있는 것을 확인하고 사용하고 이 때 ``_open``은 무시한다.
- 체크 박스 미체크
  - ``_open=on`` : 스프링 MVC가 ``_open``만 있는 것을 확인하고 ``open``의 값이 체크되지 않았다고 인식한다. 이러면 ``null``이 아니라 ``false``가 넘어온다.

<br>

## 체크 박스 - 단일2
> 타임리프는 히든 필드를 편리하게 사용할 수 있다.


- HTML
```html
<form action="item.html" th:action th:object="${item}" method="post">
.
.
.
    <div>판매 여부</div>
    <div>
        <div class="form-check">
            <input type="checkbox" th:field="*{open}" class="form-check-input">
            <label for="open" class="form-check-label">판매 오픈</label>
        </div>
    </div>
```

``th:field``는 체크 박스의 히든 필드와 관련된 부분도 함께 자동으로 생성해준다. 또한 ``check`` 속성도 추가해주는데 이거는 값이 true인 경우 ``checked="checked"``를 
자동으로 처리해줘서 개발자가 직접 조건문을 넣는 등 로직을 만들 필요가 없게 된다.

<br>

## 체크 박스 - 멀티

- 컨트롤러 추가
```java
@ModelAttribute("regions")
public Map<String, String> regions() {
    Map<String, String> regions = new LinkedHashMap<>();
    regions.put("SEOUL", "서울");
    regions.put("BUSAN", "부산");
    regions.put("JEJU", "제주");
    return regions;
}
```
``LinkedHashMap`` : 순서를 보장받기 위함

``@ModelAttribute``를 컨트롤러 별도의 메소드에 적용하면 각 컨트롤러 메서드의 model에 자동으로 담기게 된다.

- HTML
```html
 <!-- multi checkbox -->
<form action="item.html" th:action th:object="${item}" method="post">
  .
  .
  .
<div>
    <div>등록 지역</div>
    <div th:each="region : ${regions}" class="form-check form-check-inline">
        <input type="checkbox" th:field="*{regions}" th:value="${region.key}" class="form-check-input">
        <label th:for="${#ids.prev('regions')}"
           th:text="${region.value}" class="form-check-label">서울</label>
    </div>
</div>
```

``th:for="${#ids.prev('regions')}"`` : ``th:each`` 루프에서 반복해서 HTML 태그를 생성할텐데 name은 같아도 되지만 ``id``는 같아선 안된다.
타임리프는 반복해서 태그를 만들 때 임의로 1, 2, 3 숫자를 뒤에 붙여준다.

- 렌더링 된 HTML
```html
 <!-- multi checkbox -->
  <div>
      <div>등록 지역</div>
      <div class="form-check form-check-inline">
          <input type="checkbox" value="SEOUL" class="form-check-input" disabled id="regions1" name="regions" checked="checked">
          <label for="regions1"
                 class="form-check-label">서울</label>
      </div>
      <div class="form-check form-check-inline">
          <input type="checkbox" value="BUSAN" class="form-check-input" disabled id="regions2" name="regions" checked="checked">
          <label for="regions2"
                 class="form-check-label">부산</label>
      </div>
      <div class="form-check form-check-inline">
          <input type="checkbox" value="JEJU" class="form-check-input" disabled id="regions3" name="regions">
          <label for="regions3"
                 class="form-check-label">제주</label>
      </div>
  </div>
```

id도 동적으로 자동 생성이 되고 check 속성까지 처리해준다. 그리고 ``th:field``는 히든필드까지 생성해 주기 때문에 아무것도 선택하지 않아도 ``null``이 아닌
빈 배열``[]``이 넘어온다.

<br>

## 라디오 버튼
> enum을 활용한 여러 선택지 중에 하나를 선택

- 컨트롤러 추가
```java
@ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }
```

- HTML
```html
<form action="item.html" th:action th:object="${item}" method="post">
. 
.
.
    <div>
       <div>상품 종류</div>
       <div th:each="type : ${itemTypes}" class="form-check form-check-inline">
           <input type="radio" th:field="*{itemType}" th:value="${type.name()}" class="form-check-input">
           <label th:for="${#ids.prev('itemType')}" th:text="${type.description}" class="form-check-label"></label>
       </div>
    </div>
```

- 렌더링 된 HTML
```html
<div>
      <div>상품 종류</div>
      <div class="form-check form-check-inline">
          <input type="radio" value="BOOK" class="form-check-input" id="itemType1" name="itemType">
          <label for="itemType1" class="form-check-label">도서</label>
      </div>
      <div class="form-check form-check-inline">
          <input type="radio" value="FOOD" class="form-check-input" id="itemType2" name="itemType">
          <label for="itemType2" class="form-check-label">음식</label>
      </div>
      <div class="form-check form-check-inline">
          <input type="radio" value="ETC" class="form-check-input" id="itemType3" name="itemType">
          <label for="itemType3" class="form-check-label">기타</label>
      </div>
</div>
```

체크 박스와 다르게 라디오 버튼은 히든 필드를 생성하지 않는다. 라디오 버튼은 이미 선택이 되어 있다면 수정시에도 항상 하나를 선택하도록 되어 있으므로(체크를 다시 풀 수 없다.)
별도의 히든 필드를 사용할 필요가 없다.

<br>

## 셀렉트 박스

- 컨트롤러 추가
```java
@ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }
```

- HTML
```html
<form action="item.html" th:action th:object="${item}" method="post">
.
.
.
    <div>
        <div>배송 방식</div>
        <select th:field="*{deliveryCode}" class="form-select">
            <option value="">==배송 방식 선택==</option>
            <option th:each="deliveryCode : ${deliveryCodes}" th:value="${deliveryCode.code}"
                    th:text="${deliveryCode.displayName}"></option>
        </select>
    </div>
```

- 렌더링 된 HTML
```html
<div>
  <div>배송 방식</div>
  <select class="form-select" disabled id="deliveryCode" name="deliveryCode">
    <option value="">==배송 방식 선택==</option>
    <option value="FAST">빠른 배송</option>
    <option value="NORMAL" selected="selected">일반 배송</option>
    <option value="SLOW">느린 배송</option>
  </select>
</div>
```
셀렉트 박스도 ``th:field``가 자동으로 selected="selected"를 처리해준다.