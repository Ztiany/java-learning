package juejin.netty.wechat.common.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.StandardCharsets;

public class JSONSerializer implements Serializer {

    private final Gson mGson = new GsonBuilder().create();

    @Override
    public byte getAlgorithm() {
        return Algorithms.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return mGson.toJson(object).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return mGson.fromJson(new String(bytes, StandardCharsets.UTF_8), clazz);
    }

}
