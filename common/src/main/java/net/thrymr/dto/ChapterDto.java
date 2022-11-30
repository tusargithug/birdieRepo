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
    private Long questionId;
    private Boolean isActive;
    private Long unitId;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isSorting;
    private String addedOn;
    private String videoId;
    private String profilePictureId;
}