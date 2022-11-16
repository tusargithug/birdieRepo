package net.thrymr.model.master;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class PsychometricTest extends BaseEntity {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "psychometricTest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MtQuestion> mtQuestions = new HashSet<>();

    @OneToMany(mappedBy = "psychometricTest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PsychometricTestOption> options = new HashSet<>();

    @OneToMany(mappedBy = "psychometricTest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RiskLevel> riskLevels = new HashSet<>();
}
