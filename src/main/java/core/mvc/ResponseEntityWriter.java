package core.mvc;

import javax.servlet.http.HttpServletResponse;

public class ResponseEntityWriter {
    public static ModelAndView ok(final HttpServletResponse response, final Object entity) {
        ResponseWriter.ok(response);
        return new ModelAndView(new JsonView())
                .addObject("user", entity);
    }

    public static ModelAndView created(final HttpServletResponse response, final String location) {
        ResponseWriter.created(response, location);
        return new ModelAndView(new JsonView());
    }
}
