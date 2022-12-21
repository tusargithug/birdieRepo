package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "vendor")
public class Vendor extends BaseEntity {
    @Column(name = "vendorPOC")
    private String POC;

    private String vendorName;

    private String vendorId;

    private String countryCode;

    @Column(name = "mobileNumber", unique = true)
    private String mobileNumber;

    @Column(name = "email", unique = true)
    private String email;
}
