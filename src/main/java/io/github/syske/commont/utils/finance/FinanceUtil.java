package io.github.syske.commont.utils.finance;

import io.github.syske.commont.utils.other.CommonUtil;

import java.util.StringTokenizer;

public class FinanceUtil {
    /**
     * 金额转换为大写人民币
     * @param  ind_amt   金额
     * @return String    大写人民币
     */
    public static String moneyToChinese(double ind_amt) {
        //int	 i ;
        int li_lendec;
        int li_lenint;
        int li_amout_flag = 0;
        int li_dec_length = 2;
        String ls_numstr;
        String a;
        String b;
        String c;
        String d;
        String bbak = "";
        String[] ls_dxint = new String[13];
        String[] ls_dxdec = new String[2];
        String[] ls_sz = new String[13];
        String ls_dxstr = "万仟佰拾亿仟佰拾万仟佰拾元";
        String ls_szstr = "零壹贰叁肆伍陆柒捌玖";

        if (ind_amt == 0) {
            ls_dxstr = "零元整";
            return ls_dxstr;
        } else {
            if (ind_amt < 0) {
                ind_amt = Math.abs(ind_amt); //取绝对值
                li_amout_flag = 1;
            }
        }
        //格式数据为 ######0.00 格式
        ls_numstr = CommonUtil.format(ind_amt, "0.00");
        li_lenint = ls_numstr.substring(0, ls_numstr.indexOf(".")).length();
        //截取小数点最后两位
        if (ls_numstr.substring(ls_numstr.indexOf(".") + 1, ls_numstr.length()).
                equals("00")) {
            li_lendec = 0;
        } else {
            li_lendec = 2;
            if (ls_numstr.substring(ls_numstr.indexOf(".") + 2, ls_numstr.length()).
                    equals("0")) {
                li_dec_length = 1;
            }
        }

        for (int i = 1; i <= 13; i++) {
            //ls_dxint[i]=mid(ls_dxstr,26 - i*2+1,2)
            //System.out.println(ls_dxstr.substring( 13 - i ,13 - i + 1 ));
            ls_dxint[i - 1] = ls_dxstr.substring(13 - i, 13 - i + 1);
            if (i < 11) {
                //ls_sz[i]=mid(ls_szstr,i*2 - 1,2)
                ls_sz[i - 1] = ls_szstr.substring(i - 1, i);
            } else {
                ls_sz[i - 1] = " ";
            }
        }
        ls_dxdec[0] = "角";
        ls_dxdec[1] = "分";
        ls_dxstr = " ";

        for (int y = 1; y <= li_lenint; y++) {
            a = ls_numstr.substring(0, li_lenint).substring(y - 1, y);
            b = ls_sz[Integer.parseInt(a)];
            c = ls_dxint[li_lenint - y];
            if (!ls_dxstr.equals(" ")) {
                d = ls_dxstr.substring(ls_dxstr.length() - 1, ls_dxstr.length());
            } else {
                d = "";
            }
            if (b.equals("零") &&
                    (d.equals("零") || b.equals(bbak) || c.equals("元") ||
                            c.equals("万") || c.equals("亿"))) {
                b = "";
            }
            if (a.equals("0") && !c.equals("元") && !c.equals("万") && !c.equals("亿")) {
                c = "";
            }
            if ((c.equals("元") || c.equals("万") || c.equals("亿")) && d.equals("零") &&
                    a.equals("0")) {
                ls_dxstr = ls_dxstr.substring(0, ls_dxstr.length() - 1);
                d = ls_dxstr.substring(ls_dxstr.length() - 1, ls_dxstr.length());
                if (c.equals("元") && d.equals("万") || c.equals("万") && d.equals("亿")) {
                    c = "";
                }
            }
            ls_dxstr = ls_dxstr + b + c;
            bbak = b;
        }

        for (int z = 1; z <= li_lendec; z++) {
            a = ls_numstr.substring(li_lenint + 1, li_lenint + li_lendec + 1).
                    substring(z - 1, z);
            if (a.equals("0") && ls_dxdec[z - 1].equals("分")) {
                b = "";
            } else {
                b = ls_sz[Integer.parseInt(a)];
            }
            if (!a.equals("0")) {
                ls_dxstr = ls_dxstr + b + ls_dxdec[z - 1];
            } else {
                ls_dxstr = ls_dxstr + b;
            }
        }

        if (li_lendec == 0 || li_dec_length == 1) {
            ls_dxstr = ls_dxstr + "整";
        }
        if (ind_amt < 1.00) {
            ls_dxstr = "零" + ls_dxstr.trim();
        }
        if (li_amout_flag == 1) {
            ls_dxstr = "负" + ls_dxstr;
        }

        return ls_dxstr;
    }

