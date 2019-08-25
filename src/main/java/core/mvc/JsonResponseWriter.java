package core.mvc;

import java.io.Writer;
import java.util.Map;

public class JsonResponseWriter {

    private static final int SINGLE_VALUE = 1;

    public static void write(Writer writer, Map<String, ?> model) {
        if (model.size() == SINGLE_VALUE) {
            JsonUtils.writeValue(writer, model.values().iterator().next());
            return;
        }

        JsonUtils.writeValue(writer, model);
    }

}
