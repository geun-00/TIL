# 스프링 MVC 구조 

## 전체 구조
![img.png](image/img.png)

구조는 전에 직접 만든 mvc 프레임워크랑 똑같다.

명칭만 좀 다른데
- FrontController -> DispatcherServlet
- handlerMappingMap -> HandlerMapping
- MyHandlerAdapter -> HandlerAdapter
- viewResolver -> ViewResolver(인터페이스)
- MyView -> View(인터페이스)

### DispatcherServlet 구조
> 스프링 MVC도 프론트 컨트롤러 패턴으로 구현되어 있는데 스프링 MVC의 프론트 컨트롤러가 ``DispatcherServlet``이다. 이것이 스프링 MVC의 핵심이다.

![img_1.png](image/img_1.png)
부모 클래스에서 ``HttpServlet``을 상속 받아서 사용하고 있다.

스프링 부트는 ``DispatcherServlet``을 서블릿으로 자동으로 등록하면서 모든 경로("/")에 대해서 매핑한다.

#### 요청 흐름
- 서블릿이 호출되면 ``HttpServlet``이 제공하는 ``service()``가 호출된다.
- 스프링 MVC는 ``DispatcherServlet``의 부모인 ``FrameworkServlet``에서 ``service()``를 오버라이드 해두었다.
- ``FrameworkServlet.service()``를 시작으로 여러 메서드가 호출되면서 ``DispatcherServlet.doDispatch()``가 호출된다.

- DispatcherServlet.doDispatch()
```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
    HttpServletRequest processedRequest = request; 
    HandlerExecutionChain mappedHandler = null; 
    ModelAndView mv = null;
    // 1. 핸들러 조회
    mappedHandler = getHandler(processedRequest); 
    if (mappedHandler == null) {
        noHandlerFound(processedRequest, response); 
        return;
        }
    // 2. 핸들러 어댑터 조회 - 핸들러를 처리할 수 있는 어댑터
    HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
    // 3. 핸들러 어댑터 실행 -> 4. 핸들러 어댑터를 통해 핸들러 실행 -> 5. ModelAndView 반환 
    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
    processDispatchResult(processedRequest, response, mappedHandler, mv,dispatchException);
    }
    
private void processDispatchResult(HttpServletRequest request,HttpServletResponse response, HandlerExecutionChain mappedHandler, 
        ModelAndView mv, Exception exception) throws Exception {
        // 뷰 렌더링 호출
        render(mv, request, response);
}

protected void render(ModelAndView mv, HttpServletRequest request,HttpServletResponse response) throws Exception {
        View view;
        String viewName = mv.getViewName();
        // 6. 뷰 리졸버를 통해서 뷰 찾기, 7. View 반환
        view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
        // 8. 뷰 렌더링
        view.render(mv.getModelInternal(), request, response);
}
```

- 동작 순서
  1. **핸들러 조회** : 핸들러 매핑을 통해 요청 URL에 매핑된 핸들러(컨트롤러)를 조회한다.
  2. **핸들러 어댑터 조회** : 핸들러를 실행할 수 있는 핸들러 어댑터를 조회한다.
  3. **핸들러 어댑터 실행** : 핸들러 어댑터를 실행한다.
  4. **핸들러 실행** : 핸들러 어댑터가 실제 핸들러를 실행한다.
  5. **ModelAndView 반환** : 핸들러 어댑터는 핸들러가 반환하는 정보를 ``ModelAndView``로 **변환해서** 반환한다.
  6. **viewResolver 호출** : 뷰 리졸버를 찾고 실행한다.
  7. **view 반환** : 뷰 리졸버는 뷰의 논리 이름을 물리 이름으로 바꾸고, 렌더링 역할을 담당하는 뷰 객체를 반환한다.
  8. **뷰 렌더링** : 뷰를 통해서 뷰를 렌더링한다.

<br>

## 스프링 MVC 
> 스프링은 어노테이션 기반 컨트롤러를 제공해 매우 유연하고 실용적이다.

가장 우선순위가 높은 핸들러 매핑과 어댑터는 ``RequestMappingHandlerMapping``, ``RequestMappingHandlerAdapter``이다.

``@RequestMapping``은 이것의 앞글자를 따서 만든 것인데 지금 스프링에서 주로 사용하는 어노테이션 기반의 핸들러 매핑과 어댑터이다.


### V1
- 회원등록 폼 컨트롤러
```java
@Controller
public class SpringMemberFormControllerV1 {

    @RequestMapping("/springmvc/v1/members/new-form")
    public ModelAndView process() {
        return new ModelAndView("new-form");
    }
}
```
- ``@Controller``
  - 스프링이 자동으로 스프링 빈으로 등록한다.(내부에 ``@Component``가 있어 ComponentScan의 대상이 된다.)
  - 스프링 MVC에서 어노테이션 기반 컨트롤러로 인식한다.
- ``@RequestMapping``
  - 요청 정보를 매핑한다. 해당 URL이 호출되면 이 메서드가 호출된다.
- ``ModelAndView``
  - 모델과 뷰 정보를 담아서 반환한다.

``RequestMappingHandlerMapping``은 클래스 레벨에 ``@Controller``가 붙어 있는 경우에 매핑 정보로 인식한다.

- 회원 저장 컨트롤러
```java
@Controller
public class SpringMemberSaveControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView process(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);

        System.out.println("member = " + member);

        memberRepository.save(member);

        ModelAndView mv = new ModelAndView("save-result");
        mv.addObject("member", member);
        return mv;
    }
}
```

- 회원 목록 컨트롤러
```java
@Controller
public class SpringMemberListControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process() {
        List<Member> members = memberRepository.findAll();
        ModelAndView mv = new ModelAndView("members");
        mv.addObject("members", members);

        return mv;
    }
}
```

<br>

### V2
> ``@RequestMapping``을 클래스 단위에 적용하여 유연하게 하나로 통합할 수 있다.

- 통합 컨트롤러
```java
/**
 * 클래스 단위 -> 메서드 단위
 * @RequestMapping 클래스 레벨과 메서드 레벨 조합
 */
@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView newForm() {
        return new ModelAndView("new-form");
    }

    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        Member member = new Member(username, age);

        memberRepository.save(member);

        ModelAndView mav = new ModelAndView("save-result");
        mav.addObject("member", member);

        return mav;
    }

    @RequestMapping
    public ModelAndView members() {
        List<Member> members = memberRepository.findAll();
        ModelAndView mav = new ModelAndView("members");
        mav.addObject("members", members);
        return mav;
    }
}
```
클래스 레벨 URL + 메서드 레벨 URL로 조합된다.

<br>

### V3
> 스프링 MVC는 편리하게 개발할 수 있도록 수 많은 편의 기능을 제공한다.

```java
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {
    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @GetMapping( "/new-form")
    public String newForm() {
        return "new-form";
    }

    @PostMapping("/save")
    public String save(@RequestParam("username") String username, 
                       @RequestParam("age") int age, 
                       Model model) {

        Member member = new Member(username, age);
        memberRepository.save(member);
        model.addAttribute("member", member);

        return "save-result";
    }

    @GetMapping
    public String members(Model model) {
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members", members);
        return "members";
    }
}
```
- 컨트롤러에서 Model을 파라미터로 받을 수 있다.
- 뷰의 논리 이름을 직접 반환할 수 있다.
- HTTP 요청 파라미터를 ``@RequertParam``으로 받을 수 있다.
- ``@GetMapping``, ``@PostMapping``: URL만 매핑하는 것이 아니라 HTTP Method도 구분할 수 있다.