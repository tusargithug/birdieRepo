package net.thrymr.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.AppUser;
import java.util.ArrayList;
import java.util.List;

/*
 *@author Chanda Veeresh
 *@version 1.0
 *@since  20-07-2022
 */
@Getter
@Setter
@NoArgsConstructor
public class RolesDto {
    private String name;

    private List<AppUser> users = new ArrayList<>();
}
