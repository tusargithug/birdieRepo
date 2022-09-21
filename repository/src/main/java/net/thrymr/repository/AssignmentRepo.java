package net.thrymr.repository;

import net.thrymr.model.master.MtAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepo extends JpaRepository<MtAssignment,Long> {

}

