package net.thrymr.repository;

import net.thrymr.model.master.MtUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRpo extends JpaRepository<MtUnit, Long>, JpaSpecificationExecutor<MtUnit> {

    // Optional<Object> findByIdAndChapters(Long id, List<Chapter> chapters);
}
