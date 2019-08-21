# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

# Step2 요구사항
JDBC에 대한 공통 라이브러리를 만든다 
(SQL 쿼리, 파라미터, SELECT 경우 조회한 데이터 추출)
! try-catch 절로 소스 코드의 가독성 해치면 안됨 (Exception x)

```
[개발자 구현해야하는 부분]
SQL 쿼리
SQL 문에 전달할 값(파라미터)
ResultSet에서 데이터 추출

[JDBC 라이브러리] 
- Connection
- Statement
- ResultSet
- SQL 문에 인자 setting
- 트랜잭션 관리
```

-[x] UserDao 회원목록 리스트 구현
-[x] UserDao 개인정보수정 구현 
-[ ] UserDaoTest 이용하여 리팩토링 (SQL문, 파라미터, SELECT 일 경우 조회한 데이터 추출)

> 아래 기능을 이용하여 클린코드 구현
익명 클래스(anonymous class)
함수형 인터페이스(functional interface)
generic
가변 인자
try-with-resources
compiletime exception vs runtime exception
람다(lambda)
