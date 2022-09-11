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

# 기능 요구사항 (Interceptor 구현)
- 여러 Servlet 에서 공통으로 처리해야하는 중복 로직이 있는 경우는 Servlet Filter 를 통해 해결할 수 있다.
- Servlet Filter 를 활용하면 Servlet 을 실행하기 전/후에 공통적인 작업을 추가할 수 있다.
- MVC 프레임워크를 구현하면서 Servlet 이 담당하던 역할을 Controller 가 담당하고 있다. 이 Controller 에도 공통으로 처리할 필요가 있는 로직이 필요하다.
- Controller 전/후에 로직을 추가할 수 있는 Interceptor 를 구현해 본다.
- Interceptor 를 구현한 후 각 Controller 메소드의 실행 속도를 측정한 후 debug level 로 log 를 출력한다.

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
    - SQL 문과 insert 혹은 update 에 필요한 가변 인자, 혹은 PreparedStatementSetter 를 파라미터로 받는다.
  - query 메서드
    - select 쿼리 (multi) 를 담당한다.
    - SQL 문과 select 결과를 반환하기 위해 필요한 RowMapper, 그리고 필요하다면 select 에 필요한 가변 인자, 혹은 PreparedStatementSetter 를 파라미터로 받는다. 
  - queryForObject 메서드
    - select 쿼리 (single) 를 담당한다.
    - SQL 문과 select 결과를 반환하기 위해 필요한 RowMapper, 그리고 select 에 필요한 가변 인자, 혹은 PreparedStatementSetter 를 파라미터로 받는다.

- RowMapper 인터페이스
  - jdbc 의 PreparedStatement 의 결과인 ResultSet 에서 우리가 얻고자 하는 Entity 를 매핑해주는 메서드를 제공한다.
  - ObjectRowMapper (제네릭 이용)
    - 특정 엔티티를 매핑하도록 구현한다. (리플렉션 이용)
    - 해당 오브젝트의 기본 생성자가 존재해야 매핑할 수 있다.

- ResultSetExtractor 인터페이스
  - RowMapper 인터페이스와 메소드의 역할은 동일하나, Collection 데이터를 반환할 때 사용한다.
  - RowMapperResultSetExtractor 구현체
    - RowMapper 를 필드로 가진다.
    - ResultSet 의 값을 iterate 하여 값을 반환한다.

- PreparedStatementCreator 인터페이스
  - Connection 으로 부터 PreparedStatement 를 만든다.
  - SimplePreparedStatementCreator 인터페이스 
    - String sql 을 필드로 가진다.
    - String sql 을 통해 connection 과 함께 PreparedStatement 를 만든다.

- PreparedStatementSetter 인터페이스
  - PreparedStatement 에 값을 셋팅하도록 하는 메서드를 제공한다.
  - SimplePreparedStatementSetter 구현체
    - 가변 인자를 통해 PreparedStatement 에 값을 셋팅하도록 구현한다.


- DataAccessException 예외
  - SQLException 컴파일 예외를 런타임 예외로 변환하여 throw 한다. (RuntimeException 을 상속 받는다.)

**Interceptor 를 이용하여 각 Controller 의 실행 속도를 측정한다.**
- HandlerInterceptor 인터페이스
  - preHandle 메서드
    - handler 실행 전에 이 메서드를 호출한다. 만약 return 값이 false 라면 이후 동작은 하지 않는다.
  - PostHandle 메서드
    - handler 실행 후에 이 메서드를 호출한다.
  - afterCompletion 메서드
    - handler 실행 후, view 가 render 가 된 후에 이 메서드를 호출한다.
  - TimeMeasuringInterceptor 구현체
    - StopWatch 를 이용하여 핸들러의 preHandle 메서드에서 해당 스탑 워치를 시작하고, HttpServletRequest (thread-safe) attribute 로 저장한다.
    - postHandle 에서 HttpServletRequest 의 attribute 로 스탑 워치를 가져와서 실행 속도를 debug level 로 출력한다.
- InterceptorRegistration 객체
  - 하나의 인터셉터와 해당 인터셉터가 적용 혹은 적용되지 않아야할 include/exclude patterns 를 관리한다.
  - InterceptorRegistry 에 인터셉트를 셋팅하는 시점에 함께 적용 patterns 를 셋팅한다.
  - requestUri 와 해당 인터셉터 uri 조건과 비교하여 인터셉터 조건과 매칭될 경우, 해당 인터셉터를 반환한다.
- InterceptorRegistry 객체
  - InterceptorRegistration 을 리스트로 관리한다.
  - interceptor 를 등록하거나, requestUri 에 맞는 인터셉터 리스트를 반환한다.
- HandlerInterceptorExecutor 객체
  - 등록된 인터셉터들의 preHandle, postHandle, applyCompletion 메서드를 차례로 실행한다. 