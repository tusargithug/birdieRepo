package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.master.MtCity;
import net.thrymr.model.master.MtRegion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CountryDto {
    private Long id;
    private String countryName;
    private String countryCode;
    private Long regionId;
    private List<MtCity> cities = new ArrayList<>();
}
