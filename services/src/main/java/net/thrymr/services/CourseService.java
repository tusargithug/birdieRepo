package net.thrymr.services;

import net.thrymr.dto.CourseDto;
import net.thrymr.model.master.Course;

public interface CourseService {
    Course updateCourse(CourseDto request);

    String createCourse(CourseDto request);
}
