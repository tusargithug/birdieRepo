package net.thrymr.services;

import net.thrymr.dto.CounsellorDto;
import net.thrymr.model.master.MtCounsellor;

import java.util.List;

public interface CounsellorService {
    String createCounsellor(CounsellorDto counsellorDto);

    String updateCounsellorById(CounsellorDto counsellorDto);

    String deleteCounsellorById(Long id);

    List<MtCounsellor> getAllCounsellor(CounsellorDto response);
}
