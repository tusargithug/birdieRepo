package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.FileType;
import net.thrymr.model.FileDetails;
import net.thrymr.model.Vendor;
import net.thrymr.model.VendorSite;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class FileDetailsDto {
    private Long id;
    private Long groupId;
    private String fileId;
    private String fileName;
    private String contentType;
    private FileType fileType;
    private List<FileDetails> fileDetailsList;

    public FileDetailsDto(FileType fileType, List<FileDetails> fileDetailsList) {
        this.fileType = fileType;
        this.fileDetailsList = fileDetailsList;
    }
}