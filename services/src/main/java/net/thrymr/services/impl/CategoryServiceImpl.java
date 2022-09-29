package net.thrymr.services.impl;

import net.thrymr.dto.CategoryDto;
import net.thrymr.model.master.Category;
import net.thrymr.model.master.Course;
import net.thrymr.repository.CategoryRepo;
import net.thrymr.repository.CourseRepo;
import net.thrymr.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        return "Intensity deleted successfully";
    }

    @Override
    public String createCategory(CategoryDto request) {
        Category category=new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setSequence(request.getSequence());
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
    public Category updateCategory(CategoryDto request) {
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
        return categoryRepo.save(category.get());
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepo.findAll();
    }
}
