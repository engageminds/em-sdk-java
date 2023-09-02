package ai.engageminds.sdk;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * default implement of JsonProvider with Jackson
 */
public class JacksonProvider implements JsonProvider {

    private final ObjectMapper mapper;

    public JacksonProvider() {
        ObjectMapper m = new ObjectMapper();
        m.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        m.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        m.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        m.configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true);
        m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.mapper = m;
    }

    public JacksonProvider(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public byte[] format(Object o) throws Exception {
        return mapper.writeValueAsBytes(o);
    }

    @Override
    public <T> T parse(byte[] bs, Class<T> clazz) throws Exception {
        return mapper.readValue(bs, clazz);
    }
}
