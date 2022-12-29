package net.thrymr.repository;

import net.thrymr.model.master.MtOptions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OptionsRepo extends JpaRepository<MtOptions,Long> {
    List<MtOptions> findAllByQuestion(Long id);

    List<MtOptions> findAllByIdIn(List<Long> mtOptionsIds);


    List<MtOptions> findAllByQuestionId(Long questionId);
}
