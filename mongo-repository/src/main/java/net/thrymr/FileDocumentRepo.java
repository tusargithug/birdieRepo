package net.thrymr;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileDocumentRepo extends MongoRepository<FileDocument, String> {
    Optional<FileDocument> findByFileId(String id);
}