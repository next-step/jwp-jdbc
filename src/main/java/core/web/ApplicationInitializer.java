package core.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public interface ApplicationInitializer {
    void onStartup(ServletContext servletContext) throws ServletException;
}
