# 스프링 부트

## 스프링 부트 핵심 기능

- **WAS** : `Tomcat`같은 웹 서버를 내장해서 별도의 웹 서버를 설치하지 않아도 된다.
- **라이브러리 관리** 
  - 손쉬운 빌드 구성을 위한 스타터 종속성을 제공한다.
  - 스프링과 외부 라이브러리의 버전을 자동으로 관리해준다.
- **자동 구성** : 프로젝트 시작에 필요한 스프링과 외부 라이브러리의 빈을 자동 등록해준다.
- **외부 설정** : 환경에 따라 달라져야 하는 외부 설정을 공통화할 수 있다.
- **프로덕션 준비** : 모니터링을 위한 메트릭, 상태를 확인할 수 있는 기능을 제공한다.

**스프링 부트는 스프링 프레임워크를 쉽게 사용할 수 있게 도와주는 도구일 뿐 본질은 스프링 프레임워크다.**<br>
하지만 스프링 부트가 제공하는 편의 기능이 너무 막강해서 스프링 부트 사용은 거의 필수이다.

스프링 부트는 편리한 만큼 매우 많은 것을 자동화 해준다. **그만큼 스프링 부트가 어떤 원리로 작동하는지 알아두어야 문제가 발생했을 때 빠른 대처가 가능할 것이다.**


- [웹 서버와 서블릿 컨테이너](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/servletContainer/ServletContainer.md)
- [내장 톰캣](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/embedTomcat/EmbedTomcat.md)
- [스프링부트 스타터와 라이브러리 관리](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/starterLibrary/Library.md)
- [자동 구성](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/autoConfig/AutoConfig.md)
- [외부 설정과 프로필](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/externalConfig/ExternalConfig.md)
- [액츄에이터](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/actuator/Actuator.md)
- [마이크로미터](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/micrometer.md)
  - [프로메테우스](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Prometheus.md)
  - [그라파나](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/Grafana.md)
- [모니터링 메트릭 활용](https://github.com/genesis12345678/TIL/blob/main/Spring/springboot/monitoring/MetricsUse.md)