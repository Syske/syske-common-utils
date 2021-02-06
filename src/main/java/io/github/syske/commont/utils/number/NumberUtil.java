package io.github.syske.commont.utils.number;

import io.github.syske.commont.utils.string.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

    /**
     * 四舍五入
     * @param  a 要操作的数据
     * @param  fractionPos 舍入的小数位数
     * @return double
     */
    public static double round(double a, int fractionPos) {
        //取多一位，增加精度
        double dblBase = Math.pow(10, fractionPos + 1);
        //截掉小数点后fractionPos+1位的数据
        long intBase = (long) (mul(dblBase, a));
        //除以10，四舍五入
        long dblTemp = Math.round(intBase / 10.0);

        return dblTemp / (dblBase / 10);
    }

    /**
     * 双精度乘法计算（主要解决浮点漂移问题）
     * @param v1 double
     * @param v2 double
     * @return double
     */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指（主要解决浮点漂移问题）
     * 定精度，以后的数字四舍五入。
     * @param v1 被除数
     * @param v2 除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static double div(double v1, double v2, int scale) {

        if (scale < 0) {

            throw new IllegalArgumentException(

                    "The scale must be a positive integer or zero");

        }

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 提供精确的加法运算（主要解决浮点漂移问题）
     * @param v1 double  被加数
     * @param v2 double  加数
     * @return double   两个参数的和
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * @param v1 double 被减数
     * @param v2 double 减数
     * @return double  两个参数的差
     */

    public static double sub(double v1, double v2) {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();

    }


    /**
     * 四舍五入到2位小数
     * @param a 要操作的数据
     * @return double
     */
    public static double round2frac(double a) {
        return round(a, 2);
    }

    /**
     * 格式化数字（两位小数）
     * @param  number 原double
     * @return double 格式化后的double
     */
    public static double format1frac(double number) {
        return Double.parseDouble(format(number, "0.00"));
    }

    /**
     * 格式化数字（两位小数）
     * @param  number 原double
     * @return String 转为子串
     */
    public static String format2frac(double number) {
        return format(number, "0.00");
    }

    /**
     * 格式化数字
     * @param  number       原数字
     * @param  patternSize  小数位的长度
     * @return String
     */
    public static String format(String number, int patternSize) {
        double d = toDouble(number);
        String pattern = patternSize < 1 ? "0" :
                StringUtils.fixSuffixStr("0.", patternSize + 2, "0");
        return format(d, pattern);
    }

    public static String format(double number, int patternSize) {
        String pattern = patternSize < 1 ? "0" :
                StringUtils.fixSuffixStr("0.", patternSize + 2, "0");
        return format(number, pattern);
    }


    /**
     * 格式化数字（不推荐使用）
     * @param  number  原double
     * @param  pattern 输出数字字串的格式（eg:"0.00"）
     * @return String  转为子串
     */
    public static String format(double number, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }

    /**
     * 判断字符串是否为正整数
     * @param s String字符串
     * @return boolean
     */
    public static boolean isNumber(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        String re = "^[0-9]*$";
        Pattern pettern = Pattern.compile(re);
        Matcher matcher = pettern.matcher(s);
        boolean rs = matcher.find();
        return rs;
    }

    /**
     * 判断是否为长整数
     * @param  sValue 数字串
     * @return boolean
     */
    public static boolean isLong(String sValue) {
        if (StringUtils.isEmpty(sValue)) {
            return false;
        }
        try {
            Long.parseLong(sValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否为Double
     * @param  sValue 数字串
     * @return boolean
     */
    public static boolean isDouble(String sValue) {
        if (StringUtils.isEmpty(sValue)) {
            return false;
        }
        try {
            Double.parseDouble(sValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否为带规定位数小数的字符串，可以用于输入小数格式判断
     * @param  sValue   输入的字符串
     * @param  precise  小数位长度
     * @return boolean
     */
    public static boolean isDecimalByDigits(String sValue, int precise) {
        if (isLong(sValue)) {
            return true;
        }
        if (!isDouble(sValue)) {
            return false;
        }

        int indexZero = sValue.indexOf(".");
        if (indexZero == 0 && precise == 0) {
            return true;
        }

        String decimalStr = StringUtils.subStringByByte(sValue, indexZero + 1,
                sValue.length() - indexZero);
        return decimalStr.length() > precise ? false : true;
    }

    /**
     * 检查是否是有效的设置位数的比例数eg:0.25
     *                       ZHM 最好使用isDecimalByDigits这个方法！
     *                       2003-10-09
     * @param  sValue 小数
     * @param  digits 设置的小数位数（0到1之间）
     * @return boolean 是否是有效的设置位数的小数
     */
    public static boolean isProportion(String sValue, int digits) {
        if (StringUtils.isEmpty(sValue)) {
            return false;
        }
        if (!isDouble(sValue)) {
            return false;
        }
        if (sValue.length() != 2 + digits) {
            return false;
        }

        double dValue = toDouble(sValue);
        if (dValue == 0) {
            return true;
        }
        if ((dValue >= 0.0) && (dValue <= 1.00)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否 为0~~1的小数
     * @param  sValue   输入的字符串
     * @return boolean
     */
    public static boolean isBetweenZeroToOne(String sValue) {
        if (!isDouble(sValue)) {
            return false;
        }
        double dl = toDouble(sValue);
        if (0 <= dl && dl <= 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert a String to Int.
     * @param  sValue 数字串
     * @return int
     */
    public static int toInt(String sValue) {
        try {
            sValue = format(sValue, 0);
            return Integer.parseInt(sValue);
        } catch (Exception e) {
            //cat.debug("Convert  [" + sValue + "] to Int Failed!");
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Convert a String to Long.
     * @param  sValue 数字串
     * @return int
     */
    public static long toLong(String sValue) {
        try {
            sValue = format(sValue, 0);
            return Long.parseLong(sValue);
        } catch (Exception e) {
            //cat.debug("Convert  [" + sValue + "] to Long Failed!");
            //e.printStackTrace();
            return 0;
        }
    }

    /**
     * Convert a String to Double.
     * @param  sValue 数字串
     * @return int
     */
    public static double toDouble(String sValue) {
        try {
            return Double.parseDouble(sValue);
        } catch (Exception e) {
            // cat.debug("Convert  [" + sValue + "] to double Failed!") ;
            //e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * 转换double to String
     * @param  doubleValue vdouble
     * @return String
     */
    public static String double2Str(double doubleValue) {
        return new Double(doubleValue).toString();
    }

    /**
     * Convert a Integer to String .
     * @param  iValue int
     * @return String
     */
    public static String int2Str(int iValue) {
        return new Integer(iValue).toString();
    }




    /**
     * 长整形数字的加法
     * @param str1 String 第一个数字，不能为空必须是数字
     * @param str2 String 第二个数字，不能为空必须是数字
     * @return String 返回相加结果
     */
    public static String addBigInteger(String str1, String str2) {
        int i = 0;
        char[] num1 = new char[str1.length()];
        char[] num2 = new char[str2.length()];
        for (; i < str1.length(); i++) {
            num1[i] = str1.charAt(i);
        }
        for (i = 0; i < str2.length(); i++) {
            num2[i] = str2.charAt(i);
        }
        return addMethod(num1, num2);
    }



    public static String addMethod(char[] add1, char[] add2) { //BigInteger相加方法
        int len1 = add1.length;
        int len2 = add2.length;
        //System.out.println("The length of add1 is : " + len1);
        //System.out.println("The length of add2 is : " + len2);
        int len = Math.max(len1, len2);
        int i;
        char[] temp1 = new char[len];
        char[] temp2 = new char[len];

        char[] result = new char[len + 1];

        for (i = 0; i < len1; i++) {
            temp1[len - 1 - i] = add1[len1 - 1 - i];
        }
        for (i = 0; i < len2; i++) {
            temp2[len - 1 - i] = add2[len2 - 1 - i];
        }

        int m = 0;
        for (i = 0; i < len; i++) { //相加
            if (temp1[len - 1 - i] != 0) {
                temp1[len - 1 - i] -= 48;
            }
            if (temp2[len - 1 - i] != 0) {
                temp2[len - 1 - i] -= 48;
            }
            m = temp1[len - 1 - i] + temp2[len - 1 - i];
            if (m >= 10) {
                m -= 10;
                result[len - i] += m;
                result[len - 1 - i] += 1;
            } else {
                result[len - i] += m;
            }
        }
        //System.out.print("add1 + add2 = "); //输出相加结果
        i = 0;
        if (result[0] == 0) {
            i = 1;
        }
        StringBuffer sb = new StringBuffer();
        for (; i < len + 1; i++) {
            //System.out.print(Integer.toString(result[i]));
            sb.append(Integer.toString(result[i]));
        }
        return sb.toString();
    }


    /**
     * 长整形数字的减法
     * @param str1 String 第一个数字，不能为空必须是数字
     * @param str2 String 第二个数字，不能为空必须是数字
     * @return String 返回相减结果（str1>str2为正数，str1<str2为负数）
     */
    public static String subBigInteger(String str1, String str2) {
        int i = 0;
        char[] num1 = new char[str1.length()];
        char[] num2 = new char[str2.length()];
        for (; i < str1.length(); i++) {
            num1[i] = str1.charAt(i);
        }
        for (i = 0; i < str2.length(); i++) {
            num2[i] = str2.charAt(i);
        }
        return subMethod(num1, num2);
    }


    /**
     * BigInteger相减方法
     *
     * @param sub1
     * @param sub2
     * @return
     */
    public static String subMethod(char[] sub1, char[] sub2) {
        int len1 = sub1.length;
        int len2 = sub2.length;
        //System.out.println("The length of add1 is : " + len1);
        //System.out.println("The length of add2 is : " + len2);
        int len = Math.max(len1, len2);
        int i;
        char[] temp1 = new char[len];
        char[] temp2 = new char[len];
        char[] result = new char[len + 1];

        if (len1 > len2) {
            for (i = 0; i < len1; i++) {
                temp1[len - 1 - i] = sub1[len1 - 1 - i];
            }
            for (i = 0; i < len2; i++) {
                temp2[len - 1 - i] = sub2[len2 - 1 - i];
            }
        } else { //保证减数大于被减数
            for (i = 0; i < len1; i++) {
                temp2[len - 1 - i] = sub1[len1 - 1 - i];
            }
            for (i = 0; i < len2; i++) {
                temp1[len - 1 - i] = sub2[len2 - 1 - i];
            }
        }

        int m = 0;
        for (i = 0; i < len; i++) { //相减
            if (temp1[len - 1 - i] != 0) {
                temp1[len - 1 - i] -= 48;
            }
            if (temp2[len - 1 - i] != 0) {
                temp2[len - 1 - i] -= 48;
            }
            m = temp1[len - 1 - i] - temp2[len - 1 - i];
            if (m < 0) { //如果计算的那一位的减数小于被减数那么相前一位借10
                m += 10;
                result[len - i] += m;
                temp1[len - 1 - i] -= 1;
            } else {
                result[len - i] += m;
            }
        }
        //System.out.print("add1 - add2 = "); //输出减法结果
        i = 0;
        while (result[i] == 0) {
            i++;
            if (i == len) { //是否是数组的极限（出现这种情况为减数与被减数一致）
                break;
            }
        }
        StringBuffer sb = new StringBuffer();

        if ((len2 > len1) || (len1 == len2 && sub1[0] < sub2[0])) {
            //System.out.print("-");
            sb.append("-");
        }
        for (; i < len + 1; i++) {
            //System.out.print(Integer.toString(result[i]));
            sb.append(Integer.toString(result[i]));
        }
        return sb.toString();
    }


    /**
     * 取两个数中小的那个
     * @param number1 double
     * @param number2 double
     * @return double
     * @throws Exception
     */
    public static double getMinNumber(double number1, double number2) {
        double minnumber = 0;

        if (number1 > number2) {
            minnumber = number2;
        } else {
            minnumber = number1;
        }
        return minnumber;
    }

    /**
     * 取两个数中大的那个
     * @param number1 double
     * @param number2 double
     * @return double
     * @throws Exception
     */
    public static double getMaxNumber(double number1, double number2) {
        double maxnumber = 0;

        if (number1 > number2) {
            maxnumber = number1;
        } else {
            maxnumber = number2;
        }
        return maxnumber;
    }

    /**
     * 两个String数值进行加、减、乘、除的四则运算
     * @param decimal1 数值1
     * @param decimal2 数值2
     * @param sign     + - * /
     * @return String 计算结果
     */
    public static String stringCalculate(String decimal1, String decimal2,
                                         String sign) {
        if ("+".equals(sign)) {
            return format2frac(toDouble(decimal1) + toDouble(decimal2));
        } else if ("-".equals(sign)) {
            return format2frac(toDouble(decimal1) - toDouble(decimal2));
        } else if ("*".equals(sign)) {
            return format2frac(toDouble(decimal1) * toDouble(decimal2));
        } else if ("/".equals(sign)) {
            return format2frac(toDouble(decimal1) / toDouble(decimal2));
        } else {
            return "";
        }
    }
}
