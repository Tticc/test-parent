package com.tester.testercommon.util;

import java.util.regex.Pattern;

public class PasswordUtil {
	private static final Pattern SP_CHAR = Pattern.compile(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"`=]+.*");
	private static final Pattern LOW_LETTER = Pattern.compile(".*[a-z]+.*");
	private static final Pattern UP_LETTER = Pattern.compile(".*[A-Z]+.*");
	private static final Pattern NUMBER = Pattern.compile(".*[0-9]+.*");
	private static final Pattern LENGTH = Pattern.compile(".{8,16}");
	public static final Pattern PASSWORD_RULE_REGX = Pattern.compile("(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"`=])[a-zA-Z0-9\\S]{8,}$");

	/**
	 * 密码规则校验。推荐使用
	 * @Date 9:35 2021/8/24
	 * @Author 温昌营
	 **/
	public static boolean checkPwd(String pwd) {
		return PASSWORD_RULE_REGX.matcher(pwd).matches();
	}

	/**
	 * 密码规则校验
	 * @Date 9:35 2021/8/24
	 * @Author 温昌营
	 **/
	public static boolean checkPwd2(String pwd) {
		return SP_CHAR.matcher(pwd).matches()
				&& LOW_LETTER.matcher(pwd).matches()
				&& UP_LETTER.matcher(pwd).matches()
				&& NUMBER.matcher(pwd).matches()
				&& LENGTH.matcher(pwd).matches();
	}

}
