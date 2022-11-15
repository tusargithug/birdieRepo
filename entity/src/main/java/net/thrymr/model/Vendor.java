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

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AppUser appUser;
}
