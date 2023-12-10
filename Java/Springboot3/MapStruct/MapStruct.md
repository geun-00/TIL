# MapStruct

프로젝트에서 레이어 간 데이터 이동을 할 때 DTO를 사용해서 이동할 것이다.
DTO 내부에 필드의 개수가 많다면 데이터를 옮기는 코드만 해도 꽤 길어질 것이다.
빌더 패턴으로 가독성 좋게 해줄 수 있겠지만 코드의 길이는 변함이 없다.<br>
**이러한 노가다 작업을 *MapStruct*를 활용하여 쉽게 처리할 수 있다.**

### build.gradle dependency

```java
 implementation 'org.mapstruct:mapstruct:1.4.2.Final'
 annotationProcessor 'org.mapstruct:mapstruct-processor:1.4.2.Final'
```

<br>

### Mapper 인터페이스 생성

```java
@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.FIELD,
        unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface MemberInfoResponseMapper {

    MemberInfoResponseMapper INSTANCE = Mappers.getMapper(MemberInfoResponseMapper.class);


    @Mapping(target = "simplifiedPostResponseList", source = "posts")
    @Mapping(target = "memberId", source = "member.id")
    MemberInfoResponse toMemberInfoResponse(Member member, List<SimplifiedPostResponse> posts);
}
```


여기서 사용한 DTO는 [Record 정리](https://github.com/genesis12345678/TIL/blob/main/Java/Record/record.md)할 때 사용한 ResponseDto다.<br>
인터페이스를 만들면 애플리케이션 실행 시 자동으로 MapperImpl을 생성해준다.

```java
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-12-10T22:10:23+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class MemberInfoResponseMapperImpl implements MemberInfoResponseMapper {

    @Override
    public MemberInfoResponse toMemberInfoResponse(Member member, List<SimplifiedPostResponse> posts) {
        if ( member == null && posts == null ) {
            return null;
        }

        Long memberId = null;
        String name = null;
        String email = null;
        String address = null;
        String imageURL = null;
        String nickName = null;
        String message = null;
        String socialId = null;
        SocialType socialType = null;
        if ( member != null ) {
            memberId = member.getId();
            name = member.getName();
            email = member.getEmail();
            address = member.getAddress();
            imageURL = member.getImageURL();
            nickName = member.getNickName();
            message = member.getMessage();
            socialId = member.getSocialId();
            socialType = member.getSocialType();
        }
        List<SimplifiedPostResponse> simplifiedPostResponseList = null;
        if ( posts != null ) {
            List<SimplifiedPostResponse> list = posts;
            if ( list != null ) {
                simplifiedPostResponseList = new ArrayList<SimplifiedPostResponse>( list );
            }
        }

        MemberInfoResponse memberInfoResponse = new MemberInfoResponse( memberId, name, email, address, imageURL, nickName, message, socialId, socialType, simplifiedPostResponseList );

        return memberInfoResponse;
    }
}
```
source가 되는 Member 엔티티에서 getter메소드를 이용해 DTO에 매핑시켜주는 코드를 만들어준다.<br>
이 때 나는 Member 엔티티에서 필드가 id로 되어 있어 source ="id"로 해야할 줄 알았다.
하지만 컴파일 시 매핑에러가 나서(에러를 조기에 잡아준다는 점도 장점이다.) 여러 번 시도해보다가
방법을 찾게 되었다.

<br>

#### Mapper 적용 전 코드

```java
public MemberInfoResponse getMemberInfo(String username, Pageable pageable) {

        Member member = memberRepository.findByEmail(username)
                .orElseThrow(EntityNotFoundException::new);

        Page<SimplifiedPostResponse> simplifiedPosts = postRepository.findByMemberIdOrderByCreatedAtDesc(member.getId(), pageable)
                .map(SimplifiedPostResponse::new);

        return MemberInfoResponse.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .imageUrl(member.getImageURL())
                .address(member.getAddress())
                .message(member.getMessage())
                .nickName(member.getNickName())
                .socialId(member.getSocialId())
                .socialType(member.getSocialType())
                .simplifiedPostResponseList(simplifiedPosts.getContent())
                .build();

    }
```

이것도 빌더를 이용해서 최대한 정리한거지만 코드가 너무 길어진다.

<br>

#### Mapper 적용 후 코드

```java
private final MemberInfoResponseMapper memberInfoResponseMapper;

public MemberInfoResponse getMemberInfo(String username, Pageable pageable) {

        return memberRepository.findByEmail(username)
                .map(member -> {
                    Page<Post> posts = postRepository.findByMemberIdOrderByCreatedAtDesc(member.getId(), pageable);
                    List<SimplifiedPostResponse> simplifiedPostResponses = posts.getContent().stream().map(SimplifiedPostResponse::new).toList();

                    return memberInfoResponseMapper.INSTANCE.toMemberInfoResponse(member, simplifiedPostResponses);
                })
                .orElseThrow(EntityNotFoundException::new);
    }
```
코드가 확 줄게 되었다. <br>
(참고로 toList()도 Java17에서 새로 생긴 기능이다. 11버전에서 collect(Collectors.toList());랑 똑같다.)

<br>
<br>

### Mapper 설정

```java
@Mapper(componentModel = "spring")
```
Mapper를 생성할 때 Bean으로 생성할 수 있다.(mapperImpl에서 @component를 달아준다.)

<br>

```java
@Mapper(source = "매핑 될 객체", target = "매핑 할 객체")
```
- **source** : 값을 가져오는 객체로 getter가 필요하다.
- **target** : 값을 받는 객체로 빌더 or 생성자가 필요하다.

target 객체를 생성할 때 builder가 있다면 builder로 만들고 그렇지 않다면
생성자를 통해 생성한다.<br>
source와 target은 필드명이 동일하지 않은 필드에만 적용해주면 된다.

<br>

```java
@Mapper(injectionStrategy = InjectionStrategy.FIELD)
```
필드 방식으로 DI, CONSTRUCTOR(생성자 주입)와 FIELD가 있다.

<br>

```java
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
```
source나 target에 매핑이 되지 않는 필드들에 대해 무시할 수 있다.<br>
ERROR, WARN, IGNORE가 있고 ERROR 사용 시 매핑이 맞지 않으면 애플리케이션이 실행이 아예 되지 않는다.<br>
WARN이나 IGNORE 사용 시 매핑이 맞지 않아도 실행은 되나 매핑이 맞지 않은 필드는 null로 대체된다.<br>
이 때 constant 또는 defaultValue를 사용해 특정 값을 지정해 줄 수 있다.

<br>

[더 많은 정보](https://mein-figur.tistory.com/entry/mapstruct-1)
