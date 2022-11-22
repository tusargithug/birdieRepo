package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class FileDetailsDto {
    private Long id;
    private Long groupId;
    private String fileId;
    private String fileName;
    private String contentType;
}