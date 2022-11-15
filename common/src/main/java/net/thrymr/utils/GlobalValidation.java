package net.thrymr.utils;

import net.thrymr.exception.CustomException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.util.List;

public class GlobalValidation {

    private static final Logger LOG = LogManager.getLogger(GlobalValidation.class);

    private GlobalValidation() {

    }

    public static void isStringEmpty(String value, String message) {
        if (!BaseCommonUtil.isStringNotEmpty(value)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void checkCustomPropertiesAreMissing(String value, String message) {
        if (!BaseCommonUtil.isStringNotEmpty(value)) {
            LOG.error("Required environment variables are missing in current service");
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, message);
        }
    }

    public static void isTrue(boolean value, String message) {
        if (!value) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void isObjectEmpty(Object value, String message) {
        if (value == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static <T> void isListEmpty(List<T> list, String message) {
        if (list == null || list.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void isStringNullOrEmpty(String value, String message) {
        if (value == null || value.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void isDoubleNullOrEmpty(Double value, String message) {
        if (value == null || value == 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void isLongNullOrEmpty(Long value, String message) {
        if (value == null || value == 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void isNumberNullOrLessthanOrEqualsToZero(Integer value, String message) {
        if (value == null || value <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void isNumberNullOrLessthanZero(Integer value, String message) {
        if (value < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void isNumberNullOrLessthanZeroChild(Integer value, String message) {
        if (value == null || value < 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }
    }

    public static void isNumberInBetween(Integer valueToValidate, Integer minValue, Integer maxValue, String message) {
        if (minValue > valueToValidate && valueToValidate > maxValue) {
            throw new CustomException(HttpStatus.BAD_REQUEST, message);
        }

    }

}
