package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MiniSession extends BaseEntity{
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "miniSession")
    private List<Groups> groupsList;
    @ElementCollection(targetClass = String.class)
    private List<String> tags;
}
