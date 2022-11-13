# JDBC 라이브러리 구현

## 진행 방법

* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정

* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

### 1단계 - REST API 및 테스트 리팩토링

- [x] 지금까지 구현한 @MVC 프레임워크는 HTML 밖에 지원하지 않는다. HTML 이외에 JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.

### 2단계 - JDBC 라이브러리 구현

- [x] JDBC에 대한 공통 라이브러리를 만들어 개발자가 SQL 쿼리, 쿼리에 전달할 인자, SELECT 구문의 경우 조회한 데이터를 추출하는 3가지 구현에만 집중하도록 해야 한다.
    - | 작업 | JDBC 라이브러리 | 개발자가 구현할 부분 |
                  |-----|--------------|-----------------|
      | Connection 생성 및 close | O | X |
      | SQL 문 | X | O |
      | Statement 생성 및 close | O | X |
      | ResultSet 생성 및 close | O | X |
      | SQL 문에 전달할 값 | X | O |
      | ResultSet에서 데이터 추출 | X | O |
      | SQL 문에 인자 setting | O | X |
      | 트랜잭션 관리 | O | X |

- [x] 또한 SQLException을 런타임 Exception으로 변환해 try/catch 절로 인해 소스 코드의 가독성을 헤치지 않도록 해야 한다. 리팩토링을 하는 과정에서 최대한 컴파일 에러를 발생시키지 않으면서 점진적으로 리팩토링한다.

### 4단계 - Interceptor 구현

- [ ] Interceptor를 구현한 후 각 Controller 메소드의 실행 속도를 측정한 후 debug level로 log를 출력하는 요구사항을 구현한다.
