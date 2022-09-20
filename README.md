# JDBC 라이브러리 구현
## 1단계 : REST API 및 테스트 리팩토링
### 요구사항 
- JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.
  - [x] JsonView 구현
    - ~~JsonViewTest 통과~~
  - [ ] Controller 구현

## 2단계 : JDBC 라이브러리 구현
- 클린코드로 구현하기!!!
  - 익명 클래스
  - 함수형 인터페이스
  - generic
  - 가변 인자
  - try-with-resources
  - compiletime exception vs runtime exception
  - 람다
### 요구사항
- JDBC 라이브러리 구현
  - SQL 쿼리문 전달
  - 쿼리에 전달할 인자
  - SELECT 구문의 경우 조회한 데이터
- 리팩토링 - 점진적 리팩토링
  - SQLException -> RuntimeException (try/catch으로 가독성 헤치지 않게)

















## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)
