package net.thrymr.repository;

import net.thrymr.model.master.MtMoodSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodSourceRepo extends JpaRepository<MtMoodSource, Long> {
    List<MtMoodSource> findAllByIdIn(List<Long> sourceIds);
}
