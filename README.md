# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

## 1단계 - REST API 및 테스트 리팩토링

## 요구사항

- HTML만 지원하는 현재의 @MVC 프레임워크에 `JSON` 데이터 요청 및 응답 기능 추가.
  - `JsonViewTest`를 통과하는 `JsonView`를 구현.
  - `UserAcceptanceTest`를 통과하는 `Controller`를 구현.

- Java Object를 JSON 데이터로 변환하는 View 인터페이스에 대한 구현체 `JsonView`를 추가.
  - JSON으로 응답할 때의 ContentType은 `MediaType.APPLICATION_JSON_UTF8_VALUE`로 변환.
  - Map에 담긴 model 데이터가 
    - 1개인 경우 value값을 반환.
    - 2개 이상인 경우 Map 자체를 JSON으로 변환해 반환. 
