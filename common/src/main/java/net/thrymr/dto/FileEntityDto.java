package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.FileType;


@Setter
@Getter
@NoArgsConstructor
public class FileEntityDto {
    private String fileId;
    private String contentType;
    private String name;
    private FileType fileType = FileType.NONE;
}
