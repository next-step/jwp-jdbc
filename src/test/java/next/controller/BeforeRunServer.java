package next.controller;

import org.apache.catalina.startup.Tomcat;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;

public class BeforeRunServer implements BeforeAllCallback, ExtensionContext.Store.CloseableResource {

    private static boolean started = false;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {

            String webappDirLocation = "webapp/";
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8080);

            tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());

            tomcat.start();

            context.getRoot().getStore(ExtensionContext.Namespace.GLOBAL).put("tomcat start", this);
        }
    }

    @Override
    public void close() throws Throwable {

    }
}
