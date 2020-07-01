package core.mvc.asis;

import core.mvc.JspView;
import core.mvc.ModelAndView;
import core.mvc.tobe.Handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Handler {
    private String forwardUrl;

    public ForwardController(String forwardUrl) {
        this.forwardUrl = forwardUrl;
        if (forwardUrl == null) {
            throw new NullPointerException("forwardUrl is null. 이동할 URL을 입력하세요.");
        }
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView(new JspView(forwardUrl));
    }

    @Override
    public String getHandlerName() {
        return this.getClass().getSimpleName();
    }
}
