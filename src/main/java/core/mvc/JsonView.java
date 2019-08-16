package core.mvc;

import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class JsonView implements View {

    private final static MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;

    private final MessageConverter messageConverter;

    public JsonView() {
        this(MessageConverters.getInstance().getMessageConverter(mediaType));
    }

    public JsonView(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(mediaType.toString());

        Optional<?> modelValue = getModelValueWithSize(model);

        if (!modelValue.isPresent()) {
            return;
        }

        messageConverter.write(modelValue.get(), response.getOutputStream());
    }

    private Optional<?> getModelValueWithSize(Map<String, ?> model) {
        Set<String> keys = model.keySet();

        if (CollectionUtils.isEmpty(keys)) {
            return Optional.empty();
        }

        if(keys.size() == 1) {
            return model.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .findFirst();
        }

        return Optional.ofNullable(model);
    }

}
