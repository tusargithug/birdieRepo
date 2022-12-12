package net.thrymr.repository;

import net.thrymr.model.master.EducationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepo extends JpaRepository<EducationDetails,Long> {
}
