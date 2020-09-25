package javaProject.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 折线图X轴日期补齐工具类
 * 
 * @author 56525
 *
 */
public class DateUtil {
	public static void main(String[] args) throws ParseException {
		// 数据库查询结果集
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map4 = new HashMap<String, Object>();
		map1.put("name", "微信");
		map1.put("time", "2015,2018");
		map1.put("data", "10,12");
		map2.put("name", "短信");
		map2.put("time", "2016,2017,2018");
		map2.put("data", "3,6,9");
		map4.put("name", "邮件");
		map4.put("time", "2015,2016");
		map4.put("data", "61,91");
		list.add(map1);
		list.add(map2);
		list.add(map4);
		// 入参
		String start = "2015";
		String end = "2018";
		try {
			System.out.println(polishDate("1", start, end, list));
		} catch (Exception e) {
			System.err.println(e.toString());
		}

	}

	/**
	 * 
	 * @param flag:查询格式1：年，2：月，3：日，4：当天每半小时统计一次
	 * @param start:开始时间
	 * @param end:截止时间
	 * @param list:查询结果集
	 * @return
	 * @throws ParseException
	 */
	public static List<Map<String, Object>> polishDate(String flag, String start, String end, List<Map<String, Object>> list)
			throws ParseException {
		String xData[] = null;
		String yData[] = null;
		// 统计每半个小时的数据
		if ("4".equals(flag)) {
			xData = new String[48];
			yData = new String[48];
		} else {
			// 间隔日期
			int sepaDate = getResult(flag, start, end);
			// 构造时间轴数组和数据轴数组用于返回值
			xData = new String[sepaDate + 1];
			yData = new String[sepaDate + 1];
		}

		// 构建最终返回的resultTime和datas，其中datas数组先把数据全部变为0，resultTime直接是所有日期
		String resultTime = "";
		String resultData = "";
		for (int i = 0; i < xData.length; i++) {
			xData[i] = startDate(flag, start, i);
			yData[i] = "0";
			if ("".equals(resultTime)) {
				resultTime = xData[i];
			} else {
				resultTime = resultTime + "," + xData[i];
			}
		}
		// 根据结果集条数进行循环
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map3 = null;
		for (int i = 0; i < list.size(); i++) {
			map3 = new HashMap<String, Object>();
			String name = list.get(i).get("name").toString();
			String time[] = list.get(i).get("time").toString().split(",");
			String data[] = list.get(i).get("data").toString().split(",");
			for (int j = 0; j < time.length; j++) {
				// 根据查询结果中的时间进行对比
				for (int k = 0; k < xData.length; k++) {
					if (time[j].equals(xData[k])) {
						yData[k] = data[j];
						// 结果相同跳出本次循环
						break;
					}
				}
			}
			// 根据datas以逗号形式拼接
			for (int j = 0; j < yData.length; j++) {
				if ("".equals(resultData)) {
					resultData = yData[j];
				} else {
					resultData = resultData + "," + yData[j];
				}
			}
			map3.put("name", name);
			map3.put("time", resultTime);
			map3.put("data", resultData);
			// 清空datas和resultData
			for (int j = 0; j < yData.length; j++) {
				yData[j] = "0";
			}
			resultData = "";
			resultList.add(map3);
		}
		return resultList;
	}

	/**
	 * 开始时间增加
	 * 
	 * @param start
	 * @return
	 * @throws ParseException
	 */
	public static String startDate(String flag, String start, int i) throws ParseException {
		try {
			start = start.replace("-", "");
			SimpleDateFormat sdf = null;
			Calendar rightNow = Calendar.getInstance();
			if ("1".equals(flag)) {
				sdf = new SimpleDateFormat("yyyy");
				Date dt = sdf.parse(start);
				rightNow.setTime(dt);
				rightNow.add(Calendar.YEAR, i);
			} else if ("2".equals(flag)) {
				sdf = new SimpleDateFormat("yyyyMM");
				Date dt = sdf.parse(start);
				rightNow.setTime(dt);
				rightNow.add(Calendar.MONTH, i);
			} else if ("3".equals(flag)) {
				sdf = new SimpleDateFormat("yyyyMMdd");
				Date dt = sdf.parse(start);
				rightNow.setTime(dt);
				rightNow.add(Calendar.DAY_OF_MONTH, i);
			} else {
				start = start + " 00:00:00";
				sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				Date dt = sdf.parse(start);
				rightNow.setTime(dt);
				for (int j = 0; j < i; j++) {
					rightNow.add(Calendar.MINUTE, 30);
				}
			}
			Date dt1 = rightNow.getTime();
			if ("4".equals(flag)) {
				start = sdf.format(dt1).substring(9, 14);
			} else {
				start = sdf.format(dt1);
			}
		} catch (Exception e) {
			System.out.println("处理开始时间错误:" + e.toString());
		}
		return start;
	}

	/**
	 * 获取两个日期的间隔时间
	 * 
	 * @param flag:1表示年，2表示月，3表示日
	 * @param startTime:开始时间
	 * @param endTime:截至时间
	 * @return
	 * @throws ParseException
	 */
	public static int getResult(String flag, String startTime, String endTime) throws ParseException {
		Date fromDate = new Date();
		Date toDate = new Date();
		SimpleDateFormat sdf = null;
		if ("1".equals(flag)) {
			sdf = new SimpleDateFormat("yyyy");
		} else if ("2".equals(flag)) {
			sdf = new SimpleDateFormat("yyyy-MM");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		fromDate = sdf.parse(startTime);
		toDate = sdf.parse(endTime);
		Calendar from = Calendar.getInstance();
		from.setTime(fromDate);
		Calendar to = Calendar.getInstance();
		to.setTime(toDate);
		// 开始时间年月
		int fromYear = from.get(Calendar.YEAR);
		// 截至时间年月
		int toYear = to.get(Calendar.YEAR);
		int fromMonth = 0;
		int toMonth = 0;
		if ("2".equals(flag)) {
			fromMonth = from.get(Calendar.MONTH) + 1;
			toMonth = to.get(Calendar.MONTH) + 1;
		}
		int year = toYear - fromYear;
		int month = toYear * 12 + toMonth - (fromYear * 12 + fromMonth);
		int day = (int) ((to.getTimeInMillis() - from.getTimeInMillis()) / (24 * 3600 * 1000));
		if ("1".equals(flag)) {
			return year;
		} else if ("2".equals(flag)) {
			return month;
		} else {
			return day;
		}
	}
}
