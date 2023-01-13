package net.thrymr.service.impl;

import graphql.kickstart.tools.GraphQLMutationResolver;
import net.thrymr.dto.AppUserDto;
import net.thrymr.dto.ChangePasswordDto;
import net.thrymr.dto.LoginDto;
import net.thrymr.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@Component
public class LoginUserMutationResolver implements GraphQLMutationResolver {
    @Autowired
    LoginUserService loginUserService;

    @MutationMapping("/loginUser")
    public String loginUser(@Argument(name = "input") LoginDto request) {
        return loginUserService.loginUser(request);
    }

    @MutationMapping("/signUpUser")
    public String signUpUser(@Argument (name = "input") AppUserDto request) {
        return loginUserService.signUpUser(request);
    }

    @MutationMapping("changePassword")
    public String changePassword(@Argument (name = "input") ChangePasswordDto changePasswordDto) {
        return loginUserService.changePassword(changePasswordDto);
    }
}
