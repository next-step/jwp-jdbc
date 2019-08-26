package study.handlebar;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.StringTemplateSource;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import next.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HandlebarTest {

    private final String fileName = "fileName";
    private Handlebars handlebars;
    private Writer writer;

    @BeforeEach
    void setup() {
        this.handlebars = new Handlebars();
        writer = new StringWriter();
    }

    @DisplayName("태그 없을때")
    @Test
    void apply() throws Exception {
        String content = "content";
        TemplateSource templateSource = new StringTemplateSource(fileName, content);

        Template template = handlebars.compile(templateSource);
        template.apply(Collections.emptyMap(), writer);

        assertThat(writer.toString()).isEqualTo(content);
    }

    @DisplayName("값이 하나일때")
    @Test
    void apply_one_value() throws Exception {
        Map<String, String> model = ImmutableMap.of("name", "name!!!!");

        String content = "name : {{name}}";
        TemplateSource templateSource = new StringTemplateSource(fileName, content);

        Template template = handlebars.compile(templateSource);
        template.apply(model, writer);

        assertThat(writer.toString()).isEqualTo("name : name!!!!");
    }

    @DisplayName("값이 리스트일때")
    @Test
    void apply_list_value() throws Exception {
        List<String> names = ImmutableList.of("name1", "name2", "name3");
        Map<String, List<String>> model = ImmutableMap.of("names", names);

        StringBuilder content = new StringBuilder();
        content.append("<ul>");
        content.append("{{#names}}");
        content.append("<li>{{this}}</li>");
        content.append("{{/names}}");
        content.append("</ul>");

        TemplateSource templateSource = new StringTemplateSource(fileName, content.toString());

        Template template = handlebars.compile(templateSource);

        template.apply(model, writer);

        assertThat(writer.toString()).isEqualTo("<ul><li>name1</li><li>name2</li><li>name3</li></ul>");
    }

    @DisplayName("값이 객체일때")
    @Test
    void apply_object_value() throws Exception {
        User user = new User("userId!!", "pass!!", "name!!!", "email@email.com");
        Map<String, Object> model = ImmutableMap.of("user", user);

        StringBuilder content = new StringBuilder();
        content.append("{{#user}}");
        content.append("userId : {{userId}}").append(System.lineSeparator());
        content.append("password : {{password}}").append(System.lineSeparator());
        content.append("name : {{name}}").append(System.lineSeparator());
        content.append("email : {{email}}");
        content.append("{{/user}}");

        TemplateSource templateSource = new StringTemplateSource(fileName, content.toString());

        Template template = handlebars.compile(templateSource);

        template.apply(model, writer);

        String expected = "userId : userId!!\n" +
                "password : pass!!\n" +
                "name : name!!!\n" +
                "email : email@email.com";

        assertThat(writer.toString()).isEqualTo(expected);
    }

    @DisplayName("값이 객체 리스트 일때")
    @Test
    void apply_object_list() throws Exception {
        User user1 = new User("userId11", "pass11", "name11", "email11@email.com");
        User user2 = new User("userId22", "pass22", "name22", "email22@email.com");
        User user3 = new User("userId33", "pass33", "name33", "email33@email.com");
        Map<String, Object> model = ImmutableMap.of("users", ImmutableList.of(user1, user2, user3));

        StringBuilder content = new StringBuilder();
        content.append("{{#users}}");
        content.append("{{#this}}");
        content.append("userId : {{userId}}").append(System.lineSeparator());
        content.append("password : {{password}}").append(System.lineSeparator());
        content.append("name : {{name}}").append(System.lineSeparator());
        content.append("email : {{email}}");
        content.append("{{/this}}");
        content.append("{{/users}}");

        TemplateSource templateSource = new StringTemplateSource(fileName, content.toString());
        Template template = handlebars.compile(templateSource);

        template.apply(model, writer);

        String expected = "userId : userId11\n" +
                "password : pass11\n" +
                "name : name11\n" +
                "email : email11@email.comuserId : userId22\n" +
                "password : pass22\n" +
                "name : name22\n" +
                "email : email22@email.comuserId : userId33\n" +
                "password : pass33\n" +
                "name : name33\n" +
                "email : email33@email.com";

        assertThat(writer.toString()).isEqualTo(expected);
    }
}
