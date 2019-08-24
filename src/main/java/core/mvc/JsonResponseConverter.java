package core.mvc;

import java.util.Map;

public class JsonResponseConverter {

    private static final int SINGLE_VALUE = 1;

    public static String convert(Map<String, ?> model) {
        if (model.size() == SINGLE_VALUE) {
            return JsonUtils.convertToJsonString(model.values().iterator().next());
        }

        return JsonUtils.convertToJsonString(model);
    }

}
