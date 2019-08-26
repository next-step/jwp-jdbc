package core.mvc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import next.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HandlebarsViewTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
    }

    @Test
    void render() throws Exception {
        HandlebarsView handlebarsView = new HandlebarsView("qna/list");

        List<Question> questions = ImmutableList.of(new Question("writer!!", "title!!", "contents!!"));
        ImmutableMap<String, List<Question>> model = ImmutableMap.of("questions", questions);

        handlebarsView.render(model, request, response);

        String expectedResult = getExpectedResult();
        assertThat(response.getContentAsString()).isEqualTo(expectedResult);
    }

    private String getExpectedResult() {
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
                "            writer : writer!!, contents : contents!!\n" +
                "        </li>\n" +
                "            \n" +
                "        \n" +
                "    </ul>\n" +
                "</body>\n" +
                "</html>";
    }
}