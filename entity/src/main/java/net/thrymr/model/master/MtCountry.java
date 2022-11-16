package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;

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
@Table(name = "mt_country")
public class MtCountry extends BaseEntity {

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "country_code")
    private String countryCode;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MtRegion region;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "country")
    private List<MtCity> cities = new ArrayList<>();

}
