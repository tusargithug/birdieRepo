package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.FileEntity;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_meditation")
public class MtMeditation extends BaseEntity {

    private String name;

    private String meditationVideoLink;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileEntity file;
}
