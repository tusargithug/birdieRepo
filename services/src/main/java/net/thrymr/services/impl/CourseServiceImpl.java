package net.thrymr.services.impl;

import net.thrymr.dto.CourseDto;
import net.thrymr.model.master.Category;
import net.thrymr.model.master.Course;
import net.thrymr.repository.CategoryRepo;
import net.thrymr.repository.CourseRepo;
import net.thrymr.services.CourseService;
import net.thrymr.utils.Validator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    private final CategoryRepo categoryRepo;

    public CourseServiceImpl(CourseRepo courseRepo, CategoryRepo categoryRepo) {
        this.courseRepo = courseRepo;
        this.categoryRepo = categoryRepo;
    }

    @Override
    public String updateCourse(CourseDto request) {
        if(Validator.isValid(request.getId())) {
            Optional<Course> optionalCourse = courseRepo.findById(request.getId());
            Course course = null;
            if (optionalCourse.isPresent()) {
                course = optionalCourse.get();
                if(Validator.isValid(request.getCode())) {
                    course.setCode(request.getCode());
                }
                if(Validator.isValid(request.getDescription())) {
                    course.setDescription(request.getDescription());
                }
                if(Validator.isValid(request.getSequence())) {
                    course.setSequence(request.getSequence());
                }
                if (Validator.isValid(request.getName())) {
                    course.setName(request.getName());
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
            Optional<Category> optionalCategory=categoryRepo.findById(request.getCategoryId());
            if(optionalCategory.isPresent()){
                course.setCategory(optionalCategory.get());
            }
        }
        courseRepo.save(course);
        return "Course Created successfully";
    }

    @Override
    public List<Course> getAllCourse() {
        return courseRepo.findAll();
    }
}
