package net.thrymr.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLearningStatusDto {

    private Long id;
    private String userId;
    private String userName;
    private Long unitSequence;
    private Long chapterNumber;
    private Long videoSequence;
    private Long totalUnits;
    private Long totalChaptersInUnit;
    private Long totalVideosInChapter;
    private Integer pageSize;
    private Integer pageNumber;
    private Boolean sortUserName;
}
