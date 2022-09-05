# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


# 기능 요구사항 (JSON 응답)
지금까지의 @MVC 프레임워크는 HTML 밖에 지원하지 않는다. HTML 이외의 JSON 으로 데이터를 요청하고 응답을 받도록 지원한다.
- core.mvc.JsonViewTest 의 모든 테스트를 패스 하도록 JsonView 를 구현한다.
- next.controller.UserAcceptanceTest 테스트를 패스 하도록 Controller 를 추가한다. Controller 는 애노테이션 기반 MVC 를 사용한다.

# 기능 요구사항 (JDBC 라이브러리 구현)
JDBC 에 대한 공통 라이브러리를 만들어서 개발자가 SQL 쿼리, 쿼리에 전달할 인자, SELECT 구문의 경우 조회한 데이터를 추출하는 3가지 구현에만 집중하도록 돕는다.
또한 SQLException 을 런타임 Exception 으로 변환해서 try - catch 절로 인해 소스 코드의 가독성을 해지지 않도록 한다.

- 자바 기능을 최대한으로 활용해 가능한 깔끔한 코드 (clean code)를 구현하는 연습을 한다.
  - 익명 클래스 (anonymous class)
  - 함수형 인터페이스 (functional interface)
  - generic
  - 가변 인자
  - try-with-resources
  - compiletime exception vs runtime exception
  - 람다(lambda)

| 작업                    |JDBC 라이브러리 | 개발자가 구현할 부분  |
|-----------------------|---|--------------|
| Connection 생성 및 close | O  |    X          |
| SQL 문                 |  X |   O           |
| Statement 생성 및 close	 | O  |  X            |
| ResultSet 생성 및 close	 | O  |   X           |
| SQL 문에 전달할 값	         | X  |   O           |
| ResultSet에서 데이터 추출	   | X  |  O            |
| SQL 문에 인자 setting	    |  O |     X         |
| 트랜잭션 관리	              |  O |   X           |

# 기능 목록
**UserAcceptanceTest 를 위한 UserApiController 를 생성한다.**
- createUser
  - user 에 대한 정보를 @RequestBody 로 받아서 DataBase 에 저장하고, userId 를 반환한다.
- readUser
  - userId 를 @RequestParam 으로 받아서 DataBase 에서 찾아서 user 정보를 반환한다.
- updateUser
  - userId 를 @RequestParam, 수정할 user 정보를 @RequestBody 로 받아서 DataBase 에서 기존의 user 를 찾고, 수정할 정보로 update 해 준다. 
- RequestBodyArgumentResolver
  - @RequestBody 애노테이션이 붙은 파라미터에 대한 resolver 를 지원한다.

**UserDao 의 클라이언트 코드 중복을 최소화 하기 위한 JDBC 라이브러리를 구현한다.**
- JdbcTemplate 객체 (제네릭 이용)
  - update 메서드
    - insert, update 쿼리를 담당한다.
    - SQL 문과 insert 혹은 update 에 필요한 가변 인자를 파라미터로 받는다.
  - query 메서드
    - select 쿼리 (multi) 를 담당한다.
  - queryForObject 메서드
    - select 쿼리 (single) 를 담당한다.

- RowMapper 인터페이스
  - jdbc 의 PreparedStatement 의 결과인 ResultSet 에서 우리가 얻고자 하는 Entity 를 매핑해주는 메서드를 제공한다.
  - ObjectRowMapper (제네릭 이용)
    - 특정 엔티티를 매핑하도록 구현한다. (리플렉션 이용)
    - 해당 오브젝트의 기본 생성자가 존재해야 매핑할 수 있다.

- DataAccessException 예외
  - SQLException 컴파일 예외를 런타임 예외로 변환하여 throw 한다. (RuntimeException 을 상속 받는다.)
