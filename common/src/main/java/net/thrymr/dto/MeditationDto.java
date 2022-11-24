package net.thrymr.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.FileEntity;

import javax.persistence.OneToOne;

@Setter
@Getter
@NoArgsConstructor
public class MeditationDto {

    private Long id;

    private String name;

    private String meditationVideoLink;

    private String fileId;

    private Boolean isActive;
}
