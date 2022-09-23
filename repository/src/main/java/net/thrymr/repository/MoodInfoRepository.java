package net.thrymr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.thrymr.model.master.MtMoodInfo;

@Repository
public interface MoodInfoRepository extends JpaRepository<MtMoodInfo, Long> {

}
