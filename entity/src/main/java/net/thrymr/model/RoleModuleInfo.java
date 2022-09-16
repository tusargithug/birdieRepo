package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "role_module_info")
public class RoleModuleInfo extends  BaseEntity {

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "role_id", nullable = false)
   private Roles roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

   @Column(name = "permission")
    private String permission;
}
