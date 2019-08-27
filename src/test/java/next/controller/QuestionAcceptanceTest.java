package next.controller;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionAcceptanceTest extends AcceptanceTestSupport {

    @Test
    void questions() throws Exception {
        String responseBody = client()
                .get()
                .uri("/questions")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        String expectedPage = getExpectedPage();
        assertThat(responseBody).isEqualTo(expectedPage);
    }

    private String getExpectedPage() throws IOException {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"kr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Question List</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <ul>\n" +
                "        \n" +
                "            \n" +
                "        <li>\n" +
                "            writer : 자바지기, contents : Ruby on Rails(이하 RoR)는 2006년 즈음에 정말 뜨겁게 달아올랐다가 금방 가라 앉았다. Play 프레임워크는 정말 한 순간 잠시 눈에 뜨이다가 사라져 버렸다. RoR과 Play 기반으로 개발을 해보면 정말 생산성이 높으며, 웹 프로그래밍이 재미있기까지 하다. Spring MVC + JPA(Hibernate) 기반으로 진행하면 설정할 부분도 많고, 기본으로 지원하지 않는 기능도 많아 RoR과 Play에서 기본적으로 지원하는 기능을 서비스하려면 추가적인 개발이 필요하다.\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "            \n" +
                "        <li>\n" +
                "            writer : 김문수, contents : 설계를 희한하게 하는 바람에 꼬인 문제같긴 합니다만. 여쭙습니다.\n" +
                "상황은 mybatis select 실행될 시에 return object 의 getter 가 호출되면서인데요. getter 안에 다른 property 에 의존중인 코드가 삽입되어 있어서, 만약 다른 mybatis select 구문에 해당 property 가 없다면 exception 이 발생하게 됩니다.\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "            \n" +
                "        <li>\n" +
                "            writer : 자바지기, contents : 자바로 구현할 때 귀찮은 작업 중의 하나는 객체의 복잡도가 증가하는 경우 test fixture를 생성하는 것이 여간 귀찮은 작업이 아니다.\n" +
                "스칼라는 named parameter를 활용해 test fixture를 생성할 수 있다.\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "            \n" +
                "        <li>\n" +
                "            writer : 자바지기, contents : 어느 순간부터 DB id를 설계할 때 특별히 신경을 쓰지 않은 것 같네요. 최근에는 JPA 사용하면서 무의식적으로 auto increment를 사용하고 있어요. 물론 auto increment가 적합한 경우도 있겠지만 그렇지 않은 경우도 많다고 생각해요. 보통 DB 설계할 때 각 테이블의 id는 어떤 방식을 사용하나요?\n" +
                "UUID를 사용하는 것도 하나의 방식이 될 수 있을거 같은데요. UUID 사용에 따른 장단점은 JPA Implementation Patterns: Using UUIDs As Primary Keys 문서에서 다루고 있고, 단점을 극복하는 방법을 댓글에서 볼 수 있네요.\n" +
                "테이블 id를 설계할 때 어떤 방식으로 접근하나요? 저는 요즘 너무 아무 생각없이 id를 추가하고 있다는 마음이 들어 질문 남겨 봅니다.\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "            \n" +
                "        <li>\n" +
                "            writer : johnburr, contents : 이곳에서 보면 이클립스의 jre에 대해서 3개의 설정이 나옵니다.\n" +
                "jre 버전 설정\n" +
                "실행환경 설정(execution environment)\n" +
                "컴파일러 설정\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "            \n" +
                "        <li>\n" +
                "            writer : 자바지기, contents : 오늘 무엇인가 정리하다가 도대체 프로그래머가 알아야하는 기본 지식은 어디까지일까라는 의문이 들었다. 물론 컴퓨터 기본 구조, 네트워크, 자료 구조, 알고리즘, C, C++, 자바, 다양한 프레임워크 등등 모든 영역을 잘하면 좋겠지만 모든 영역을 학습하기에는 지금의 지식이 너무 방대하기 때문이다.\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "            \n" +
                "        <li>\n" +
                "            writer : javajigi, contents : 이번 slipp에서 진행하는 5번째 스터디 주제가 trello의 아키텍처를 분석하고, trello에서 사용하는 기술을 학습하는 과정이다. 이 아이디어로 스터디를 진행하게 된 계기는 http://www.mimul.com/pebble/default/2014/03/17/1395028081476.html 글을 보고 스터디 주제로 진행해 보면 좋겠다는 생각을 했다.\n" +
                "이번 스터디를 진행하면서 가장 힘든 점이 javascript라 생각한다. 지금까지 javascript를 사용해 왔지만 깊이 있게 사용한 경험은 없기 때문에 이번 기회에 틈틈히 학습해 보려고 생각하고 있다. 단, 학습 방법을 지금까지와는 다르게 오픈 소스 라이브러리 중에서 괜찮은 놈을 하나 골라 소스 코드를 읽으면서 학습하는 방식으로 진행해 보려고 한다. 아무래도 책 한권을 처음부터 읽으면서 단순 무식하게 공부하기 보다는 이 방식이 좋지 않을까라는 기대 때문이다. javascript의 개발 스타일도 이해할 수 있기 때문에 좋은 접근 방법이라 생각한다.\n" +
                "혹시 javascript를 학습하기 좋은 라이브러리가 있을까? 소스 코드 라인 수가 많지 않으면서 소스 코드 스타일에서도 배울 점이 많은 그런 라이브러리면 딱 좋겠다.\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "            \n" +
                "        <li>\n" +
                "            writer : 자바지기, contents : 오늘 자바 8에 추가된 람다와 관련한 내용을 읽다가 다음과 같이 내용이 있어 궁금증이 생겼다.\n" +
                "람다 표현식에서 변수를 변경하는 작업은 스레드에 안전하지 않다. - 가장 빨리 만나는 자바8 28페이지...\n" +
                "람다 표현식을 이전 버전의 anonymous inner class와 같은 용도로 판단했을 때 기존의 anonymous inner class에서도 final 변수에만 접근할 수 있었다.\n" +
                "지금까지 anonymous inner class에서 final 변수로 정의하는 이유가 현재 method의 Context가 anonymous inner class 인스턴스까지 확대되기 때문에 anonymous inner class 내에서 값을 변경할 경우 그에 따른 side effect가 생길 가능성이 많아 final로 정의하는 것으로 생각했다.\n" +
                "그런데 위 내용은 스레드에 안전하지 않기 때문에 람다 표현식에서 변수 값을 변경하는 것을 막는다고 이야기하고 있다. 왜 스레드에 안전하지 않은 것일까?\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "    </ul>\n" +
                "</body>\n" +
                "</html>";
    }
}
