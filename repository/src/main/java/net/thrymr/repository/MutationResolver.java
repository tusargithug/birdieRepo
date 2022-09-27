package net.thrymr.repository;

import graphql.kickstart.tools.GraphQLMutationResolver;
import net.thrymr.dto.AppUserDto;
import net.thrymr.model.AppUser;
import org.apache.commons.lang3.Validate;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Component;


import java.util.Optional;

@Component
public class MutationResolver implements GraphQLMutationResolver {

private final AppUserRepo appUserRepo;


    public MutationResolver(AppUserRepo appUserRepo) {
        this.appUserRepo = appUserRepo;
    }

    @MutationMapping
    public String createAppUser(String firstName,String lastName,String email,String mobile,String alternateMobile ,String empId ){
        AppUser user=new AppUser();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMobile(mobile);
        user.setAlternateMobile(alternateMobile);
        user.setEmpId(empId);
        appUserRepo.save(user);
        return "User Saved successfully";
    }

    @MutationMapping
    public String  updateAppUser(@Arguments AppUserDto request){
        Optional<AppUser>optionalAppUser=appUserRepo.findById(request.getId());
        if(optionalAppUser.isPresent()){
            AppUser user= optionalAppUser.get();
            if(request.getEmpId()!=null){
                user.setEmpId(request.getEmpId());
            }
            if(request.getMobile()!=null) {
                user.setMobile(request.getMobile());
            }

            // user.setPassword(request.getPassword());
            if(request.getLastName()!=null) {
                user.setLastName(request.getLastName());
            }
            if(request.getFirstName()!=null) {
                user.setFirstName(request.getFirstName());
            }
            if(request.getEmail()!=null) {
                user.setEmail(request.getEmail());
            }

            if(request.getAlternateMobile()!=null) {
                user.setAlternateMobile(request.getAlternateMobile());
            }



            appUserRepo.save(user);
            return "User updated successfully";
        }
        return "No data found";
    }



    //delete App user by id
    @MutationMapping
    public String deleteAppUserById(Long id){
        Optional<AppUser>optionalAppUser=appUserRepo.findById(id);
        optionalAppUser.ifPresent(appUserRepo::delete);
        return "User deleted successfully";
    }
}
