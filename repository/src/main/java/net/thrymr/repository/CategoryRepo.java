package net.thrymr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.thrymr.model.master.Category;


public interface CategoryRepo extends JpaRepository<Category, Long> {

}
