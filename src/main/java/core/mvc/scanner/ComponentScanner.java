package core.mvc.scanner;

import core.annotation.Component;
import core.util.ReflectionUtils;
import java.util.List;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

public class ComponentScanner {

    private String[] basePackages;

    public ComponentScanner(String... basePackages) {
        if(basePackages.length == 0){
            throw new IllegalArgumentException("must not empty basePackage");
        }
        this.basePackages = basePackages;
    }

    public<T> List<T> scan(Class<T> type){
        Reflections reflections = new Reflections(this.basePackages, new TypeAnnotationsScanner(), new SubTypesScanner(), new MethodAnnotationsScanner());
        return reflections.getTypesAnnotatedWith(Component.class).stream()
            .filter(clazz -> type.isAssignableFrom(clazz))
            .map(clazz -> (T) ReflectionUtils.newInstance(clazz))
            .collect(Collectors.toList());
    }
}
