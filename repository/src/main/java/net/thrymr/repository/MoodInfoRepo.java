package net.thrymr.repository;

import net.thrymr.model.master.MtMoodInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MoodInfoRepo extends JpaRepository<MtMoodInfo, Long> {

    List<MtMoodInfo> findBySearchKeyContainingIgnoreCase(String searchKey);
}
