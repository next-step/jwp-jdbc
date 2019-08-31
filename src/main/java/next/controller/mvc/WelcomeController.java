package next.controller.mvc;

import core.annotation.web.Controller;
import core.annotation.web.RequestMapping;
import core.annotation.web.RequestMethod;
import core.mvc.ModelAndView;
import core.mvc.view.HandlebarsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WelcomeController {

    @RequestMapping(value = "/handlebars", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new HandlebarsView("handlebars/welcome"))
                .addObject("name", "Chris")
                .addObject("value", 10000)
                .addObject("taxed_value", 10000 - (10000 * 0.4))
                .addObject("in_ca", true);
    }
}
