package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Groups extends BaseEntity {
    private String groupName;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MiniSession miniSession;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groups")
    private List<FileDetails> fileDetails;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "groups")
    private List<GroupDetails> groupDetailsList = new ArrayList<>();
}
