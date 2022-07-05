package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class SendMessageDto {
    private String mobile;
    private String messageBody;
    private String messageType;

    private String toEmail;
    private String emailSubject;
    private String otp;
    private String userName;
    private String actionType;


    private String attachmentName;
    private String attachmentType;
    private byte[] attachmentData;



}
