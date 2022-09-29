package net.thrymr.services;

import net.thrymr.dto.CategoryDto;
import net.thrymr.model.master.Category;

public interface CategoryService {
    String deleteCourseById(Long id);

    String createCategory(CategoryDto request);

    String deleteCategoryById(Long id);

    Category updateCategory(CategoryDto request);
}
