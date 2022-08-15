# JDBC 라이브러리 구현
## 진행 방법
* 프레임워크 구현에 대한 요구사항을 파악한다.
* 요구사항에 대한 구현을 완료한 후 자신의 github 아이디에 해당하는 브랜치에 Pull Request(이하 PR)를 통해 코드 리뷰 요청을 한다.
* 코드 리뷰 피드백에 대한 개선 작업을 하고 다시 PUSH한다.
* 모든 피드백을 완료하면 다음 단계를 도전하고 앞의 과정을 반복한다.

## 온라인 코드 리뷰 과정
* [텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

# 🚀 1단계 - REST API 및 테스트 리팩토링

### 요구사항
> HTML 이외에 JSON으로 데이터를 요청하고 응답을 받도록 지원해야 한다.

### 기능 목록
- [x] JsonUtils
  - [x] Java Object를 Json 문자열로 변환한다  
- [x] JsonView 구현
  - [x] 응답을 Json 포맷으로 한다
    - [x] 응답 헤더에 ContentType을 'application/json'으로 설정한다  
  - [x] 응답 데이터의 요소가 1개인 경우 
  - [x] 응답 데이터의 요소가 2개 이상인 경우 
- [ ] UserApiController 구현
  - [ ] Annotation 기반 Controller로 구현한다
- [ ] RequestBodyArgumentResolver 구현
  - [ ] 클라이언트의 요청이 RequestBody의 데이터를 변환한다 
