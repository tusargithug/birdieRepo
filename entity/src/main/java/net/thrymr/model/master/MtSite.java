package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.AppUser;
import net.thrymr.model.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MtSite extends BaseEntity {
    @Column(name = "site_id")
    private String siteId;
    @Column(name = "site_name")
    private String siteName;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MtCity city;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MtCountry country;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MtRegion region;
    @OneToOne(cascade = CascadeType.ALL)
    private AppUser siteManager;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MtTeam> mtTeams = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "mtSite")
    private List<AppUser> appUser;
    /*@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "site")
    private List<Counsellor> counsellorList;*/
}
