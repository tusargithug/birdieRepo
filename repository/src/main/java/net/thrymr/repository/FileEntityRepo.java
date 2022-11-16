package net.thrymr.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.thrymr.model.master.FileEntity;


public interface FileEntityRepo extends JpaRepository<FileEntity, Long> {

}
