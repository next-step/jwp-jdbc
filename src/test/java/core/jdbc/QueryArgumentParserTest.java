package core.jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DisplayName("쿼리 파서")
class QueryArgumentParserTest {

    @Test
    @DisplayName("문자열로 생성")
    void instance() {
        assertThatNoException()
                .isThrownBy(() -> new QueryArgumentParser("SELECT * from User"));
    }

    @Test
    @DisplayName("#{?} 형태의 인자 패턴을 ? 문자로 변경")
    void questionSymbolArgumentsSql() {
        //given
        QueryArgumentParser selectUser = new QueryArgumentParser("SELECT * from User where userId = #{userId}, password = #{password}");
        //when
        String sql = selectUser.questionSymbolArgumentsSql();
        //then
        assertThat(sql).isEqualTo("SELECT * from User where userId = ?, password = ?");
    }

    @Test
    @DisplayName("쿼리에 인자가 없으면 그대로 반환")
    void questionSymbolArgumentsSql_noneArgument_equalSql() {
        //given
        String selectUserSql = "SELECT * from User";
        //when
        String questionSymbolArgumentsSql = new QueryArgumentParser(selectUserSql).questionSymbolArgumentsSql();
        //then
        assertThat(questionSymbolArgumentsSql).isEqualTo(selectUserSql);
    }

    @Test
    @DisplayName("주어진 데이터로 필요한 인자 추출")
    void arguments() {
        //given
        int userId = 1;
        String password = "password";
        QueryArgumentParser selectUser = new QueryArgumentParser("SELECT * from User where userId = #{userId}, password = #{password}");
        //when
        Map<Integer, Object> arguments = selectUser.arguments(Map.of("userId", userId, "password", password));
        //then
        assertThat(arguments).containsValues(userId, password);
    }
}
