# 나만의 라이브러리 구현

## 1단계 - REST API 및 테스트 리팩토링

> 지금까지 구현한 @MVC 프레임워크는 HTML 밖에 지원하지 않는다.
> HTML 이외에 JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.

* core.mvc.JsonViewTest의 모든 테스트를 pass하도록 JsonView를 구현한다.
* next.controller.UserAcceptanceTest 테스트를 pass하도록 Controller를 추가한다. Controller는 애노테이션 기반 MVC를 사용한다.

### 1단계 note

고칠 것 목록

- HandlerMappingRegistry - fix typo

궁금한 친구들 목록

- User.java에도 적어두었지만 jackson은 json properties를 이용해 생성자의 타입을 추론하는걸까?
    - 기본 생성자가 있으면 추론을 안하는 것 같음.. 나중에 코드를 보는걸로  