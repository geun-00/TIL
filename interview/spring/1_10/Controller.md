# @Controller와 @RestController의 차이는 무엇인가요?

- `@Controller`는 주로 뷰 리졸버를 통해 `View`를 반환해 뷰를 렌더링 시키기 위해 사용된다.
- `@RestController`는 각각의 `@RequestMapping`에 `@ResponseBody`가 붙은 것으로 뷰가 아닌 `JSON`을 응답으로 보내기 위해 사용된다.