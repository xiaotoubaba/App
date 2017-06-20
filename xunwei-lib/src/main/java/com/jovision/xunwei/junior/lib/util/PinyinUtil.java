package com.jovision.xunwei.junior.lib.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by xujiashuai@jovision.com on 2016/6/2.
 */
public class PinyinUtil {

    private static final HanyuPinyinOutputFormat format;

    static {
        format = new HanyuPinyinOutputFormat();
        // UPPERCASE：大写  (ZHONG)
        // LOWERCASE：小写  (zhong)
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        // WITHOUT_TONE：无音标  (zhong)
        // WITH_TONE_NUMBER：1-4数字表示英标  (zhong4)
        // WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        // WITH_V：用v表示ü  (nv)
        // WITH_U_AND_COLON：用"u:"表示ü  (nu:)
        // WITH_U_UNICODE：直接用ü (nü)
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    public static String getPinyin(String src) {
        if (src == null || src.length() <= 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (char ch : src.toCharArray()) {
            try {
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(ch, format);
                if (pinyin == null || pinyin.length <= 0) {//如果不是汉字，原样返回
                    sb.append(ch);
                } else if (pinyin.length == 1) {//单音字
                    sb.append(pinyin[0]);
                } else {//多音字，取第一个，很黄很暴力！
                    sb.append(pinyin[0]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String getPingYin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                if (Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else {
                    t4 += Character.toString(t1[i]);
                }
            }
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    public static String getHeadChar(String str) {

        String convert = "";
        char word = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
        if (pinyinArray != null) {
            convert += pinyinArray[0].charAt(0);
        } else {
            convert += word;
        }
        return convert.toUpperCase();
    }

    public static String getPinYinHeadChar(String str) {

        String convert = "";
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                convert += pinyinArray[0].charAt(0);
            } else {
                convert += word;
            }
        }
        return convert.toUpperCase();
    }


}
