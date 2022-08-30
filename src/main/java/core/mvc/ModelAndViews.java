package core.mvc;

public interface ModelAndViews {

    static ModelAndView createJsonView() {
        return new ModelAndView(new JsonView());
    }
}
