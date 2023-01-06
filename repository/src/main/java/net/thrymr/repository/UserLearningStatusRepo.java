package net.thrymr.repository;

import net.thrymr.model.UserLearningStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserLearningStatusRepo extends JpaRepository<UserLearningStatus, Long>, JpaSpecificationExecutor<UserLearningStatus> {

    Optional<UserLearningStatus> findByUserId(String userId);
}
