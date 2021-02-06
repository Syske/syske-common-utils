package io.github.syske.commont.utils.other;


//java class

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * syske-common-utils
 *
 * @version 2006-09-11
 * @Description: 汉字工具类
 */

public class HanZiUtility {
    private static LinkedHashMap spellMap = null;

    static {
        if (spellMap == null) {
            spellMap = new LinkedHashMap(400);
        }
        initialize();
        System.out.println("Chinese transfer Spell Done.");
    }

    public HanZiUtility() {
    }

    /**
     * 获得汉字的拼音的首字母(字母与数字的值不变位置也不变  如：猪d八3戒345----ZdB4J345 其中ZBJ为首字母)
     *
     * @param str String 要转换的汉字
     *
     * @return String    与汉字对应的拼音的首字母
     */
    public static String getPYSZM(String str) {
        String string = "";
        try {
            string = new String(str.getBytes(), "GBK");
        } catch (UnsupportedEncodingException ex) {

        }
        StringBuffer resultBuf = new StringBuffer();
        String[] name = CommonUtil.splitStr(getFullSpell(string), "-");
        for (int i = 0; i < name.length; i++) {
            resultBuf.append("");
            //      if (65 <= name.charAt(i) && name.charAt(i) < 97)
            //	resultBuf.append(name.charAt(i));
            if (name[i].length() == 1) {
                resultBuf.append(name[i]);
            } else {
                resultBuf.append(name[i].charAt(0));
            }
        }
        return resultBuf.toString();
    }

    /**
     * 获得汉字的拼音(字母与数字的值不变位置也不变  如：猪d八3戒345----ZhudBa3Jie345 其中Zhu Ba Jie为拼音)
     *
     * @param str String 要转换的汉字
     *
     * @return String    与汉字对应的拼音的首字母
     */
    public static String getPY(String str) {
        String string = "";
        try {
            string = new String(str.getBytes(), "GBK");
        } catch (UnsupportedEncodingException ex) {

        }
        StringBuffer resultBuf = new StringBuffer();
        String[] name = CommonUtil.splitStr(getFullSpell(string), "-");
        for (int i = 0; i < name.length; i++) {
            resultBuf.append(name[i]);
        }
        return resultBuf.toString();
    }


    /**
     * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
     *
     * @param cnStr String
     *              字符串
     *
     * @return String
     * 转换成全拼后的字符串
     */
    public static String getFullSpell(String cnStr) {
        if (null == cnStr || "".equals(cnStr.trim())) {
            return cnStr;
        }

        boolean isChinese = false;

        char[] chars = cnStr.toCharArray();
        StringBuffer retuBuf = new StringBuffer();
        StringBuffer resultBuf = new StringBuffer();

        for (int i = 0, Len = chars.length; i < Len; i++) {
            int ascii = getCnAscii(chars[i]);
            if (ascii == 0) { //取ascii时出错
                retuBuf.append(chars[i]);
            } else {
                String spell = getSpellByAscii(ascii);
                if (spell == null) {
                    retuBuf.append(chars[i]);
                } else {
                    //System.out.println("spell:"+spell);
                    retuBuf.append(spell);
                }
                // end of if spell == null
                if (!isAlphabet(ascii)) {
                    isChinese = true;
                }
                if (i != chars.length - 1) {
                    retuBuf.append("-");
                }
            } // end of if ascii <= -20400

        } // end of for
        if (isChinese) {
            //resultBuf.append(cnStr);
            resultBuf.append(retuBuf.toString());
        } else {
            resultBuf.append(retuBuf.toString());
        }
        return resultBuf.toString();
    }

    /**
     * 获得单个汉字的Ascii.
     *
     * @param cn char
     *           汉字字符
     *
     * @return int
     * 错误返回 0,否则返回ascii
     */
    public static int getCnAscii(char cn) {
        byte[] bytes = null;
        try {
            bytes = (String.valueOf(cn)).getBytes("GBK");
        } catch (UnsupportedEncodingException ex) {
        }
        if (bytes == null || bytes.length > 2 || bytes.length <= 0) { //错误
            return 0;
        }
        if (bytes.length == 1) { //英文字符
            return bytes[0];
        }
        if (bytes.length == 2) { //中文字符
            int hightByte = 256 + bytes[0];
            int lowByte = 256 + bytes[1];
            int ascii = (256 * hightByte + lowByte) - 256 * 256;
            return ascii;
        }
        return 0; //错误
    }

