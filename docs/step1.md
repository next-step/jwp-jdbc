# 1단계 - REST API 및 테스트 리팩토링

## 실습 환경 구축 및 코드 리뷰
* 미션 시작 버튼을 클릭해 리뷰어 매칭과 브랜치를 생성한다.
* JDBC 라이브러리 구현 실습을 위한 저장소 저장소 브랜치에 자신의 github 아이디에 해당하는 브랜치가 있는지 확인한다. 없으면 미션 시작 버튼을 눌러 미션을 시작한다.
* 온라인 코드리뷰 요청 1단계 문서의 1단계부터 5단계까지 참고해 실습 환경을 구축한다.
* next.WebServerLauncher를 실행한 후 브라우저에서 http://localhost:8080으로 접근한다.
* 브라우저에 질문/답변 게시판이 뜨면 정상적으로 세팅된 것이다.

## 기능 요구사항
> 지금까지 구현한 @MVC 프레임워크는 HTML 밖에 지원하지 않는다.
> HTML 이외에 JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.

* core.mvc.JsonViewTest의 모든 테스트를 pass하도록 JsonView를 구현한다.
* next.controller.UserAcceptanceTest 테스트를 pass하도록 Controller를 추가한다. Controller는 애노테이션 기반 MVC를 사용한다.

## 힌트
* 구글에서 how to get body from httpservletrequest과 같은 키워드로 검색해 body 데이터를 읽는다.
* Jackson 라이브러리를 활용해 JSON to Java Object로 변환한다.
    * Jackson에 대한 학습 테스트를 추가해 사용법을 익힌다.
    * Intro to the Jackson ObjectMapper 문서 참고
* Java Object를 JSON 데이터로 변환하는 View 인터페이스에 대한 구현체를 추가한다.
    * JSON으로 응답할 때의 ContentType은 MediaType.APPLICATION_JSON_UTF8_VALUE 으로 반환한다.
    * Map에 담긴 model 데이터가 1개인 경우 value 값을 반환, 2개 이상인 경우 Map 자체를 JSON으로 변환해 반환한다.
* userId를 queryString으로 전달하는 경우 HttpServletRequest에서 값을 꺼내는 방법은 request.getParameter("userId")로 구현할 수 있다.

## 자신이 구현한 @MVC를 사용하고 싶다면
* 자신이 구현한 MVC 프레임워크를 사용해 미션을 진행하고 싶다면 core.mvc와 core.mvc.tobe 패키지 코드를 자신이 구현한 코드로 바꿔치기 한 후 미션을 진행한다.