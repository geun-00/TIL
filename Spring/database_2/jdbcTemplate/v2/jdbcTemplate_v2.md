# JdbcTemplate V2

- 이름 지정 파라미터
```java
/**
 * NamedParameterJdbcTemplate
 * SqlParameterSource
 * - BeanPropertySqlParameterSource
 * - MapSqlParameterSource
 * Map
 *
 * BeanPropertyRowMapper
 */
@Slf4j
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    private final NamedParameterJdbcTemplate template;

    public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Item save(Item item) {
        String sql = "insert into item(item_name, price, quantity) " +
                     "values(:itemName, :price, :quantity)";

        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        long key = keyHolder.getKey().longValue();
        item.setId(key);

        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item " +
                     "set item_name = :itemName, price = :price, quantity = :quantity " +
                     "where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);
        template.update(sql, param);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity " +
                     "from item " +
                     "where id = :id";
        try {
            Map<String, Object> param = Map.of("id", id);
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select id, item_name, price, quantity " +
                     "from item";
        // 동적 쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }

        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%', :itemName , '%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }
        log.info("sql={}", sql);

        return template.query(sql, param, itemRowMapper());
    }


    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class);
    }
}
```
파라미터를 전달하면 `Map`처럼 `key, value` 데이터 구조를 만들어서 전달해야 한다. `key`는 `:파라미터이름`, `value`는 해당 파라미터의 값이 된다.

이름 지정 바인딩에서 자주 사용하는 파라미터 종류 3가지
- `Map`
  - 단순히 `Map`을 사용, `findById()`
- `SqlParameterSource`
  - `MapSqlParameterSource`
    - `Map`과 유사한데 SQL에 좀 더 특화된 기능을 제공한다. `SqlParameterSource`인터페이스의 구현체이다.
    - 메서드 체인을 통해 편리한 사용법을 제공한다, `update()`
  - `BeanPropertySqlParameterSource`
    - **자바빈 프로퍼티 규약을 통해서 자동으로 파라미터 객체를 생성한다.**(`getXxx()`)
    - `SqlParameterSource`인터페이스의 구현체이다, `save()`,`findAll()`
    - 가장 편리해 보이지만 항상 사용할 수 있는 것은 아니다. 왜냐하면 `update()`에서는 `id`를 바인딩 해야하는데 `ItemUpdateDto`에는 `id`가 없다.
```java
private RowMapper<Item> itemRowMapper() {
    return BeanPropertyRowMapper.newInstance(Item.class);
}
```
`ResultSet`의 결과를 받아서 `자바빈 규약`에 맞추어 데이터를 변환해준다. 이 때 보통 관계형 DB는 `snake_case`, 자바 객체는 `camelCase` 표기법을 사용하는
관례의 불일치가 발생하는데 `BeanPropertyRowMapper`는 `snake_case`을 자동으로 `camelCase`로 변환해준다. 만약 컬럼 이름과 객체 이름이 완전히 다른 경우에는 별칭(`as`)을 사용하면 된다.

- Config
```java
@Configuration
@RequiredArgsConstructor
public class JdbcTemplateV2Config {

    private final DataSource dataSource;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JdbcTemplateItemRepositoryV2(dataSource);
    }
}
```
```java
@Import(JdbcTemplateV2Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemServiceApplication.class, args);
    }
}
```