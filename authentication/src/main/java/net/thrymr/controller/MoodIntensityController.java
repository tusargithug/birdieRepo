package net.thrymr.controller;
import net.thrymr.dto.request.MoodSourceIntensityRequestDto;
import net.thrymr.model.UserMoodCheckIn;
import net.thrymr.model.master.MtMoodIntensity;
import net.thrymr.services.MoodIntensityService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
public class MoodIntensityController {
    private final Logger logger = LoggerFactory.getLogger(MoodIntensityController.class);

    private final MoodIntensityService moodIntensityService;


    public MoodIntensityController(MoodIntensityService moodIntensityService) {
        this.moodIntensityService = moodIntensityService;
    }

    @QueryMapping(name = "getMoodIntensitiesById")
    public MtMoodIntensity getMoodIntensitiesById(@Argument Long id) {
        return  moodIntensityService.getMoodIntensitiesById(id);
    }

    @MutationMapping(name = "deleteMoodIntensitiesById")
    public String deleteMoodIntensitiesById(@Argument Long id) {
        return moodIntensityService.deleteMoodIntensitiesById(id);
    }

    @QueryMapping(name = "getAllMoodIntensities")
    public List<MtMoodIntensity> getAllMoodIntensities() {
        return moodIntensityService.getAllMoodIntensities();
    }

    @MutationMapping(name = "updateMoodIntensity")
    public String UpdateUserMoodCheckIn(@Argument(name = "input") MoodSourceIntensityRequestDto request) {
        return moodIntensityService.UpdateUserMoodCheckIn(request);
    }

    /*@RolesAllowed("EMPLOYEE")
    @MutationMapping(name = "createUserMoodCheckIn")
    public String createUserMoodCheckIn(@Argument(name = "input") MoodSourceIntensityRequestDto request){
        return moodIntensityService.createUserMoodCheckIn(request);
    }*/
    @MutationMapping("createUserMoodCheckIn")
    public String createUserMoodCheckIn(@Argument(name = "input") MoodSourceIntensityRequestDto request){
        return moodIntensityService.createUserMoodCheckIn(request);
    }

//    @RolesAllowed("ADMIN")
//    @RequestMapping(value = "/demo", method = RequestMethod.GET)
//    public ResponseEntity<String> getAdmin(Authentication authentication, Principal principal) {
//        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
//        AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
//        System.out.println(accessToken.getEmail());
//        //model.addAttribute("username", accessToken.getGivenName());
//        return ResponseEntity.ok("Hello Admin");
//    }
    @QueryMapping(name = "getAllMoodCheckIn")
    public List<UserMoodCheckIn> getAllMoodCheckIn(){
        return moodIntensityService.getAllMoodCheckIn();
    }

    @MutationMapping(name = "deleteUserMoodCheckInById")
    public String deleteUserMoodCheckInById(@Argument Long id){
        return moodIntensityService.deleteUserMoodCheckInById(id);
    }

    @QueryMapping(name = "getAllMoodIntensitiesByMoodInfoId")
    public List<MtMoodIntensity> getAllMoodIntensitiesByMoodInfoId(@Argument Long id) {
        return moodIntensityService.getAllMoodIntensitiesByMoodInfoId(id);
    }

}
