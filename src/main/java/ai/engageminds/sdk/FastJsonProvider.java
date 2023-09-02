package ai.engageminds.sdk;

import com.alibaba.fastjson.JSON;

/**
 * default implement of JsonProvider with Fastjson
 */
public class FastJsonProvider implements JsonProvider {

    @Override
    public byte[] format(Object o) {
        return JSON.toJSONBytes(o);
    }

    @Override
    public <T> T parse(byte[] bs, Class<T> clazz) {
        return JSON.parseObject(bs, clazz);
    }
}
