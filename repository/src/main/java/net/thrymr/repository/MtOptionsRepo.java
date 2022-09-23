package net.thrymr.repository;

import net.thrymr.model.master.MtOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MtOptionsRepo extends JpaRepository<MtOptions,Long> {
    List<MtOptions> findAllByMtQuestionId(Long id);
}