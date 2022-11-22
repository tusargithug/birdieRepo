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
@Table(name = "team")
public class Team extends BaseEntity {
    @Column(name = "team_id")
    private String teamId;
    @Column(name = "team_name")
    private String teamName;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Site site;
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private ShiftTimings shiftTimings;

}

