package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TeamMembers extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Team team;

    @ManyToOne
    private AppUser appUser;
}
