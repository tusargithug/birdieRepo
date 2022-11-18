package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.master.MtTeam;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TeamMembers extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private MtTeam team;

    @OneToOne
    private AppUser appUser;
}
