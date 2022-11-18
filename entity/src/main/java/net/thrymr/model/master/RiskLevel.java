package net.thrymr.model.master;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;
import net.thrymr.enums.Risk;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class RiskLevel extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PsychometricTest psychometricTest;

    @Column(name = "range_from")
    private Float rangeFrom;

    @Column(name = "range_to")
    private Float rangeTo;

    @Enumerated
    private Risk risk;

}
