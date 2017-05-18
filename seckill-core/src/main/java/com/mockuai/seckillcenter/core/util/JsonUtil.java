package com.mockuai.seckillcenter.core.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Created by zengzhangqiang on 4/24/15.
 */
public class JsonUtil {
    public static Gson gson;

    static {
        GsonBuilder gb = new GsonBuilder();
        gb.disableHtmlEscaping();
        gb.setFieldNamingPolicy(
                FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gson = gb.create();
    }

    public static <T> T parseJson(String jsonStr, Class<T> tClass) {
        return gson.fromJson(jsonStr, tClass);
    }

    public static <T> T parseJson(String jsonStr, Type type) {
        return gson.fromJson(jsonStr, type);
    }

    /**
     * 使用Gson生成json字符串
     *
     * @param src
     * @return
     */
    public static String toJson(Object src) {
        return gson.toJson(src);
    }

}