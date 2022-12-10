package net.thrymr.model.master;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.BaseEntity;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class LanguageDetails extends BaseEntity {
    private String languageName;
}
