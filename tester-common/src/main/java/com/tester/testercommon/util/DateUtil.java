package com.tester.testercommon.util;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {


	public static Date getNow(){
		return getDateFromLocalDateTime(LocalDateTime.now());
	}
	/**
	 * 获取当天起始时间 2019-11-24 00:00:00
	 * @param
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2019/11/25
	 */
	public static Date getTodayStart(){
		LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
		return getDateFromLocalDateTime(startOfToday);
	}
	/**
	 * 获取明天起始时间 2019-11-24 00:00:00
	 * @param
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2019/11/25
	 */
	public static Date getTomorrowStart(){
		LocalDateTime startOfToday = LocalDate.now().plusDays(1).atStartOfDay();
		return getDateFromLocalDateTime(startOfToday);
	}
	/**
	 * 获取昨天起始时间 2019-11-24 00:00:00
	 * @param
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2019/11/25
	 */
	public static Date getYesterdayStart(){
		LocalDateTime startOfToday = LocalDate.now().minusDays(1).atStartOfDay();
		return getDateFromLocalDateTime(startOfToday);
	}

	/**
	 * 获取本月起始时间 2019-11-01 00:00:00
	 * @param
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2019/11/25
	 */
	public static Date getMonthStart(){
		LocalDateTime startOfThisMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		return getDateFromLocalDateTime(startOfThisMonth);
	}
	/**
	 * 获取下一个月月初。
	 * @param
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2020/1/7
	 */
	public static Date getNextMonthStart(){
		LocalDateTime startOfNextMonth = LocalDate.now().plusMonths(1).atStartOfDay();
		return getDateFromLocalDateTime(startOfNextMonth);
	}

	/**
	 * 获取本周起始时间 2019-11-24 00:00:00。设定周一为每周起始
	 * @param
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2019/11/25
	 */
	public static Date getWeekStart(){
		LocalDateTime startOfThisWeek = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
		return getDateFromLocalDateTime(startOfThisWeek);
	}

	/**
	 * 获取n个月前的月初 - start of day
	 * @param months
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2019/11/25
	 */
	public static Date getMonthsAgoStartOfMonth(int months){
		LocalDateTime monthsAgo = LocalDate.now().minusMonths(months).withDayOfMonth(1).atStartOfDay();
		return getDateFromLocalDateTime(monthsAgo);
	}
	/**
	 * 获取n个月前的月末尾 - start of day
	 * @param months
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2019/11/25
	 */
	public static Date getMonthsAgoEndOfMonth(int months){
		LocalDateTime monthsAgo = LocalDate.now().minusMonths((long)months-1).withDayOfMonth(1).minusDays(1).atStartOfDay();
		return getDateFromLocalDateTime(monthsAgo);
	}
	/**
	 * 获取n个月前的今天 - start of day
	 * @param months
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2020/1/7
	 */
	public static Date getMonthsAgoStartOfDay(int months){
		LocalDateTime monthsAgo = LocalDate.now().minusMonths(months).atStartOfDay();
		return getDateFromLocalDateTime(monthsAgo);
	}
	/**
	 * 获取n周前的今天
	 * @param weeks
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2019/11/25
	 */
	public static Date getWeeksAgoStartOfDay(int weeks){
		LocalDateTime weeksAgo = LocalDate.now().minusWeeks(weeks).atStartOfDay();
		return getDateFromLocalDateTime(weeksAgo);
	}
	/**
	 * 获取n天前
	 * @param days
	 * @return java.util.Date
	 * @throws
	 * @author 温昌营
	 * @date 2020-6-28
	 */
	public static Date getDaysAgoStartOfDay(int days){
		LocalDateTime weeksAgo = LocalDate.now().minusDays(days).atStartOfDay();
		return getDateFromLocalDateTime(weeksAgo);
	}

	/** yyyyMMdd，例如：20200302*/
	public static LocalDate getLocalDate(String dateStr){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		return LocalDate.parse(dateStr, formatter);
	}

	/** yyyyMMddHHmmss，例如：20200302121212*/
	public static LocalDateTime getLocalDateTime(String dateStr){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		return LocalDateTime.parse(dateStr, formatter);
	}
	/**
	 * 格式化日期
	 * @param date
	 * @return java.lang.String
	 * @throws
	 * @author 温昌营
	 * @date 2020/1/7
	 */
	public static String dateFormat(Date date) {
		return dateFormat(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 格式化日期
	 * @param date
	 * @return java.lang.String
	 * @throws
	 * @author 温昌营
	 * @date 2020/1/7
	 */
	public static String dateFormat(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	public static Date getDateFromLocalDateTime(LocalDateTime localDateTime){
		ZoneId zoneId = ZoneId.systemDefault();
		return Date.from(localDateTime.atZone(zoneId).toInstant());
	}
}
