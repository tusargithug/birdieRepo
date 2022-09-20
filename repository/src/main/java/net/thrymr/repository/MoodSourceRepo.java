package net.thrymr.repository;

import net.thrymr.model.master.MoodSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodSourceRepo extends JpaRepository<MoodSource, Long> {
    List<MoodSource> findAllByIdIn(List<Long> sourceIds);
}
