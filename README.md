# JDBC 라이브러리 구현
<hr />

## 1단계 - REST API 및 테스트 리팩토링

### 요구사항 1 - JsonViewTest 테스트 통과
- HTML 이외에 JSON 으로 데이터를 요청하고 응답을 받도록 지원
- core.mvc.JsonViewTest 모든 테스트 pass 하도록 JsonView 구현

### 요구사항 2 - UserAcceptanceTest 테스트 통과
- next.controller.UserAcceptanceTest 테스트를 pass 하도록 Controller를 추가 
- Controller는 애노테이션 기반 MVC 사용

<hr />

## 2단계 - JDBC 라이브러리 구현
### 요구사항 
- JDBC에 대한 공통 라이브러리를 만들기
  - 개발자는 SQL 쿼리, 쿼리에 전달할 인자, SELECT 구문의 경우 조회한 데이터를 추출하는 3가지 구현에만 집중하도록 해야 함
- SQLException을 런타임 Exception 으로 변환
- src/test/java에 있는 next.dao.UserDaoTest 클래스를 활용

<hr />

## 3단계 - JDBC 라이브러리 구현(힌트)
### 요구사항
- 2단계와 요구사항 동일
- 각 쿼리에 전달할 인자를 자바의 가변인자, PreparedStatementSetter 를 통해 전달