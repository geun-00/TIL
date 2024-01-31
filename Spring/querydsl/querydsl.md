# QueryDSL

**QueryDSL**은 쿼리를 문자가 아닌 코드로 작성하는 오픈소스 프로젝트다.

매우 쉽고 간결하며 모양도 SQL 쿼리와 비슷하게 개발할 수 있다. JPQL 빌더 역할을 한다.

- 설정

`build.gradle`

```properties
dependencies{
...
implementation 'com.querydsl:querydsl-jpa:5.0.0:jakarta'
annotationProcessor "com.querydsl:querydsl-apt:${dependencyManagement.importedProperties['querydsl.version']}:jakarta"
annotationProcessor "jakarta.annotation:jakarta.annotation-api"
annotationProcessor "jakarta.persistence:jakarta.persistence-api"
}

def querydslSrcDir = 'src/main/generated'

clean {
    delete file(querydslSrcDir)
}
tasks.withType(JavaCompile) {
    options.generatedSourceOutputDirectory = file(querydslSrcDir)
}
```
`clean`후 `compileJava`를 실행하면 `src/main/generated` 밑에 QueryDSL 기능을 사용할 수 있는 Q파일(`QClass`)들이 생성된다.<br>
QClass 들은 기본적으로 엔티티 클래스로 만들어진다. `Q` + {엔티티 명}으로 만들어지고 내부에 만들어진 static QClass 객체를 필요한 곳에서 사용하는 방식이다.

예를 들어 QMember.member를 `static import`해서 `member`로 사용할 수 있다.

이 파일들은 `gitignore`에 등록해주는 것이 좋다.

**빈 등록**
```java
@Configuration
@RequiredArgsConstructor
public class QueryDSLConfig {
    
    private final EntityManager em;
    
    @Bean
    public JPAQueryFactory queryFactory() {
        return new JPAQueryFactory(em);
    }
}
```
QueryDSL을 사용하려면 `JPAQueryFactory`가 필요하다. 스프링 빈으로 등록해 놓고 필요한 곳에서 사용하면 된다.

> **JPAQueryFactory 동시성 문제**
> 
> 동시성 문제는 JPAQueryFactory를 생성할 때 제공하는 `EntityManager`에 달려있다. 스프링 프레임워크는 여러 쓰레드에서 동시에 같은 `EntityManager`에 접근해도
> 트랜잭션마다 별도의 영속성 컨텍스트를 제공하기 때문에 동시성 문제는 전혀 없다. [참고](https://github.com/genesis12345678/TIL/blob/main/Spring/jpa/persistenceContext/entityManager/entityManager.md)

QueryDSL이 실행하는 JPQL 쿼리를 확인하려면 이렇게 설정하면 된다.
```properties
spring.jpa.properties.hibernate.use_sql_comments: true
```

- [기본 문법 - 1](https://github.com/genesis12345678/TIL/blob/main/Spring/querydsl/basic/basic_1.md)
- [기본 문법 - 2](https://github.com/genesis12345678/TIL/blob/main/Spring/querydsl/basic/basic_2.md)
- [기본 문법 - 3](https://github.com/genesis12345678/TIL/blob/main/Spring/querydsl/basic/basic_3.md)


- [중급 문법 - 1](https://github.com/genesis12345678/TIL/blob/main/Spring/querydsl/intermidate/intermediate_1.md)
- [중급 문법 - 2](https://github.com/genesis12345678/TIL/blob/main/Spring/querydsl/intermidate/intermediate_2.md)