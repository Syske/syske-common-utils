package io.github.syske.commont.utils.encrypt;

import com.alibaba.fastjson.JSONObject;
import io.github.syske.commont.utils.string.StringUtils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

/**
 * AES加密解密工具类
 */
public class AESUtil {
    // /** 算法/模式/填充 **/
    private static final String CipherMode = "AES/CBC/PKCS5Padding";

    /**
     * 秘钥的大小
     */
    private static final int KEYSIZE = 128;

    /**
     * 解密字节数组
     *
     * @param content
     * @param password
     * @param iv
     *
     * @return
     */
    public static byte[] decrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.DECRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     *
     * @param content
     * @param password
     * @param iv
     * @return
     */
    public static JSONObject decrypt(String content, String password, String iv) {
        JSONObject result = new JSONObject();
        result.put("isPass", false);
        byte[] data = null;

        try {
            data = new Base64().decode(content);// 先用base64解密
            data = decrypt(data, password, iv);
            if (null == data) {
                result.put("isPass", false);
                return result;
            }
            String results = null;
            results = new String(data, "UTF-8");
            result.put("isPass", true);
            result.put("datas", results);
        } catch (Exception e) {
            result.put("isPass", false);
        }
        return result;
    }

    /**
     * @param content  业务数据
     * @param password 密钥
     *
     * @return 返回密文
     */
    public static String encryptStr(String content, String password) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encrypt(data, password, "UTF-8");
        String result = new Base64().encodeToString(data);
        return result;
    }

    /**
     * 加密
     *
     * @param content
     * @param password
     * @param iv
     * @return
     */
    public static byte[] encrypt(byte[] content, String password, String iv) {
        try {
            SecretKeySpec key = createKey(password);
            Cipher cipher = Cipher.getInstance(CipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, key, createIV(iv));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据密钥生成SecretKeySpec
     *
     * @param key
     * @return
     */
    private static SecretKeySpec createKey(String key) {
        byte[] data = null;
        if (key == null) {
            key = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(key);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
    }

    private static IvParameterSpec createIV(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(16);
        sb.append(password);
        while (sb.length() < 16) {
            sb.append("0");
        }
        if (sb.length() > 16) {
            sb.setLength(16);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }


    /**
     * AES加密
     *
     * @param data 待加密内容
     * @param key  加密秘钥
     *
     * @return
     */
    public static byte[] encrypt(String data, String key) {
        if (!StringUtils.isEmpty(data)) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                // 选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(key.getBytes());
                keyGenerator.init(KEYSIZE, random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                byte[] byteContent = data.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);// 初始化
                byte[] result = cipher.doFinal(byteContent);
                return result; // 加密
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * AES加密，返回String
     *
     * @param data 待加密内容
     * @param key  加密秘钥
     *
     * @return
     */
    public static String encryptToStr(String data, String key) {

        return !StringUtils.isEmpty(data) ? parseByte2HexStr(encrypt(data, key)) : null;
    }

    /**
     * AES解密
     *
     * @param data 待解密字节数组
     * @param key  秘钥
     *
     * @return
     */
    public static byte[] decrypt(byte[] data, String key) {
        if (data != null && data.length > 0) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                // 选择一种固定算法，为了避免不同java实现的不同算法，生成不同的密钥，而导致解密失败
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                random.setSeed(key.getBytes());
                keyGenerator.init(KEYSIZE, random);
                SecretKey secretKey = keyGenerator.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);// 初始化
                byte[] result = cipher.doFinal(data);
                return result; // 加密
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * AES解密，返回String
     *
     * @param enCryptdata 待解密字节数组
     * @param key         秘钥
     *
     * @return
     */
    public static String decryptToStr(String enCryptdata, String key) {
        return !StringUtils.isEmpty(enCryptdata) ? new String(decrypt(parseHexStr2Byte(enCryptdata), key)) : null;
    }

    /**
     * 将二进制转换成16进制
     *
     * @param buf 二进制数组
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr 16进制字符串
     *
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
