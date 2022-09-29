package net.thrymr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.thrymr.model.master.Course;


public interface CourseRepo extends JpaRepository<Course,Long> {

}
