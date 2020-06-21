# 나만의 라이브러리 구현

## 1단계 - REST API 및 테스트 리팩토링

> 지금까지 구현한 @MVC 프레임워크는 HTML 밖에 지원하지 않는다.
> HTML 이외에 JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.

* core.mvc.JsonViewTest의 모든 테스트를 pass하도록 JsonView를 구현한다.
* next.controller.UserAcceptanceTest 테스트를 pass하도록 Controller를 추가한다. Controller는 애노테이션 기반 MVC를 사용한다.
