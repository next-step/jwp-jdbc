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