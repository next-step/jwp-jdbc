package core.jdbc;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import core.jdbc.support.template.JdbcTemplate;
import core.jdbc.support.template.RowMapper;

public class JdbcTemplateTest {

	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setup() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("jwp.sql"));
		DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
		jdbcTemplate = JdbcTemplate.getInstance(ConnectionManager.getDataSource());
	}

	@Test
	@DisplayName("JdbcTemplate 생성 테스트")
	public void createInstance() {
		// given & when & then
		assertThat(jdbcTemplate).isNotNull();
		assertThat(jdbcTemplate.getConnection()).isNotNull();
	}

	public Map common_select_one(String writer) {
		String sql = "SELECT WRITER, TITLE, CONTENTS, CREATEDDATE FROM QUESTIONS WHERE WRITER = ?";
		RowMapper<Map> rm = rs -> {
			Map m = new HashMap();
			m.put("writer", rs.getString(1));
			m.put("title", rs.getString(2));
			m.put("contents", rs.getString(3));
			m.put("createddate", rs.getString(4));
			return m;
		};
		return jdbcTemplate.selectOne(sql, rm, writer);
	}

	public List<Map> common_select_all() {
		String sql = "SELECT WRITER, TITLE, CONTENTS, CREATEDDATE FROM QUESTIONS";
		RowMapper<Map> rm = rs -> {
			Map m = new HashMap();
			m.put("writer", rs.getString(1));
			m.put("title", rs.getString(2));
			m.put("contents", rs.getString(3));
			m.put("createddate", rs.getString(4));
			return m;
		};
		return jdbcTemplate.selectAll(sql, rm);
	}

	@Test
	@DisplayName("단건 조회 테스트")
	public void selectOne() {
		// given
		String expect = "자바지기";
		// when
		Map actual = common_select_one(expect);
		// then
		assertThat(actual.get("writer")).isEqualTo(expect);
	}

	@Test
	@DisplayName("전체 조회 테스트")
	public void selectAll() {
		// given & when & then
		assertThat(common_select_all().size()).isEqualTo(8);
	}

	@Test
	@DisplayName("가변인자 insert 테스트")
	public void insert() {
		// given
		String sql = "INSERT INTO QUESTIONS (WRITER, TITLE, CONTENTS, CREATEDDATE) VALUES (?, ?, ?, ?)";
		// when
		jdbcTemplate.insert(sql, "writer", "title", "content", new Timestamp(System.currentTimeMillis()).toString());
		// then
		assertThat(common_select_all().size()).isEqualTo(9);
	}

	@Test
	@DisplayName("PreparedStatementSetter insert 테스트")
	public void insertWithPreparedStatementSetter() {
		// given
		String sql = "INSERT INTO QUESTIONS (WRITER, TITLE, CONTENTS, CREATEDDATE) VALUES (?, ?, ?, ?)";
		// when
		jdbcTemplate.insert(sql, pstmt -> {
			pstmt.setString(1, "writer");
			pstmt.setString(2, "title");
			pstmt.setString(3, "content");
			pstmt.setString(4, new Timestamp(System.currentTimeMillis()).toString());
		});
		// then
		assertThat(common_select_all().size()).isEqualTo(9);
	}

	@Test
	@DisplayName("가변인자 update 테스트")
	public void updateWithVariableFactors() {
		// given
		String writer = "자바지기";
		String expectTitle = "title2";
		String expectContent = "content2";
		String sql = "UPDATE QUESTIONS SET TITLE = ?, CONTENTS = ? WHERE WRITER = ?";
		// when
		jdbcTemplate.update(sql, expectTitle, expectContent, writer);
		// then
		Map actual = common_select_one(writer);
		assertThat(actual.get("title")).isEqualTo(expectTitle);
		assertThat(actual.get("contents")).isEqualTo(expectContent);
	}

	@Test
	@DisplayName("PreparedStatementSetter update 테스트")
	public void updateWithPreparedStatementSetter() {
		// given
		String writer = "자바지기";
		String expectTitle = "title2";
		String expectContent = "content2";
		String sql = "UPDATE QUESTIONS SET TITLE = ?, CONTENTS = ? WHERE WRITER = ?";
		// when
		jdbcTemplate.update(sql, pstmt -> {
			pstmt.setString(1, expectTitle);
			pstmt.setString(2, expectContent);
			pstmt.setString(3, writer);
		});
		// then
		Map actual = common_select_one(writer);
		assertThat(actual.get("title")).isEqualTo(expectTitle);
		assertThat(actual.get("contents")).isEqualTo(expectContent);
	}
}
