package net.thrymr.repository;

import net.thrymr.model.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileDetailsRepo extends JpaRepository<FileDetails,Long> {
    Optional<FileDetails> findByFileId(String fileId);

    List<FileDetails> findAllByOrderByFileContentTypeAsc();

}
