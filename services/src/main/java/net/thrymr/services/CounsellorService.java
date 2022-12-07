package net.thrymr.services;

import net.thrymr.dto.CounsellorDto;
import net.thrymr.dto.response.PaginationResponse;
import net.thrymr.model.Counsellor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CounsellorService {
    String createCounsellor(CounsellorDto counsellorDto);

    String updateCounsellorById(CounsellorDto counsellorDto);

    String deleteCounsellorById(Long id);

    PaginationResponse getAllCounsellorPagination(CounsellorDto response);

    Counsellor getCounsellorById(Long id);
}
