package ai.engageminds.sdk;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public interface HttpSender {

    String UA = "em-sdk-java/" + EmSdk.VERSION;

    /**
     * send http reqeust, method POST
     */
    void send(Request req, BiConsumer<Response, Exception> cb);

    @Data
    class Request {
        private String url;
        private String compress;
        private byte[] body;
    }

    @Data
    class Response {
        private int statusCode;
        private String reasonPhrase;
        private Headers headers;
        private String body;
    }

    class Headers {
        private final Map<String, String> hs = new HashMap<>();

        public String get(String name) {
            return hs.get(name.toLowerCase());
        }

        public void set(String name, String value) {
            hs.put(name.toLowerCase(), value);
        }
    }
}
