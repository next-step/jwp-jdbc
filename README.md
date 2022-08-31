# JDBC 라이브러리 구현

## 진행 방법

- 프레임워크 구현에 대한 요구사항을 파악한다.
- 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
- 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
- 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정

- [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## Step1

## 기능 요구사항

- 지금까지 구현한 @MVC 프레임워크는 HTML 밖에 지원하지 않는다.
- HTML 이외에 JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.
- core.mvc.JsonViewTest의 모든 테스트를 pass하도록 JsonView를 구현한다.
- next.controller.UserAcceptanceTest 테스트를 pass하도록 Controller를 추가한다. Controller는 애노테이션 기반 MVC를 사용한다.

## 힌트

- 구글에서 `how to get body from httpservletrequest` 과 같은 키워드로 검색해 body 데이터를 읽는다.
- [Jackson 라이브러리](https://github.com/FasterXML/jackson)를 활용해 JSON to Java Object로 변환한다.
  - Jackson에 대한 학습 테스트를 추가해 사용법을 익힌다.
  - [Intro to the Jackson ObjectMapper](https://www.baeldung.com/jackson-object-mapper-tutorial) 문서 참고
- Java Object를 JSON 데이터로 변환하는 View 인터페이스에 대한 구현체를 추가한다.
  - JSON으로 응답할 때의 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE 으로 반환한다.
  - Map에 담긴 model 데이터가 1개인 경우 value 값을 반환, 2개 이상인 경우 Map 자체를 JSON으로 변환해 반환한다.
- userId를 queryString으로 전달하는 경우 HttpServletRequest에서 값을 꺼내는 방법은 `request.getParameter("userId")` 로 구현할 수 있다.

## Step2

## 요구사항

- JDBC에 대한 공통 라이브러리를 만들어 개발자가 SQL 쿼리, 쿼리에 전달할 인자, SELECT 구문의 경우 조회한 데이터를 추출하는 3가지 구현에만 집중하도록 해야 한다.
- 또한 SQLException을 런타임 Exception으로 변환해 try/catch 절로 인해 소스 코드의 가독성을 헤치지 않도록 해야 한다.
- 리팩토링을 하는 과정에서 최대한 컴파일 에러를 발생시키지 않으면서 점진적으로 리팩토링한다.
-  src/test/java에 있는 next.dao.UserDaoTest 클래스를 활용한다.
- UserDaoTest 테스트 코드가 성공하도록 회원목록과 개인정보수정 실습을 진행한 후 UserDao에 대한 리팩토링 실습을 진행한다.
