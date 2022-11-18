package net.thrymr.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

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

    @Column(columnDefinition = "text")
    private String text;

    @ElementCollection(targetClass = String.class)
    private List<String> tags;
}
