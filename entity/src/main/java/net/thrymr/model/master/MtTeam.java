package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.AppUser;
import net.thrymr.model.BaseEntity;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "mt_team")
public class MtTeam extends BaseEntity {
    @Column(name = "team_id")
    private String teamId;
    @Column(name = "team_name")
    private String teamName;
    //TODO doubt
    @OneToOne(cascade = CascadeType.ALL)
    private AppUser teamLeader;
    @OneToOne(cascade = CascadeType.ALL)
    private AppUser teamManager;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MtSite mtSite;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MtShiftTimings mtShiftTimings;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AppUser> appUserList;
    /*@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "team")
    private List<Counsellor> counsellorList;*/
}

