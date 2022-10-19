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
@Table(name = "city")
public class MtCity extends BaseEntity {

    @Column(name = "city_name")
    private String cityName;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private MtCountry country;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "city")
    private List<Site> sites=new ArrayList<>();

}
