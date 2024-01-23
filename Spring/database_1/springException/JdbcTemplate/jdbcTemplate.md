# JdbcTemplate

레포지토리에서 JDBC 반복 문제가 발생했다.
- 커넥션 조회, 커넥션 동기화
- 쿼리 실행
- 결과 바인딩
- 리소스 종료

스프링은 JDBC의 반복 문제를 해결하기 위해 `JdbcTemplate`이라는 템플릿을 제공한다.

```java
/**
 * JdbcTemplate 사용
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{

   private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values(?, ?)";
        template.update(sql, member.getMemberId(), member.getMoney());

        return member;
    }

    @Override
    public Member findById(String memberId){
        String sql = "select * from member where member_Id = ?";
        return template.queryForObject(sql, memberRowMapper(), memberId);
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money = ? where member_id = ?";
        template.update(sql, money, memberId);
    }

    @Override
    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";
        template.update(sql, memberId);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
```
`JdbcTemplate`은 JDBC로 개발할 때 발생하는 반복을 대부분 해결해준다. **트랜잭션을 위한 커넥션 동기화**, 예외 발생 시 **스프링 예외 변환기**도 자동으로 실행해준다.