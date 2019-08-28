package next.controller;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.mvc.ModelAndView;
import next.model.Question;
import next.service.QuestionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController() {
        questionService = new QuestionService();
    }

    @RequestMapping("/questions")
    public ModelAndView questions(HttpServletRequest request, HttpServletResponse response) {
        List<Question> questions = questionService.findAllQuestions();

        ModelAndView modelAndView = new ModelAndView("/qna/list.html");
        modelAndView.addObject("questions", questions);
        return modelAndView;
    }

}
