package net.thrymr.repository;

import net.thrymr.model.master.MtLearningVideos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningVideoRepo extends JpaRepository<MtLearningVideos,Long> {
}
