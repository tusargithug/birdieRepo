package net.thrymr.repository;

import net.thrymr.model.master.MtPsychoEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsychoEducationRepo extends JpaRepository<MtPsychoEducation, Long> {
}
