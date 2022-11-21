package net.thrymr.repository;

import net.thrymr.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface FileEntityRepo extends JpaRepository<FileEntity,Long> {

}
