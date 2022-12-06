package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CounsellorEmployee extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser appUser;

    @ManyToOne( fetch = FetchType.LAZY)
    private Counsellor counsellor;
}
