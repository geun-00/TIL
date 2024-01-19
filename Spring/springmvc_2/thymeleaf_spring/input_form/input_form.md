# 입력 폼 처리

## 입력 폼

- 컨트롤러
```java
@GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "form/addForm";
    }
```

- HTML
```html
 <form action="item.html" th:action th:object="${item}" method="post">
        <div>
            <label for="itemName">상품명</label>
            <input type="text" th:field="*{itemName}" class="form-control" placeholder="이름을 입력하세요">
        </div>
        <div>
            <label for="price">가격</label>
            <input type="text" th:field="*{price}" class="form-control" placeholder="가격을 입력하세요">
        </div>
        <div>
            <label for="quantity">수량</label>
            <input type="text" th:field="*{quantity}" class="form-control" placeholder="수량을 입력하세요">
        </div>
</form>
```
``th:object`` : 커맨드 객체 지정<br>
``*{...}`` : 선택 변수 식으로 ``th:object``에서 선택한 객체에 접근한다.<br>
``th:field`` : HTML 태그의 id, name, value 속성을 자동으로 만들어준다.(지정한 변수명으로)

- 렌더링 된 HTML
```html
<form action="" method="post">
        <div>
            <label for="itemName">상품명</label>
            <input type="text" class="form-control" placeholder="이름을 입력하세요" id="itemName" name="itemName" value="">
        </div>
        <div>
            <label for="price">가격</label>
            <input type="text" class="form-control" placeholder="가격을 입력하세요" id="price" name="price" value="">
        </div>
        <div>
            <label for="quantity">수량</label>
            <input type="text" class="form-control" placeholder="수량을 입력하세요" id="quantity" name="quantity" value="">
        </div>
</form>
```

<br>

## 수정 폼

- 컨트롤러
```java
 @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "form/editForm";
    }
```

- HTML
```html
<form action="item.html" th:action th:object="${item}" method="post">
        <div>
            <label for="id">상품 ID</label>
            <input type="text" id="id" class="form-control" th:field="*{id}" readonly>
        </div>
        <div>
            <label for="itemName">상품명</label>
            <input type="text" class="form-control" th:field="*{itemName}" >
        </div>
        <div>
            <label for="price">가격</label>
            <input type="text" class="form-control" th:field="*{price}">
        </div>
        <div>
            <label for="quantity">수량</label>
            <input type="text" class="form-control" th:field="*{quantity}">
        </div>
</form>
```

- 렌더링 된 HTML
```html
 <form action="" method="post">
        <div>
            <label for="id">상품 ID</label>
            <input type="text" id="id" class="form-control" readonly name="id" value="1">
        </div>
        <div>
            <label for="itemName">상품명</label>
            <input type="text" class="form-control" id="itemName" name="itemName" value="itemA" >
        </div>
        <div>
            <label for="price">가격</label>
            <input type="text" class="form-control" id="price" name="price" value="10000">
        </div>
        <div>
            <label for="quantity">수량</label>
            <input type="text" class="form-control" id="quantity" name="quantity" value="10">
        </div>
</form>
```

``th:field``가 id, name, value를 자동으로 만들어주기 때문에 편리하다.