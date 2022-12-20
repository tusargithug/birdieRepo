package net.thrymr.dto.response;

import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.*;
import net.thrymr.model.master.MtMeditation;
import net.thrymr.model.master.MtPsychoEducation;
import net.thrymr.model.master.MtWorksheet;

import java.util.*;

@Setter
@Getter
@NoArgsConstructor
public class PaginationResponse {
    private List<Site> siteList = new ArrayList<>();
    private List<Team> teamList = new ArrayList<>();
    private List<VendorSite> vendorSiteList = new ArrayList<>();
    private Map<Vendor, Site> SiteVendorMap = new LinkedHashMap<>();
    private List<AppUser> appUserList = new ArrayList<>();
    private List<MiniSession> miniSessionList = new ArrayList<>();
    private List<MtMeditation> meditationList = new ArrayList<>();
    private List<MtWorksheet> worksheetList = new ArrayList<>();
    private Set<Chapter> chapterList = new HashSet<>();
    private List<Unit> unitList = new ArrayList<>();
    private List<CounsellorEmployee> counsellorEmployeeList = new ArrayList<>();
    private List<MtPsychoEducation> psychoEducationList = new ArrayList<>();
    private List<Counsellor> counsellorList = new ArrayList<>();
    private List<CounsellorSlot> counsellorSlotList = new ArrayList<>();
    private int totalPages = 0;
    private Long totalElements = 0L;
}
