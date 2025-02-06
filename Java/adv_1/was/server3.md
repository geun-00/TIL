# 자바 - HTTP 서버 개발(with 리플렉션 + 애노테이션)

### 애노테이션이 필요한 이유

- 리플렉션 서블릿은 요청 URL과 메서드 이름이 같다면 해당 메서드를 동적으로 호출할 수 있다.
하지만 요청 이름과 메서드 이름을 다르게 하고 싶다면 어떻게 해야할까?
- 그리고 `/`, `/favicon.ico`, `/add-memeber`와 같은 URL은 자바 메서드로
만들 수 없다.

결국 메서드 이름만으로는 해결이 어렵다. 추가 정보를 코드 어딘가에 적어두고
읽을 수 있어야 한다.

```java
public class Controller {
    
    // "/site1"
    public void page1(HttpRequest request, HttpResponse response) {
        //...
    }

    // "/"
    public void home(HttpRequest request, HttpResponse response) {
        //...
    }

    // "/add-member"
    public void addMember(HttpRequest request, HttpResponse response) {
        //...
    }
}
```

리플렉션 같은 기술로 메서드 이름 뿐만 아니라 주석까지 읽어서 처리할 수 있으면 좋을 것 같다.
그러면 해당 메서드에 있는 주석을 읽어서 URL 경로와 비교할 수 있고, 같다면 해당 주석이 달린
메서드를 호출하면 된다.

그런데 주석은 코드가 아니기 때문에 컴파일 시점에 모두 제거된다. 애노테이션을 사용하면
프로그램 실행 중에 읽어서 사용할 수 있는 주석을 만들어 해결할 수 있다.

---

## 애노테이션 서블릿 - V7

```java
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Mapping {
    String value();
}
```
```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
/**
 * 애노테이션 서블릿
 * @version 7
 */
public class AnnotationServletV1 implements HttpServlet {

    private final List<Object> controllers;

    public AnnotationServletV1(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    String value = mapping.value(); //매핑된 URL 정보 추출

                    if (value.equals(path)) {
                        invoke(controller, method, request, response);
                        return;
                    }
                }
            }
        }

        throw new PageNotFoundException("request = " + request);
    }

    private void invoke(Object controller, Method method, HttpRequest request, HttpResponse response) {
        try {
            method.invoke(controller, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
```
- 리플렉션을 사용하는 코드와 비슷하다. 차이는 호출할 메서드를 찾을 때 메서드의 이름을
비교하는 대신 메서드에서 `@Mapping` 애노테이션을 찾고, 그곳의 `value` 값으로 비교한다.
```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;
/**
 * 애노테이션 서블릿
 * @version 7
 */
public class SiteControllerV7 {

    @Mapping("/")
    public void home(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>home</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li><a href='/site1'>site1</a></li>");
        response.writeBody("<li><a href='/site2'>site2</a></li>");
        response.writeBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writeBody("</ul>");
    }

    @Mapping("/site1")
    public void site1(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }

    @Mapping("/site2")
    public void site2(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }
}
```
```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

import java.io.IOException;
/**
 * 애노테이션 서블릿
 * @version 7
 */
public class SearchControllerV7 {

    @Mapping("/search")
    public void search(HttpRequest request, HttpResponse response) throws IOException {

        String query = request.getParameter("q");

        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }
}
```
```java
import was.httpserver.HttpServer;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.annotation.AnnotationServletV1;

import java.io.IOException;
import java.util.List;
/**
 * 애노테이션 서블릿
 * @version 7
 */
public class ServerMainV7 {

    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {

        List<Object> controllers = List.of(new SiteControllerV7(), new SearchControllerV7());
        AnnotationServletV1 annotationServlet = new AnnotationServletV1(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(annotationServlet);
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
```

애노테이션을 사용해 편리하고 실용적인 웹 애플리케이션을 만들 수 있게 되었다.
현대의 웹 프레임워크들은 대부분 애노테이션을 사용해서 편리하게 호출 메서드를 찾을 수
있는 지금과 같은 방식을 제공한다. 

---

## 애노테이션 서블릿 - V8

위 버전의 아쉬운 부분이 있는데 `HttpRequest`, `HttpResponse`가 필요하지 않아도
항상 인자로 받도록 되어 있다.

```java
@Mapping("/site1")
public void site1(HttpRequest request, HttpResponse response) {
    response.writeBody("<h1>site1</h1>");
}

@Mapping("/site2")
public void site2(HttpRequest request, HttpResponse response) {
    response.writeBody("<h1>site2</h1>");
}
```

컨트롤러의 메서드를 만들 때 둘 중에 필요한 메서드만 유연하게 받을 수 있도록
개선해보자.

