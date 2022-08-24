# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# 🚀 1단계 - REST API 및 테스트 리팩토링

### 요구사항
> HTML 이외에 JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.

### 기능 목록
- [x] JsonUtils
  - [x] Java Object를 Json 문자열로 변환한다  
- [x] JsonView 구현
  - [x] 응답을 Json 포맷으로 한다
    - [x] 응답 헤더에 ContentType을 'application/json'으로 설정한다  
  - [x] 응답 데이터의 요소가 1개인 경우 
  - [x] 응답 데이터의 요소가 2개 이상인 경우 
- [x] RequestParameterUtils 
  - [x] 클라이언트의 요청 파라미터를 QeuryString 혹은 RequestBody에서 구한다
- [x] UserApiController 구현
  - [x] Annotation 기반 Controller로 구현한다
  - [x] 회원가입 `POST /api/users`
  - [x] 회원조회 `GET /api/users`
  - [x] 회원수정 `PUT /api/users`

### 1단계 피드백
- [x] JsonView 에서 한글 데이터를 처리할 수 있도록 수정(한글이 깨지지 않도록)
- [x] JsonUtils#getParameter 메서드명 수정 (`getAsStringOrNull`)
  - 리턴 타입을 메서드명에 명시함으로써 사용자에게 API 를 쉽게 사용할 수 있도록(?) 할 수 있다
  - null 이 리턴될 수 있어서 메서드명에 `OrNull` 을 추가함 
- [x] RequestBody 를 처리할 수 있는 ArgumentResolver 추가
  - [x] `@RequestBody` 애너테이션 추가
  - [x] RequestParameterUtils를 활용한 query string, json 지원 
- [x] AbstractModelArgumentResolver 추상화 
  - [x] RequestBodyArgumentResolver에서 동일한 코드를 재사용 
- [x] RequestParameterUtils ThreadLocal 추가 
  - [x] HttpServletRequest에서 한 번 조회한 post data 를 여러 파라미터에도 적용할 수 있도록 ThreadLocal 사용 


# 🚀 2단계 - JDBC 라이브러리 구현

### 요구사항
> JDBC에 대한 공통 라이브러리를 만들어 개발자가 `SQL 쿼리`, `쿼리에 전달할 인자`, `SELECT 구문의 경우 조회한 데이터를 추출`하는   
> 3가지 구현에만 집중하도록 해야 한다.  
> 또한 SQLException을 런타임 Exception으로 변환해 try/catch 절로 인해 소스 코드의 가독성을 헤치지 않도록 해야 한다.  
> 리팩토링을 하는 과정에서 `최대한 컴파일 에러를 발생시키지 않으면서` 점진적으로 리팩토링한다.

| 작업                    | JDBC 라이브러리 | 개발자 구현 대상 |
|-----------------------|------------|-----------|
| Connection 생성 및 close | O          | X         |
| Statement 생성 및 close  | O          | X         |
| ResultSet 생성 및 close  | O          | X         |
| SQL 에 인자 setting      | O          | X         |
| 트랜잭션 관리               | O          | X         |
| SQL 문                 | X          | O         |
| SQL 문에 전달할 값          | X          | O         |
| ResultSet 데이터 추출      | X          | O         |

### 기능 목록
- [x] UserDao 기능 구현 (UserDaoTest 성공 확인)
  - [x] update
  - [x] findAll
- [x] DataSourceUtils
  - [x] Connection 객체를 닫는다 (쓰레드 풀에 반환한다)
  - [x] Statement 객체를 닫는다
  - [x] ResultSet 객체를 닫는다
- [x] JdbcTemplate (라이브러리)
  - [x] SQLException은 RuntimeException으로 변환
  - [x] execute (insert, update, delete)
    - [x] 개발자는 `SQL 문`, `SQL문에 전달한 인자` 2가지 인자만 전달할 수 있는 메서드 제공
    - [x] Connection, Statement 생성 및 close
    - [x] 트랜잭션 시작 및 종료
    - [x] SQL 문에 인자를 맵핑
  - [x] select
    - [x] 개발자는 `SQL 문`, `SQL문에 전달한 인자`, `SELECT 결과 추출` 3가지 인자만 전달할 수 있는 메서드 제공
    - [x] Connection, Statement, ResultSet 생성 및 close
    - [x] SQL 문에 인자를 맵핑
    - [x] List 반환 
- [x] 리팩토링
  - [x] UserDao에서 JdbcTemplate을 사용하도록 변경  
    - [x] insert, udpate 
    - [x] select 

# 🚀 3단계 - JDBC 라이브러리 구현(힌트)

### 2단계 피드백 및 3단계 기능 목록 
- [x] (1단계) RequestBodyArgumentResolver를 AbstractModelArgumentResolver 상속 받지 않도록 변경
  - [x] 객체 맵핑에 대한 코드를 재사용하고자 상속을 했는데, 재사용을 위한 코드를 별도의 유틸로 분리하면 상속을 하지 않고 구현 가능 
- [x] (1단계) RequestParameterUtils에서 ThreadLocal 제거
  - 상속 구조가 아닌 독립적인 객체 RequestBodyArgumentResolver로 구현하면 ThreadLocal이 필요 없다?
  - Controller에서 RequestBody 애너테이션이 여러 파라미터에 적용된 경우는 어떻게?
  - [x] HttpServletRequest의 Reader 객체를 얻어 mark(), reset() 활용 
    - HttpServletRequest의 Reader를 재사용해야 하다 보니 `close`를 하지 못한다. 
    - 1회 파싱 후 close하고, 해당 데이터를 들고 다녀야 하는데 어느 시점에 어떻게 데이터를 파싱해서 들고 다녀야할까?
- [x] JdbcTemplate 에서 try-with-resources 사용 (close 에 대한 검증이 보장된다는 가정)
  - [x] 트랜잭션 처리 
    - [x] catch 절에서 rollback 불가 
    - [x] AutoCloseable 활용한 커밋 여부에 따라 롤백   
  - [ ] DatasourceUtils 의 필요성이 없어짐
- [x] PreparedStatementCreator 구현체 추가
  - [x] 생성자를 통해 SQL 문을 인자로 받아서 PreparedStatement 반환  
- [x] sql 인자를 맵핑하는 객체 도출
  - [x] 인자를 가지는 객체 
  - [x] 인자의 타입을 확인하고 해당 타입으로 형 변환
  - [x] PreparedStatement에 인자를 추가 할 때 타입에 맞는 메서드로 맵핑 
- [x] RowMapperFunction 테스트 코드 추가 
- [x] JdbcTemplateTest에 DELETE DML 테스트 코드 추가
  - 요구사항에 fit하게 맞추기 보다 라이브러리를 만든다는 마인드!! 학습 목적!! 

# 🚀 4단계 - Interceptor 구현

### 3단계 피드백
- [x] UserApiController 에 UserDao 적용
  - 만들었으면 써먹어 봐야지
- [x] RowMapperFunctionTest 에서 resultSet 의 결과를 확인하기 위한 if문 추가
  - if 문으로 resultSet 의 결과를 한번 더 검증할 수 있어서 다양한 테스트 케이스 추가 가능  
- [x] RowMapperFunctionTest 에서 Connection 과 PreparedStatement 없이 ResultSet 만 구현하여 테스트하도록 변경
  - Connection 과 PreparedStatement 에 대한 의존을 없애면 조금 더 단위 테스트 답게 작성할 수 있을 것 같다!
- [ ] RequestParameterUtilsTest#re 메서드명 수정
