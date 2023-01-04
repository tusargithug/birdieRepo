package net.thrymr.constant;

import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Constants {
    @Value("${SMS.SENDER.ID}")
    private static  String SMS_SENDER_ID;
    @Value("${SMS.USER.ID}")
    public static String SMS_USER_ID ;
    @Value("${SMS.PASSWORD}")
    public static  String SMS_PASSWORD ;


    public static final String AUTH_HEADER_NAME = "Authorization";
    public static final String AUTH_HEADER_TOKEN_PREFIX = "Bearer";
    public static final String X_API_KEY_VALUE = "OBnQ8ei8Q52AqLvErYnJl95Z4VorxPJ54rmctPLy";
    public static final String X_API_KEY = "X-API-KEY";
    public static final String SECRET = "QXJpc2UsIGF3YWtlLCBhbmQgc3RvcCBub3QgdGlsbCB0aGUgZ29hbCBpcyByZWFjaGVk";

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String DATE_FORMAT1 = "yyyy-MM-dd";


    public static final String TIME_FORMAT = "H:mm";
    public static final String TIME_FORMAT_2 = "HH:mm";
    public static final String TIME_FORMAT_24 = "HH:mm a";
    public static final String TIME_FORMAT_12_HOURS = "hh:mm a";
    public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String CUSTOM_DATE_TIME_FORMAT = "dd MMM yyyy HH:mm a";
    public static final String  DATE_FORMAT_3="dd-MMM-yyyy";
    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final String EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String DEFAULT_LANGUAGE = "en";

    public static final String EUR = "EUR";

    public static final long VALIDITY_TIME_MS = 1 * 24 * 60 * 60 * 1000;// 1 day Validity

    public static final long IDEAL_USER_LOGIN_SESSION_TIME_OUT = 120; //in minute


    /**
     * Sendgrid - Mail
     */
    public static final String SENDGRID_API_KEY = "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";
    public static final String SENDGRID_TEMPLATE_API_KEY = "d-541b2bf3d4fe44c59f24f9510eb380dc";
    public static final String SENDGRID_FROM_MAIL_ID = "info@hegdehospital.com";

    /**
     * SmsConutryAPI - SMS
     */

    public static final String SMS_GET_URL = "http://api.smscountry.com/SMSCwebservice_bulk.aspx?User=" + "SMS_USER_ID" + "&passwd=" + "SMS_PASSWORD" + "&sid=" + "SMS_SENDER_ID" + "&mtype=N&DR=Y&";

    public static final String FILE_PREVIEW_URL = "/api/v1/mongo/file-preview/";
    public static final String FILE_DOWNLOAD_URL = "/api/v1/mongo/file/";

    public static Map<Integer, String> getVideoFiles(){
        Map<Integer, String> videoFileNames = new HashMap<>();
        videoFileNames.put(1,"Chapter 1 - What_is_Mental_Health_ Presentation_22_December_2022");
        videoFileNames.put(2,"Chapter 2 - Why is it necessary to work on Mental health - Presentation - 22 December 2022");
        videoFileNames.put(3,"Chapter 3 - Its okay not to be okay - Presentation - 26 December 2022");
        videoFileNames.put(4,"Chapter 4 - Negative attitudes and beliefs around Mental Health - Presentation - 22 December 2022");
        videoFileNames.put(5,"Chapter 5 - Impact of negative beliefs around Mental health");
        videoFileNames.put(6,"Chapter 6 - Creating a safe environment");
        return videoFileNames;
    }

    public static Boolean isValidEmailPattern(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }
    public static Boolean isValidMobileNumberPattern(String mobile) {
        String ePattern = "^[6-9]{1}[0-9]{9}$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
