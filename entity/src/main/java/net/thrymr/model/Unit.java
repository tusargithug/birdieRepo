package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Unit extends  BaseEntity{

    @Column(name = "unit_name")
    private String unitName;

    @Column(unique = true)
    private Long sequence;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "unit")
    private List<Chapter> chapters =new ArrayList<>();
}
