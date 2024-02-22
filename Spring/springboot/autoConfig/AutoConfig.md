# 자동 구성

## 예제
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String memberId;
    private String name;
}
```
- 간단한 회원 객체

```java
@Slf4j
@Configuration
public class DBConfig {

    @Bean
    public DataSource dataSource(){
        log.info("dataSource 빈 등록");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:mem:test");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public TransactionManager transactionManager() {
        log.info("transactionManager 빈 등록");
        return new JdbcTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        log.info("jdbcTemplate 빈 등록");
        return new JdbcTemplate(dataSource());
    }
}
```
- `JdbcTemplate`을 사용해서 DB에 보관하고 관리하기 위해 `DataSource`, `TransactionManager`, `JdbcTemplate` 스프링 빈 등록

```java
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final JdbcTemplate template;

    public void initTable() {
        template.execute("create table member(member_id varchar primary key , name varchar)");
    }

    public void save(Member member) {
        template.update("insert into member(member_id, name) values (?, ?)",
                member.getMemberId(), member.getName());
    }

    public Member find(String memberId) {
        return template.queryForObject("select member_id, name from member where member_id=?",
                BeanPropertyRowMapper.newInstance(Member.class),
                memberId);
    }

    public List<Member> findAll() {
        return template.query("select member_id, name from member",
                BeanPropertyRowMapper.newInstance(Member.class));
    }
}
```
- `JdbcTemplate`을 사용해서 회원을 관리하는 레포지토리

```java
@SpringBootTest
class MemberRepositoryTest {
    
    @Autowired MemberRepository memberRepository;

    @Transactional
    @Test
    void memberTest() {
        // given
        Member member = new Member("idA", "memberA");
        memberRepository.initTable();
        memberRepository.save(member);
        
        // when
        Member findMember = memberRepository.find(member.getMemberId());
        // then
        assertThat(findMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findMember.getName()).isEqualTo(member.getName());
    }
}
```
- `@Transactional`을 사용해서 트랜잭션 기능을 적용했다.
- `@Transactional`을 사용하려면 `TransactionManager`가 스프링 빈으로 등록되어 있어야 한다.

**DB 데이터를 보관하고 관리하기 위해 `DataSource`, `TransactionManager`, `JdbcTemplate` 같은 객체들을 스프링 빈으로 등록했다. 꽤 번거로운 작업이다.**

`DBConfig`에 `@Configuration`을 없애고 스프링 컨테이너에 등록이 잘 되는지 확인해보자.

```java
@Slf4j
//@Configuration
public class DBConfig { ... }
```
```java
@Slf4j
@SpringBootTest
class DBConfigTest {

    @Autowired DataSource dataSource;
    @Autowired TransactionManager transactionManager;
    @Autowired JdbcTemplate jdbcTemplate;

    @Test
    void checkBean() {
        log.info("dataSource={}", dataSource);
        log.info("transactionManager={}", transactionManager);
        log.info("JdbcTemplate={}", jdbcTemplate);

        assertThat(dataSource).isNotNull();
        assertThat(transactionManager).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
    }
}
```
- 실행을 해보면 `DBConfig`에서 로그로 남겨놨던 "~~ 빈 등록" 로그가 보이지 않는다. 스프링 빈으로 등록되지 않았다는 것이다.
- 그런데 테스트는 정상 통과하고 출력 결과에 빈들이 존재한다.

**사실 이 빈들은 모두 스프링 부트가 자동으로 등록해 준 것이다.**

<br>

- [스프링 부트의 자동 구성 - 1](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/autoConfig/SpringBootAutoConfig_1.md)
- [스프링 부트의 자동 구성 - 2](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/autoConfig/SpringBootAutoConfig_2.md)

