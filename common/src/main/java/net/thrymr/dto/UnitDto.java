package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UnitDto {
    private Long id;
    private String unitName;
    private Boolean sortCreatedOn;
    private Integer chapterCount;
    private Boolean isActive;
    private List<ChapterDto> addChapters=new ArrayList<>();
    private String searchKey;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean sortUserName;
    private String createdOn;
}
