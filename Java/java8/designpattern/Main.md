# 디자인 패턴

### 객체 지향 디자인 원칙

- 애플리케이션에서 달라지는 부분을 찾아내고, 달라지지 않는 부분과 분리한다.
- 구현보다는 인터페이스에 맞춰서 프로그래밍한다.
- 상속보다는 구성을 활용한다.
- 상호작용하는 객체 사이에는 가능하면 느슨한 결합을 사용해야 한다.
- 클래스는 확장에는 열려 있어야 하지만, 변경에는 닫혀 있어야 한다.
- 추상화된 것에 의존하게 만들고 구상 클래스에 의존하지 않게 만든다.
- 진짜 절친에게만 이야기해야 한다.
  - 어떤 객체든 그 객체와 상호작용을 하는 클래스의 개수와 상호작용 방식에 주의를 기울여야 한다.
- 할리우드 원칙 (먼저 연락하지 마세요. 저희가 연락 드리겠습니다.)
  - 고수준 모듈(추상클래스, 인터페이스)에 의존하고 고수준 모듈에서 연락(메서드 실행)하라는 원칙
  - 고수준 모듈이 필요할 때 저수준 모듈(서브클래스)을 호출한다.
- 어떤 클래스가 바뀌는 이유는 하나뿐이어야 한다.

## 생성 패턴

### [팩토리 메서드 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/creational/factoryMethod/FactoryMethod.md)
### [추상 팩토리 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/creational/absractFactory/AbstractFactory.md)
### [싱글톤 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/creational/singleton/Singleton.md)
### []()
### []()
### []()
### []()

## 구조 패턴

### [데코레이터 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/structural/decorator/Decorator.md)
### [어댑터 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/structural/adapter/Adapter.md)
### [퍼사드 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/structural/facade/Facade.md)
### []()
### []()
### []()
### []()

## 행동 패턴

### [전략 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/behavioral/strategy/Strategy.md)
### [옵저버 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/behavioral/observer/Observer.md)
### [커맨드 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/behavioral/command/Command.md)
### [템플릿 메서드 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/behavioral/templateMethod/TemplateMethod.md)
### [반복자 패턴](https://github.com/genesis12345678/TIL/blob/main/Java/java8/designpattern/behavioral/iterator/Iterator.md)
### []()
### []()
### []()