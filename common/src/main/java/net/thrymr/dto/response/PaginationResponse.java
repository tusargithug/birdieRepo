package net.thrymr.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.*;
import net.thrymr.model.master.MtMeditation;
import net.thrymr.model.master.MtPsychoEducation;
import net.thrymr.model.master.MtWorksheet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
public class PaginationResponse {
    private List<Site> siteList=new ArrayList<>();
    private List<Team> teamList=new ArrayList<>();
    private Set<Vendor> vendorList=new HashSet<>();
    private List<AppUser> appUserList=new ArrayList<>();
    private List<MiniSession> miniSessionList=new ArrayList<>();
    private List<MtMeditation> meditationList=new ArrayList<>();
    private List<MtWorksheet> worksheetList=new ArrayList<>();
    private Set<Chapter> chapterList= new HashSet<>();
    private List<Unit> unitList = new ArrayList<>();
    private List<MtPsychoEducation> psychoEducationList = new ArrayList<>();
    private int totalPages=0;
    private Long totalElements=0L;
}
