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
    private String profilePicture;
    private String description;
    private Boolean isActive = Boolean.FALSE;
    private String video;
    private Long unitId;
    private Integer pageNumber;
    private Integer pageSize;
    private String addedOn;
}