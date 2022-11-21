package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "mt_worksheet")
public class MtWorksheet extends BaseEntity {

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;


}
