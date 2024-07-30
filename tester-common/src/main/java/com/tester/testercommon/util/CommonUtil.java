package com.tester.testercommon.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tester.base.dto.exception.BusinessException;
import org.springframework.lang.NonNull;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.UUID;

public class CommonUtil {


	/**
	 * 与类似。JSON.toJSONString(T t);<br/>
	 * 不过可以是@JSONProperty注解起作用,而JSON.toJSONString不能
	 *  @JsonProperty("Account")
	 *     private String account;
	 * @param data
	 * @return java.lang.String
	 * @Date 18:53 2021/1/20
	 * @Author 温昌营
	 **/
	private static <T> String formatDataToJson(T data) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(data);
	}



	public static String upperFirstLatter(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1);
	}
	public static String lowerFirstLatter(String str){
		return str.substring(0, 1).toLowerCase()+str.substring(1);
	}


	/**
	 * 获取汉字拼音首字母
	 * @param characters
	 * @return java.lang.String
	 * @Date 16:35 2020/12/4
	 * @Author 温昌营
	 **/
	public static String getSpells(String characters) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < characters.length(); ++i) {
			char ch = characters.charAt(i);
			if (ch >> 7 == 0) {
				sb.append(ch);
			} else {
				Character spell = getFirstLetter(ch);
				sb.append(spell);
			}
		}
		return sb.toString();
	}
	private static Character getFirstLetter(char ch) {
		byte[] uniCode;
		try {
			uniCode = String.valueOf(ch).getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return uniCode[0] < 128 && uniCode[0] > 0 ? null : convert(uniCode);
	}
	private static char convert(byte[] bytes) {
		int[] secPosValueList = new int[]{1601, 1637, 1833, 2078, 2274, 2302, 2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027, 4086, 4390, 4558, 4684, 4925, 5249, 5600};
		char[] firstLetter = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x', 'y', 'z'};
		char result = '#';
		int i;
		for(i = 0; i < bytes.length; ++i) {
			bytes[i] = (byte)(bytes[i] - 160);
		}
		int secPosValue = bytes[0] * 100 + bytes[1];

		for(i = 0; i < 23; ++i) {
			if (secPosValue >= secPosValueList[i] && secPosValue < secPosValueList[i + 1]) {
				result = firstLetter[i];
				break;
			}
		}
		return result;
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
		return s.substring(0, 1).toLowerCase()+s.substring(1);
	}

	/**
	 * 将首字母转为大写。
	 * @author Wen, Changying
	 * @param s
	 * @return
	 * @date 2021-3-8 10:27:03
	 */
	public static String toUpperCaseFirstOne(@NonNull String s) {
		if(Character.isUpperCase(s.charAt(0))) return s;
		return s.substring(0, 1).toUpperCase()+s.substring(1);
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-","");
	}

	/**
	 * spring事务完成后执行consumer
	 * @param consumer
	 * @return void
	 * @Date 16:10 2020/12/2
	 * @Author 温昌营
	 **/
	public static void doAfterTransactionCommitted(MyConsumer<Void> consumer) throws BusinessException {
		if (TransactionSynchronizationManager.isActualTransactionActive()) {
			TransactionSynchronizationManager.registerSynchronization(
					new TransactionSynchronizationAdapter() {
						@Override
						public void afterCommit() {
							try {
								consumer.accept(null);
							} catch (BusinessException e) {
								e.printStackTrace();
							}
						}
					});
		}else {
			consumer.accept(null);
		}
	}


	public static String getFileSuffix(String fileName) {
		if (fileName != null && !"".equals(fileName.trim())) {
			int pointIndex = fileName.lastIndexOf(".");
			return pointIndex == -1 ? "" : fileName.substring(pointIndex).replace(".", "");
		} else {
			return "";
		}
	}

	/**
	 * 由于getMethod只能获取本类和父类、接口的public<br/>
	 * 而getDeclaredMethod只能获取本类的private/default/protected/public方法<br/>
	 * 因此都不能获取父类的私有方法<br/>
	 * 因此用这个方法来获取。通过递归getSuperclass的方式获取到最顶层
	 * @param clazz
	 * @param method
	 * @param paramClz
	 * @return
	 */
	public static Method getTargetMethod(Class clazz, String method, Class<?> ...paramClz){
		Method targetMethod = null;
		for (; targetMethod == null || clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				targetMethod = clazz.getDeclaredMethod(method, paramClz);
				targetMethod.setAccessible(true);
			} catch (Exception e) {
			}
		}
		return targetMethod;
	}
}
