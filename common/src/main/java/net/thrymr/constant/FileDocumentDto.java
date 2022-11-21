package net.thrymr.constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileDocumentDto {
    private String fileId;

    private String fileName;

    private String fileContentType;

    private String fileSizeInBytes;

    private String fileType;

    private String publicUrl;

    private byte[] file;

    private Boolean isActive;
}