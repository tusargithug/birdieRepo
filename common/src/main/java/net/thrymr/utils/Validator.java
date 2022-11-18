package net.thrymr.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Validator {

//    private static String dateFormat = Constants.DATE_FORMAT;

    public static Boolean isObjectValid(Object object) {
        if (object != null)
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public static Boolean isValid(Long id) {
        if (id != null && id > 0)
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    public static Boolean isValid(Integer id) {
        if (id != null && id > 0)
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    public static Boolean isValid(String value) {
        if ((value != null) && !value.trim().isEmpty() && !value.equals("null")) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public static Boolean isValid(List<?> objList) {
        if ((objList != null) && !objList.isEmpty()) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public static Boolean isValid(Set<?> objList) {
        if ((objList != null) && !objList.isEmpty()) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public static Boolean isValid(Map<?, ?> objMap) {
        if ((objMap != null) && !objMap.isEmpty()) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

//    public static Boolean isValidForDateFormat(String dateStr) {
//        DateFormat sdf = new SimpleDateFormat(dateFormat);
//        sdf.setLenient(false);
//        try {
//            sdf.parse(dateStr);
//        } catch (ParseException e) {
//            return false;
//        }
//        return true;
//    }

}