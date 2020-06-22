# 나만의 라이브러리 구현

## 2단계 - JDBC 라이브러리 구현

JDBC 라이브러리 구현 요구사항

1. JDBC에 대한 공통 라이브러리를 만들어 개발자가 다음에만 집중하도록 함.
    - SQL 쿼리
    - 쿼리에 전달할 인자
    - 조회한 데이터를 추출(매핑)
2. SQLException을 Runtime으로 변환해 try/catch로 가독성이 부셔지는 일을 막자.

분석 & 설계

- SQL 연산을 표현하는 JdbcOperation 정의
    - query: 리스트를 조회
    - queryForSingleObject: 단일 row 조회 (=_=윈도우 API WaitForSingleObject가 떠오름..)
    - update: 업데이트(insert, update, delete 쿼리??)
- 결과 매퍼 (RowMapper)
    - 맵과 객체를 연결
- CommonJDBC 클래스
    - 위의 친구를 잘 포장하는 친구


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