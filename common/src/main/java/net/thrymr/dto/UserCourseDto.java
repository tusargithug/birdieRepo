package net.thrymr.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.CourseStatus;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseDto {

    private Long courseId;

    private Long appUserId;

    private List<Long> mtOptionsIds = new ArrayList<>();

    private String startedDate;

    private String completedDate;

    private CourseStatus status = CourseStatus.NOT_STARTED;

    private Boolean isActive;


}
