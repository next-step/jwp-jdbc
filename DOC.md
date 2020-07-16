HttpServletRequest#Inputstream 사용 (한 번 읽으면 다시 읽을 수 없다. 컨트롤러가 아닌 인터셉터나 필터에서 읽으면 이슈가 있다)
- 래핑해서 사용하면 해결할 수 있을 것 같다.

Jackson 라이브러리를 통해 JSON to Java 객체로 변환
`JsonUtils` 에 toJsonAsString 메서드 추가

파라미터로 받은 `HttpServletResponse` 객체를 통해 테스트 코드 성공 시키기
- content-type 테스트 코드에 맞게 MediaType.APPLICATION_JSON_VALUE 추가
- `HttpServletResponse`#getWriter#println 을 통해 response body 출력


JDBC Library 만들기

인터페이스와 구현체 분리
제네릭 활용해보기 (<T, V> , List<T>)

1. 인터페이스 - 구현체
2. 구현체에서 제네릭의 값을 통해 Reflection 활용
3. 1번 인터페이스를 상속한 인터페이스를 제공


인터셉터 구현해보기

유저가 사용하기 쉽게 제공을 해줘야 한다.
ArgumentResolver 처럼 인터페이스 구현체를 등록해서 사용할 수 있게 해본다.
인터페이스를 사용한다면 해당 클래스에 대해 초기화를 시켜본다. 
디스페쳐 서블릿에서 등록된 인터페이스를 실행
등록되어진 인터페이스는 Controller 클래스에 대한 메서드(@RequestMapping 포함인)들
추상클래스로 래핑해서 사용할 수 있게 해본다. 

일단 구현 해보자
