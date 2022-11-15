package net.thrymr.model.master;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class PsychometricTestOption extends BaseEntity {

    private String option;

    private int score;

    @ManyToOne(cascade = CascadeType.ALL)
    private PsychometricTest psychometricTest;
}
