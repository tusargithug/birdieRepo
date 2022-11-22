package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.enums.TagType;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MiniSession extends BaseEntity{
    private String miniSessionName;

    @ElementCollection(targetClass = String.class)
    private List<String> tags;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "miniSession")
    private List<Groups> groupsList;
}
