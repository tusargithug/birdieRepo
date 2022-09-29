package net.thrymr.services;

import net.thrymr.dto.CourseDto;
import net.thrymr.model.master.Course;

import java.util.List;

public interface CourseService {
    Course updateCourse(CourseDto request);

    String createCourse(CourseDto request);

    List<Course> getAllCourse();
}
