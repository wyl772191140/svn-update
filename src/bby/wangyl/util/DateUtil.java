
package bby.wangyl.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间工具类
 * @author Administrator
 *
 */
public class DateUtil {
		
	public final static DateFormat YYYY_MM_DD_MM_HH_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public final static DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	public final static DateFormat YYYYMMDDMMHHSSSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	public final static DateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

	public static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	
	public static boolean isValidDate(String date) {
		boolean convertSuccess = true;
		// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
		try {
			// 设置lenient为false.
			// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
			YYYY_MM_DD_MM_HH_SS.setLenient(false);
			YYYY_MM_DD_MM_HH_SS.parse(date);
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		}
		return convertSuccess;
	}

	/**
	 * 时间转换为yyyy-MM-dd HH:mm:ss格式的字符串
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date){
		return YYYY_MM_DD_MM_HH_SS.format(date);
	}
	
	public static Date strToDate(String dateString){
		Date date = null;
		try {
			date = YYYY_MM_DD_MM_HH_SS.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date strToYYMMDDDate(String dateString){
		Date date = null;
		try {
			date = YYYY_MM_DD.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 计算两个时间之间相差的天数
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long diffDays(Date startDate,Date endDate){
		long days = 0;
		long start = startDate.getTime();
		long end = endDate.getTime();
		//一天的毫秒数1000 * 60 * 60 * 24=86400000
		days = (end - start) / 86400000;
		return days;
	}
	
	/**
	 * 日期加上月数的时间
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date dateAddMonth(Date date,int month){
		return add(date,Calendar.MONTH,month);
	}
	
	/**
	 * 日期加上天数的时间
	 * @param date
	 * @param month
	 * @return
	 */
	public static Date dateAddDay(Date date,int day){
		return add(date,Calendar.DAY_OF_YEAR,day);
	}
	
	/**
	 * 日期加上年数的时间
	 * @param date
	 * @param year
	 * @return
	 */
	public static Date dateAddYear(Date date,int year){
		return add(date,Calendar.YEAR,year);
	}
	
	 /** 
     * 计算剩余时间 (多少天多少时多少分)
     * @param startDateStr 
     * @param endDateStr 
     * @return 
     */  
    public static String remainDateToString(Date startDate, Date endDate){  
    	StringBuilder result = new StringBuilder();
    	if(endDate == null ){
    		return "过期";
    	}
    	long times = endDate.getTime() - startDate.getTime();
    	if(times < -1){
    		result.append("过期");
    	}else{
    		long temp = 1000 * 60 * 60 *24;
    		//天数
    		long d = times / temp;

    		//小时数
    		times %= temp;
    		temp  /= 24;
    		long m = times /temp;
    		//分钟数
    		times %= temp;
    		temp  /= 60;
    		long s = times /temp;
    		
    		result.append(d);
    		result.append("天");
    		result.append(m);
    		result.append("小时");
    		result.append(s);
    		result.append("分");
    	}
    	return result.toString();
    }  
    
	public static Date add(Date date,int type,int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, value);
		return calendar.getTime();
	}
	
	/**
	 * 时间转换为时间戳
	 * @param format
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getTimeCur(String format,String date) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return  sf.parse(sf.format(date)).getTime();
	}
	/**
	 * 时间转换为时间戳
	 * @param format
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getTimeCur(String format,Date date) throws ParseException{
		SimpleDateFormat sf = new SimpleDateFormat(format);
		return  sf.parse(sf.format(date)).getTime();
	}
	
	/**
	 * 将时间戳转为字符串 
	 * @param cc_time
	 * @return
	 */
	public static String getStrTime(String cc_time) { 
	 String re_StrTime = null; 
	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss"); 
	 long lcc_time = Long.valueOf(cc_time); 
	 re_StrTime = sdf.format(new Date(lcc_time * 1000L)); 
	 return re_StrTime; 
	 } 
	
	
	/**
	 * 
	 * @description 日期字符串
	 * @param date 日期
	 * @param pattern 格式
	 * @return
	 * @author Shichao.Lu
	 * @date 2015-9-11 下午05:43:03
	 */
	public static String getDateStr(Date date, String pattern) {
		return (new SimpleDateFormat(pattern)).format(date);
	}
	
	/**
	 * 
	 * @description 获得本月第一天0点时间
	 * @author Shichao.Lu
	 * @date 2015-11-24 下午07:22:24
	 * @return
	 */
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return  cal.getTime();
	}

	/**
	 * 
	 * @description 获得本月最后一天24点时间
	 * @author Shichao.Lu
	 * @date 2015-11-24 下午07:22:32
	 * @return
	 */
	public static Date getTimesMonthnight() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 24);
		return cal.getTime();
	}
	
	/**
	 * 
	 *@Description 获取几天前的时间
	 *@author Shichao.Lu
	 *@date 2016-1-21
	 */
	public static Date queryDateBefore(int day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DATE, cal.get(Calendar.DATE)-day);
		return cal.getTime();
	}
	
	/**
	 * 获取当月第一天的日期
	 * @return
	 */
	public static Date getBeginCurrMonth(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	/**
	 * 获取月第一天的日期
	 * 
	 * @return
	 */
	public static Date getBeginDateOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 获取月最后一天的日期
	 * 
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		return cal.getTime();
	}
	
	/**
	 * 获取当前day
	 * @return
	 */
	public static int getCurrentDay(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		String day = sdf.format(new Date());
		return Integer.parseInt(day);
	}
	
	/**
	 * 获取当前月份最后一天日期
	 * @return
	 */
	public static Date getLastDateOfMonth(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		return cal.getTime();
	}
	
	/**
	 * 获取当前月份最后一天，也是月总天数
	 * @return
	 */
	public static int getLastDayOfMonth(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		String day = sdf.format(cal.getTime());
		return Integer.parseInt(day);
	}
	
	
	/**
	 * 获取指定格式前缀的当前日期字符串：目的是生成请求流水号
	 * @param secifiedFormatPreffix 指定格式的前缀
	 * @param format 日期格式
	 * @return
	 */
	public static String getSpecifiedFormatCurDateStr(String secifiedFormatPreffix,String format) {
		StringBuffer dateBuf = new StringBuffer();
		dateBuf.append(secifiedFormatPreffix)
			   .append(DateUtil.getDateStr(new Date(), format));
		return dateBuf.toString();
	}
}
