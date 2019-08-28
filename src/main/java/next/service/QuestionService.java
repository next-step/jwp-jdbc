package next.service;

import next.dao.QuestionDao;
import next.model.Question;

import java.util.List;

public class QuestionService {

    private final QuestionDao questionDao;

    public QuestionService() {
        questionDao = new QuestionDao();
    }

    public List<Question> findAllQuestions() {
        return questionDao.findAll();
    }

}
