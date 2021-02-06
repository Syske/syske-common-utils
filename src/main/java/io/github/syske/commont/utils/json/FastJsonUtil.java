package io.github.syske.commont.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: syske-common-utils
 * @description:
 * @author: syske
 * @create: 2020-03-06 22:44
 */

public class FastJsonUtil {
    public FastJsonUtil() {
    }

    public static String beanToJson(Object object, String dataFormatString) {
        if (object != null) {
            return isEmpty(dataFormatString) ? JSONObject.toJSONString(object) : JSON.toJSONStringWithDateFormat(object, dataFormatString, new SerializerFeature[0]);
        } else {
            return null;
        }
    }

    public static String beanToJson(Object object) {
        return object != null ? JSON.toJSONString(object, new SerializerFeature[]{SerializerFeature.DisableCircularReferenceDetect}) : null;
    }

    public static String stringToJsonByFastjson(String key, String value) {
        if (!isEmpty(key) && !isEmpty(value)) {
            Map<String, String> map = new HashMap();
            map.put(key, value);
            return beanToJson(map, (String)null);
        } else {
            return null;
        }
    }

    public static Object jsonToBean(String json, Object clazz) {
        return !isEmpty(json) && clazz != null ? JSON.parseObject(json, clazz.getClass()) : null;
    }

    public static Map<String, Object> jsonToMap(String json) {
        return isEmpty(json) ? null : (Map)JSON.parseObject(json, Map.class);
    }

    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}

