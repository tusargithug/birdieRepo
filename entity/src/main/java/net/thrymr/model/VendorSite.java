package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class VendorSite extends BaseEntity{
    @ManyToOne
    private Vendor vendor;
    @ManyToOne
    private Site site;


}
