# Record

Java 11 -> 17로 버전을 변경하면서 record에 대해 새로 알게 되었다.

record를 DTO 용도로 사용하면 좋다고 하는데 어떤 특징이 있는지 알아보자.

1. 모든 필드에 대한 생성자를 만들어준다.(AllargsConstructor)
2. getter, equals, hashCode, toString을 자동으로 생성해준다.
3. 모든 필드를 final로 생성해줌으로써 불변(immutable) 데이터로 관리할 수 있다.
4. 각 필드에 getter는 getId() 이런 식이 아니라 Id()처럼 필드명 그대로가 된다.
5. final class여서 상속할 수 없다.

기존 ResponseDto
```java
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Schema(name = "회원 정보 조회 응답")
public class MemberInfoResponse {

    @Schema(description = "회원ID", example = "1")
    private Long memberId;

    @Schema(description = "회원이름", example = "사용자1")
    private String name;

    @Schema(description = "회원이메일", example = "test@email.com")
    private String email;

    @Schema(description = "회원주소", example = "서울특별시")
    private String address;

    @Schema(description = "회원이미지URL", example = "https://meatwiki.nii.ac.jp/confluence/images/icons/profilepics/anonymous.png")
    private String imageUrl;

    @Schema(description = "회원별명", example = "별명1")
    private String nickName;

    @Schema(description = "회원 한 줄 메시지", example = "날씨가 좋습니다.")
    private String message;

    @Schema(description = "소셜유저 인 경우에만 나타남")
    private String socialId;

    @Schema(description = "소셜유저 인 경우에만 나타남", example = "KAKAO or GOOGLE")
    private SocialType socialType;

    @Schema(description = "등록했던 글")
    private List<SimplifiedPostResponse> simplifiedPostResponseList;
}
```

수정된 ResponseDto
```java
@Schema(name = "회원 정보 조회 응답")
public record MemberInfoResponse(
        @Schema(description = "회원ID", example = "1") Long memberId,
        @Schema(description = "회원이름", example = "사용자1") String name,
        @Schema(description = "회원이메일", example = "test@email.com") String email,
        @Schema(description = "회원주소", example = "서울특별시") String address,
        @Schema(description = "회원이미지URL", example = "https://meatwiki.nii.ac.jp/confluence/images/icons/profilepics/anonymous.png") String imageURL,
        @Schema(description = "회원별명", example = "별명1") String nickName,
        @Schema(description = "회원 한 줄 메시지", example = "날씨가 좋습니다.") String message,
        @Schema(description = "소셜유저 인 경우에만 나타남") String socialId,
        @Schema(description = "소셜유저 인 경우에만 나타남", example = "KAKAO or GOOGLE") SocialType socialType,
        @Schema(description = "등록했던 글") List<SimplifiedPostResponse> simplifiedPostResponseList) {

}
```

record가 자동적으로 필드를 private final로 선언되니 따로 접근제어자를 적어주지 않아도 된다.<br>
또한 @Getter, @AllArgsConstructor 등 생략이 가능해 코드량이 줄었다.

밑에 메소드도 정의가 가능하다.

```java
@Schema(name = "회원 정보 조회 시 List로 응답되는 등록했던 글 응답")
public record SimplifiedPostResponse(@Schema(description = "글ID", example = "1") Long postId,
                                     @Schema(description = "글 등록 날짜", example = "2023-11-28 15:14:41") LocalDateTime createdAt,
                                     @Schema(description = "등록한 글당 하나의 이미지만 응답") List<String> mediaUrls)
{
    public SimplifiedPostResponse(Post post) {
        this(post.getPostId(), post.getCreatedAt(),
                post.getMediaFiles().isEmpty()
                        ? Collections.emptyList()
                        : Collections.singletonList(post.getMediaFiles().get(0).getFileUrl()));
    }
}
```

