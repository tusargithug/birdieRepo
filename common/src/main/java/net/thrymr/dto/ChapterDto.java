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
    private Boolean isActive;
    private Long questionId;
    private Long unitId;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isSorting;
    private String addedOn;
    private String videoId;
    private String profilePictureId;
    private Boolean check;
    private String searchKey;
    private Integer sequence;
}