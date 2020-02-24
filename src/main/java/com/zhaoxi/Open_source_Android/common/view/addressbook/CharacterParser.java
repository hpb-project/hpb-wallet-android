package com.zhaoxi.Open_source_Android.common.view.addressbook;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 中文转拼音转换类
 * @author J
 *
 */
public class CharacterParser {

	private static CharacterParser characterParser = new CharacterParser();

	public static CharacterParser getInstance() {
		return characterParser;
	}

	public static StringBuffer sb = new StringBuffer();

	/**
	 * 获取汉字字符串的首字母，英文字符不变
	 * 例如：阿飞→af
	 */
	public static String getPinYinHeadChar(String chines) {
		sb.setLength(0);
		char[] chars = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] > 128) {
				try {
					sb.append(PinyinHelper.toHanyuPinyinStringArray(chars[i], defaultFormat)[0].charAt(0));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				sb.append(chars[i]);
			}
		}
		return sb.toString();
	}

}
