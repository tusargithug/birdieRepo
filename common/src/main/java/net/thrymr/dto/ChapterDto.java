package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.master.FileEntity;

@Getter
@Setter
@NoArgsConstructor
public class ChapterDto {
    private Long id;
    private String chapterName;
    private FileEntity profilePicture;
    private String description;
    private Boolean isActive=Boolean.FALSE;
    private FileEntity video;
    private Long unitId;
    private Integer pageNumber;
    private Integer pageSize;
    private String addedOn;
}