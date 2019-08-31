package next;

import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class WebServerLauncher {
    private static final Logger logger = LoggerFactory.getLogger(WebServerLauncher.class);

    public static final String WEBAPP_DIR_LOCATION = "webapp/";

    public static void main(String[] args) throws Exception {

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        tomcat.addWebapp("/", new File(WEBAPP_DIR_LOCATION).getAbsolutePath());
        logger.info("configuring app with basedir: {}", new File("./" + WEBAPP_DIR_LOCATION).getAbsolutePath());

        tomcat.start();
        tomcat.getServer().await();
    }
}
