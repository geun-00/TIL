# 스프링 시큐리티 초기화 과정

## 자동 설정에 의한 기본 보안 작동

- 스프링은 다음 의존성을 추가하는 것만으로 스프링 시큐리티의 초기화 작업 및 보안 설정이 이루어진다.

```text
implementation 'org.springframework.boot:spring-boot-starter-security'
```

- **별도의 설정이나 코드를 작성하지 않아도 기본적인 웹 보안 기능이 연동되어 작동한다.**
  - 기본적으로 **모든 요청에 대하여** 인증여부를 검증하고, 인증이 승인되어야 자원에 접근이 가능하다.
  - 인증 방식은 **폼 로그인 방식**과 **httpBasic** 로그인 방식을 제공한다.
  - 인증을 시도할 수 있는 로그인 페이지가 자동적으로 생성되어 렌더링 된다.
  - 인증 승인이 이루어질 수 있도록 한 개의 계정이 기본적으로 제공된다.
    - `SecurityProperties` 설정 클래스에서 생성
    - `username` : user
    - `password` : 랜덤 문자열

---

- **SpringBootWebSecurityConfiguration**
  - 스프링 부트의 자동 설정에 의해 다음과 같은 기본 보안 설정 클래스를 생성한다.

```java
package org.springframework.boot.autoconfigure.security.servlet;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
class SpringBootWebSecurityConfiguration {
    
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnDefaultWebSecurity
	static class SecurityFilterChainConfiguration {

		@Bean
		@Order(SecurityProperties.BASIC_AUTH_ORDER)
		SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
			http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
			http.formLogin(withDefaults());
			http.httpBasic(withDefaults());
			return http.build();
		}

	}
    ...
}
```

- `@ConditionalOnDefaultWebSecurity`
```java
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(DefaultWebSecurityCondition.class)
public @interface ConditionalOnDefaultWebSecurity { }
```
- `DefaultWebSecurityCondition`
```java
class DefaultWebSecurityCondition extends AllNestedConditions {

    ...
    
	@ConditionalOnClass({ SecurityFilterChain.class, HttpSecurity.class })
	static class Classes { }

	@ConditionalOnMissingBean({ SecurityFilterChain.class })
	static class Beans { }
}
```
- 두 개의 조건을 만족해야 스프링 시큐리티의 기본 보안 웹 동작을 수행한다.
  - `@ConditionalOnClass({ SecurityFilterChain.class, HttpSecurity.class })`
    - SpringSecurity 의존성을 추가하는 것만으로 만족이 된다.
  - `@ConditionalOnMissingBean({ SecurityFilterChain.class })`
    - custom하게 `SecurityFilterChain`을 생성해 빈을 등록하면 만족하지 않는다.

---

- **SecurityProperties**
  - 스프링 시큐리티가 기본적으로 제공하는 유저 정보
```java
package org.springframework.boot.autoconfigure.security;

@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {
    
    ...
    
    public static class User {

        private String name = "user";
        private String password = UUID.randomUUID().toString();
        private List<String> roles = new ArrayList<>();

        ...
    }
}
```