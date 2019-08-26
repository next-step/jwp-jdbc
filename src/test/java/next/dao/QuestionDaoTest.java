package next.dao;

import next.model.Question;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuestionDaoTest extends DaoTestSupport {

    private QuestionDao questionDao;

    @Override
    protected void initialize() {
        this.questionDao = new QuestionDao();
    }

    @Test
    void findAll() throws Exception {
        List<Question> questions = questionDao.findAll();

        assertThat(questions).hasSize(8);
    }

    @Test
    void findById() throws Exception {
        long questionId = 7;
        Question question = questionDao.findById(questionId).orElseThrow(IllegalArgumentException::new);

        assertThat(question.getQuestionId()).isEqualTo(questionId);
        assertThat(question.getTitle()).isEqualTo("javascript 학습하기 좋은 라이브러리를 추천한다면...");
        assertThat(question.getWriter()).isEqualTo("javajigi");
        assertThat(question.getCountOfComment()).isEqualTo(2);
    }

    @Test
    void insert() throws Exception {
        String writer = "writer!!";
        String title = "title!!!";
        String contents = "contents!!!";
        Question question = new Question(writer, title, contents);

        Question result = questionDao.insert(question);

        assertThat(result.getQuestionId()).isEqualTo(9);
        assertThat(result.getWriter()).isEqualTo(writer);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getContents()).isEqualTo(contents);
    }
}