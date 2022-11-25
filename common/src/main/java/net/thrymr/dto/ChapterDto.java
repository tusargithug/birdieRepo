package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChapterDto {
    private Long id;
    private String chapterName;
    private String description;
    private Boolean isActive=Boolean.FALSE;
    private Long unitId;
    private Integer pageNumber;
    private Integer pageSize;
    private String addedOn;
    private String videoId;
    private String profilePictureId;
}