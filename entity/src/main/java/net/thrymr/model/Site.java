package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtCountry;
import net.thrymr.model.master.MtRegion;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Site extends BaseEntity {
    @Column(name = "site_id",unique = true)
    private String siteId;

    @Column(name = "site_name")
    private String siteName;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private MtCity city;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private MtCountry country;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private MtRegion region;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Team> teams =new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "site")
    private List<AppUser> appUser;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "site")
    private List<Counsellor> counsellorList=new ArrayList<>();
}
