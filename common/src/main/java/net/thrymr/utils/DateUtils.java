package net.thrymr.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class DateUtils {

    public static List<LocalDate> getDatesBetweenTwoDates(LocalDate startDate, LocalDate endDate) {
        LocalDate startDt = startDate;
        LocalDate endDt = endDate;
        List<LocalDate> dates = new ArrayList<>();
        while (!startDt.isAfter(endDt)) {
            dates.add(startDt);
            startDt = startDt.plusDays(1);
        }
        return dates;
    }

    public static List<LocalTime> getTimeRanges(LocalTime stTm, LocalTime endTm, int slotDuration) {
        LocalTime initial = stTm;
        LocalTime startTime = stTm;
        LocalTime endTime = endTm;
        List<LocalTime> timeRangeList = new ArrayList<>();
        while (!startTime.isBefore(initial) && startTime.isBefore(endTime)) {
            timeRangeList.add(startTime);
            startTime = startTime.plusMinutes(slotDuration);
        }
        return timeRangeList;
    }

    public static LocalDateTime toParseLocalDateTime(String ldt, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(ldt, formatter);
    }

    public static String toFormatLocalDateTime(LocalDateTime ldt, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return ldt.format(formatter);
    }

    public static LocalTime toParseLocalTime(String ldt, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalTime.parse(ldt, formatter);
    }

    public static String toFormatLocalTime(LocalTime ldt, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return ldt.format(formatter);
    }


    public static LocalDate toParseLocalDate(String ldt, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(ldt, formatter);
    }

    public static String toFormatLocalDate(LocalDate ldt, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return ldt.format(formatter);
    }

    public static String dateToString(Date date) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return simpleDateFormat.format(date);
        } else {
            return "";
        }
    }
    public static Date toFormatStringToDate(String ldt, String format) throws ParseException {

        return new SimpleDateFormat(format).parse(ldt);
    }

}
