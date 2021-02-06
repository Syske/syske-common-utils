package io.github.syske.commont.utils.date;

import io.github.syske.commont.utils.other.CommonUtil;
import io.github.syske.commont.utils.number.NumberUtil;
import io.github.syske.commont.utils.string.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    public final static String dateStyleStr = "yyyy-MM-dd";
    public final static String ymStyleStr = "yyyyMM";

    /**
     * 获取当前时间（应用服务器） 时间的格式："yyyy-MM-dd HH:mm:ss"
     * @return String
     */
    public static String get19BitCurTimeAPP() {
        String timeFormat = "yyyy-MM-dd HH:mm:ss";
        return date2Str(new Date(), timeFormat);
    }

    /**
     * 获取当前日期（应用服务器） 日期的格式："yyyy-MM-dd"
     * @return String
     */
    public static String get10BitCurTimeAPP() {
        String timeFormat = "yyyy-MM-dd";
        return date2Str(new Date(), timeFormat);
    }

    /**
     * 获取当前年月（应用服务器） 年月的格式："yyyyMM"
     * @return String
     */
    public static String get6BitCurTimeAPP() {
        String timeFormat = "yyyyMM";
        return date2Str(new Date(), timeFormat);
    }

    /**
     * 获取当前年份（应用服务器） 年份的格式："yyyy"
     * @return String
     */
    public static String get4BitCurTimeAPP() {
        String timeFormat = "yyyy";
        return date2Str(new Date(), timeFormat);
    }

    /**
     * 获取当前时间（应用服务器）
     * @param  timeFormat 时间的格式，如："yyyyMMddHHmmss.SSSSSS"
     * @return String
     */
    public static String getCurTimeAPP(String timeFormat) {
        return date2Str(new Date(), timeFormat);
    }

    /**
     * 把时间转换为字串
     * @param  date   待转换的时间
     * @param  format 转换格式：yyyyMMdd
     * @return String
     */
    public static String date2Str(Date date, String format) {
        if (CommonUtil.isNull(date)) {
            return "";
        }

        String precision = "";
        if ("yyyyMMddHHmmss.SSSSSS".equalsIgnoreCase(format)) {
            precision = "000";
            format = "yyyyMMddHHmmss.SSSSSS";
        }
        if (dateStyleStr.equalsIgnoreCase(format)) {
            format = dateStyleStr;
        }
        format = StringUtils.isEmpty(format) ? dateStyleStr : format;
        format = "yyyyMMddHHmmss.SSSSSS".equalsIgnoreCase(format) ?
                "yyyyMMddHHmmss.SSS" : format;
        //SimpleDateFormat的转换毫秒的精度最多为3位
        SimpleDateFormat df = new SimpleDateFormat(format);

        return df.format(date) + precision;
    }

    /**
     * 把字串转换为日期
     * @param  sdate  字串形式的日期（最长的格式：yyyyMMddHHmmss.SSS）
     * @param  format 字串格式
     * @return Date   转换为日期类型
     */
    public static Date str2Date(String sdate, String format) throws Exception {
        if (StringUtils.isEmpty(sdate)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date new_date = null;
        try {
            new_date = df.parse(sdate);
        } catch (Exception e) {
            throw new Exception("把字串转换为日期错误：" + sdate + "，需转换的日期格式为：" + format, e);
        }
        return new_date;
    }


    /**
     * 日期格式转换
     * @param sdate     String
     * @param informat  String
     * @param outformat String
     * @return Date
     */
    public static String strDateTrans(String sdate, String informat,
                                      String outformat) throws Exception {
        Date date = str2Date(sdate, informat);
        return date2Str(date, outformat);
    }

    /**
     * 计算指定年度的前后的年度
     * @param  year        传入年度
     * @param  relayYears  增加、减少的年度
     * @return String      返回新的年度
     */
    public static String relayYear(String year, int relayYears) throws Exception {
        String newYear = "";
        try {
            newYear = date2Str(dateAdd(str2Date(year, "yyyy"),
                    java.util.GregorianCalendar.YEAR, relayYears),
                    "yyyy");
        } catch (Exception e) {
            throw new Exception("年月计算失败", e);
        }
        return newYear;
    }

    /**
     * 计算指定年月的前后的年月
     * @param  yearMon   传入年月
     * @param  relayMons 增加、减少的月数
     * @return String    返回新的年月
     */
    public static String relayYM(String yearMon, int relayMons) throws Exception {
        String newYM = "";
        try {
            newYM = date2Str(dateAdd(str2Date(yearMon, ymStyleStr),
                    java.util.GregorianCalendar.MONTH, relayMons),
                    ymStyleStr);
        } catch (Exception e) {
            throw new Exception("年月计算失败", e);
        }
        return newYM;
    }


    /**
     * 计算指定年月日的前后的天数
     * @param  yearMonDay   传入年月日
     * @param  relayDay     天数
     * @return String    返回新的年月日
     */
    public static String relayYMD(String yearMonDay, int relayDay) throws Exception {
        String newYMD = "";
        try {
            newYMD = date2Str(dateAdd(str2Date(yearMonDay, dateStyleStr),
                    java.util.GregorianCalendar.YEAR, relayDay),
                    dateStyleStr);
        } catch (Exception e) {
            throw new Exception("年月计算失败", e);
        }
        return newYMD;
    }


    /**
     * 计算指定年月日的前后的年月
     * @param  yearMonDay   传入年月
     * @param  relayMons 增加、减少的月数
     * @return String    返回新的年月
     */
    public static String relayDate(String yearMonDay, int relayMons) throws Exception {
        String newYMD = "";
        try {
            newYMD = date2Str(dateAdd(str2Date(yearMonDay, dateStyleStr),
                    java.util.GregorianCalendar.DATE, relayMons),
                    dateStyleStr);
        } catch (Exception e) {
            throw new Exception("年月计算失败", e);
        }
        return newYMD;
    }


    /**
     * 计算指定年月的前后的年月
     * @param  yearMon   传入年月
     * @param  relayMons 增加、减少的月数
     * @return String    返回新的年月
     */
    public static String relayYM2(String yearMon, int relayMons) {
        String newYM = "";
        String ymStyleStr = "yyyy-MM";
        try {
            newYM = date2Str(dateAdd(str2Date(yearMon, ymStyleStr),
                    java.util.GregorianCalendar.MONTH, relayMons),
                    ymStyleStr);
        } catch (Exception e) {}
        return newYM;
    }

    /**
     * 计算指定年月的前后的年月
     * @param  yearMon   传入年月
     * @param  relayMons 增加、减少的月数
     * @return String    返回新的年月
     */
    public static String relayYM3(String yearMon, int relayMons) {
        String newYM = "";
        String ymStyleStr = "yyyy-MM-dd";
        try {
            newYM = date2Str(dateAdd(str2Date(yearMon, ymStyleStr),
                    java.util.GregorianCalendar.MONTH, relayMons),
                    ymStyleStr);
        } catch (Exception e) {}
        return newYM;
    }


    /**
     * 计算指定年月的前后的年月,relayMons=1返回传入年月
     * @param  yearMon   传入年月 yyyyMM
     * @param  relayMons 增加、减少的月数
     * @return String    返回新的年月
     */
    public static String addYM(String yearMon, int relayMons) {
        String newYM = "";
        try {
            newYM = date2Str(dateAdd(str2Date(yearMon, ymStyleStr),
                    java.util.GregorianCalendar.MONTH, relayMons - 1),
                    ymStyleStr);
        } catch (Exception e) {}
        return newYM;
    }

    /**
     * 计算指定年月的前后的年月,relayMons=1返回传入年月
     * @param  yearMon   传入年月 yyyyMM
     * @param  relayMons 增加、减少的月数
     * @return String    返回新的年月
     */
    public static String addYM1(String yearMon, int relayMons) {
        String newYM = "";
        try {
            newYM = date2Str(dateAdd(str2Date(yearMon, ymStyleStr),
                    java.util.GregorianCalendar.MONTH, relayMons),
                    ymStyleStr);
        } catch (Exception e) {}
        return newYM;
    }


    /**
     * 返回两个日期之间的月数
     * @param  startDate 开始日期（前）
     * @param  endDate   结束日期（后）
     * @return int       月数
     */
    public static int monthsBetween(Date startDate, Date endDate) {
        GregorianCalendar gcStart = new GregorianCalendar();
        GregorianCalendar gcEnd = new GregorianCalendar();
        gcStart.setTime(startDate);
        gcEnd.setTime(endDate);
        return (gcEnd.get(GregorianCalendar.YEAR) -
                gcStart.get(GregorianCalendar.YEAR)) * 12 +
                gcEnd.get(GregorianCalendar.MONTH) -
                gcStart.get(GregorianCalendar.MONTH);
    }

    /**
     * 计算两个年月间的月数--同月返回为0
     * @param  begYM 开始年月（前）YYYYMM
     * @param  endYM 结束年月（后）YYYYMM
     * @return int
     */
    public static int monthsBetweenYM(String begYM, String endYM) throws Exception {
        if (!isYearMonth(begYM)) {
            begYM = date2Str(str2Date("1900-01-01", dateStyleStr), ymStyleStr);
        }
        if (!isYearMonth(endYM)) {
            endYM = date2Str(str2Date("1900-01-01", dateStyleStr), ymStyleStr);
        }

        int months = 0;
        try {
            int begYear = new Integer(begYM.substring(0, 4)).intValue();
            int endYear = new Integer(endYM.substring(0, 4)).intValue();
            int begMonth = new Integer(begYM.substring(4, 6)).intValue();
            int endMonth = new Integer(endYM.substring(4, 6)).intValue();
            months = (endYear - begYear) * 12 + (endMonth - begMonth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return months;
    }

    /**
     * 计算两个年月间的月数--同月返回为1
     * @param  begYM 开始年月（前）YYYYMM
     * @param  endYM 结束年月（后）YYYYMM
     * @return int
     */
    public static int monthsBetweenYM2(String begYM, String endYM) throws Exception {
        if (!isYearMonth(begYM)) {
            begYM = date2Str(str2Date("1900-01-01", dateStyleStr), ymStyleStr);
        }
        if (!isYearMonth(endYM)) {
            endYM = date2Str(str2Date("1900-01-01", dateStyleStr), ymStyleStr);
        }

        int months = 0;
        try {
            int begYear = new Integer(begYM.substring(0, 4)).intValue();
            int endYear = new Integer(endYM.substring(0, 4)).intValue();
            int begMonth = new Integer(begYM.substring(4, 6)).intValue();
            int endMonth = new Integer(endYM.substring(4, 6)).intValue();
            months = (endYear - begYear) * 12 + (endMonth - begMonth + 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return months;
    }

    /**
     * 计算两个日期间的月数
     * @param  begYMD 开始年月（前）YYYY-MM-dd
     * @param  endYMD 结束年月（后）YYYY-MM-dd
     * @return int
     */
    public static int monthsBetweenYMD(String begYMD, String endYMD) throws Exception {
        String begYMD1 = changeDateFormat(begYMD, "yyyyMM");
        String endYMD1 = changeDateFormat(endYMD, "yyyyMM");
        return monthsBetweenYM2(begYMD1, endYMD1);
    }

    /**
     * 取两个日期之间的天数（开始日期 > 结束日期 则 返回负天数）
     * @param  begDate  开始日期yyyy-MM-dd
     * @param  endDate  结束日期yyyy-MM-dd
     * @return int 之间的天数
     * @
     */
    public static int dateBetween(String begDate, String endDate) throws Exception {
        if (!isDate2(begDate) || !isDate2(endDate)) {
            throw new Exception("输入日期格式错误： 开始日期" + begDate + " 结束日期" + endDate);
        }
        int sign = notAfter(begDate, endDate, dateStyleStr) ? 1 : -1;

        GregorianCalendar gc1 = new GregorianCalendar();
        GregorianCalendar gc2 = new GregorianCalendar();
        gc1.setTime(str2Date(begDate, dateStyleStr));
        gc2.setTime(str2Date(endDate, dateStyleStr));

        ElapsedTime et = new ElapsedTime();
        int days = et.getDays(gc1, gc2);
        int months = et.getMonths(gc1, gc2);
        return days * sign;
    }


    /**
     * 计算两个年月间的月数--同月返回为1 (注意：调用本方法，要保证年月的格式正确。为提高效率不对年月进行效验)
     * @param  begYM 开始年月（前）YYYYMM
     * @param  endYM 结束年月（后）YYYYMM
     * @return int
     */
    public static int countMonths(String begYM, String endYM) {
        int months = 0;
        try {
            int begYear = new Integer(begYM.substring(0, 4)).intValue();
            int endYear = new Integer(endYM.substring(0, 4)).intValue();
            int begMonth = new Integer(begYM.substring(4, 6)).intValue();
            int endMonth = new Integer(endYM.substring(4, 6)).intValue();
            months = (endYear - begYear) * 12 + (endMonth - begMonth + 1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return months;
    }

    private static class ElapsedTime {
        public int getDays(GregorianCalendar g1, GregorianCalendar g2) {
            int elapsed = 0;
            GregorianCalendar gc1, gc2;

            if (g2.after(g1)) {
                gc2 = (GregorianCalendar) g2.clone();
                gc1 = (GregorianCalendar) g1.clone();
            } else {
                gc2 = (GregorianCalendar) g1.clone();
                gc1 = (GregorianCalendar) g2.clone();
            }

            gc1.clear(Calendar.MILLISECOND);
            gc1.clear(Calendar.SECOND);
            gc1.clear(Calendar.MINUTE);
            gc1.clear(Calendar.HOUR_OF_DAY);

            gc2.clear(Calendar.MILLISECOND);
            gc2.clear(Calendar.SECOND);
            gc2.clear(Calendar.MINUTE);
            gc2.clear(Calendar.HOUR_OF_DAY);

            while (gc1.before(gc2)) {
                gc1.add(Calendar.DATE, 1);
                elapsed++;
            }
            return elapsed;
        }

        public int getMonths(GregorianCalendar g1, GregorianCalendar g2) {
            int elapsed = 0;
            GregorianCalendar gc1, gc2;

            if (g2.after(g1)) {
                gc2 = (GregorianCalendar) g2.clone();
                gc1 = (GregorianCalendar) g1.clone();
            } else {
                gc2 = (GregorianCalendar) g1.clone();
                gc1 = (GregorianCalendar) g2.clone();
            }

            gc1.clear(Calendar.MILLISECOND);
            gc1.clear(Calendar.SECOND);
            gc1.clear(Calendar.MINUTE);
            gc1.clear(Calendar.HOUR_OF_DAY);
            gc1.clear(Calendar.DATE);

            gc2.clear(Calendar.MILLISECOND);
            gc2.clear(Calendar.SECOND);
            gc2.clear(Calendar.MINUTE);
            gc2.clear(Calendar.HOUR_OF_DAY);
            gc2.clear(Calendar.DATE);

            while (gc1.before(gc2)) {
                gc1.add(Calendar.MONTH, 1);
                elapsed++;
            }
            return elapsed;
        }
    }


    /**
     * 根据增加or减少的时间得到新的日期
     * @param  curDate   当前日期
     * @param  field     需操作的'年'or'月'or'日'
     *                   (Calendar.MONTH Calendar.DATE )
     * @param  addNumber 增加or减少的时间
     * @return Date      新的日期
     */
    public static Date dateAdd(Date curDate, int field, int addNumber) {
        GregorianCalendar curGc = new GregorianCalendar();
        curGc.setTime(curDate);
        curGc.add(field, addNumber);
        return curGc.getTime();
    }
    /**
     * 根据增加or减少的时间得到新的日期
     * @param  curDate   当前日期
     * @param  field     需操作的'年'or'月'or'日'
     *                   (Calendar.MONTH Calendar.DATE )
     * @param  addNumber 增加or减少的时间
     * @return Date      新的日期
     */
    public static String dateAdd(String curDate, int field, int addNumber) throws Exception {
        GregorianCalendar curGc = new GregorianCalendar();

        curGc.setTime(str2Date(curDate,"yyyy-MM-dd HH:mm:ss"));
        curGc.add(field, addNumber);
        return date2Str(curGc.getTime(),"yyyy-MM-dd HH:mm:ss");
    }
    /**
     * 根据日期获取月份
     * @param dateStr String
     * @return String
     */
    public static String date2Month(String dateStr) throws Exception {
        Date date = str2Date(dateStr, dateStyleStr);
        String ym = date2Str(date, ymStyleStr);
        return ym;
    }



    /**
     * 判断firstDate是否不在lastDate之后
     * @param  lastDate    开始日期
     * @param  firstDate   结束日期
     * @param  dateFormat  eg:ymStyleStr ; dateStyleStr
     * @return boolean
     * @
     */
    public static boolean notAfter(String firstDate, String lastDate,
                                   String dateFormat) throws Exception {
        Date first = null;
        Date last = null;
        try {
            if (lastDate.equals(firstDate)) {
                return true;
            }
            first = str2Date(firstDate, dateFormat);
            last = str2Date(lastDate, dateFormat);
        } catch (Exception ex) {
            throw new Exception("errors.date: " + firstDate + " or " + lastDate);
        }
        return first.before(last);
    }

    /**
     * 判断查询日期是否在指定日期之间
     * @param  firstDate  开始日期
     * @param  lastDate   结束日期
     * @param  queryDate  查询日期
     * @param  dateFormat eg:ymStyleStr ; dateStyleStr
     * @return boolean
     */
    public static boolean isBetweenDate(String firstDate, String lastDate,
                                        String queryDate, String dateFormat) {
        try {
            if (!StringUtils.isEmpty(queryDate) &&
                    (!notAfter(firstDate, queryDate, dateFormat) ||
                            !notAfter(queryDate, lastDate, dateFormat))) {
                return false;
            } else {
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 找出两个年月间除去ArrayList里面指明的年月后剩下的年月
     * 主要在处理无记录补缴时使用
     * @param  begYM      开始年月
     * @param  endYM      结束年月
     * @param  yearMonths 年月
     * @return ArrayList  新的年月数组
     */
    public static ArrayList invertYM(String begYM, String endYM,
                                     ArrayList yearMonths) throws Exception {
        //先找出这两个年月间的月数
        int months = monthsBetweenYM(begYM, endYM);
        //将该时间段内的年月放到hashtable里面记录
        Hashtable hs = new Hashtable();
        for (int i = 0; i < months + 1; i++) {
            hs.put(relayYM(begYM, i), relayYM(begYM, i));
        }
        //将已经存在在yearMonths里面的年月从hashtable里面移走
        for (int i = 0; i < yearMonths.size(); i++) {
            hs.remove(yearMonths.get(i));
        }
        //将剩下的结果放到arrayList里面
        ArrayList al = new ArrayList(hs.values());
        return al;
    }


    /**
     * 判断是否正确的日期格式
     * @param  sValue  日期字串：yyyy-MM-dd HH:mm:ss 或 yyyyMMddHHmmss
     * @return boolean
     */
    public static boolean isDateTime(String sValue) {
        boolean flag = false;

        if (sValue.length() == 19) {
            String date = sValue.substring(0, 10);
            String time = sValue.substring(11, 19);
            flag = isDate(date);
            if (flag == true) {
                flag = isTime(time);
            }

            return flag;
        } else if (sValue.length() == 14) {
            String date = sValue.substring(0, 8);
            String time = sValue.substring(8, 14);
            flag = isDate(date);
            if (flag == true) {
                flag = isTime(time);
            }
        }
        return flag;
    }

    /**
     * 判断是否正确的日期格式
     * @param  sValue  日期字串：yyyyMMdd 或 yyyy-MM-dd
     * @return boolean
     */
    public static boolean isDate(String sValue) {
        if (sValue.length() == 10) {
            return isDate2(sValue);
        } else if (sValue.length() == 8) {
            return isDate1(sValue);

        } else {
            return false;
        }
    }

    /**
     * 判断是否正确的日期格式
     * @param  sValue  日期字串：yyyyMMdd
     * @return boolean
     */
    public static boolean isDate1(String sValue) {
        if (StringUtils.isEmpty(sValue)) {
            return false;
        }
        int intYear;
        int intMon;
        int intDay;
        boolean boolLeapYear = false;
        if (sValue.getBytes().length != 8) {
            //长度只能为8位
            return false;
        }
        intYear = NumberUtil.toInt(sValue.substring(0, 4));
        intMon = NumberUtil.toInt(sValue.substring(4, 6));
        intDay = NumberUtil.toInt(sValue.substring(6, 8));
        if (intMon > 12 || intMon < 1) {
            return false;
        }
        if (intYear < 1800) {
            return false;
        }
        if ((intMon == 1 || intMon == 3 || intMon == 5 || intMon == 7 ||
                intMon == 8 || intMon == 10 || intMon == 12) &&
                (intDay > 31 || intDay < 1)) {
            return false;
        }
        if ((intMon == 4 || intMon == 6 || intMon == 9 || intMon == 11) &&
                (intDay > 30 || intDay < 1)) {
            return false;
        }
        if (intMon == 2) {
            if ((intYear % 100) == 0) {
                if ((intYear % 400) == 0) {
                    boolLeapYear = true;
                }
            } else {
                if ((intYear % 4) == 0) {
                    boolLeapYear = true;
                }
            }
            if (boolLeapYear) {
                if (intDay > 29 || intDay < 1) {
                    return false;
                }
            } else {
                if (intDay > 28 || intDay < 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断是否正确的日期格式：现在
     * @param  sValue  日期字串：yyyy-MM-dd
     * @return boolean
     */
    public static boolean isDate2(String sValue) {
        if (StringUtils.isEmpty(sValue)) {
            return false;
        }
        if (sValue.length() != 10) {
            return false;
        }
        String temp1 = sValue.substring(0, 4);
        String temp2 = sValue.substring(5, 7);
        String temp3 = sValue.substring(8, 10);
        String seperator1 = sValue.substring(4, 5);
        String seperator2 = sValue.substring(7, 8);
        sValue = temp1 + temp2 + temp3;
        if (!(seperator1.equals("-") && seperator2.equals("-"))) {
            return false;
        }

        return isDate1(sValue);
    }

    /**
     * 判断是否正确的时间格式
     * @param  sValue  日期字串：HHmmss
     * @return boolean
     */
    public static boolean isTime(String sValue) {
        if (sValue.length() == 8) {
            return isTime2(sValue);
        } else if (sValue.length() == 6) {
            return isTime1(sValue);
        } else {
            return false;
        }
    }


    /**
     * 判断是否正确的时间格式
     * @param  sValue  日期字串：HHmmss
     * @return boolean
     */
    public static boolean isTime1(String sValue) {
        if (StringUtils.isEmpty(sValue)) {
            return false;
        }
        int intHour;
        int intMinute;
        int intSecond;
        if (sValue.getBytes().length != 6) {
            //长度只能为6位
            return false;
        }
        intHour = NumberUtil.toInt(sValue.substring(0, 2));
        intMinute = NumberUtil.toInt(sValue.substring(2, 4));
        intSecond = NumberUtil.toInt(sValue.substring(4, 6));

        //小时
        if (intHour > 24 || intHour < 0) {
            return false;
        }
        //分钟
        if (intMinute > 60 || intMinute < 0) {
            return false;
        }
        //秒
        if (intSecond > 60 || intSecond < 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否正确的时间格式
     * @param  sValue  日期字串：HH:mm:ss
     * @return boolean
     */
    public static boolean isTime2(String sValue) {
        if (StringUtils.isEmpty(sValue)) {
            return false;
        }
        String dateSeparator = ":";
        String hour;
        String minute;
        String second;
        if (sValue.getBytes().length != 8) {
            //长度只能为8位
            return false;
        }
        hour = sValue.substring(0, 2);
        minute = sValue.substring(3, 5);
        second = sValue.substring(6, 8);
        String seperator1 = sValue.substring(2, 3);
        String seperator2 = sValue.substring(5, 6);
        sValue = hour + minute + second;
        if (!(seperator1.equals(":") && seperator2.equals(":"))) {
            return false;
        }
        return isTime1(sValue);
    }

    /**
     * 改变日期格式：入参必须是正确的格式（要经过isDate2（）,或isDate效验）
     * @param  sdate   日期字串：yyyyMMdd or yyyy-MM-dd
     * @param  format  日期格式
     * @return String
     */
    public static String changeDateFormat(String sdate, String format) throws Exception {
        Date date = null;
        String changeDate = "";
        if (format.equals("yyyyMMdd")) {
            date = str2Date(sdate, "yyyy-MM-dd");
            changeDate = date2Str(date, "yyyyMMdd");

        } else if (format.equals("yyyy-MM-dd")) {
            date = str2Date(sdate, "yyyyMMdd");
            changeDate = date2Str(date, "yyyy-MM-dd");

        } else if (format.equals("yyyyMM")) {
            date = str2Date(sdate, "yyyy-MM-dd");
            changeDate = date2Str(date, "yyyyMM");

        } else if (format.equals("yyyy-MM")) {
            date = str2Date(sdate, "yyyyMMdd");
            changeDate = date2Str(date, "yyyy-MM");

        } else if (format.equals("yyyyMMddHHmmss")) {
            date = str2Date(sdate, "yyyy-MM-dd HH:mm:ss");
            changeDate = date2Str(date, "yyyyMMddHHmmss");

        } else if (format.equals("yyyy-MM-dd HH:mm:ss")) {
            date = str2Date(sdate, "yyyyMMddHHmmss");
            changeDate = date2Str(date, "yyyy-MM-dd HH:mm:ss");
        }

        return changeDate;
    }

    /**
     * 比较两个日期的大小：入参的格式是（yyyy-MM-dd或yyyyMMdd）
     * @param startDate String 开始日期
     * @param endDate String   结束日期
     * startDate(2013-01-01)早于等于endDate(2014-01-01)返回true,否则返回false;
     * @return boolean
     */
    public static boolean compareToDate(String startDate, String endDate) {
        if ("".equals(endDate)) {
            return true;
        }
        int i = 0;
        i = endDate.compareTo(startDate);
        if (i > 0 || i == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较两个日期的大小：入参的格式是（yyyy-MM-dd或yyyyMMdd）
     * @param startDate String 开始日期
     * @param endDate String   结束日期
     * startDate(2013-01-01)早于endDate(2014-01-01)返回true,否则返回false;
     * @return boolean
     */
    public static boolean compareToDate3(String startDate, String endDate) {
        if (endDate.equals("")) {
            return true;
        }
        int i = 0;
        i = endDate.compareTo(startDate);
        if (i > 0 ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否正确的年月格式
     * @param  sValue  日期字串：yyyyMM
     * @return boolean
     */
    public static boolean isYearMonth(String sValue) {
        if (StringUtils.isEmpty(sValue)) {
            return false;
        }
        if (sValue.length() != 6) {
            return false;
        }
        String temp1 = sValue.substring(0, 4);
        String temp2 = sValue.substring(4);
        sValue = temp1 + temp2 + "01";
        return isDate(sValue);
    }

    /**
     * 根据输入年月得到该年月的包含最后一天的String型日期
     * @param  yearMonth  年月yyyyMM
     * @return String     yyyy-MM-dd
     */
    public static String getYmEndDay(String yearMonth) throws Exception {
        return date2Str(dateAdd(str2Date(getYmFirstDay(relayYM(yearMonth, 1)),
                dateStyleStr),
                java.util.GregorianCalendar.DATE, -1)
                , dateStyleStr);
    }

    /**
     * 根据输入年月得到该年月的包含最后一天的String型日期
     * @param  yearMonth  年月yyyy-MM
     * @return String     yyyy-MM-dd
     */
    public static String getYmEndDay2(String yearMonth) throws Exception {
        String dateStyleStr = "yyyy-MM-dd";
        String ymStyleStr = "yyyy-MM";
        return date2Str(dateAdd(str2Date(getYmFirstDay2(relayYM2(yearMonth, 1)),
                dateStyleStr),
                java.util.GregorianCalendar.DATE, -1)
                , dateStyleStr);
    }


    /**
     * 根据输入年月得到该年月的包含第一天的String型日期
     * @param  yearMonth  年月yyyyMM
     * @return String     yyyyMMdd
     */
    public static String getYmFirstDay(String yearMonth) throws Exception {
        return date2Str(str2Date(yearMonth, ymStyleStr), dateStyleStr);
    }

    /**
     * 根据输入年月得到该年月的包含第一天的String型日期
     * @param  yearMonth  年月yyyyMM
     * @return String     yyyyMMdd
     */
    public static String getYmFirstDay2(String yearMonth) throws Exception {
        String dateStyleStr = "yyyy-MM-dd";
        String ymStyleStr = "yyyy-MM";
        return date2Str(str2Date(yearMonth, ymStyleStr), dateStyleStr);
    }


    /**
     * 根据输入的String日期(yyyyMMdd)，得出年月
     * @param  strDate 输入的String日期
     * @param  inFormat  输入日期的格式（如：yyyyMMddHHmmss.SSS）
     * @param  outFormat  输出日期的格式（如：yyyyMMddHHmmss.SSS）
     * @return String  年月
     */
    public static String getStrDateYM(String strDate, String inFormat,
                                      String outFormat) throws Exception {
        if (StringUtils.isEmpty(inFormat)) {
            inFormat = dateStyleStr;
        }

        return date2Str(str2Date(strDate, inFormat), outFormat);
    }

    /**
     * 得到此年的上一个年度
     * @return String 上一个年度
     */
    public static String last_Year() {
        Date systemDate = new Date();
        String year = date2Str(systemDate, "yyyy");
        int nowYear = Integer.parseInt(year);
        int lastYear = nowYear - 1;
        String last_Year = String.valueOf(lastYear);
        return last_Year;
    }

    /**
     * 得到此年的下一个年度
     * @param year 年度
     * @return String 下一个年度
     */
    public static String next_Year(String year) {
        int nowYear = Integer.parseInt(year);
        int nextYear = nowYear + 1;
        String next_Year = String.valueOf(nextYear);
        return next_Year;
    }

    /**
     * 将多少年多少个月转换为月  1年1月转换为13月
     */
    public static int sumMonth(String yearAndMonth) {
        int flagYear = yearAndMonth.indexOf("年");
        String syear = yearAndMonth.substring(0, flagYear);
        int year = Integer.parseInt(syear);
        int flagMonth = yearAndMonth.indexOf("月");
        String smonth = yearAndMonth.substring(flagYear + 1, flagMonth);
        int month = Integer.parseInt(smonth);
        int sumMonth = month + year * 12;
        return sumMonth;
    }

    /**
     * 将月转换为多少年多少个月
     */
    public static String yearAndMonth(int month) {
        int year = month / 12;
        int monthTr = month % 12;
        String yearAndMonth = year + "年" + monthTr + "月";
        return yearAndMonth;
    }

    /**
     * 返回两个年度之间的差的值
     * @param startYear 格式为yyyy
     * @param lastYear 格式为yyyy
     */
    public static String bachValue(String startYear, String lastYear) {
        int istartYear = Integer.parseInt(startYear);
        int ilastYear = Integer.parseInt(lastYear);
        int bachValue = istartYear - ilastYear;
        String sbachValue = String.valueOf(bachValue);
        return sbachValue;
    }

    /**
     * 返回为 12年1月 为13
     * @param smonth int 输入的月数
     * @return String 有余数就进位的年
     */
    public static String yearEnter(String smonth) {
        int month = Integer.parseInt(smonth);
        int year = month / 12;
        int residual = month % 12;
        if (residual != 0) {
            year += 1;
        } else {
            year = year;
        }
        String syear = String.valueOf(year);
        return syear;
    }

    /**
     * 将月转换为多少年，18月1.5年
     */
    public static String monthToYear(int month) {
        double year = NumberUtil.format1frac(month / 12.0);
        String yearAndMonth = String.valueOf(year) + "年";
        return yearAndMonth;
    }

    /**
     * 根据输入年月得到该季度所有的月份
     * @param  yearMonth  年月yyyy-MM-dd
     * @return String     yyyy-MM-dd
     */

    public static ArrayList getQuarterMonths(String yearMonth) {
        ArrayList monthsList = new ArrayList();
        int year = new Integer(yearMonth.substring(0, 4)).intValue();
        int month = new Integer(yearMonth.substring(5, 7)).intValue();
        String startYm = "" + year + "-";
        if (month >= 1 && month <= 3) {
            monthsList.add(year + "01");
            monthsList.add(year + "02");
            monthsList.add(year + "03");
        } else if (month >= 4 && month <= 6) {
            monthsList.add(year + "04");
            monthsList.add(year + "05");
            monthsList.add(year + "06");

        } else if (month >= 7 && month <= 9) {
            monthsList.add(year + "07");
            monthsList.add(year + "08");
            monthsList.add(year + "09");

        } else if (month >= 10 && month <= 12) {
            monthsList.add(year + "10");
            monthsList.add(year + "11");
            monthsList.add(year + "12");

        }
        return monthsList;
    }

    /**
     * 根据输入年月得到该年月的包含第一天的String型日期
     * @param  yearMonth  年月yyyy-MM-dd
     * @return String     yyyy-MM-dd
     */
    public static String getQuarterFirstDay2(String yearMonth) throws Exception {
        int year = new Integer(yearMonth.substring(0, 4)).intValue();
        int month = new Integer(yearMonth.substring(5, 7)).intValue();
        String startYm = "" + year + "-";
        if (month >= 1 && month <= 3) {
            startYm = startYm + "01";
        } else if (month >= 4 && month <= 6) {
            startYm = startYm + "04";
        } else if (month >= 7 && month <= 9) {
            startYm = startYm + "07";
        } else if (month >= 10 && month <= 12) {
            startYm = startYm + "10";
        }

        return getYmFirstDay2(startYm);
    }

    /**
     * 根据输入年月得到该年月的包含第一天的String型日期
     * @param  yearMonth  年月yyyy-MM-dd
     * @return String     yyyy-MM-dd
     */
    public static String getQuarterLastDay2(String yearMonth) throws Exception {
        int year = new Integer(yearMonth.substring(0, 4)).intValue();
        int month = new Integer(yearMonth.substring(5, 7)).intValue();
        String startYm = "" + year + "-";
        if (month >= 1 && month <= 3) {
            startYm = startYm + "04";
        } else if (month >= 4 && month <= 6) {
            startYm = startYm + "06";
        } else if (month >= 7 && month <= 9) {
            startYm = startYm + "09";
        } else if (month >= 10 && month <= 12) {
            startYm = startYm + "12";
        }
        return getYmEndDay2(startYm);
    }

    /**
     * 日期转换为时间的函数　用于按照日期查找时间范围
     * @param date String
     * @return String
     * @throws Exception
     */
    public static String getDateStopTime(String date) {
        if (date != null) {
            return date + " 23:59:59";
        } else {
            return date;
        }
    }

    public static String getSimpleDateStr(String datestr) {
        if (datestr != null) {
            return datestr.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        } else {
            return datestr;
        }
    }

    /**
     * 得到上个月的最后一天
     * @return String
     * @
     */
    public static String getLastMonth() {
        Calendar cal = Calendar.getInstance(); //当前日期
        cal.set(Calendar.DATE, 1); //设为当前月的1号
        cal.add(Calendar.DATE, -1); //减一天，变为上月最后一天

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.format(cal.getTime());

        return simpleDateFormat.format(cal.getTime());
    }

    /**
     * 取应用服务器当前年月（应用服务器）上一个月 第一天："yyyy-MM-dd"
     * @return String
     */
    public static String getLastMonthFirstDay() {

        Calendar cal = Calendar.getInstance(); //当前日期
        cal.set(Calendar.DATE, 1); //日期设置为1
        cal.add(Calendar.MONTH, -1); //减一天，变为上月

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.format(cal.getTime());

        return simpleDateFormat.format(cal.getTime());

    }


    /**
     * 取应用服务器当前年份（应用服务器） 年份的格式："yyyyMMdd"
     * @return String
     */
    public static String get8BitCurTimeAPP() {
        String timeFormat = "yyyyMMdd";
        return date2Str(new Date(), timeFormat);
    }

    /**
     * 计算指定年月日的前后的天数
     * @param  yearMonDay   传入年月日
     * @param  relayDay     天数
     * @return String    返回新的年月日
     */
    public static String addYMD(String yearMonDay, int relayDay) {
        String newYMD = "";
        try {
            newYMD = date2Str(dateAdd(str2Date(yearMonDay, dateStyleStr),
                    java.util.GregorianCalendar.DATE, relayDay),
                    dateStyleStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newYMD;
    }

    /**
     * 返回日期是星期几
     * @param  sdate 日期
     * @param  format 转换格式：yyyy-MM-dd
     * @return int       星期
     */
    public static String dateofweek2(String sdate, String format) throws Exception {

        String week = "";

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(str2Date(sdate, format));

        int date = calendar.get(calendar.DAY_OF_WEEK);

        switch (date) {
            case 1:
                week = "0";
                break;
            case 2:
                week = "1";
                break;
            case 3:
                week = "2";
                break;
            case 4:
                week = "3";
                break;
            case 5:
                week = "4";
                break;
            case 6:
                week = "5";
                break;
            case 7:
                week = "6";
                break;

        }
        return week;

    }

    /**
     * 将字符日期转化为整形
     * @param  date YYYY-MM-dd
     *
     * @return int yyyyMMdd
     */
    public static int strTOnum(String date) {

        int	 num =Integer.parseInt(date.replaceAll("-", ""));

        return num;

    }

    /**
     * 比较两个日期的大小，同一个日期返回false：入参的格式是（yyyy-MM-dd或yyyyMMdd）
     * @param startDate String 开始日期
     * @param endDate String   结束日期
     * @return boolean
     */
    public static boolean compareToDate2(String startDate, String endDate) {
        if (endDate.equals("")) {
            return true;
        }
        int i = 0;
        i = endDate.compareTo(startDate);
        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }




    /**
     * 日期转换为时间的函数　用于按照日期查找时间范围
     * @param date String
     * @return String
     * @throws Exception
     */
    public static String getDateStartTime(String date) {
        if (date != null) {
            return date + " 00:00:00";
        } else {
            return date;
        }
    }

    /**
     * 获取传入日期的倒计时
     *
     * @param targetDateStr 目标日期，格式 yyyy-MM-dd
     * @return 返回天数
     */
    public static int getCountDownDays(String targetDateStr, Date today) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String nowDateStr = format.format(today);  //第二个日期
        //算两个日期间隔多少天
        Date nowDate = format.parse(nowDateStr);
        Date targetDate = format.parse(targetDateStr); // 目标日期
        int days = (int) ((targetDate.getTime() - nowDate.getTime()) / (1000*3600*24));
        return days;
    }

}
