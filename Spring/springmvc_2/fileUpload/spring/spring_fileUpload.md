# 스프링 파일 업로드
> 스프링은 `MultipartFile`이라는 인터페이스로 멀티파트 파일을 매우 편리하게 지원한다.

- 컨트롤러
```java
@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFile(@RequestParam("itemName") String itemName,
                           @RequestParam("file") MultipartFile file,
                           HttpServletRequest request) throws IOException {
        
        log.info("request ={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);
        

        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일 저장 fullPath={}", fullPath);
            file.transferTo(new File(fullPath));
        }

        return "upload-form";
    }
}
```
업로드 하는 HTML Form의 name에 맞춰 `@RequestParam`을 적용하면 된다. `@ModelAttribute`에서도 `MultipartFile`을 동일하게 사용할 수 있다.
- `file.getOriginalFilename()` : 업로드 파일 명
- `file.transferTo()` : 파일 저장

<Br>

### 파일 업로드, 다운로드 예제
- Item 상품 도메인
```java
@Data
public class Item {

    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
```
- UploadFile - 업로드 파일 정보 보관
```java
@Data
@AllArgsConstructor
public class UploadFile {
    private String uploadFileName;
    private String storeFileName;
}
```
`uploadFileName` : 고객이 업로드한 파일명<br>
`storeFileName` : 서버 내부에서 관리하는 파일명

고객이 업로드한 파일명 그대로 서버 내부에 파일을 저장하면 안 된다. 왜냐하면 서로 다른 고객이 같은 파일이름을 업로드 한 경우 기존 파일 이름과 충돌이 날 수 있다.
서버에서는 저장할 파일명이 겹치지 않도록 내부에서 관리하는 별도의 파일명이 필요하다.

- ItemRepository
```java
@Repository
public class ItemRepository {

    private final Map<Long, Item> store = new HashMap<>();
    private long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }
}
```
- FileStore - 파일 저장과 관련된 로직
```java
@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> files) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                storeFileResult.add(storeFile(file));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        file.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);

    }

    private String createStoreFileName(String filename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(filename);//확장자 추출 "png"

       return uuid + "." + ext;
    }

    private String extractExt(String filename) {
        int pos = filename.lastIndexOf(".");
        return filename.substring(pos + 1);
    }
}
```
- ItemForm
```java
@Data
public class ItemForm {

    private Long itemId;
    private String itemName;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
```
이미지를 다중 업로드 하기 위해 `MultipartFile`을 `List`로 사용한다.<br>
멀티파트(`attachFile`)는 `@ModelAttribute`에서 사용할 수 있다.

- 컨트롤러
```java
@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }

    @GetMapping("/items/{id}")
    public String items(@PathVariable("id") Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);

        return "item-view";
    }

    @ResponseBody
    @GetMapping("/images/{fileName}")
    public Resource downloadImage(@PathVariable("fileName") String fileName) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(fileName));
    }

    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable("itemId") Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}",uploadFileName);
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);

    }
}
```
- `@GetMapping("/items/new")` : 등록 폼을 보여준다.
- `@PostMapping("/items/new")` : 폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트
- `@GetMapping("/items/{id}")` : 상품을 보여준다.
- `@GetMapping("/images/{fileName}")` : `<img>` 태그로 이미지를 조회할 때 사용한다. `urlResource`로 이미지 파일을 읽어서 `@ResponseBody`로 이미지 바이너리를 반환한다.
- `@GetMapping("/attach/{itemId}")` : 파일을 다운로드 할 때 실행한다. 파일 다운로드 시 권한 체크같은 상황을 가정하고 이미지 `id`를 요청한다. 파일 다운로드
시에는 고객이 업로드한 파일 이름으로 다운로드 하는 게 좋은데 이때는 `Content-Disposition`헤더에 `attachment; filename="업로드 파일명"` 값을 주면 된다.

- Item-form.html
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="container">
    <div class="py-5 text-center">
        <h2>상품 등록</h2>
    </div>
    <form th:action method="post" enctype="multipart/form-data">
        <ul>
            <li>상품명 <input type="text" name="itemName"></li>
            <li>첨부파일<input type="file" name="attachFile" ></li>
            <li>이미지 파일들<input type="file" multiple="multiple" name="imageFiles" ></li>
        </ul>
        <input type="submit"/>
    </form>
</div> <!-- /container -->
</body>
</html>
```
다중 파일 업로드를 하려면 `multiple="multiple"` 옵션을 주면 된다.

- Item-view.html
```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
</head>
<body>
<div class="container">
    <div class="py-5 text-center">
        <h2>상품 조회</h2>
    </div>
    상품명: <span th:text="${item.itemName}">상품명</span><br/>
    첨부파일: <a th:if="${item.attachFile}" th:href="|/attach/${item.id}|"
             th:text="${item.getAttachFile().getUploadFileName()}" /><br/>

    <img th:each="imageFile : ${item.imageFiles}"
         th:src="|/images/${imageFile.getStoreFileName()}|" width="300" height="300"/>
</div> <!-- /container -->
</body>
</html>
```
첨부 파일은 링크를 걸어두고 이미지는 `<img>`태그를 반복해서 출력한다.