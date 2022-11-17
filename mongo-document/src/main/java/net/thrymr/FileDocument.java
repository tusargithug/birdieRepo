package net.thrymr;

import lombok.*;
import net.thrymr.model.FileDetails;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document
public class FileDocument extends BaseDocument {

    private String fileId;

    private String fileName;

    private String fileContentType;

    private String fileSizeInBytes;

    private String fileType;

    private String publicUrl;

    private byte[] file;

    private FileDetails fileEntity;
}