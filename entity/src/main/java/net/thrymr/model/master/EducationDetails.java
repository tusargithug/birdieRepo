package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class EducationDetails extends BaseEntity {
    private String educationName;
}
