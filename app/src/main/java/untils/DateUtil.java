package untils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static String changeTime(String imgDate_Time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
        SimpleDateFormat convertDate = new SimpleDateFormat("MMMdd");
        SimpleDateFormat convertTime = new SimpleDateFormat("yyyy-MM-dd");

        Date d = null, d2 = null;

        try {
            d = simpleDateFormat.parse(imgDate_Time);
            d2 = simpleDateFormat.parse(imgDate_Time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String dateFormat = convertDate.format(d);
        String timeFormat = convertTime.format(d2);
        String wek = dateToWeek(timeFormat);
        int index = dateFormat.indexOf("月");
        String month = dateFormat;
        month = month.substring(0, index);
        String newMonth = dateToMonth(month);
        dateFormat = dateFormat.replace("月", " ");
        index = dateFormat.indexOf(" ");
        dateFormat = dateFormat.substring(index + 1, dateFormat.length());
        if (dateFormat.indexOf("0") == 0) {
            dateFormat = dateFormat.substring(1, 2);
        }
        dateFormat = newMonth.concat(" ").concat(dateFormat).concat("th").concat(" ").concat(wek);
        return dateFormat;
    }


    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String dateToMonth(String num) {
        String month;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        return months[Integer.valueOf(num) - 1];
    }
}
