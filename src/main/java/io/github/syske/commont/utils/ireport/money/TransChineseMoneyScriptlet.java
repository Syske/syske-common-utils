package io.github.syske.commont.utils.ireport.money;


import net.sf.jasperreports.engine.JRAbstractScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

import java.math.BigDecimal;

/**
 * @program: TransChineseMoneyScriptlet
 * @description: 人民币大小写转换
 * @author: CaoLei
 * @create: 2019-12-30 17:30
 */
public class TransChineseMoneyScriptlet extends JRAbstractScriptlet {
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆",
            "伍", "陆", "柒", "捌", "玖"};

    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元",
            "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "兆", "拾",
            "佰", "仟"};
    private static final String CN_FULL = "整";
    private static final String CN_NEGATIVE = "负";
    private static final int MONEY_PRECISION = 2;
    private static final String CN_ZEOR_FULL = "零元整";

    public static String number2CNMontrayUnit(Double numberOfMoney) {
        StringBuffer sb = new StringBuffer();

        BigDecimal cnMoney = new BigDecimal(String.valueOf(numberOfMoney));
        int signum = cnMoney.signum();

        if (signum == 0) {
            return "零元整";
        }

        long number = cnMoney.movePointRight(2)
                .setScale(0, 4).abs().longValue();

        long scale = number % 100L;
        int numUnit = 0;
        int numIndex = 0;
        boolean getZero = false;

        if (scale <= 0L) {
            numIndex = 2;
            number /= 100L;
            getZero = true;
        }
        if ((scale > 0L) && (scale % 10L <= 0L)) {
            numIndex = 1;
            number /= 10L;
            getZero = true;
        }
        int zeroSize = 0;

        while (number > 0L) {
            numUnit = (int) (number % 10L);
            if (numUnit > 0) {
                if ((numIndex == 9) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[6]);
                }
                if ((numIndex == 13) && (zeroSize >= 3)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[10]);
                }
                sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                getZero = false;
                zeroSize = 0;
            } else {
                zeroSize++;
                if (!getZero) {
                    sb.insert(0, CN_UPPER_NUMBER[numUnit]);
                }
                if (numIndex == 2) {
                    if (number > 0L)
                        sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                } else if (((numIndex - 2) % 4 == 0) && (number % 1000L > 0L)) {
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[numIndex]);
                }
                getZero = true;
            }

            number /= 10L;
            numIndex++;
        }

        if (signum == -1) {
            sb.insert(0, "负");
        }

        if (scale <= 0L) {
            sb.append("整");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        double money = 2020004.01D;
        String s = number2CNMontrayUnit(Double.valueOf(money));
        System.out.println("===");
        System.out.println("你输入的金额为：【" + money + "】   #--# [" + s.toString() + "]");
    }

    public void afterColumnInit()
            throws JRScriptletException {
    }

    public void afterDetailEval()
            throws JRScriptletException {
    }

    public void afterGroupInit(String arg0)
            throws JRScriptletException {
    }

    public void afterPageInit()
            throws JRScriptletException {
    }

    public void afterReportInit()
            throws JRScriptletException {
    }

    public void beforeColumnInit()
            throws JRScriptletException {
    }

    public void beforeDetailEval()
            throws JRScriptletException {
    }

    public void beforeGroupInit(String arg0)
            throws JRScriptletException {
    }

    public void beforePageInit()
            throws JRScriptletException {
    }

    public void beforeReportInit()
            throws JRScriptletException {
    }
}