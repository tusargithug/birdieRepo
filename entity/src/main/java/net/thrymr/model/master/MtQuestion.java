package net.thrymr.model.master;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import net.thrymr.model.BaseEntity;
import net.thrymr.enums.QuestionCalType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_question")
public class MtQuestion extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String question;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "question")
    private Set<MtOptions> mtOptions = new HashSet<>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PsychometricTest psychometricTest;

    @Enumerated(EnumType.STRING)
    private QuestionCalType questionCalType;

    private int sequence;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MtAssessment assessment;
}
