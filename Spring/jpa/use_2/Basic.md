# 회원 API

### 등록 - V1
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }
}
```

- **등록 V1 - 엔티티를 `@RequestBody`에 직접 매핑**
  - 문제점
    - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
    - 엔티티에 API 검증을 위한 로직이 들어간다.(`@NotEmtpy` 등)
    - 실무에서는 엔티티를 위한 다양한 API가 만들어지는데, 한 엔티티에 각각의 API를 위한 모든 요청 요구사항을 담기는 어렵다.
    - **엔티티가 변경되면 API 스펙이 변한다.**

### 등록 - V2
```java
@PostMapping("/v2/members")
public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
    Member member = new Member();
    member.setName(request.getName());

    Long id = memberService.join(member);
    return new CreateMemberResponse(id);
}

@Data
static class CreateMemberRequest {
    @NotEmpty
    private String name;
}
```

- **등록 V2 - 엔티티 대신에 DTO를 `@RequestBody`에 매핑**
  - 엔티티와 프레젠테이션 계층을 위한 로직을 분리할 수 있다.
  - 엔티티와 API 스펙을 명확하게 분리할 수 있다.
  - **엔티티가 변해도 API 스펙이 변하지 않는다.**
  - **실무에서는 절대 엔티티를 API 스펙에 노출하면 안 된다.**

### 수정
```java
@PatchMapping("/v2/members/{id}")
public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                           @RequestBody @Valid UpdateMemberRequest request) {
    memberService.update(id, request.getName());
    Member findMember = memberService.findOne(id);
    return new UpdateMemberResponse(findMember.getId(), findMember.getName());
}

@Data
static class UpdateMemberRequest{
    private String name;
}

@Data
@AllArgsConstructor
static class UpdateMemberResponse{
    private Long id;
    private String name;
}
```
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findById(id).get();
        member.setName(name);
    }
}
```
- 회원 수정도 별도의 DTO를 요청 파라미터에 매핑한다.
- 서비스 로직에서는 **변경 감지**를 사용해서 데이터를 수정한다.

### 조회 - V1
```java
@GetMapping("/v1/members")
public List<Member> membersV1() {
    return memberService.findMembers();
}
```

- **조회 V1 - 응답 값으로 엔티티를 직접 외부에 노출**
  - 문제점
    - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
    - **기본적으로 엔티티의 모든 값이 노출된다.**
    - 응답 스펙을 맞추기 위해 로직이 추가된다.(`@JsonIgnore` 등)
    - 실무에서는 같은 엔티티에 대해 API가 용도에 따라 다양하게 만들어지는데, 한 엔티티에 각각의 API를 위한 프레젠테이션 응답 로직을 담기는 어렵다.
    - **엔티티가 변경되면 API 스펙이 변한다.**
    - 컬렉션을 직접 반환하면 향후 API 스펙을 변경하기 어렵다.

### 조회 - V2
```java
@GetMapping("/v2/members")
public Result membersV2() {
    List<MemberDto> list = memberService.findMembers()
                                        .stream()
                                        .map(m -> new MemberDto(m.getId() , m.getName()))
                                        .toList();
    return new Result<>(list.size(), list);
}


@Data
@AllArgsConstructor
static class Result<T> {
    private int count;
    private T data;
}

@Data
@AllArgsConstructor
static class MemberDto {
    private Long id;
    private String name;
}
```

- **조회 V2 - 응답 값으로 엔티티가 아닌 별도의 DTO 사용**
  - 엔티티를 DTO로 변환해서 반환한다.
  - **엔티티가 변해도 API 스펙은 변하지 않는다.**
  - `Result` 클래스로 컬렉션을 한번 감싸서 반환하면 향후 필요한 필드를 추가할 수 있다.(예: `count`)