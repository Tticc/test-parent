package com.tester.testercommon.util;

import com.tester.testercommon.exception.BusinessException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CommonUtil {


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

	public static void copyPropertiesIgnoreNull(Object source, Object target){
		copyPropertiesIgnoreNull(source,target,null, (String[]) null);
	}
	public static void copyPropertiesIgnoreNull(Object source, Object target, @Nullable Class<?> editable,
												@Nullable String... ignoreProperties) throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
						"] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null) {
					Method readMethod = sourcePd.getReadMethod();
					if (readMethod != null &&
							ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
						try {
							if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
								readMethod.setAccessible(true);
							}
							Object value = readMethod.invoke(source);
							// 空值不复制 - wenc
							if(null == value){
								continue;
							}
							// 空值不复制 - wenc
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, value);
						}
						catch (Throwable ex) {
							throw new FatalBeanException(
									"Could not copy property '" + targetPd.getName() + "' from source to target", ex);
						}
					}
				}
			}
		}
	}
}
