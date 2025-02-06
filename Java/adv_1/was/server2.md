# 자바 - HTTP 서버 개발(with 리플렉션)

---

## 리플렉션이 필요한 이유

이전에 커맨드 패턴으로 만든 서블릿은 유용하지만 몇 가지 단점이 있다.

**문제 1. 하나의 클래스에 하나의 기능만 만들 수 있다.**
- 기능 하나를 만들 때 마다 각각 별도의 클래스를 만들고 구현해야 한다.
    - `Site1Servlet`, `Site2Servlet`, `SearchServlet` 등..
- 클래스가 너무 많아지기 때문에 복잡해진다.
- 만약 다음과 같이 하나의 클래스 안에서 다양한 기능을 처리할 수 있다면 문제를 해결할 수 있을 것이다.

```java
public class ReflectController {
    
public void site1(HttpRequest request, HttpResponse response) {         
    response.writeBody("<h1>site1</h1>");
}

public void site2(HttpRequest request, HttpResponse response) { 
    response.writeBody("<h1>site2</h1>");
}

public void search(HttpRequest request, HttpResponse response) {
        String query = request.getParameter("q");
        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");         
        response.writeBody("</ul>");
    } 
}
```
- 이렇게 하면 비슷한 기능을 한 곳에 모을 수 있고, 작은 기능 하나를 추가할 때마다
  클래스를 계속 만들지 않아도 된다.

**문제 2. 새로 만든 클래스를 URL 경로와 항상 매핑해야 한다.**

```java
servletManager.add("/site1", new Site1Servlet()); 
servletManager.add("/site2", new Site2Servlet()); 
servletManager.add("/search", new SearchServlet());
...
```

- 새로운 기능을 하나 추가할 때마다 이런 매핑 작업도 함께 추가해야 한다.
- 그런데 위에서 본 `ReflectController`를 보면 메서드 이름과 URL 경로의 이름이 같다.
- 만약 URL 경로의 이름과 같은 이름의 메서드를 찾아서 호출할 수 있다면 번거로운
  매핑 작업을 제거할 수 있을 것이다.

**자바의 리플렉션 기능을 사용해 자바 프로그램 실행 중에 이름으로 메서를 찾고, 또 찾은
메서드를 호출할 수 있다.**

---

## 리플렉션을 처리하는 서블릿 구현 - V6

```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
/**
 * HTTP 서버 개발 - 리플렉션 적용
 * @version 6
 */
public class SiteControllerV6 {

    public void site1(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site1</h1>");
    }

    public void site2(HttpRequest request, HttpResponse response) {
        response.writeBody("<h1>site2</h1>");
    }
}
```
```java
import was.httpserver.HttpRequest;
import was.httpserver.HttpResponse;
import java.io.IOException;
/**
 * HTTP 서버 개발 - 리플렉션 적용
 * @version 6
 */
public class SearchControllerV6 {

    public void search(HttpRequest request, HttpResponse response) throws IOException {

        String query = request.getParameter("q");

        response.writeBody("<h1>Search</h1>");
        response.writeBody("<ul>");
        response.writeBody("<li>query: " + query + "</li>");
        response.writeBody("</ul>");
    }
}
```
- `XxxController`에서 호출 대상이 되는 메서드는 반드시 `HttpRequest`와 `HttpResponse`를 인자로 받아야 한다.
- 이런 컨트롤러들을 호출하려면 서블릿에 리플렉션 기능을 구현하면 된다.
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
 * HTTP 서버 개발 - 리플렉션 서블릿
 * @version 6
 */
public class ReflectionServlet implements HttpServlet {

    private final List<Object> controllers;

    public ReflectionServlet(List<Object> controllers) {
        this.controllers = controllers;
    }

    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        for (Object controller : controllers) {
            Class<?> clazz = controller.getClass();
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                String methodName = method.getName();

                if (path.equals("/" + methodName)) {
                    invoke(controller, method, request, response);
                    return;
                }
            }
        }

        throw new PageNotFoundException("request = " + path);
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
- 이 서블릿은 요청이 오면 모든 컨트롤러를 순회한다. 그리고 선언된 메서드 정보를 통해
    URL의 요청 경로와 메서드 이름이 맞는지 확인한다.
- 메서드 이름이 맞다면 `invoke()`를 통해 해당 메서드를 동적으로 호출한다.
- 이때 `HttpRequest`, `HttpResponse` 정보도 함께 넘겨주기 때문에 대상 메서드는 반드시 이 두 객체를
인자로 받아야 한다.
```java
import was.httpserver.HttpServer;
import was.httpserver.ServletManager;
import was.httpserver.servlet.DiscardServlet;
import was.httpserver.servlet.reflection.ReflectionServlet;
import was.v5.servlet.HomeServlet;

import java.io.IOException;
import java.util.List;
/**
 * HTTP 서버 개발 - 리플렉션 적용
 * @version 6
 */
public class ServerMainV6 {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {

        List<Object> controllers = List.of(new SiteControllerV6(), new SearchControllerV6());
        ReflectionServlet reflectionServlet = new ReflectionServlet(controllers);

        ServletManager servletManager = new ServletManager();
        servletManager.setDefaultServlet(reflectionServlet);
        servletManager.add("/", new HomeServlet());
        servletManager.add("/favicon.ico", new DiscardServlet());

        HttpServer server = new HttpServer(PORT, servletManager);
        server.start();
    }
}
```
- 리플렉션 서블릿을 생성하고 사용한 컨트롤러들을 인자로 전달한다.
- 리플렉션 서블릿을 기본 서블릿으로 등록하여 다른 서블릿에서 경로를 찾지 못할 때
    리플렉션 서블릿이 호출된다.
- `/`와 `/favicon.ico`의 경우 메서드 이름을 `/`이나 `favicon.ico`로 만들 수 없기 때문에
    메서드 이름으로 매핑할 수 없다.

리플렉션을 사용하여 하나의 클래스에 하나의 기능만 만들 수 있다는 단점과 새로 만든 클래스를
URL 경로와 항상 매핑해야 한다는 단점을 해결할 수 있었다.

- 하지만 요청 이름과 메서드 이름을 다르게 하고 싶다면 어떻게 해야할까? 예를 들어
메서드 이름은 더 자세히 적고 싶을 수 있다.
- 그리고 `/`, `/favicon.ico`와 같이 자바 메서드 이름으로 처리하기 어려운 URL은 어떻게 해결할 수 있을까? 
- 또한 URL은 주로 `-`(dash)를 구분자로 사용한다. `/add-member`와 같은 URL은 어떻게 
해결할 수 있을까?

---

[이전 ↩️ - 리플렉션]()

[메인 ⏫](https://github.com/genesis12345678/TIL/blob/main/Java/adv_1/Main.md)

[다음 ↪️ - 애노테이션]()