    /**
     * 根据ASCII码到SpellMap中查找对应的拼音
     *
     * @param ascii int
     *              字符对应的ASCII
     *
     * @return String
     * 拼音,首先判断ASCII是否>0&<160,如果是返回对应的字符,
     * <p>
     * 否则到SpellMap中查找,如果没有找到拼音,则返回null,如果找到则返回拼音.
     */
    public static String getSpellByAscii(int ascii) {
        if (ascii > 0 && ascii < 160) { //单字符
            return String.valueOf((char) ascii);
        }

        if (ascii < -20319 || ascii > -10247) { //不知道的字符
            return null;
        }

        Set keySet = spellMap.keySet();
        Iterator it = keySet.iterator();

        String spell0 = null;
        ;
        String spell = null;

        int asciiRang0 = -20319;
        int asciiRang;
        while (it.hasNext()) {

            spell = (String) it.next();
            Object valObj = spellMap.get(spell);
            if (valObj instanceof Integer) {
                asciiRang = ((Integer) valObj).intValue();

                if (ascii >= asciiRang0 && ascii < asciiRang) { //区间找到
                    return (spell0 == null) ? spell : spell0;
                } else {
                    spell0 = spell;
                    asciiRang0 = asciiRang;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否是字母
     *
     * @param ascii int
     *
     * @return boolean
     */
    private static boolean isAlphabet(int ascii) {
        if (ascii > 0 && ascii < 160) {
            return true;
        } else {
            return false;
        }
    }

    private static void spellPut(String spell, int ascii) {
        spellMap.put(spell, new Integer(ascii));
    }

    private static void initialize() {
        spellPut("A", -20319);
        spellPut("Ai", -20317);
        spellPut("An", -20304);
        spellPut("Ang", -20295);
        spellPut("Ao", -20292);
        spellPut("Ba", -20283);
        spellPut("Bai", -20265);
        spellPut("Ban", -20257);
        spellPut("Bang", -20242);
        spellPut("Bao", -20230);
        spellPut("Bei", -20051);
        spellPut("Ben", -20036);
        spellPut("Beng", -20032);
        spellPut("Bi", -20026);
        spellPut("Bian", -20002);
        spellPut("Biao", -19990);
        spellPut("Bie", -19986);
        spellPut("Bin", -19982);
        spellPut("Bing", -19976);
        spellPut("Bo", -19805);
        spellPut("Bu", -19784);
        spellPut("Ca", -19775);
        spellPut("Cai", -19774);
        spellPut("Can", -19763);
        spellPut("Cang", -19756);
        spellPut("Cao", -19751);
        spellPut("Ce", -19746);
        spellPut("Ceng", -19741);
        spellPut("Cha", -19739);
        spellPut("Chai", -19728);
        spellPut("Chan", -19725);
        spellPut("Chang", -19715);
        spellPut("Chao", -19540);
        spellPut("Che", -19531);
        spellPut("Chen", -19525);
        spellPut("Cheng", -19515);
        spellPut("Chi", -19500);
        spellPut("Chong", -19484);
        spellPut("Chou", -19479);
        spellPut("Chu", -19467);
        spellPut("Chuai", -19289);
        spellPut("Chuan", -19288);
        spellPut("Chuang", -19281);
        spellPut("Chui", -19275);
        spellPut("Chun", -19270);
        spellPut("Chuo", -19263);
        spellPut("Ci", -19261);
        spellPut("Cong", -19249);
        spellPut("Cou", -19243);
        spellPut("Cu", -19242);
        spellPut("Cuan", -19238);
        spellPut("Cui", -19235);
        spellPut("Cun", -19227);
        spellPut("Cuo", -19224);
        spellPut("Da", -19218);
        spellPut("Dai", -19212);
        spellPut("Dan", -19038);
        spellPut("Dang", -19023);
        spellPut("Dao", -19018);
        spellPut("De", -19006);
        spellPut("Deng", -19003);
        spellPut("Di", -18996);
        spellPut("Dian", -18977);
        spellPut("Diao", -18961);
        spellPut("Die", -18952);
        spellPut("Ding", -18783);
        spellPut("Diu", -18774);
        spellPut("Dong", -18773);
        spellPut("Dou", -18763);
        spellPut("Du", -18756);
        spellPut("Duan", -18741);
        spellPut("Dui", -18735);
        spellPut("Dun", -18731);
        spellPut("Duo", -18722);
        spellPut("E", -18710);
        spellPut("En", -18697);
        spellPut("Er", -18696);
        spellPut("Fa", -18526);
        spellPut("Fan", -18518);
        spellPut("Fang", -18501);
        spellPut("Fei", -18490);
        spellPut("Fen", -18478);
        spellPut("Feng", -18463);
        spellPut("Fo", -18448);
        spellPut("Fou", -18447);
        spellPut("Fu", -18446);
        spellPut("Ga", -18239);
        spellPut("Gai", -18237);
        spellPut("Gan", -18231);
        spellPut("Gang", -18220);
        spellPut("Gao", -18211);
        spellPut("Ge", -18201);
        spellPut("Gei", -18184);
        spellPut("Gen", -18183);
        spellPut("Geng", -18181);
        spellPut("Gong", -18012);
        spellPut("Gou", -17997);
        spellPut("Gu", -17988);
        spellPut("Gua", -17970);
        spellPut("Guai", -17964);
        spellPut("Guan", -17961);
        spellPut("Guang", -17950);
        spellPut("Gui", -17947);
        spellPut("Gun", -17931);
        spellPut("Guo", -17928);
        spellPut("Ha", -17922);
        spellPut("Hai", -17759);
        spellPut("Han", -17752);
        spellPut("Hang", -17733);
        spellPut("Hao", -17730);
        spellPut("He", -17721);
        spellPut("Hei", -17703);
        spellPut("Hen", -17701);
        spellPut("Heng", -17697);
        spellPut("Hong", -17692);
        spellPut("Hou", -17683);
        spellPut("Hu", -17676);
        spellPut("Hua", -17496);
        spellPut("Huai", -17487);
        spellPut("Huan", -17482);
        spellPut("Huang", -17468);
        spellPut("Hui", -17454);
        spellPut("Hun", -17433);
        spellPut("Huo", -17427);
        spellPut("Ji", -17417);
        spellPut("Jia", -17202);
        spellPut("Jian", -17185);
        spellPut("Jiang", -16983);
        spellPut("Jiao", -16970);
        spellPut("Jie", -16942);
        spellPut("Jin", -16915);
        spellPut("Jing", -16733);
        spellPut("Jiong", -16708);
        spellPut("Jiu", -16706);
        spellPut("Ju", -16689);
        spellPut("Juan", -16664);
        spellPut("Jue", -16657);
        spellPut("Jun", -16647);
        spellPut("Ka", -16474);
        spellPut("Kai", -16470);
        spellPut("Kan", -16465);
        spellPut("Kang", -16459);
        spellPut("Kao", -16452);
        spellPut("Ke", -16448);
        spellPut("Ken", -16433);
        spellPut("Keng", -16429);
        spellPut("Kong", -16427);
        spellPut("Kou", -16423);
        spellPut("Ku", -16419);
        spellPut("Kua", -16412);
        spellPut("Kuai", -16407);
        spellPut("Kuan", -16403);
        spellPut("Kuang", -16401);
        spellPut("Kui", -16393);
        spellPut("Kun", -16220);
        spellPut("Kuo", -16216);
        spellPut("La", -16212);
        spellPut("Lai", -16205);
        spellPut("Lan", -16202);
        spellPut("Lang", -16187);
        spellPut("Lao", -16180);
        spellPut("Le", -16171);
        spellPut("Lei", -16169);
        spellPut("Leng", -16158);
        spellPut("Li", -16155);
        spellPut("Lia", -15959);
        spellPut("Lian", -15958);
        spellPut("Liang", -15944);
        spellPut("Liao", -15933);
        spellPut("Lie", -15920);
        spellPut("Lin", -15915);
        spellPut("Ling", -15903);
        spellPut("Liu", -15889);
        spellPut("Long", -15878);
        spellPut("Lou", -15707);
        spellPut("Lu", -15701);
        spellPut("Lv", -15681);
        spellPut("Luan", -15667);
        spellPut("Lue", -15661);
        spellPut("Lun", -15659);
        spellPut("Luo", -15652);
        spellPut("Ma", -15640);
        spellPut("Mai", -15631);
        spellPut("Man", -15625);
        spellPut("Mang", -15454);
        spellPut("Mao", -15448);
        spellPut("Me", -15436);
        spellPut("Mei", -15435);
        spellPut("Men", -15419);
        spellPut("Meng", -15416);
        spellPut("Mi", -15408);
        spellPut("Mian", -15394);
        spellPut("Miao", -15385);
        spellPut("Mie", -15377);
        spellPut("Min", -15375);
        spellPut("Ming", -15369);
        spellPut("Miu", -15363);
        spellPut("Mo", -15362);
        spellPut("Mou", -15183);
        spellPut("Mu", -15180);
        spellPut("Na", -15165);
        spellPut("Nai", -15158);
        spellPut("Nan", -15153);
        spellPut("Nang", -15150);
        spellPut("Nao", -15149);
        spellPut("Ne", -15144);
        spellPut("Nei", -15143);
        spellPut("Nen", -15141);
        spellPut("Neng", -15140);
        spellPut("Ni", -15139);
        spellPut("Nian", -15128);
        spellPut("Niang", -15121);
        spellPut("Niao", -15119);
        spellPut("Nie", -15117);
        spellPut("Nin", -15110);
        spellPut("Ning", -15109);
        spellPut("Niu", -14941);
        spellPut("Nong", -14937);
        spellPut("Nu", -14933);
        spellPut("Nv", -14930);
        spellPut("Nuan", -14929);
        spellPut("Nue", -14928);
        spellPut("Nuo", -14926);
        spellPut("O", -14922);
        spellPut("Ou", -14921);
        spellPut("Pa", -14914);
        spellPut("Pai", -14908);
        spellPut("Pan", -14902);
        spellPut("Pang", -14894);
        spellPut("Pao", -14889);
        spellPut("Pei", -14882);
        spellPut("Pen", -14873);
        spellPut("Peng", -14871);
        spellPut("Pi", -14857);
        spellPut("Pian", -14678);
        spellPut("Piao", -14674);
        spellPut("Pie", -14670);
        spellPut("Pin", -14668);
        spellPut("Ping", -14663);
        spellPut("Po", -14654);
        spellPut("Pu", -14645);
        spellPut("Qi", -14630);
        spellPut("Qia", -14594);
        spellPut("Qian", -14429);
        spellPut("Qiang", -14407);
        spellPut("Qiao", -14399);
        spellPut("Qie", -14384);
        spellPut("Qin", -14379);
        spellPut("Qing", -14368);
        spellPut("Qiong", -14355);
        spellPut("Qiu", -14353);
        spellPut("Qu", -14345);
        spellPut("Quan", -14170);
        spellPut("Que", -14159);
        spellPut("Qun", -14151);
        spellPut("Ran", -14149);
        spellPut("Rang", -14145);
        spellPut("Rao", -14140);
        spellPut("Re", -14137);
        spellPut("Ren", -14135);
        spellPut("Reng", -14125);
        spellPut("Ri", -14123);
        spellPut("Rong", -14122);
        spellPut("Rou", -14112);
        spellPut("Ru", -14109);
        spellPut("Ruan", -14099);
        spellPut("Rui", -14097);
        spellPut("Run", -14094);
        spellPut("Ruo", -14092);
        spellPut("Sa", -14090);
        spellPut("Sai", -14087);
        spellPut("San", -14083);
        spellPut("Sang", -13917);
        spellPut("Sao", -13914);
        spellPut("Se", -13910);
        spellPut("Sen", -13907);
        spellPut("Seng", -13906);
        spellPut("Sha", -13905);
        spellPut("Shai", -13896);
        spellPut("Shan", -13894);
        spellPut("Shang", -13878);
        spellPut("Shao", -13870);
        spellPut("She", -13859);
        spellPut("Shen", -13847);
        spellPut("Sheng", -13831);
        spellPut("Shi", -13658);
        spellPut("Shou", -13611);
        spellPut("Shu", -13601);
        spellPut("Shua", -13406);
        spellPut("Shuai", -13404);
        spellPut("Shuan", -13400);
        spellPut("Shuang", -13398);
        spellPut("Shui", -13395);
        spellPut("Shun", -13391);
        spellPut("Shuo", -13387);
        spellPut("Si", -13383);
        spellPut("Song", -13367);
        spellPut("Sou", -13359);
        spellPut("Su", -13356);
        spellPut("Suan", -13343);
        spellPut("Sui", -13340);
        spellPut("Sun", -13329);
        spellPut("Suo", -13326);
        spellPut("Ta", -13318);
        spellPut("Tai", -13147);
        spellPut("Tan", -13138);
        spellPut("Tang", -13120);
        spellPut("Tao", -13107);
        spellPut("Te", -13096);
        spellPut("Teng", -13095);
        spellPut("Ti", -13091);
        spellPut("Tian", -13076);
        spellPut("Tiao", -13068);
        spellPut("Tie", -13063);
        spellPut("Ting", -13060);
        spellPut("Tong", -12888);
        spellPut("Tou", -12875);
        spellPut("Tu", -12871);
        spellPut("Tuan", -12860);
        spellPut("Tui", -12858);
        spellPut("Tun", -12852);
        spellPut("Tuo", -12849);
        spellPut("Wa", -12838);
        spellPut("Wai", -12831);
        spellPut("Wan", -12829);
        spellPut("Wang", -12812);
        spellPut("Wei", -12802);
        spellPut("Wen", -12607);
        spellPut("Weng", -12597);
        spellPut("Wo", -12594);
        spellPut("Wu", -12585);
        spellPut("Xi", -12556);
        spellPut("Xia", -12359);
        spellPut("Xian", -12346);
        spellPut("Xiang", -12320);
        spellPut("Xiao", -12300);
        spellPut("Xie", -12120);
        spellPut("Xin", -12099);
        spellPut("Xing", -12089);
        spellPut("Xiong", -12074);
        spellPut("Xiu", -12067);
        spellPut("Xu", -12058);
        spellPut("Xuan", -12039);
        spellPut("Xue", -11867);
        spellPut("Xun", -11861);
        spellPut("Ya", -11847);
        spellPut("Yan", -11831);
        spellPut("Yang", -11798);
        spellPut("Yao", -11781);
        spellPut("Ye", -11604);
        spellPut("Yi", -11589);
        spellPut("Yin", -11536);
        spellPut("Ying", -11358);
        spellPut("Yo", -11340);
        spellPut("Yong", -11339);
        spellPut("You", -11324);
        spellPut("Yu", -11303);
        spellPut("Yuan", -11097);
        spellPut("Yue", -11077);
        spellPut("Yun", -11067);
        spellPut("Za", -11055);
        spellPut("Zai", -11052);
        spellPut("Zan", -11045);
        spellPut("Zang", -11041);
        spellPut("Zao", -11038);
        spellPut("Ze", -11024);
        spellPut("Zei", -11020);
        spellPut("Zen", -11019);
        spellPut("Zeng", -11018);
        spellPut("Zha", -11014);
        spellPut("Zhai", -10838);
        spellPut("Zhan", -10832);
        spellPut("Zhang", -10815);
        spellPut("Zhao", -10800);
        spellPut("Zhe", -10790);
        spellPut("Zhen", -10780);
        spellPut("Zheng", -10764);
        spellPut("Zhi", -10587);
        spellPut("Zhong", -10544);
        spellPut("Zhou", -10533);
        spellPut("Zhu", -10519);
        spellPut("Zhua", -10331);
        spellPut("Zhuai", -10329);
        spellPut("Zhuan", -10328);
        spellPut("Zhuang", -10322);
        spellPut("Zhui", -10315);
        spellPut("Zhun", -10309);
        spellPut("Zhuo", -10307);
        spellPut("Zi", -10296);
        spellPut("Zong", -10281);
        spellPut("Zou", -10274);
        spellPut("Zu", -10270);
        spellPut("Zuan", -10262);
        spellPut("Zui", -10260);
        spellPut("Zun", -10256);
        spellPut("Zuo", -10254);
    }

}
