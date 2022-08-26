# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 🚀 1단계 - REST API 및 테스트 리팩토링
1. [X] 요구사항 1 : JsonView 구현하기
    - core.mvc.JsonViewTest의 모든 테스트를 pass하도록 JsonView를 구현한다.
2. [X] 요구사항 2 - UserController 추가하기
    - next.controller.UserAcceptanceTest 테스트를 pass하도록 Controller를 추가한다. Controller는 애노테이션 기반 MVC를 사용한다.

## 🚀 2단계 - JDBC 라이브러리 구현
1. [X] 요구사항 1 : JDBC에 대한 공통 라이브러리를 만들어 개발자가 SQL 쿼리, 쿼리에 전달할 인자, SELECT 구문의 경우 조회한 데이터를 추출하는 3가지 구현에만 집중하도록 해야 한다.
2. [X] 요구사항 2 - SQLException을 런타임 Exception으로 변환해 try/catch 절로 인해 소스 코드의 가독성을 헤치지 않도록 해야 한다.
3. [X] 요구사항 3 -  회원목록과 개인정보수정 실습을 진행한 후 UserDao에 대한 리팩토링 실습을 진행한다.

## 🚀 3단계 - JDBC 라이브러리 구현(힌트)
1. [ ] 요구사항 1 : 자바 객체와 테이블 칼럼 이름이 같을 경우 자동으로 매팅해 주는 RowMapper를 추가해보기
