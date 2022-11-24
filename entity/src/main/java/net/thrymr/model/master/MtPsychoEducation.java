package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.FileEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_psycho_education")
public class MtPsychoEducation extends BaseEntity {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne
    private FileEntity file;
}
