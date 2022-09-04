# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 1단계 - REST API 및 테스트 리팩토링

## 요구사항

- HTML만 지원하는 현재의 @MVC 프레임워크에 `JSON` 데이터 요청 및 응답 기능 추가.
  - `JsonViewTest`를 통과하는 `JsonView`를 구현.
  - `UserAcceptanceTest`를 통과하는 `Controller`를 구현.

- Java Object를 JSON 데이터로 변환하는 View 인터페이스에 대한 구현체 `JsonView`를 추가.
  - JSON으로 응답할 때의 ContentType은 `MediaType.APPLICATION_JSON_UTF8_VALUE`로 변환.
  - Map에 담긴 model 데이터가 
    - 1개인 경우 value값을 반환.
    - 2개 이상인 경우 Map 자체를 JSON으로 변환해 반환. 

## 2단계 - JDBC 라이브러리 구현

## 요구사항

- JDBC에 대한 공통 라이브러리를 구현
  - Connection 생성 및 close
  - Statement 생성 및 close
  - ResultSet 생성 및 close
  - SQL문에 인자 setting
  - 트랜잭션 관리

구현된 라이브러리를 사용할 개발자가 다음의 3가지 구현에만 집중할 수 있도록 한다.
 - SQL 쿼리
 - 쿼리에 전달할 인자
 - SELECT 구문의 경우 조회한 데이터를 추출

 - SQL Exception을 런타임 Exception으로 변환

 - `UserDaoTest`가 통과하도록 회원목록과 개인정보 수정 실습을 진행
   - `UserDao`에 대한 리팩토링 진행
     - `UserDao`의 JDBC를 사용하는 반복 코드를 `JdbcTemplate`을 구현하여 중복 제거.
 
### Domain

- `JdbcTemplate`
  - `queryForObject()` - 단건 조회
    하나의 로우를 조회
  - `query()` - 목록 조회
    여러 로우를 조회 (리스트로 반환)
  - `update()` - 변경 (등록, 수정, 삭제)
    데이터 변경. SQL 결과에 영향받은 로우 수 `int` 반환

- `RowMapper`
  - 데이터베이스의 반환 결과인 `ResultSet`을 객체로 변환(매핑).

- `PreparedStatementSetter`
  - PreparedStatement에 값을 저장.
