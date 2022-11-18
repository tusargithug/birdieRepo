package net.thrymr.model.master;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thrymr.model.AppUser;
import net.thrymr.model.BaseEntity;
import net.thrymr.model.RoleModuleInfo;

@Entity
@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "mt_roles")
public class MtRoles extends BaseEntity {

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "mtRoles", cascade = CascadeType.ALL)
    private List<AppUser> users = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "mtRoles", cascade = CascadeType.ALL)
    private List<RoleModuleInfo> roleModuleInfoList = new ArrayList<>();

    public MtRoles(String name) {
        this.name = name;
    }


}
