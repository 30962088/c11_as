package com.cctv.xiqu.android.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date getDate(String datetime){
		long count = Long.parseLong(datetime.replaceAll("\\/Date\\((.*)\\)\\/", "$1"));
		return new Date(count);
	}
	public static String getWeek(Date date){  
        String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;  
        if(week_index<0){  
            week_index = 0;  
        }   
        return weeks[week_index];  
    }
	
	public static String getPageTitle(Date date){
		String[] weeks = {"日","一","二","三","四","五","六"};  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;  
        if(week_index<0){  
            week_index = 0;  
        }   
        return weeks[week_index]+"\n"+cal.get(Calendar.DATE);  
	}
	
}
