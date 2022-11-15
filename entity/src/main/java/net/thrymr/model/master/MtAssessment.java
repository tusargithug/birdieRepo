package net.thrymr.model.master;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import lombok.NoArgsConstructor;
import net.thrymr.enums.FrequencyType;
import net.thrymr.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_assessment")
public class MtAssessment extends BaseEntity {
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    @ManyToMany(mappedBy = "assignments", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();
    private String instructions;
    private FrequencyType frequencyType;
    private String high;
    private String moderate;
    private String low;
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MtQuestion> questionList = new HashSet<>();
}
