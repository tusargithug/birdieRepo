package net.thrymr.controller;
import net.thrymr.dto.CounsellorDto;
import net.thrymr.model.AppUser;
import net.thrymr.model.Counsellor;
import net.thrymr.services.CounsellorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class CounsellorController {
    @Autowired
     CounsellorService counsellorService;

    @MutationMapping(name ="createCounsellor")
    public String createCounsellor(@Argument(name = "input") CounsellorDto counsellorDto){
        return counsellorService.createCounsellor(counsellorDto);
    }
    @MutationMapping(name ="updateCounsellorById")
    public String updateCounsellorById(@Argument(name = "input") CounsellorDto counsellorDto){
        return counsellorService.updateCounsellorById(counsellorDto);
    }
    @MutationMapping(name ="deleteCounsellorById")
    public String deleteCounsellorById(@Argument Long id){
        return counsellorService.deleteCounsellorById(id);
    }

    @QueryMapping(name = "getAllCounsellor")
    public List<Counsellor> getAllCounsellor(@Argument (name = "input") CounsellorDto response){
        return counsellorService.getAllCounsellor(response);
    }

    @QueryMapping(name = "getCounsellorById")
    public Counsellor getCounsellorById(@Argument Long id){
        return counsellorService.getCounsellorById(id);
    }
}
