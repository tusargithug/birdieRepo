package net.thrymr.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Getter
@Setter
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private String errorMessage;
    private int errorCode;
    private HttpStatus httpStatus;

    public CustomException(HttpStatus httpStatus, String errorMessage) {
        this.errorCode = httpStatus.value();
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
