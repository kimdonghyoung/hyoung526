package util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ComUtil {
	public static String getDate(){
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		Date current = new Date();
		String date = formater.format(current);
		Calendar cal = new GregorianCalendar();
		int mHour = cal.get(Calendar.HOUR_OF_DAY);
		int mMinute = cal.get(Calendar.MINUTE);
		int mSecond = cal.get(Calendar.SECOND);
		int mMiliSecond = cal.get(Calendar.MILLISECOND);
		String nowDate = date +" "+ mHour+":"+ mMinute+":"+ mSecond+"."+ mMiliSecond;
		return nowDate;
	}
}
