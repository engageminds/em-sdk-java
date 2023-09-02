package ai.engageminds.sdk;

import com.alibaba.fastjson2.JSON;

/**
 * default implement of JsonProvider with Fastjson
 */
public class FastJson2Provider implements JsonProvider {

    @Override
    public byte[] format(Object o) {
        return JSON.toJSONBytes(o);
    }

    @Override
    public <T> T parse(byte[] bs, Class<T> clazz) {
        return JSON.parseObject(bs, clazz);
    }
}
