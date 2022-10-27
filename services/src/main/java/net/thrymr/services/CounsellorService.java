package net.thrymr.services;

import net.thrymr.dto.CounsellorDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.Counsellor;

import java.util.List;

public interface CounsellorService {
    String createCounsellor(CounsellorDto counsellorDto);

    String updateCounsellorById(Long id, CounsellorDto counsellorDto);

    String deleteCounsellorById(Long id);

    List<AppUser> getAllCounsellor(CounsellorDto response);
}
