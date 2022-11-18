package net.thrymr.repository;

import net.thrymr.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepo extends JpaRepository<Chapter, Long>, JpaSpecificationExecutor<Chapter> {
}
