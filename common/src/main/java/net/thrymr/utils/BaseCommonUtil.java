package net.thrymr.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.thrymr.dto.request.GetDataRequestDto;
import net.thrymr.exception.CustomException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Random;


public class BaseCommonUtil {

    public static Boolean checkId(Long id) {
        if (id != null && id > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int calculateAge(Date birthDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dateOFBirth = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(dateOFBirth, currentDate).getYears();
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[0xFFFF];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }

    public static Timestamp currentTimestamp() {
        return new Timestamp(new Date().getTime());
    }


    public static String formatUtilDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }



    public static String getStringFromObject(Object object) {
        if (object != null) {
            try {
                return new ObjectMapper().writeValueAsString( object );
            } catch (JsonProcessingException e) {
                return "";
            }
        }
        return "";
    }



    /**
     * @param value
     * @return true if value is not empty else false
     */
    public static boolean isStringNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * @param value
     * @return true if value is not empty else false
     */
    public static boolean isNotNullOrEmpty(Object value) {
        return value != null && !value.toString().trim().isEmpty();
    }


    public static boolean isLongNotNullOrEmpty(Long value) {
        return value != null && value !=0;
    }


    public static Pageable getPageable(GetDataRequestDto request){
        Pageable pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), Sort.Direction.DESC, "updatedOn");

        if (Validator.isValid(request.getOrder()) && Validator.isValid(request.getField())) {
            Optional<Sort.Direction> direction = Sort.Direction.fromOptionalString(request.getOrder());
            if (!direction.isPresent()) {
                throw  new CustomException(HttpStatus.BAD_REQUEST, "Invalid sorting order.");
            }
            pageable = PageRequest.of(request.getPageNumber(), request.getPageSize(), direction.get(), request.getField());
        }

        return pageable;
    }



    public static String generateOTP(int len) {
        String numbers = "0123456789";
        Random randomMethod = new Random();
        char[] otp = new char[len];
        for (int i = 0; i < len; i++) {
            otp[i] = numbers.charAt( randomMethod.nextInt( numbers.length() ) );
        }
        return new String( otp );
    }
}

