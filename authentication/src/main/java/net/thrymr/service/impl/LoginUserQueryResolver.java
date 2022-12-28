package net.thrymr.service.impl;

import graphql.kickstart.tools.GraphQLQueryResolver;
import net.thrymr.dto.response.LoginResponse;
import net.thrymr.service.LoginUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;

@Component
public class LoginUserQueryResolver implements GraphQLQueryResolver {
    @Autowired
    LoginUserService loginUserService;

    @QueryMapping("logoutUser")
    public String logoutUser() {
        return loginUserService.logoutUser();
    }

    @QueryMapping("getLoggedInUser")
    public LoginResponse getLoggedInUser() {
        return loginUserService.getLoggedInUser();
    }
}
