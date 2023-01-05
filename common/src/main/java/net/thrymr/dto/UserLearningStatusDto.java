package net.thrymr.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserLearningStatusDto {

    private Long id;
    private String userId;
    private String userName;
    private Long unitNumber;
    private Long chapterNumber;
    private Long sequence;
    private Integer pageSize;
    private Integer pageNumber;
    private Boolean sortUserName;
}
