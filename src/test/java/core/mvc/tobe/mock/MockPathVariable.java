package core.mvc.tobe.mock;

import core.annotation.web.PathVariable;

import java.lang.annotation.Annotation;

public class MockPathVariable implements PathVariable {
    @Override
    public String value() {
        return "value";
    }

    @Override
    public String name() {
        return "name";
    }

    @Override
    public boolean required() {
        return false;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return PathVariable.class;
    }
}
