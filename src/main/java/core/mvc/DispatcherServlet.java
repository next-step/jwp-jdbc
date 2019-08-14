package core.mvc;

import core.mvc.asis.Controller;
import core.mvc.asis.RequestMapping;
import core.mvc.tobe.AnnotationHandlerMapping;
import core.mvc.tobe.HandlerExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    private RequestMapping rm;

    private AnnotationHandlerMapping ahm;

    @Override
    public void init() {
        rm = new RequestMapping();
        rm.initMapping();

        ahm = new AnnotationHandlerMapping("next.controller");
        ahm.initialize();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUri = req.getRequestURI();
        logger.debug("Method : {}, Request URI : {}", req.getMethod(), requestUri);

        try {
            HandlerExecution handler = ahm.getHandler(req);
            if (handler != null) {
                render(handler.handle(req, resp), req, resp);
                return;
            }

            Controller controller = rm.findController(requestUri);
            String viewName = controller.execute(req, resp);
            move(viewName, req, resp);
        } catch (Throwable e) {
            logger.error("Exception : {}", e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(ModelAndView mav, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        View view = mav.getView();
        view.render(mav.getModel(), req, resp);
    }

    private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (viewName.startsWith(JspView.DEFAULT_REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(JspView.DEFAULT_REDIRECT_PREFIX.length()));
            return;
        }

        RequestDispatcher rd = req.getRequestDispatcher(viewName);
        rd.forward(req, resp);
    }
}
