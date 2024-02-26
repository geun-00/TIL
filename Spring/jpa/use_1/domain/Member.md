# 회원 도메인 개발

### 레포지토리
```java
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
```
- `@Repository` : 컴포넌트 스캔의 대상 + JPA 예외를 스프링 기반 예외로 예외를 변환해준다.

### 서비스
```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복 회원 검증
     * @param member
     */
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 전체 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 한 명 조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }
}
```
- `@Service` : 컴포넌트 스캔의 대상
- `@Transactional`
  - `readOnly=true`
    - 데이터의 변경이 없는 읽기 전용 메서드에서 사용한다.
    - 영속성 컨텍스트를 `플러시`하지 않으므로 약간의 성능 향상이 있다.(읽기 전용에는 다 적용)
    - 데이터베이스 드라이버가 지원하면 DB에서 성능 향상이 있다.
- 실무에서는 검증 로직이 있어도 멀티 쓰레드 상황을 고려해서 회원 테이블의 회원명 컬럼에 유니크 제약 조건을 추가하는 것이 안전하다.

### 테스트 코드
```java
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입 성공")
    void join() {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);
        Member findMember = memberRepositoryOld.findOne(savedId);

        //then
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("중복 회원 예외")
    void duplicateMember() {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //then
        assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class);
    }
}
```
- `@SpringBootTest` : 스프링 부트를 띄우고 테스트한다.(이게 없으면 `@Autowired`가 실패한다.)
- `@Transactional` 
  - **각각의 테스트를 실행할 때마다 트랜잭션을 시작하고 테스트가 끝나면 트랜잭션을 강제로 롤백한다.**
  - 테스트는 반복 가능해야 한다.
  - 이 애노테이션이 테스트 케이스에서 사용될 때만 롤백한다.

> **테스트 케이스를 위한 설정**<br>
> 테스트 케이스는 격리된 환경에서 실행하고 끝나면 데이터를 초기화하는 것이 좋기 때문에 메모리 DB를 사용하는 것이 가장 이상적이다.
> 일반적으로 테스트 케이스를 위한 스프링 환경과 애플리케이션을 실행하는 환경은 보통 다르므로 설정 파일을 다르게 사용해야 한다.
> 
> `/test/resources`밑에 `.properties`나 `.yml`같은 설정 파일을 만들고 테스트용 설정 파일을 추가하면 된다. 그러면 테스트에서 스프링을 실행하면 이 위치에 있는
> 설정 파일을 읽는다.(이 위치에 없으면 `src/main/resources/`의 설정 파일을 읽는다.)
> 
> 스프링 부트는 `datasource` 설정이 없으면 기본적인 메모리 DB를 사용하고 `driver-class`도 현재 등록된 라이브러리를 보고 찾아준다.
> 그리고 `ddl-auto`도 `create-drop`모드로 동작한다.<br>
> 따라서 JPA 관련된 별도의 추가 설정을 하지 않아도 된다.