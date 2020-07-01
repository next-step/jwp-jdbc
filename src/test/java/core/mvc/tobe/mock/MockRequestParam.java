package core.mvc.tobe.mock;

import core.annotation.web.RequestParam;

import java.lang.annotation.Annotation;

public class MockRequestParam implements RequestParam {
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
        return RequestParam.class;
    }
}
