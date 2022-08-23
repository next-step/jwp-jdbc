package core.mvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class ModelAndView {
    private Object view;
    private Map<String, Object> model = new HashMap<>();

    private HttpStatus status;

    public ModelAndView() {
    }

    public ModelAndView(Object view) {
        this.view = view;
        this.status = HttpStatus.OK;
    }

    public ModelAndView(Object view, Map<String, Object> model) {
        this.view = view;
        this.model = model;
    }

    public ModelAndView(Object view, HttpStatus status) {
        this.view = view;
        this.status = status;
    }

    public ModelAndView(Object view, Map<String, Object> model, HttpStatus status) {
        this.view = view;
        this.model = model;
        this.status = status;
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
        return this;
    }

    public Object getObject(String attributeName) {
        return model.get(attributeName);
    }

    public Map<String, Object> getModel() {
        return Collections.unmodifiableMap(model);
    }

    public View getView() {
        return (this.view instanceof View ? (View) this.view : null);
    }

    public String getViewName() {
        return (this.view instanceof String ? (String) this.view : null);
    }

    public HttpStatus getStatus() {
        return status;
    }

}
