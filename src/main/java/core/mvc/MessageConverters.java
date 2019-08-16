package core.mvc;

import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MessageConverters {

    Set<MessageConverter> messageConverters;
    Map<MediaType, MessageConverter> cache;

    private MessageConverters() {
        this.messageConverters = new HashSet<>();
        this.cache = new HashMap<>();
    }

    public void add(MessageConverter messageConverter) {
        this.messageConverters.add(messageConverter);
    }

    public MessageConverter getMessageConverter(MediaType mediaType) {
        return cache.computeIfAbsent(mediaType, this::getMessageConverterByMediaType);
    }

    private MessageConverter getMessageConverterByMediaType(MediaType mediaType) {
        return messageConverters.stream()
                .filter(messageConverter -> messageConverter.support(mediaType))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("지원하는 컨버터를 찾을수 없습니다."));
    }

    private static class Lazy {
        private static final MessageConverters instance = new MessageConverters();
    }

    public static MessageConverters getInstance() {
        return Lazy.instance;
    }
}
