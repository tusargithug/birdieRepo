package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "vendor")
public class Vendor extends BaseEntity {
    @Column(name = "vendorPOC")
    private String POC;

    private String vendorName;

    @Column(unique = true)
    private String vendorId;

    private String countryCode;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "email")
    private String email;
}
