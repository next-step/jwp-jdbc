package next;

import java.io.File;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServerLauncher {

    private static final Logger logger = LoggerFactory.getLogger(WebServerLauncher.class);
    private static final String webappDirLocation = "webapp/";

    private final Tomcat tomcat;
    private Thread thread;

    public WebServerLauncher() {
        tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        logger.info("configuring app with basedir: {}",
            new File("./" + webappDirLocation).getAbsolutePath());
    }

    public void start() throws Exception {
        if (thread != null) {
            throw new IllegalStateException("already started");
        }
        thread = new Thread(() -> {
            try {
                tomcat.start();
                tomcat.getServer().await();
            } catch (LifecycleException e) {
                throw new IllegalStateException(e);
            }
        });
        thread.setDaemon(false);
        thread.start();;
    }

    public void stop() throws Exception {
        tomcat.stop();
    }

    public static void main(String[] args) throws Exception {
        WebServerLauncher webServerLauncher = new WebServerLauncher();
        webServerLauncher.start();
    }
}
