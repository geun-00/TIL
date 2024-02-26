# 회원 웹 계층 개발

### DTO
```java
@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipCode;
}
```

### 컨트롤러
```java
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 등록 뷰
     */
    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    /**
     * 회원 등록 뷰에서 넘어온 데이터로 회원 등록 처리
     */
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipCode());

        Member member = new Member();
        member.setAddress(address);
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }

    /**
     * 회원 전체 조회 뷰
     */
    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("members", memberService.findMembers());

        return "members/memberList";
    }
}
```

> **참고**<br>
> 요구사항이 정말 간단할 때는 DTO(`MemberForm`) 없이 엔티티(`Member`)를 직접 등록과 수정 화면에서 사용할 수 있다. 
> 하지만 화면 요구사항이 복잡해지기 시작하면 엔티티에 화면을 처리하기 위한 기능이 점점 증가한다. 결국 엔티티가 점점 화면에 종속적으로 변하고 
> 이렇게 화면 기능 때문에 지저분해진 엔티티는 결국 유지보수하기 어려워진다.<br>
> **실무에서 엔티티는 핵심 비즈니스 로직만 가지고 있고, 화면을 위한 로직은 없어야 한다.** 화면이나 API에 맞는 폼 객체나 DTO를 사용하자.
> 화면이나 API 요구사항을 이것들로 처리하고 엔티티는 최대한 순수하게 유지하자.