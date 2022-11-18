package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.FileType;

import javax.persistence.Column;


@Setter
@Getter
@NoArgsConstructor
public class FileDetailsDto {
    private Long id;
    private String fileId;
    private String fileName;
    private String contentType;
}
