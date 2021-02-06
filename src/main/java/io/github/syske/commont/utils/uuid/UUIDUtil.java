package io.github.syske.commont.utils.uuid;

import java.util.Random;
import java.util.UUID;

/**
 * @program: syske-common-utils
 * @description: uuid
 * @author: syske
 * @create: 2020-03-14 21:52
 */
public class UUIDUtil {
    private UUIDUtil() {}

    /**
     * 返回32位uuid
     * @return
     */
    public static String getUUIDStr() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}