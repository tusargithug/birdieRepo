package net.thrymr.repository;

import net.thrymr.model.GroupDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface GroupDetailsRepo extends JpaRepository<GroupDetails,Long> {
}
