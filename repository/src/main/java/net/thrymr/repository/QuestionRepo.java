package net.thrymr.repository;

import net.thrymr.model.master.MtQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends JpaRepository<MtQuestion,Long> {
}
