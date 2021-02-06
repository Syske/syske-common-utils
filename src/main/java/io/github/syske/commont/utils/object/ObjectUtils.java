package io.github.syske.commont.utils.object;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * syske-common-utils
 *
 * 对象工具类
 */
public class ObjectUtils {
    private final static Logger logger = LoggerFactory.getLogger(ObjectUtils.class);
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        Map<String, Object> map = new HashMap<String,Object>();
        Class<?> clazz = obj.getClass();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = null;

                value = field.get(obj);

                map.put(fieldName, value);
            }
        } catch (IllegalAccessException e) {
            logger.error("对象转换map失败", e);
            throw new Exception("对象转换map失败", e);
        }
        return map;
    }

    /**
     * 获取参数不为空
     * @param value
     * @param defaultValue
     * @param <T>
     * @return
     */
    public static <T> T nvl(T value,T defaultValue){
        return value !=null ? value : defaultValue;
    }

    public static Object[] toObjectArray(Object source) {
        if (source instanceof Object[]) {
            return (Object[])((Object[])source);
        } else if (source == null) {
            return EMPTY_OBJECT_ARRAY;
        } else if (!source.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + source);
        } else {
            int length = Array.getLength(source);
            if (length == 0) {
                return EMPTY_OBJECT_ARRAY;
            } else {
                Class<?> wrapperType = Array.get(source, 0).getClass();
                Object[] newArray = (Object[])((Object[])Array.newInstance(wrapperType, length));

                for(int i = 0; i < length; ++i) {
                    newArray[i] = Array.get(source, i);
                }

                return newArray;
            }
        }
    }

}
