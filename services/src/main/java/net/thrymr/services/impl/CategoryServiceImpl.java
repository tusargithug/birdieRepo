package net.thrymr.services.impl;

import net.thrymr.dto.CategoryDto;
import net.thrymr.model.master.Category;
import net.thrymr.model.master.Course;
import net.thrymr.repository.CategoryRepo;
import net.thrymr.repository.CourseRepo;
import net.thrymr.services.CategoryService;
import net.thrymr.utils.Validator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;

    private final CourseRepo courseRepo;

    public CategoryServiceImpl(CategoryRepo categoryRepo, CourseRepo courseRepo) {
        this.categoryRepo = categoryRepo;
        this.courseRepo = courseRepo;
    }

    @Override
    public String deleteCourseById(Long id) {
        Optional<Course> category = courseRepo.findById(id);
        category.ifPresent(courseRepo::delete);
        return "course deleted successfully";
    }

    @Override
    public String createCategory(CategoryDto request) {
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSequence(request.getSequence());
        if (request.getIsActive() != null && request.getIsActive().equals(Boolean.TRUE)) {
            category.setIsActive(request.getIsActive());
        }
        categoryRepo.save(category);
        return "Course Created successfully";
    }

    @Override
    public String deleteCategoryById(Long id) {
        Optional<Category> category = categoryRepo.findById(id);
        category.ifPresent(categoryRepo::delete);
        return "Intensity deleted successfully";
    }

    @Override
    public String updateCategory(CategoryDto request) {
        final Optional<Category> category = categoryRepo.findById(request.getId()).stream()
                .filter(c -> c.getId() == request.getId())
                .findFirst();
        if (!category.isPresent()) {
            return null;
        }
        category.get()
                .setName(request.getName());
        category.get()
                .setDescription(request.getDescription());
        category.get()
                .setSequence(request.getSequence());
        if (request.getIsActive().equals(Boolean.TRUE) || request.getIsActive().equals(Boolean.FALSE)) {
            category.get().setIsActive(request.getIsActive());
        }
        categoryRepo.save(category.get());
        return "update category successfully";
    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> categoryList = categoryRepo.findAll();
        if (!categoryList.isEmpty()) {
            return categoryList.stream().filter(obj -> obj.getIsActive().equals(Boolean.TRUE)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public Course getCourseById(Long id) {
        if (Validator.isValid(id)) {
            Optional<Course> optionalCourse = courseRepo.findById(id);
            if (optionalCourse.isPresent() && optionalCourse.get().getIsActive().equals(Boolean.TRUE)) {
                return optionalCourse.get();
            }
        }
        return new Course();
    }
}
