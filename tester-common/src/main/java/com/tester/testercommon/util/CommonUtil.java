package com.tester.testercommon.util;

import org.springframework.lang.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class CommonUtil {

	/**
	 * 获取格式化的日期字符串
	 * @author Wen, Changying
	 * @return String
	 * @date 2019年8月30日
	 */
	public static String getDateString() {
		return new SimpleDateFormat("yyyy-mm-dd hh:mm:ss E").format(new Date());
	}

	/**
	 * 获取格式化的日期字符串
	 * @author Wen, Changying
	 * @param format 例如{@code "yyyy-mm-dd hh:mm:ss E"}
	 * @return String
	 * @date 2019年8月30日
	 */
	public static String getDateString(@NonNull String format) {
		return new SimpleDateFormat(format).format(new Date());
	}
	
	/**
	 * 将首字母转为小写。用来根据类名获取spring容器的bean。
	 * @author Wen, Changying
	 * @param s
	 * @return
	 * @date 2019年8月30日
	 */
	public static String toLowerCaseFirstOne(@NonNull String s) {
		if(Character.isLowerCase(s.charAt(0))) return s;
		return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
}
