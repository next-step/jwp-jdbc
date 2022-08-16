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
- [x] RequestParameterUtils 
  - [x] 클라이언트의 요청 파라미터를 QeuryString 혹은 RequestBody에서 구한다
- [x] UserApiController 구현
  - [x] Annotation 기반 Controller로 구현한다
  - [x] 회원가입 `POST /api/users`
  - [x] 회원조회 `GET /api/users`
  - [x] 회원수정 `PUT /api/users`

### 1단계 피드백
- [x] JsonView 에서 한글 데이터를 처리할 수 있도록 수정(한글이 깨지지 않도록)
- [x] JsonUtils#getParameter 메서드명 수정 (`getAsStringOrNull`)
  - 리턴 타입을 메서드명에 명시함으로써 사용자에게 API 를 쉽게 사용할 수 있도록(?) 할 수 있다
  - null 이 리턴될 수 있어서 메서드명에 `OrNull` 을 추가함 
- [ ] RequestBody 를 처리할 수 있는 ArgumentResolver 추가
  - [ ] `@RequestBody` 애너테이션 추가
  - [ ] RequestBody format 이 json 인 경우
  - [ ] RequestBody format 이 query string 인 경우
