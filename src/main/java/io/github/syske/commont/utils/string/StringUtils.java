package io.github.syske.commont.utils.string;

/**
 * @program: syske-common-utils
 * @description:
 * @author: syske
 * @create: 2020-12-31 14:02
 */

import io.github.syske.commont.utils.other.CommonUtil;
import io.github.syske.commont.utils.number.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String常用操作：
 * 1、敏感数据脱敏处理
 * 2、空判断
 */
public class StringUtils {
    private final static Logger logger = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 手机号码前三后四脱敏
     *
     * @param mobilePhoneNumber
     * @return
     */
    public static String mobileEncrypt(String mobilePhoneNumber) {
        if (isEmpty(mobilePhoneNumber) || (mobilePhoneNumber.length() != 11)) {
            return mobilePhoneNumber;
        }
        return mobilePhoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 身份证前三后四脱敏
     *
      * @param id 身份证号
     * @return
     */
    public static String idCardNoEncrypt(String id) {
        if (isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.replaceAll("(?<=\\w{3})\\w(?=\\w{4})", "*");
    }

    /**
     * 护照前2后3位脱敏，护照一般为8或9位
     *
      * @param id 护照id
     * @return
     */
    public static String idPassport(String id) {
        if (isEmpty(id) || (id.length() < 8)) {
            return id;
        }
        return id.substring(0, 2) + new String(new char[id.length() - 5]).replace("\0", "*") + id.substring(id.length() - 3);
    }


    /**
     * 姓名敏感处理
     *
     * @param name
     * @return
     */
    public static String nameEncrypt(String name){
        String reg = ".{1}";
        StringBuffer sb = new StringBuffer();
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(name);
        int i = 0;
        while(m.find()){
            i++;
            if(i==1)
                continue;
            m.appendReplacement(sb, "*");
        }
        m.appendTail(sb);
        return sb.toString();
    }

     /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    /**
     * 判断字符串是否为空串""
     * @param  sValue  字符串
     * @return boolean
     */
    public static boolean isTrimEmpty(String sValue) {
        if (CommonUtil.isNull(sValue)) {
            return true;
        }
        return sValue.trim().equals("");
    }

    /**
     * 传入字符串头尾trim
     * @param  str 字符串
     * @return String
     */
    public static String trim(String str) {
        if (isEmpty(str)) {
            return "";
        }
        return str.trim();
    }

    /**
     * 从一个字符表达式中抽取出来的一段字符串
     * @param  str     字符表达式
     * @param  offset  开始偏移量（从0开始）
     * @param  length  截取长度
     * @return String  抽取出来的一段字符串
     */
    public static String subStringByByte(String str, int offset, int length) {
        String newStr = "";
        int skipLen = 0;
        int newLength = 0;

        int byteLength = 0;

        //输入校验
        if (str == null || length < 1 || offset < 0) {
            return newStr;
        }
        int strLengthByByte = str.getBytes().length;
        if (strLengthByByte < offset + 1) {
            return newStr;
        }
        if (strLengthByByte - offset < length) {
            length = strLengthByByte - offset;
        }

        //按字节取子串
        byte[] subBytes = null;
        ByteArrayInputStream bytesStream = new ByteArrayInputStream(str.getBytes());
        bytesStream.skip(offset + skipLen);

        try {
            subBytes = new byte[strLengthByByte - offset];
            //（1）判断开始的偏移量
            byteLength = bytesStream.read(subBytes, 0, strLengthByByte - offset);
            if (byteLength == -1) {
                return "";
            }
            newStr = new String(subBytes);
            byte[] bytes = newStr.getBytes();
            if (newStr == null ||
                    newStr.length() < 1 ||
                    bytes.length < byteLength) { //如果开始偏移量往后的不可转变为string,则表示截取的第一位是半个汉字
                skipLen = skipLen + 1;
            }
            //cat.debug(newStr);

            bytesStream.reset();
            bytesStream.skip(offset + skipLen);
            newLength = length - skipLen; //新的截取长度
            if (newLength < 1) {
                return "";
            }
            subBytes = new byte[newLength];
            //（2）判断结束的偏移量
            byteLength = bytesStream.read(subBytes, 0, newLength);
            newStr = new String(subBytes);
            if (newStr == null ||
                    newStr.length() < 1 ||
                    bytes.length < byteLength) { //如果重新截取的不可转变为string,则表示截取的最后一位是半个汉字
                newLength = newLength - 1;
            } else {
                return newStr;
            }
            //cat.debug(newStr);

            bytesStream.reset();
            bytesStream.skip(offset + skipLen);
            if (newLength < 1) {
                return "";
            }
            subBytes = new byte[newLength];
            //（3）
            byteLength = bytesStream.read(subBytes, 0, newLength);
            newStr = new String(subBytes);
            if (newStr == null || newStr.length() < 1 ||
                    bytes.length < byteLength) {
                return "";
            }

            return newStr;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 返回一个指定字串右边length个字节组成的字符串
     * @param  str    原字串
     * @param  strLen 截取的长度
     * @return String
     * @
     */
    public static String rightStrByByte(String str, int strLen) {
        if (isTrimEmpty(str)) {
            return "";
        }
        int offset = str.getBytes().length - strLen;
        if (offset < 0) {
            return str;
        }
        return subStringByByte(str, offset, strLen);
    }

    /**
     * 返回一个指定字串左边length个字节组成的字符串
     * @param  str    原字串
     * @param  strLen 截取的长度
     * @return String
     * @
     */
    public static String leftStrByByte(String str, int strLen)  {
        if (isEmpty(str)) {
            return "";
        }
        if (str.getBytes().length <= strLen) {
            return str;
        }
        return subStringByByte(str, 0, strLen);
    }

    /**
     * 获取字符串左边的数字串
     * @param  str    原字串
     * @return String
     * @
     */
    public static String leftNumberByByte(String str)  {
        if (isTrimEmpty(str)) {
            return "";
        }
        int len = 1;
        while (NumberUtil.isDouble(subStringByByte(str, 0, len)) && len <= str.length()) {
            len++;
        }
        return subStringByByte(str, 0, len - 1);

    }

    /**
     * 将字符串按特定分隔符拆分为一个String数组
     * @param  str          以固定符号分割的字符串
     * @param  splitChar    分隔符
     * @return 找不到时返回一个空的数组，判断数组的size
     */
    public static String[] splitStr(String str, String splitChar) {
        if (str == null || str.length() == 0) {
            return new String[0];
        }
        if (splitChar == null || splitChar.length() == 0) {
            return new String[0];
        }
        int count = 1, pos = 0;

        while ((pos = str.indexOf(splitChar, pos)) >= 0) {
            count++;
            if (pos + splitChar.length() >= str.length()) {
                break;
            } else {
                pos = pos + splitChar.length();
            }
        }
        if (count == 1) {
            return new String[0];
        }
        String arrSplit[] = new String[count];

        if (count == 1) {
            arrSplit[0] = str;
        } else {
            int i = 0;
            while (i < count) {
                if (str.indexOf(splitChar) >= 0) {
                    arrSplit[i] = str.substring(0, str.indexOf(splitChar));
                } else {
                    arrSplit[i] = str;
                    break;
                }
                str = str.substring(str.indexOf(splitChar) + splitChar.length());
                i++;
            }
        }
        return arrSplit;
    }

    /**
     * 给字串加上前缀（按字符）
     * @param  srcStr  需要加入前缀的字串
     * @param  length  返回字串总长度
     * @param  fixChar 前缀字符
     * @return String  加上前缀后的字串
     */
    public static String fixPrefixStr(String srcStr, int length, String fixChar) {
        if (isTrimEmpty(srcStr)) {
            srcStr = "";
        }
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length - srcStr.length(); i++) {
            sb.append(fixChar);
        }
        return new String(sb) + srcStr;
    }

    /**
     * 给字串加上前缀（按字节）
     * @param  srcStr  需要加入前缀的字串
     * @param  length  返回字串总长度
     * @param  fixChar 前缀字符
     * @return String  加上前缀后的字串
     */
    public static String fixPrefixStrb(String srcStr, int length, String fixChar) {
        if (isTrimEmpty(srcStr)) {
            srcStr = "";
        }
        StringBuffer sb = new StringBuffer(length);

        int srcSize = 0;
        srcSize = srcStr.getBytes().length;

        for (int i = 0; i < length - srcSize; i++) {
            sb.append(fixChar);
        }
        return new String(sb) + srcStr;
    }

    /**
     * 给字串加上后缀（按字符）
     * @param  srcStr  需要加入后缀的字串
     * @param  length  返回字串总长度
     * @param  fixChar 后缀字符
     * @return String  加上后缀后的字串
     */
    public static String fixSuffixStr(String srcStr, int length, String fixChar) {
        if (isEmpty(srcStr)) {
            srcStr = "";
        }
        StringBuffer sb = new StringBuffer(length);
        for (int i = 0; i < length - srcStr.length(); i++) {
            sb.append(fixChar);
        }
        return srcStr + new String(sb);
    }

    /**
     * 给字串加上后缀（按字节）
     * @param  srcStr  需要加入后缀的字串
     * @param  length  返回字串总长度
     * @param  fixChar 后缀字符
     * @return String  加上后缀后的字串
     */
    public static String fixSuffixStrb(String srcStr, int length, String fixChar) {
        String encoding = "GBK";
        if (isTrimEmpty(srcStr)) {
            srcStr = "";
        }
        StringBuffer sb = new StringBuffer(length);
        int srcSize = 0;
        try {
            srcSize = srcStr.getBytes(encoding).length;
        } catch (UnsupportedEncodingException ue) {}
        for (int i = 0; i < length - srcSize; i++) {
            sb.append(fixChar);
        }
        return srcStr + new String(sb);
    }

    /**
     * 替换出现的字串
     * @param  oldString  原子串
     * @param  strSearch  查询子串
     * @param  strReplace 替换子串
     * @return Sring      新的子串
     */
    public static String replaceString(String oldString, String strSearch,
                                       String strReplace) {
        int pos = oldString.indexOf(strSearch);
        while (pos >= 0) {
            oldString = oldString.substring(0, pos) + strReplace +
                    oldString.substring(pos + strSearch.length());
            pos = oldString.indexOf(strSearch);
        }
        return oldString;
    }

    /**
     * 把可能为null的字符串变量转换为0长度字串
     * @param  str    字符串变量
     * @return String ""
     */
    public static String null2Str(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str.trim();
    }

    /**
     * 把可能为null的字符串变量转换为值等于"0"的字串
     * @param  str    传入子串变量
     * @return String
     */
    public static String null2Zero(String str) {
        String zero = "0";
        if (StringUtils.isEmpty(str)) {
            return zero;
        }
        return str.trim();
    }

    /**
     * 给字串加上前缀（按字符给编号填补值）
     * @param  srcStr  需要加入前缀的字串
     * @param  length  返回字串总长度
     * @param  fixChar 前缀字符
     * @return String  加上前缀后的字串
     */
    public static String fixPrefixPublic(String srcStr, int length,
                                         String fixChar) {
        StringBuffer sb = new StringBuffer(length);
        if (isTrimEmpty(srcStr)) {
            srcStr = "";
        } else {
            int size = srcStr.length();
            if (size > length) {
                srcStr = srcStr.substring((size - length), size);
            } else {
                for (int i = 0; i < length - srcStr.length(); i++) {
                    sb.append(fixChar);
                }
            }
        }
        return new String(sb) + srcStr;
    }


    /**
     * 去掉特殊字符
     * @param str
     * @return
     */
    public static String StringFilter(String str){
        String regEx="[`~!@#$%^&*()\\-+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return  m.replaceAll("").trim();
    }

    /**
     * @Title:        replaceBlank
     * @Description:  替换空格和回车符
     * @param:        @param str
     * @param:        @return
     * @return:       String
     */
    public static String replaceBlank(String str){

        String dest = "";
        if(str != null){
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }

        return dest;
    }
}
