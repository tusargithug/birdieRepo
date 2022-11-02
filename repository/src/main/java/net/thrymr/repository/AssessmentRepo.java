package net.thrymr.repository;

import net.thrymr.model.master.MtAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepo extends JpaRepository<MtAssessment,Long> {

}

