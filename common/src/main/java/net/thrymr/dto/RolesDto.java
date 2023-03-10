package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class RolesDto {
    private Long id;

    private String name;

    private List<AppUserDto> usersDto = new ArrayList<>();
}
