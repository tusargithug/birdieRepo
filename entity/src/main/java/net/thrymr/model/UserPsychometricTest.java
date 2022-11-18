package net.thrymr.model;

import java.util.Date;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.*;


import lombok.NoArgsConstructor;
import net.thrymr.model.master.PsychometricTest;
import net.thrymr.model.master.MtQuestion;
import net.thrymr.model.master.PsychometricTestOption;
import net.thrymr.enums.Risk;
import net.thrymr.model.master.RiskLevel;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class UserPsychometricTest extends BaseEntity {

    @ManyToOne
    private AppUser appUser;
    //TODO already relation in RiskLevel need to remove
    @ManyToOne
    private PsychometricTest psychometricTest;

    @Embedded
    private Map<MtQuestion, PsychometricTestOption> selectedOptions = new HashMap<>();

    @Column(name = "submitted_on")
    private Date submittedOn;

    @Column(name = "total_score")
    private Long totalScore;

    @Enumerated
    private Risk riskIdentified;

    @ManyToOne
    private RiskLevel riskLevel;


}