    /**
     * 金额转换为大写人民币
     * @param  sumofcash 金额
     * @return String    大写人民币
     */
    public static String moneyToChinese2(double sumofcash) {
        String[] arr = {"分", "角", "元", "拾", "百", "千", "万", "拾万", "百万",
                "千万", "亿", "拾亿", "百亿", "千亿"};
        String sTmp = "";
        String sMoney = "";
        String header = "";
        if (sumofcash < 0) {
            header = "负";
        }
        sumofcash = Math.abs(sumofcash);
        double dTmp = sumofcash * 100;
        sMoney = CommonUtil.format(dTmp, "0,000.#");
        StringTokenizer st = new StringTokenizer(sMoney, ",");
        String tmp = "";
        while (st.hasMoreElements()) {
            tmp = tmp + st.nextToken();
        }
        sMoney = tmp;
        int iLen = sMoney.length();
        int count = 0;
        for (int i = 0; i < iLen; i++) {
            String sTemp = sMoney.substring(i, i + 1);
            if (sTemp.equals("0")) {
                count++;
                if (count == 1) {
                    sTmp = sTmp + trans(sTemp);
                }
            } else {
                sTmp = sTmp + trans(sTemp);
                sTmp = sTmp + arr[iLen - i - 1];
                count = 0;
            }
        }
        sTmp = sTmp.trim();
        iLen = sTmp.length();
        if (sTmp.substring(iLen - 1, iLen).equals("零")) {
            sTmp = sTmp.substring(0, iLen - 1);
        }
        sTmp = sTmp + "整";
        sTmp = header.concat(sTmp);
        return sTmp;
    }

    private static String trans(String args) {
        int iTemp = Integer.parseInt(args);
        String sRes = "";
        switch (iTemp) {
            case 1:
                sRes = "壹";
                break;
            case 2:
                sRes = "贰";
                break;
            case 3:
                sRes = "叁";
                break;
            case 4:
                sRes = "肆";
                break;
            case 5:
                sRes = "伍";
                break;
            case 6:
                sRes = "陆";
                break;
            case 7:
                sRes = "柒";
                break;
            case 8:
                sRes = "捌";
                break;
            case 9:
                sRes = "玖";
                break;
            case 0:
                sRes = "零";
        }
        return sRes;
    }

    /**
     * 金额转换为大写人民币
     * @param  num       金额
     * @return String    大写人民币
     */
    public static String moneyToChinese3(double num) {
        int li_amout_flag = 1;
        if (num < 0) {
            num = Math.abs(num); //取绝对值
            li_amout_flag = -1;
        }
        String s_num, s_char, s_num_hz;
        int i_len_of_snum, i, i_flag, i_char;
        boolean flag = false;
        i_flag = 1;
        //NumberFormatter numberFormatter = new NumberFormatter();
        //s_num = numberFormatter.formatDouble(num);
        s_num = CommonUtil.format(num, "0.00");
        if (s_num.indexOf(".") == -1) {
            s_num = s_num + ".00";
        }
        if (s_num.equals("0.00")) {
            return "零元整";
        }
        s_num_hz = "";
        i_len_of_snum = s_num.length();
        for (int k = i_len_of_snum; k > 0; k--) {
            s_char = s_num.substring(k - 1, k);
            if (s_char.equals(".")) {
                s_char = "元";
            } else {
                i_char = Integer.parseInt(s_char);
                switch (i_char) {
                    case 0:
                        s_char = "零";
                        break;
                    case 1:
                        s_char = "壹";
                        break;
                    case 2:
                        s_char = "贰";
                        break;
                    case 3:
                        s_char = "叁";
                        break;
                    case 4:
                        s_char = "肆";
                        break;
                    case 5:
                        s_char = "伍";
                        break;
                    case 6:
                        s_char = "陆";
                        break;
                    case 7:
                        s_char = "柒";
                        break;
                    case 8:
                        s_char = "捌";
                        break;
                    case 9:
                        s_char = "玖";
                }
            }
            if (!s_char.equals("零")) {
                switch (i_len_of_snum - k) {
                    case 0:
                        s_char = s_char + "分";
                        break;
                    case 1:
                        s_char = s_char + "角";
                        break;
                    case 4:
                        s_char = s_char + "拾";
                        break;
                    case 5:
                        s_char = s_char + "佰";
                        break;
                    case 6:
                        s_char = s_char + "仟";
                        break;
                    case 7:
                        s_char = s_char + "万";
                        flag = true;
                        break;
                    case 8:
                        if (i_flag == 0) {
                            s_char = s_char + "拾万";
                        } else {
                            s_char = s_char + "拾";
                        }
                        break;
                    case 9:
                        if (i_flag == 0 && !flag) {
                            s_char = s_char + "佰万";
                            flag = true;
                            //s_char = s_char + "佰";
                        } else {
                            s_char = s_char + "佰";
                        }
                        break;
                    case 10:
                        if (i_flag == 0 && !flag) {
                            s_char = s_char + "仟万";
                            flag = true;
                            //s_char = s_char + "仟";
                        } else {
                            s_char = s_char + "仟";
                        }
                        break;
                    case 11:
                        s_char = s_char + "亿";
                }
                i_flag = 1;
            } else {
                i_flag = 0;
            }
            s_num_hz = s_char + s_num_hz;
        }
        i = s_num_hz.indexOf("零零");
        while (i != -1) {
            s_num_hz = s_num_hz.substring(0, i) + s_num_hz.substring(i + 1);
            i = s_num_hz.indexOf("零零");
        }
        boolean flag2 = false; //最后一位为零
        if (s_num_hz.substring(s_num_hz.length() - 1).equals("零")) {
            s_num_hz = s_num_hz.substring(0, s_num_hz.length() - 1); //+ "整";
            flag2 = true;
        }
        int idx = s_num_hz.indexOf("元");
        if (s_num_hz.substring(idx - 1, idx).equals("零")) {
            s_num_hz = s_num_hz.substring(0, idx - 1) + s_num_hz.substring(idx);
        }
        if (flag2) {
            s_num_hz = s_num_hz + "整";
        }

        if (li_amout_flag == -1) {
            s_num_hz = "负" + s_num_hz;
        }
        return s_num_hz;
    }
}
