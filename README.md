# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)


## step1
- [x] JsonViewTest의 모든 테스트를 pass하도록 JsonView를 구현한다.
    - [참고 : RequestDispatcher](https://dololak.tistory.com/502)
    - [참고 : Jackson, ObjectMapper](https://nesoy.github.io/articles/2018-04/Java-JSON)
    - [참고 : ObjectMapper 주의사항](https://github.com/naver/kaist-oss-course/issues/11#issuecomment-101101153)
    
- [x] UserAcceptanceTest 테스트를 pass하도록 Controller를 추가한다. Controller는 애노테이션 기반 MVC를 사용한다.
    - [x] api용 controller 추가 ( UserApiController )
    - [x] CRUD 기능 구현
    - [x] 공통기능 분리
