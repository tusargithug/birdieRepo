package net.thrymr.services.impl;

import net.thrymr.dto.CourseDto;
import net.thrymr.model.master.Course;
import net.thrymr.repository.CourseRepo;
import net.thrymr.services.CourseService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    public CourseServiceImpl(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    @Override
    public Course updateCourse(CourseDto request) {
        final Optional<Course> optionalCourse = courseRepo.findById(request.getId());
                Course course=new Course();
        if (optionalCourse.isPresent()) {
            course.setCode(request.getCode());
            course.setDescription(request.getDescription());
            course.setSequence(request.getSequence());
            course.setName(request.getName());
            return null;
        }

        return courseRepo.save(course);
    }

    @Override
    public String createCourse(CourseDto request) {
        Course course=new Course();
        course.setCode(request.getCode());
        course.setDescription(request.getDescription());
        course.setSequence(request.getSequence());
        course.setName(request.getName());
        courseRepo.save(course);
        return "Course Created successfully";
    }
}
