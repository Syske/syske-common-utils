package io.github.syske.commont.utils.other;

import io.github.syske.commont.utils.date.DateUtil;
import io.github.syske.commont.utils.number.NumberUtil;
import io.github.syske.commont.utils.string.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * syske-common-utils
 * @Description: 公共工具类
 */

public class CommonUtil {

	public CommonUtil() {
	}

	/**
	 * 将字符转换为boolean
	 * @param booleanStr String （T(t), F(f)）
	 * @return boolean （True, false）
	 */
	public static boolean formatBoolean(String booleanStr) {
		if (booleanStr.toUpperCase().equals("T")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 修补15,17位个人身份证号码为18位
	 * @param  personIDCode 15,17位身份证
	 * @return String       18位身份证
	 */
	public static String fixPersonIDCode(String personIDCode) {
		String retIDCode = "";
		String id17 = "";
		if (personIDCode.trim().length() != 17 &&
				personIDCode.trim().length() != 15) {
			return personIDCode;
		}
		if (personIDCode.trim().length() == 15) {
			id17 = personIDCode.substring(0, 6) + "19" +
					personIDCode.substring(6, 15); //15为身份证补'19'
		} else {
			id17 = personIDCode;
		}
		if (id17.indexOf("A") > 0 || id17.indexOf("X") > 0 || id17.indexOf("x") > 0) {
			return personIDCode;
		}
		char[] code = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'}; //11个
		int[] factor = {0, 2, 4, 8, 5, 10, 9, 7, 3, 6, 1, 2, 4, 8, 5, 10, 9, 7}; //18个;
		int[] idcd = new int[18];
		int i;
		int j;
		int sum;
		int remainder;

		for (i = 1; i < 18; i++) {
			j = 17 - i;
			idcd[i] = Integer.parseInt(id17.substring(j, j + 1));
		}

		sum = 0;
		for (i = 1; i < 18; i++) {
			sum = sum + idcd[i] * factor[i];
		}
		remainder = sum % 11;
		String lastCheckBit = String.valueOf(code[remainder]);
		return id17 + lastCheckBit;

	}

	/**
	 * 判断是否是有效的18位或15位个人身份证号码
	 * @param  identityId 18位或15位个人身份证号码
	 * @return boolean
	 */
	public static boolean isIdentityId(String identityId) {
		if (StringUtils.isEmpty(identityId)) {
			return false;
		}
		try {
			String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4",
					"3", "2" };
			String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
					"9", "10", "5", "8", "4", "2" };
			String Ai = "";
			// ================ 号码的长度 15位或18位 ================  
			if (identityId.length() != 15 && identityId.length() != 18) {
				//"身份证号码长度应该为15位或18位。";  
				return false;
			}
			// =======================(end)========================  

			// ================ 数字 除最后以为都为数字 ================  
			if (identityId.length() == 18) {
				Ai = identityId.substring(0, 17);
			} else if (identityId.length() == 15) {
				Ai = identityId.substring(0, 6) + "19" + identityId.substring(6, 15);
			}
			if (isNumeric(Ai) == false) {
				//"身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";  
				return false;
			}
			// =======================(end)========================  

			// ================ 出生年月是否有效 ================  
			String strYear = Ai.substring(6, 10);// 年份  
			String strMonth = Ai.substring(10, 12);// 月份  
			String strDay = Ai.substring(12, 14);// 月份  
			if (DateUtil.isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
				//"身份证生日无效。";  
				return false;
			}
			GregorianCalendar gc = new GregorianCalendar();
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
					strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				//"身份证生日不在有效范围。";  
				return false;
			}
			if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
				// "身份证月份无效";  
				return false;
			}
			if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
				//"身份证日期无效";  
				return false;
			}
			// =====================(end)=====================  

			// ================ 地区码时候有效 ================  
			//		          Hashtable h = GetAreaCode();  
			//		          if (h.get(Ai.substring(0, 2)) == null) {  
			//		              errorInfo = "身份证地区编码错误。";  
			//		              return errorInfo;  
			//		          }  
			// ==============================================  
			// ================ 判断最后一位的值 ================  
			int TotalmulAiWi = 0;
			for (int i = 0; i < 17; i++) {
				TotalmulAiWi = TotalmulAiWi
						+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
						* Integer.parseInt(Wi[i]);
			}
			int modValue = TotalmulAiWi % 11;
			String strVerifyCode = ValCodeArr[modValue];
			Ai = Ai + strVerifyCode;

			if (identityId.length() == 18) {
				//不区分“x“大小写
				if (Ai.equals(identityId) == false) {
					// "身份证无效，不是合法的身份证号码";  
					return false;
				}
			} else {
				return true;
			}
			// =====================(end)=====================  
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
	/**
	 * 功能：判断字符串是否为数字 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断是否是有效的15位个人身份证号码
	 * @param  identityId 15位个人身份证号码
	 * @return boolean
	 */
	public static boolean isIdentityId15(String identityId) {
		//判断得到的身份证号码是不是空的
		if (StringUtils.isEmpty(identityId)) {
			return false;
		}

		try {
			//判断长度是不是15位
			if (identityId.length() == 15) {
				//判断所得到的身份证是不是都是数字
				if (NumberUtil.isLong(identityId)) {
					String identityId15 = "19" + identityId.substring(6, 12);
					//判断所得到的出身日期是不是正确的日期格式
					if (DateUtil.isDate(identityId15)) {
						return true;
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获得人员的出生日期
	 * @param  psidcd     身份证号
	 * @return  String
	 */
	public static String getBirthDay(String psidcd) {
		String birthDay = "";
		//如果不是有效的18位或15位个人身份证号码
		if (!isIdentityId(psidcd)) {
			return "";
		}
		//修补15位个人身份证号码为18位
		if (psidcd.length() == 15) {
			psidcd = fixPersonIDCode(psidcd);
		}

		birthDay = psidcd.substring(6, 14);
		//判断是否正确的日期格式：yyyyMMdd
		if (!DateUtil.isDate(birthDay)) {
			return "";
		}
		//    birthDay = birthDay.substring(0, 4) + "-" + birthDay.substring(4, 6) + "-" +
		//	       birthDay.substring(6, 8);

		return birthDay;
	}

	/**
	 * 获得人员的出生日期
	 * @param  psidcd     身份证号
	 * @return  String
	 */
	public static String getBirthDay2(String psidcd) throws Exception {
		String birthDay = DateUtil.changeDateFormat(getBirthDay(psidcd), "yyyy-MM-dd");
		return birthDay;
	}

	/**
	 * 获得人员的性别
	 * @param  psidcd     身份证号
	 * @return  String
	 */
	public static String getPersonSex(String psidcd) {
		String personSex = "";

		//如果不是有效的18位或15位个人身份证号码
		if (!isIdentityId(psidcd)) {
			return "";
		}
		//修补15位个人身份证号码为18位
		if (psidcd.length() == 15) {
			psidcd = fixPersonIDCode(psidcd);
		}

		//身份证号的第十七 是偶数的话，则该人员是女的；是奇数的话，则该人员是男的，
		personSex = psidcd.substring(16, 17);
		if (NumberUtil.toInt(personSex) % 2 == 0) {
			personSex = "2"; //女2
		} else {
			personSex = "1"; //男1
		}

		return personSex;
	}


	/**
	 * 判断Object是否为null
	 * @param  oValue  对象
	 * @return boolean
	 */
	public static boolean isNull(Object oValue) {
		return oValue == null;
	}

	/**
	 * 获得人员在某一年的年龄
	 * @param  psidcd     身份证号
	 * @param  yearMonth  计算年龄的年月
	 * @return int        年龄
	 */
	public static int countPersonAge(String psidcd, String yearMonth) throws Exception {
		if (psidcd.trim().length() < 18) { // 身份证位数不对
			return 0;
		}
		String birthYM = psidcd.substring(6, 12);
		return countAge(birthYM, yearMonth);
	}

	/**
	 * 获得人员在某一年的年龄
	 * @param  birthday   出生日期 yyyyMMdd
	 * @param  yearMonth  计算年龄的年月
	 * @return int        年龄
	 */
	public static int countPersonAgeByBirthday(String birthday, String yearMonth) throws Exception {
		if (!DateUtil.isDate(birthday)) {
			return 0;
		}
		String birthYM = birthday.substring(0, 4) + birthday.substring(5, 7);
		return countAge(birthYM, yearMonth);
	}


	/**
	 * 获得人员在某一年的年龄
	 * @param  birthYM    出生年月
	 * @param  yearMonth  计算年龄的年月
	 * @return int        年龄
	 */
	public static int countAge(String birthYM, String yearMonth) throws Exception {
		if (!DateUtil.isYearMonth(birthYM)) {
			return 0;
		}
		if (!DateUtil.isYearMonth(yearMonth)) {
			return 0;
		}
		int age = 0;
		age = (DateUtil.monthsBetweenYM(birthYM, yearMonth)) / 12;
		return age;
	}



	/**
	 * 将月转换为多少岁
	 */
	public static String retireAge(int month) {
		int year = month / 12;
		String yearAndMonth = year + "岁";
		return yearAndMonth;
	}

	/**
	 * 判断是否是字母
	 * @param ascii int
	 * @return boolean
	 */
	private static boolean isAlphabet(int ascii) {
		if (ascii > 0 && ascii < 160) {
			return true;
		} else {
			return false;
		}
	}

}