package net.thrymr.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thrymr.model.AppUser;
import net.thrymr.model.master.MtRoles;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


@Getter
@Setter
@NoArgsConstructor
public class LoggedInUser implements UserDetails {
    private Long id;

    private String contactNumber;

    private String firstName;

    private String lastName;

    private MtRoles role;

    private Boolean isActive;

    private String appUser;

    private String email;

    @JsonIgnore
    private String password;

    public static LoggedInUser builds(Optional<AppUser> loginData) {
        LoggedInUser loggedInUser = new LoggedInUser();
        loggedInUser.setId((loginData.get().getId()));
        loggedInUser.setContactNumber(loginData.get().getMobile());
        loggedInUser.setFirstName(loginData.get().getFirstName());
        loggedInUser.setLastName(loginData.get().getLastName());
        loggedInUser.setEmail(loginData.get().getEmail());
        loggedInUser.setPassword(loginData.get().getPassword());
        loggedInUser.setRole(loginData.get().getMtRoles());
        return loggedInUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return contactNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
