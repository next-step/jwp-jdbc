# 2단계 - JDBC 라이브러리 구현

## JDBC 라이브러리 구현을 통한 학습 효과
* 가능한 기존 기능이 정상적으로 동작하도록 하면서 리팩토링하는 경험을 한다.
* API를 사용할 다른 개발자를 배려하면서 프로그래밍하는 연습을 한다.

![만드는 사람이 수고로우면 쓰는 사람이 편하고 만드는 사람이 편하면 쓰는 사람이 수고롭다](https://firebasestorage.googleapis.com/v0/b/nextstep-real.appspot.com/o/lesson-attachments%2F-KgrP2wkZA3uUvzWxiAs%2Fapi%20design.JPG?alt=media&token=5b778acc-4fa5-4838-877b-18c05f3991e4)

* 자바 기능을 최대한으로 활용해 가능한 깔끔한 코드(clean code)를 구현하는 연습을 한다.
    * 익명 클래스(anonymous class)
    * 함수형 인터페이스(functional interface)
    * generic
    * 가변 인자
    * try-with-resources
    * compiletime exception vs runtime exception
    * 람다(lambda)


## 요구사항
> JDBC에 대한 공통 라이브러리를 만들어 개발자가 SQL 쿼리, 쿼리에 전달할 인자, SELECT 구문의 경우 조회한 데이터를 추출하는 3가지 구현에만 집중하도록 해야 한다.
> 
> 또한 SQLException을 런타임 Exception으로 변환해 try/catch 절로 인해 소스 코드의 가독성을 헤치지 않도록 해야 한다.
> 
> 리팩토링을 하는 과정에서 최대한 컴파일 에러를 발생시키지 않으면서 점진적으로 리팩토링한다.

### 작업	
#### JDBC 라이브러리	
* Connection 생성 및 close
* Statement 생성 및 close
* ResultSet 생성 및 close
* SQL 문에 인자 setting
* 트랜잭션 관리

#### 개발자가 구현할 부분
* SQL 문
* SQL 문에 전달할 값
* ResultSet에서 데이터 추출

## 추가 구현 및 테스트
> 리팩토링 과정에서 테스트는 src/test/java에 있는 next.dao.UserDaoTest 클래스를 활용하면 된다. 
> 
> 앞의 JDBC 실습 과정에 있는 회원목록, 개인정보수정 실습을 진행하지 않으면 UserDaoTest는 실패한다. 
> 
> 이 테스트 코드가 성공하도록 회원목록과 개인정보수정 실습을 진행한 후 UserDao에 대한 리팩토링 실습을 진행한다.