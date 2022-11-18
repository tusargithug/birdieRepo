package net.thrymr.repository;

import net.thrymr.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepo extends JpaRepository<FileEntity, Long> {
    Optional<FileEntity> findByFileId(String id);
}
