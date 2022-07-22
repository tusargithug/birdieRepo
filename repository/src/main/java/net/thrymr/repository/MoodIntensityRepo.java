package net.thrymr.repository;


import net.thrymr.model.master.MoodIntensity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MoodIntensityRepo extends JpaRepository<MoodIntensity, Long> {

    List<MoodIntensity> findByMoodInfoId(Long id);
}
