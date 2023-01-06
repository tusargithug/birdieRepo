package net.thrymr.repository;

import net.thrymr.model.Chapter;
import net.thrymr.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UnitRpo extends JpaRepository<Unit,Long>, JpaSpecificationExecutor<Unit> {
    Optional<Unit> findBySequence(Integer sequence);
    Optional<Unit> findBySequenceAndChaptersSequence(Integer sequence, Integer chapterSequence);
    Page<Unit> findByIdAndChaptersIsDeletedAndChaptersQuestionListIsDeletedAndChaptersQuestionListMtOptionsIsDeleted(Long unitId, Boolean aFalse, Boolean aFalse1, Boolean aFalse2, Specification<Unit> chapterSpecification, Pageable pageable);

    Optional<Unit> findByIdAndIsDeleted(Long unitId, Boolean aFalse);
}
