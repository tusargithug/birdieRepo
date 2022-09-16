package net.thrymr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "module")
public class Module extends  BaseEntity {

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<RoleModuleInfo> roleModuleInfoList = new ArrayList<>();


}
