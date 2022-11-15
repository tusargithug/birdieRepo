package net.thrymr.repository;

import net.thrymr.model.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepo extends JpaRepository<UserCourse, Long> {
}
