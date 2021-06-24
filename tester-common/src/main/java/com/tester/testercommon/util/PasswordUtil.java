package com.tester.testercommon.util;

import java.util.regex.Pattern;

public class PasswordUtil {
	private static Pattern SP_CHAR = Pattern.compile(".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"`=]+.*");
	private static Pattern LOW_LETTER = Pattern.compile(".*[a-z]+.*");
	private static Pattern UP_LETTER = Pattern.compile(".*[A-Z]+.*");
	private static Pattern NUMBER = Pattern.compile(".*[0-9]+.*");
	private static Pattern LENGTH = Pattern.compile(".{8,16}");

	public static boolean checkPwd(String pwd) {
		return SP_CHAR.matcher(pwd).matches()
				&& LOW_LETTER.matcher(pwd).matches()
				&& UP_LETTER.matcher(pwd).matches()
				&& NUMBER.matcher(pwd).matches()
				&& LENGTH.matcher(pwd).matches();
	}

}
