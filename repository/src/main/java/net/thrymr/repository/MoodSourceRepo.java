package net.thrymr.repository;

import net.thrymr.model.master.MoodSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoodSourceRepo extends JpaRepository<MoodSource, Long> {
}
