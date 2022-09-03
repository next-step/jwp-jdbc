# step1
## 기능 요구사항
> 지금까지 구현한 @MVC 프레임워크는 HTML 밖에 지원하지 않는다. <br>
> HTML 이외에 JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.

* core.mvc.JsonViewTest의 모든 테스트를 pass하도록 JsonView를 구현한다. 
* next.controller.UserAcceptanceTest 테스트를 pass하도록 Controller를 추가한다. Controller는 애노테이션 기반 MVC를 사용한다.

<hr>

# step2
## 기능 요구사항
> JDBC에 대한 공통 라이브러리를 만들어 개발자가 SQL 쿼리, 쿼리에 전달할 인자, SELECT 구문의 경우 조회한 데이터를 추출하는 3가지 구현에만 집중하도록 해야 한다. <br>
> SQLException을 런타임 Exception으로 변환해 try/catch 절로 인해 소스 코드의 가독성을 헤치지 않도록 해야 한다. <br>
> 리팩토링을 하는 과정에서 최대한 컴파일 에러를 발생시키지 않으면서 점진적으로 리팩토링한다.

## 추가 구현 및 테스트
> 리팩토링 과정에서 테스트는 src/test/java에 있는 next.dao.UserDaoTest 클래스를 활용하면 된다. 앞의 JDBC 실습 과정에 있는 회원목록, 개인정보수정 실습을 진행하지 않으면 UserDaoTest는 실패한다. 이 테스트 코드가 성공하도록 회원목록과 개인정보수정 실습을 진행한 후 UserDao에 대한 리팩토링 실습을 진행한다.

<hr>

