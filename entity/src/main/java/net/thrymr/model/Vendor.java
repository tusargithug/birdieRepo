package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
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

    @Column(unique = true)
    private String vendorId;

    private String countryCode;

    @Size(min = 10, max = 10)
    @Column(unique = true)
    private String mobileNumber;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Site> site;
}
