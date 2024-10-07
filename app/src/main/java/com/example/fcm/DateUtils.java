package com.example.fcm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DateUtils {
    public static List<String> getDatesOfMonth(int month, int year) {
        List<String> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);

        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int day = 1; day <= daysInMonth; day++) {
            calendar.set(year, month, day);
            String date = String.format("%02d-%s-%d", day, getMonthName(month), year);
            dates.add(date);
        }
        return dates;
    }

    private static String getMonthName(int month) {
        String[] monthNames = {
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        };
        return monthNames[month];
    }
}
