package study.handlebar;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Lambda;
import com.github.jknack.handlebars.Template;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HandleBarTest {
    @Test
    @DisplayName("{{}} 안에 변수가 들어가는지 확인한다.")
    void mustache() throws IOException {
        Price price = new Price("Chris", 10000, 6000, true);
        String templateString =
                "Hello {{name}}\n" +
                "You have just won {{value}} dollars!\n" +
                "{{#in_ca}}" +
                "Well, {{taxed_value}} dollars, after taxes." +
                "{{/in_ca}}";
        String expected =
                "Hello Chris\n" +
                "You have just won 10000 dollars!\n" +
                "Well, 6000.0 dollars, after taxes.";

        String actual = getActual(price, templateString);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("map과 list안의 값을 꺼내오는지 확인한다.")
    void map() throws IOException {
        Map<String, List<Repo>> map = getModel();
        String templateString =
                "{{#repo}}" +
                "<b>{{name}}</b>" +
                "{{/repo}}";
        String expected =
                "<b>resque</b>" +
                "<b>hub</b>" +
                "<b>rip</b>";

        String actual = getActual(map, templateString);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("function 결과가 출력되는지 확인한다.")
    void lambdas() throws IOException {
        Map<String, Object> map = getModelTestingLambda();
        String templateString =
                "{{#wrapped}}" +
                "{{name}} is awesome." +
                "{{/wrapped}}";
        String expected = "<b>Willy is awesome.</b>";

        String actual = getActual(map, templateString);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("repo가 비어있을때 No repos :( 가 출력되는지 확인한다.")
    void invertedSections() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("repo", Lists.newArrayList());
        String templateString =
                "{{#repo}}" +
                "  <b>{{name}}</b>" +
                "{{/repo}}" +
                "{{^repo}}" +
                "No repos :(" +
                "{{/repo}}";
        String expected = "No repos :(";

        String actual = getActual(map, templateString);
        assertThat(actual).isEqualTo(expected);
    }

    private Map<String, Object> getModelTestingLambda() {
        Map<String, Object> map = new HashMap<>();
        Lambda lambda = (text, template) -> "<b>" + template.apply(text) + "</b>";
        map.put("name", "Willy");
        map.put("wrapped", lambda);
        return map;
    }

    private Map<String, List<Repo>> getModel() {
        Map<String, List<Repo>> map = new HashMap<>();
        map.put("repo", Arrays.asList(new Repo("resque"), new Repo("hub"), new Repo("rip")));
        return map;
    }

    private Template getTemplate(String template) throws IOException {
        Handlebars handlebars = new Handlebars();
        return handlebars.compileInline(template);
    }

    private String getActual(Object model, String templateString) throws IOException {
        Template template = getTemplate(templateString);
        return template.apply(model);
    }
}
