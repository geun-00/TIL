# 스프링 데이터 JPA

**스프링 데이터 JPA**는 스프링 프레임워크에서 JPA를 편리하게 사용할 수 있도록 지원하는 프로젝트다. 이 프로젝트는 데이터 접근 계층을 개발할 때 지루하게
반복되는 `CRUD` 문제를 해결해준다. 

우선 CRUD를 처리하기 위한 공통 인터페이스를 제공하고 레포지토리를 개발할 때 인터페이스만 작성하면 **실행 시점에 JPA가 구현 객체를 동적으로 생성해서 주입해준다.**<br>
**데이터 접근 계층을 개발할 때 구현 클래스 없이 인터페이스만 작성해도 개발을 완료할 수 있다.**

스프링 데이터 JPA를 사용하면 `@Repository`도 생략이 가능하다. 컴포넌트 스캔을 스프링 데이터 JPA가 자동으로 처리해 주고 JPA 예외를 스프링 예외로 변환하는
과정도 자동으로 처리해준다.

- [공통 인터페이스 기능](https://github.com/genesis12345678/TIL/blob/main/Spring/dataJpa/common_interface/common_interface.md)
- [쿼리 메서드 - 1](https://github.com/genesis12345678/TIL/blob/main/Spring/dataJpa/query_method/query_method_1.md)
- [쿼리 메서드 - 2](https://github.com/genesis12345678/TIL/blob/main/Spring/dataJpa/query_method/query_method_2.md)
- [Web 확장 기능](https://github.com/genesis12345678/TIL/blob/main/Spring/dataJpa/extend/extend.md)
- [스프링 데이터 JPA 분석](https://github.com/genesis12345678/TIL/blob/main/Spring/dataJpa/analyse/analyse.md)
- [그 외 기능들](https://github.com/genesis12345678/TIL/blob/main/Spring/dataJpa/functions/functions.md)

> 전체 내용에 대한 출처 : [인프런 - 김영한 님의 "실전! 스프링 데이터 JPA"](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-JPA-%EC%8B%A4%EC%A0%84)