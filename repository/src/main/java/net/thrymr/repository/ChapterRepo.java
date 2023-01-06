package net.thrymr.repository;

import net.thrymr.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepo extends JpaRepository<Chapter,Long>, JpaSpecificationExecutor<Chapter> {
    Optional<Chapter> findBySequence(Integer sequence);
    Optional<Chapter> findByIdAndQuestionListIsDeletedAndQuestionListMtOptionsIsDeleted(Long id, Boolean aFalse, Boolean aFalse1);

    Optional<Chapter> findByIdAndIsDeletedAndQuestionListIsDeletedAndQuestionListMtOptionsIsDeleted(Long id, Boolean aFalse, Boolean aFalse1, Boolean aFalse2);
}
