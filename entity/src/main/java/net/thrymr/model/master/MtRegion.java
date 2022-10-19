package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.Site;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ismail Dudekula
 * @version 1.0
 * @since 30-03-2022
 */

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "region")
public class MtRegion extends BaseEntity {

    @Column(name = "region_name")
    private String regionName;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "region")
    private List<MtCountry> mtCountry =new ArrayList<>();

    @ManyToOne
    private Site site;

}
