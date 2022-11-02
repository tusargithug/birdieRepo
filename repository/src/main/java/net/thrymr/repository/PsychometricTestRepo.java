package net.thrymr.repository;

import net.thrymr.model.master.PsychometricTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PsychometricTestRepo extends JpaRepository<PsychometricTest,Long> {

}
