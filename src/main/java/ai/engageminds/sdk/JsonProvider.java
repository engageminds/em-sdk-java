package ai.engageminds.sdk;

public interface JsonProvider {

    byte[] format(Object o) throws Exception;

    <T> T parse(byte[] bs, Class<T> clazz) throws Exception;
}
