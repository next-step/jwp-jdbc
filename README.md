# JDBC 라이브러리 구현

## 1단계 - REST API 및 테스트 리팩토링

### 요구사항 1 - JsonViewTest 테스트 통과
- HTML 이외에 JSON 으로 데이터를 요청하고 응답을 받도록 지원
- core.mvc.JsonViewTest 모든 테스트 pass 하도록 JsonView 구현

### 요구사항 2 - UserAcceptanceTest 테스트 통과
- next.controller.UserAcceptanceTest 테스트를 pass 하도록 Controller를 추가 
- Controller는 애노테이션 기반 MVC 사용