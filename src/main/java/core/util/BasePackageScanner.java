package core.util;

import core.annotation.WebApplication;
import org.reflections.Reflections;

import java.util.Arrays;

public class BasePackageScanner {

    private static final Class<WebApplication> WEB_APPLICATION_ANNOTATION = WebApplication.class;

    public Object[] findBasePackage() {
        Reflections wholeReflections = new Reflections("");
        Object[] wholeBasePackage = wholeReflections.getTypesAnnotatedWith(WEB_APPLICATION_ANNOTATION)
                .stream()
                .map(this::findBasePackages)
                .flatMap(Arrays::stream)
                .toArray();

        if (wholeBasePackage.length == 0) {
            throw new IllegalStateException("Base package not initialized");
        }

        return wholeBasePackage;
    }

    private String[] findBasePackages(Class<?> clazz) {
        String[] packages = clazz.getAnnotation(WEB_APPLICATION_ANNOTATION).basePackage();

        return packages.length == 0 ? new String[]{clazz.getPackage().getName()} : packages;
    }

}
