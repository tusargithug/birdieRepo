package net.thrymr.model;

import lombok.Getter;
import lombok.Setter;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtCountry;
import net.thrymr.model.master.MtRegion;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
public class SiteResponse {
    private Long id;
    private String siteId;
    private String siteName;
    private MtCity city;
    private MtCountry country;
    private MtRegion region;
}
