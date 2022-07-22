package net.thrymr.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;


public class CommonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String generateOTP(int len) {
        String numbers = "0123456789";
        Random randomMethod = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt(randomMethod.nextInt(numbers.length()));
        }
        return new String(otp);
    }
    public static String getStringFromObject(Object object){
        if(object!=null){
            try {
                return objectMapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
