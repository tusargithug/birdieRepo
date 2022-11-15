package net.thrymr.security;

import net.thrymr.model.AppUser;
import net.thrymr.utils.Validator;
import net.thrymr.repository.AppUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    AppUserRepo appUserRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Map<Object,Object> loginData=appUserRepo.getLoginData(contactNumber);
        //  Optional<AppUser> optionalAppUser=appUserRepo.findByMobile(contactNumber);

        Optional<AppUser> optionalAppUser = appUserRepo.findByEmail(email);

        if (Validator.isValid(optionalAppUser.get().getEmail())) {
            return LoggedInUser.builds(optionalAppUser);
        } else {
            throw new UsernameNotFoundException("User Not Found with the given Email: " + email);
        }

    }
}


