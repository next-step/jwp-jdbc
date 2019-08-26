package next.dao;

import core.jdbc.BeanRowMapper;
import core.jdbc.JdbcTemplate;
import core.jdbc.KeyHolder;
import next.model.Question;

import java.util.List;
import java.util.Optional;

public class QuestionDao {

    private static final String FIND_ALL = "SELECT questionId, writer ,title, contents, createdDate, countOfAnswer FROM questions";
    private static final String FIND_BY_ID = "SELECT questionId, writer ,title, contents, createdDate, countOfAnswer FROM questions " +
            "WHERE questionId = ?";
    private static final String INSERT = "INSERT INTO QUESTIONS (writer, title, contents, createdDate, countOfAnswer) " +
            "VALUES (?, ?, ?, ?, ?)";

    private final JdbcTemplate jdbcTemplate;

    public QuestionDao() {
        this.jdbcTemplate = new JdbcTemplate();
    }

    public List<Question> findAll() {
        return this.jdbcTemplate.queryForList(FIND_ALL, new BeanRowMapper<>(Question.class, false));
    }

    public Optional<Question> findById(long questionId) {
        return this.jdbcTemplate.queryForOptionalObject(FIND_BY_ID, new BeanRowMapper<>(Question.class, false), questionId);
    }

    public Question insert(Question question) {
        KeyHolder keyHolder = new KeyHolder();
        jdbcTemplate.update(INSERT, keyHolder,
                question.getWriter(), question.getTitle(), question.getContents(), question.getCreatedDate(), question.getCountOfComment());

        return new Question(
                keyHolder.getId(),
                question.getWriter(),
                question.getTitle(),
                question.getContents(),
                question.getCreatedDate(),
                question.getCountOfComment()
        );
    }
}
