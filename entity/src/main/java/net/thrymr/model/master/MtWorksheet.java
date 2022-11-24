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
@Table(name = "mt_worksheet")
public class MtWorksheet extends BaseEntity {

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FileEntity file;
}
