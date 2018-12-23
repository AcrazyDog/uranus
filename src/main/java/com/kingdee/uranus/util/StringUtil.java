package com.kingdee.uranus.util;

import java.util.List;
import static java.lang.Character.UnicodeBlock.*;

import com.google.common.base.Strings;

/**
 * <p>
 * 
 * </p>
 * 
 * @author rd_kang_nie
 * @date 2018年6月15日 上午10:12:00
 * @version
 */
public class StringUtil {

	public static String readLinesToString(List<String> lines) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines.size(); i++) {
			sb.append(lines.get(i) + "\n");
		}
		return sb.toString();
	}

	public static boolean checkStringContainChinese(String checkStr) {
		if (!Strings.isNullOrEmpty(checkStr)) {
			char[] checkChars = checkStr.toCharArray();
			for (int i = 0; i < checkChars.length; i++) {
				char checkChar = checkChars[i];
				if (checkCharContainChinese(checkChar)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean checkCharContainChinese(char checkChar) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(checkChar);
		if (CJK_UNIFIED_IDEOGRAPHS == ub || CJK_COMPATIBILITY_IDEOGRAPHS == ub || CJK_COMPATIBILITY_FORMS == ub
				|| CJK_RADICALS_SUPPLEMENT == ub || CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub
				|| CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B == ub) {
			return true;
		}
		return false;
	}
}
