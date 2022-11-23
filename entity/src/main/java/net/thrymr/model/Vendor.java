package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "vendor")
public class    Vendor extends BaseEntity{
    @Column(name = "vendorPOC")
    private String POC;

    private String vendorName;

    private String vendorId;

    private String countryCode;

    private String mobileNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Site> site;
}