```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
/**
 * 애노테이션 서블릿
 * @version 8
 */
public class AnnotationServletV2 implements HttpServlet {

    private final List<Object> controllers;

    public AnnotationServletV2(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {
                    Mapping mapping = method.getAnnotation(Mapping.class);
                    String value = mapping.value();

                    if (value.equals(path)) {
                        invoke(controller, method, request, response);
                        return;
                    }
                }
            }
        }

        throw new PageNotFoundException("request = " + request);
    }

    private void invoke(Object controller, Method method, HttpRequest request, HttpResponse response) {

        //메서드의 파라미터 타입 확인
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        //각 타입에 맞는 값을 인자 배열에 담아서 메서드를 호출
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == HttpRequest.class) args[i] = request;
            else if (parameterTypes[i] == HttpResponse.class) args[i] = response;
            else throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i]);
        }

        try {
            method.invoke(controller, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
```
```java
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;
/**
 * 애노테이션 서블릿
 * @version 8
 */
public class SiteControllerV8 {

    @Mapping("/")
    public void home(HttpResponse response) {
        response.writeBody("<h1>home</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li><a href='/site1'>site1</a></li>");
        response.writeBody("<li><a href='/site2'>site2</a></li>");
        response.writeBody("<li><a href='/search?q=hello'>검색</a></li>");
        response.writeBody("</ul>");
    }

    @Mapping("/site1")
    public void site1(HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }

    @Mapping("/site2")
    public void site2(HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }
}
```
```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.servlet.annotation.Mapping;

import java.io.IOException;
/**
 * 애노테이션 서블릿
 * @version 8
 */
public class SearchControllerV8 {

    @Mapping("/search")
    public void search(HttpRequest request, HttpResponse response) throws IOException {
        String query = request.getParameter("q");
        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }
}
```

호출할 컨트롤러 메서드의 매개변수를 먼저 확인한 다음에 매개변수에 필요한 값을
동적으로 만들어서 전달했다. 덕분에 컨트롤러의 메서드는 자신에게 필요한 값만 선언하고
전달받을 수 있다. 이런 기능을 확장하면 다양한 객체들도 전달할 수 있다.

스프링 MVC도 이런 방식으로 다양한 매개변수의 값을 동적으로 전달한다.

---

## 애노테이션 서블릿 - V9

위 버전의 아쉬운 부분이 있다.

**문제1. 성능**
```java
@Override
public void service(HttpRequest request, HttpResponse response) throws IOException {
    String path = request.getPath();
    
    for (Object controller : controllers) {
        Method[] methods = controller.getClass().getDeclaredMethods();
        
        for (Method method : methods) {
            if (method.isAnnotationPresent(Mapping.class)) {
                
                Mapping mapping = method.getAnnotation(Mapping.class);
                String value = mapping.value();
                
                if (value.equals(path)) {
                    invoke(controller, method, request, response);
                    return;
                }
            }
        }
    }
    
    throw new PageNotFoundException("request = " + request);
}
```

- 모든 컨트롤러의 메서드를 하나씩 순서대로 찾는다. 결과적으로 `O(n)`의 성능을 보인다.
- 문제는 고객의 요청 때마다 이 로직이 호출된다는 것이다.

**문제2. 중복 매핑 문제**
```java
@Mapping("/site2")
public void site2(HttpResponse response) {
    response.writeBody("<h1>site2</h1>");
}

@Mapping("/site2")
public void page2(HttpResponse response) {
    response.writeBody("<h1>page2</h1>");
}
```

- 개발자가 실수로 `@Mapping`에 같은 URL을 정의할 수도 있다.
- 이 경우 현재 로직에서는 먼저 찾은 메서드가 호출된다.
- 이런 모호한 문제는 반드시 제거해야 한다.

```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import was.httpserver.HttpServlet;
import was.httpserver.PageNotFoundException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 애노테이션 서블릿
 * @version 9
 */
public class AnnotationServletV3 implements HttpServlet {

    private final Map<String, ControllerMethod> pathMap;

    public AnnotationServletV3(List<Object> controllers) {
        this.pathMap = new HashMap<>();
        initPathMap(controllers);
    }

    private void initPathMap(List<Object> controllers) {

        for (Object controller : controllers) {
            Method[] methods = controller.getClass().getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(Mapping.class)) {

                    String path = method.getAnnotation(Mapping.class).value();

                    //중복 경로 발생 시 오류 발생
                    if (pathMap.containsKey(path)) {
                        ControllerMethod controllerMethod = pathMap.get(path);

                        throw new IllegalStateException(
                            "경로 중복 등록, path = " + path +
                            ", method = " + method +
                            ", 이미 등록된 메서드 = " + controllerMethod.method);
                    }

                    pathMap.put(path, new ControllerMethod(controller, method));
                }
            }
        }
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        ControllerMethod controllerMethod = pathMap.get(path);

        if (controllerMethod == null) {
            throw new PageNotFoundException("request = " + request);
        }

        controllerMethod.invoke(request, response);
    }

    private static class ControllerMethod {

        private final Object controller;
        private final Method method;

        public ControllerMethod(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        private void invoke(HttpRequest request, HttpResponse response) {

            Class<?>[] parameterTypes = method.getParameterTypes();
            Object[] args = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i] == HttpRequest.class) args[i] = request;
                else if (parameterTypes[i] == HttpResponse.class) args[i] = response;
                else throw new IllegalArgumentException("Unsupported parameter type: " + parameterTypes[i]);
            }

            try {
                method.invoke(controller, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```

- `AnnotationServletV3`을 생성하는 시점에 `@Mapping`을 사용하는 컨트롤러의 메서드를
모두 찾아서 `pathMap`에 보관한다.
- `ControllerMethod` 객체에 `@Mapping`의 대상 메서드와 메서드가 있는
컨트롤러 객체를 캡슐화했다.
- 중복 경로가 발생하면 컴파일 오류가 발생하면서 서버가 실행되지 않는다.

---

[이전 ↩️ - 애노테이션]()

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/Main.md)