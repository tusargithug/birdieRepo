package net.thrymr.repository;


import net.thrymr.model.master.MtMoodIntensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MoodIntensityRepo extends JpaRepository<MtMoodIntensity, Long> {

    List<MtMoodIntensity> findByMtMoodInfoId(Long id);

    Optional<MtMoodIntensity> findByIdAndMtMoodInfoIdAndIsActiveAndIsDeleted(Long intensityId, Long moodInfoId, Boolean aTrue, Boolean aFalse);
}
