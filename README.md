# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 1단계 - REST API 및 테스트 리팩토링
1. JSON으로 데이터를 요청하고 응답을 받도록 지원한다.
   1. core.mvc.JsonViewTest의 모든 테스트를 pass 한다.
      1. JsonView 클래스의 render 메소드를 구현한다.
         1. render_no_element 메소드 테스트를 통과한다. (빈 데이터와 JSON 형식의 contentType으로 응답한다.)
         2. render_one_element 메소드 테스트를 통과한다. (model으로 Car 객체를 받아 JSON 형태로 응답한다.)
         3. render_over_two_elemnt 메소드 테스트를 통과한다. (model으로 두개 이상의 데이터를 받아 JSON 형식으로 응답한다.)
   2. next.controller.UserAcceptanceTest 테스트를 pass 하도록 Controller를 추가한다.
      1. UserAcceptanceController 클래스를 생성한다.
         1. crud 메서드의 회원가입 기능을 구현한다.
         2. crud 메서드의 조회 기능을 구현한다.
         3. crud 메서드의 수정 기능을 구현한다.

## 2단계 - JDBC 라이브러리 구현
1. JDBC에 대한 공통 라이브러리를 만든다. (개발자가 SQL 쿼리, 쿼리에 전달할 인자, SELECT 구문의 경우 조회한 데이터를 추출하는 3가지 구현에만 집중하도록 한다.)
   1. insert 메서드를 리팩토링 한다.
      1. Jdbc Library를 생성하고 UserDao에 있는 로직을 옮겨간다.
      2. parameter로 User를 받고 정상동작을 확인한 후에 리플렉션으로 Object 타입을 받을 수 있도록 한다.
      3. 메서드 시그니처는 void insert(String sql, Object obj)로 구성한다.
      4. 전달할 때 sql query는 INSERT INTO USERS VALUES (#{user.userId}, #{user.password}, #{user.name}, #{user.email}) 형태로 한다.
      5. 받은 데이터에서 #{}을 찾아내어 해당 데이터를 getter를 찾고, 해당 데이터로 치환하여 실행한다.
   2. update 메서드를 리팩토링 한다.
      1. 메서드 시그니처로 void update(String sql, Object obj) 으로 구성한다.
      2. UPDATE USERS SET name = #{user.name}, email = #{user.email} WHERE userId = #{user.userId} 형태로 전달 받는다.
   3. findAll 메서드를 리팩토링 한다.
      1. \<T> List\<T> findAll(String sql, Class\<T> resultType) 으로 받아서 실행한다.
      2. SELECT userId, password, name, email FROM USERS 형태로 전달 받는다.
      3. ResultSet 을 찾아 리턴하는 부분을 리플렉션으로 T의 생성자를 찾고 파라미터를 매핑해서 가져오며, 빈생성자와 setter 또는 모든 생성자 유무를 판단해서 데이터를 넣는다.
   4. findByUserId 메서드를 리팩토링 한다.
      1. \<T> T findById(String sql, String id, Class\<T> resultType) 로 받아서 실행한다.
      2. SELECT userId, password, name, email FROM USERS WHERE userid = #{user.userId} 형태로 전달 받는다.
      3. ResultSet 을 찾아 리턴하는 부분을 리플렉션으로 T의 생성자를 찾고 파라미터를 매핑해서 가져오며, 빈생성자와 setter 또는 모든 생성자 유무를 판단해서 데이터를 넣는다.

## 3단계 - JDBC 라이브러리 구현 (힌트)
1. INSERT, UPDATE 쿼리를 가지는 메소드의 중복을 제거한다.
2. 분리한 메소드 중에서 변화가 발생하지 않는 부분을 새로운 클래스로 추가한 후 이동한다.
3. InsertJdbcTemplate, UpdateJdbcTemplate이 UserDao에 대한 의존관계를 끊는다.
4. InsertJdbcTemplate, UpdateJdbcTemplate의 구현 부분이 같기 때문에 하나를 사용하도록 리팩토링 한다.
5. JdbcTemplate은 User와 의존관계를 가지기 때문에 재사용이 불가능하다. User와의 연관관계를 끊는다.
6. JdbcTemplate은 특정 DAO 클래스에 종속적이지 않다. 같은 방법으로 SelectJdbcTemplate를 생성한다.
7. JdbcTemplate, SelectJdbcTemplate간의 중복코드를 제거하여 하나만 사용하도록 리팩토링한다.
8. 통합하였을 때의 문제점을 찾고, 이를 해결하기 위한 방법을 찾는다.
9. SQLException을 runtimeException으로 변환하여 throw한다. try-with-resources 구문을 사용한다.
10. 제네릭을 적용해 캐스팅 부분을 제거한다.
11. preparedStatementSetter 대신 가변인자를 통해 인자를 받을 수 있도록 한다.
12. UserDao에서 PreparedStatementSetter, RowMapper 인터페이스 구현하는 부분을 람다표현식을 활용해 리팩토링한다.

## 4단계 - Interceptor 구현
1. Controller 전/후에 로직을 수행할 수 있는 Interceptor를 구현한다.
2. Interceptor를 구현한 후 Controller 메소드의 실행속도를 debug level로 출력한다.