package net.thrymr.services.impl;

import net.thrymr.dto.CourseDto;
import net.thrymr.model.master.Category;
import net.thrymr.model.master.Course;
import net.thrymr.repository.CategoryRepo;
import net.thrymr.repository.CourseRepo;
import net.thrymr.services.CourseService;
import net.thrymr.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    CategoryRepo categoryRepo;


    @Override
    public String updateCourse(CourseDto request) {
        if (Validator.isValid(request.getId())) {
            Optional<Course> optionalCourse = courseRepo.findById(request.getId());
            Course course = null;
            if (optionalCourse.isPresent()) {
                course = optionalCourse.get();
                if (Validator.isValid(request.getCode())) {
                    course.setCode(request.getCode());
                }
                if (Validator.isValid(request.getDescription())) {
                    course.setDescription(request.getDescription());
                }
                if (Validator.isValid(request.getSequence())) {
                    course.setSequence(request.getSequence());
                }
                if (Validator.isValid(request.getName())) {
                    course.setName(request.getName());
                }
                if (request.getIsActive()!=null && request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
                    course.setIsActive(request.getIsActive());
                }
                courseRepo.save(course);
            }
            return "update course successfully";
        }

        return "this id not present in database";
    }

    @Override
    public String createCourse(CourseDto request) {
        Course course = new Course();
        course.setCode(request.getCode());
        course.setDescription(request.getDescription());
        course.setSequence(request.getSequence());
        course.setName(request.getName());
        if (Validator.isValid(request.getCategoryId())) {
            Optional<Category> optionalCategory = categoryRepo.findById(request.getCategoryId());
            if (optionalCategory.isPresent()) {
                course.setCategory(optionalCategory.get());
            }
        }
        if (request.getIsActive()!=null && request.getIsActive().equals(Boolean.TRUE)) {
            course.setIsActive(request.getIsActive());
        }
        courseRepo.save(course);
        return "Course Created successfully";
    }

    @Override
    public List<Course> getAllCourse() {
        List<Course> courseList = courseRepo.findAll();
        if (!courseList.isEmpty()) {
            return courseList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
