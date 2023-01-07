package net.thrymr.repository;

import net.thrymr.model.master.MtQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepo extends JpaRepository<MtQuestion,Long> {
    List<MtQuestion> findByChapterId(Long chapterId);
}
