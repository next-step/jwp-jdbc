# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)
 
### STEP 1. REST API 및 테스트 리팩토링
- [X] Jackson에 대한 학습 테스트 작성
- [X] Java Object를 JSON 데이터로 변환하는 JsonView 구현
- [X] RequestBodyArgumentResolver 구현
### STEP 2-3. JDBC 라이브러리 구현
- [X] JDBC 템플릿 구현
  - [X] insert, update -> update
  - [X] findAll, findById -> query, queryForObject
