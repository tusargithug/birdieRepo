package net.thrymr.dto;

import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
public class CityDto {
    private Long id;
    private String cityName;
    private Long mtCountryId;
}
