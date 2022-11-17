package net.thrymr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.thrymr.model.master.FileEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface FileEntityRepo extends JpaRepository<FileEntity, Long> {

}
