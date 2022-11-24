# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## step1
- JsonViewTest 를 위한 jsonView 구현
  - contentType 설정
  - model 이 1개 또는 2개 이상일 경우 로직 구현
- UserAcceptanceTest 를 위한 Controller 구현
  - Controller 구현
  - RequestBody 처리할 수 있는 Resolver 구현

## step2 & step3
- UserDaoTest 를 위한 UserDao 내 로직 구현
  - update
  - findAll
- 로직을 메서드화 리팩토링
- insert,update 로직을 InsertJdbcTemplate, UpdateJdbcTemplate 을 구현 후 이동
- 추상 메서드 구현 및 적용(UserDao 의존성 제거)
- JdbcTemplate 으로 통합
- select 로직을 SelectJdbcTemplate 구현 후 이동
- SelectJdbcTemplate 을 JdbcTemplate 으로 통합
- PreparedStatementSetter, RowMapper 인터페이스화
- 커스텀 DataAccessException 구현 및 사용
- try-with-resources 문법 적용
- 제네릭 적용
- 유연한 메소드 사용을 위한 가변인자 문법 적용
- 인스턴스 변수 이름과 테이블 컬럼 이름이 같으면 자동으로 매핑되는 기능 구현

## step4
- Interceptor 인터페이스 구현
- LogMeasureInterceptor 구현체 구현(preHandle, postHandle 메서드)
- Interceptor 구현체 리스트를 관리하는 InterceptorRegistry 일급 콜렉션 구현